package com.dotcms.contenttype.business;

import com.dotcms.api.system.event.ContentTypePayloadDataWrapper;
import com.dotcms.api.system.event.Payload;
import com.dotcms.api.system.event.SystemEventType;
import com.dotcms.api.system.event.Visibility;
import com.dotcms.business.CloseDBIfOpened;
import com.dotcms.business.WrapInTransaction;
import com.dotcms.contenttype.model.event.ContentTypeDeletedEvent;
import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.exception.BaseRuntimeInternationalizationException;
import com.dotcms.notifications.bean.NotificationLevel;
import com.dotcms.notifications.bean.NotificationType;
import com.dotcms.notifications.business.NotificationAPI;
import com.dotcms.system.event.local.business.LocalSystemEventsAPI;
import com.dotcms.util.ContentTypeUtil;
import com.dotcms.util.I18NMessage;
import com.dotcms.util.LogTime;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.business.Role;
import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.ContentletDestroyDelegate;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.google.common.collect.Lists;
import com.liferay.portal.model.User;
import io.vavr.Lazy;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentTypeDestroyAPIImpl implements ContentTypeDestroyAPI {

    Lazy<ContentletDestroyDelegate> delegate = Lazy.of(ContentletDestroyDelegate::newInstance);

    Lazy<Integer> limitProp = Lazy.of(()->Config.getIntProperty("CT_DELETE_BATCH_SIZE", 100));


    private String updateStatement(final ContentType target, final String tempTableName, final int from, final int to ) {
        return DbConnectionFactory.isPostgres() ?
                String.format(
                        "update contentlet c set structure_inode = '%s', \n" +
                                " contentlet_as_json = jsonb_set(contentlet_as_json,'{contentType}', '\"%s\"'::jsonb, false)  \n"  +
                                " from %s ctu \n" +
                                " where ctu.inode = c.inode and ctu.row_num >= %d and ctu.row_num <= %d \n"
                                , target.inode(), target.inode(), tempTableName, from, to )
                :
                        String.format(
                                "update contentlet set structure_inode = ?, \n" +
                                        " contentlet_as_json = json_modify(contentlet_as_json,'$.contentType', '%s')  \n" +
                                        " where  c.inode in ( %s ) \n"
                                        , target.inode(), "");
    }

    /**
     * This method relocates all contentlets from a given structure to another structure And returns
     * the new structure purged from fields
     *
     * @param source
     * @param target
     * @return
     * @throws DotDataException
     */
    public int relocateContentletsForDeletion(final ContentType source, final ContentType target)
            throws DotDataException {

        if (!source.getClass().equals(target.getClass())) {
            throw new DotDataException(
                    String.format(
                            "Incompatible source and target ContentTypes. source class is (%s) target class is (%s)",
                            source.getClass().getSimpleName(), target.getClass().getSimpleName()
                    )
            );
        }

        final String tempTableName = String.format("contentlets_to_be_updated_%d", System.currentTimeMillis());

        String dumpContentletToUpdate = String.format(
                " CREATE TEMP TABLE %s AS\n"
                + " SELECT ROW_NUMBER() OVER(ORDER BY inode) row_num, inode\n"
                + " FROM contentlet c where c.structure_inode = '%s' ;\n"
                + " CREATE index ON %s(row_num); "
                + " CREATE index ON %s(inode); "
                , tempTableName
                , source.inode()
                , tempTableName
                , tempTableName
        );

        Try.of(()->new DotConnect().executeStatement(dumpContentletToUpdate)).onFailure(e->{
            Logger.error(this.getClass(), "Error trying to create temp table to update contentlets", e);
            throw new DotStateException(e);
        });

        final int allCount = new DotConnect().setSQL(String.format("select count(*) as count from %s", tempTableName)).getInt("count");

        int limit = 300;
        int from = 0;
        int to = limit;

        int times = allCount <= limit ?  1 : (int)Math.ceil(allCount / (float)limit);
        for (int i = 0; i < times; i++) {

            Logger.info(this.getClass(),
                    String.format("Relocated from (%d) to (%d) of (%d) contentlets..", from, to, allCount));

            final String updateStatement = updateStatement(target, tempTableName, from, to);
            final DotConnect updateConnect = new DotConnect().setSQL(updateStatement);
            updateConnect.loadResults();

            from += limit;
            to += limit;

        }

        return allCount;
    }


    @CloseDBIfOpened
    private int countByType(final ContentType type) {
        return FactoryLocator.getContentletFactory().countByType(type, true);
    }

    @CloseDBIfOpened
    Map<String, List<ContentletVersionInfo>> nextBatch(final ContentType type, int limit, int offset)
            throws DotDataException {

        String selectContentlets = " select c.inode,  c.identifier , c.language_id, i.host_inode, \n"
                + " (  \n"
                + "  select coalesce(f.inode,'SYSTEM_HOST') as folder_inode from folder f, identifier id where f.identifier = id.id and id.asset_type = 'folder' and id.full_path_lc || '/' = i.parent_path  \n"
                + ") \n"
                + "  from contentlet c \n"
                + "  join structure s on c.structure_inode  = s.inode  \n"
                + "  join identifier i on c.identifier = i.id \n"
                + "  where s.velocity_var_name = ? order by c.identifier \n";

        if(DbConnectionFactory.isMsSql()){
            selectContentlets = " select c.inode,  c.identifier , c.language_id, i.host_inode, \n"
                    + " (  \n"
                    + "  select coalesce(f.inode,'SYSTEM_HOST') as folder_inode from folder f, identifier id where f.identifier = id.id and id.asset_type = 'folder' and concat(id.full_path_lc , '/') = i.parent_path  \n"
                    + ") \n"
                    + "  from contentlet c \n"
                    + "  join structure s on c.structure_inode  = s.inode  \n"
                    + "  join identifier i on c.identifier = i.id \n"
                    + "  where s.velocity_var_name = ? order by c.identifier \n";
        }

        final List<Map<String, Object>> list = new DotConnect().setSQL(selectContentlets)
                .addParam(type.variable()).setMaxRows(limit).setStartRow(offset)
                .loadObjectResults();

        return list.stream().collect(
                Collectors.groupingBy(row -> (String) row.get("identifier"), Collectors.mapping(
                        row -> ContentletVersionInfo.of(
                                (String) row.get("inode"),
                                (Number) row.get("language_id"),
                                (String) row.get("host_inode"),
                                (String) row.get("folder_inode")
                        ), Collectors.toList())));
    }


    @LogTime(loggingLevel = "INFO")
    @Override
    public void destroy(ContentType type, User user) throws DotDataException, DotSecurityException {
        final long allCount = countByType(type);
        final int limit = limitProp.get();
        Logger.info(getClass(), String.format(
                "There are (%d) contents of type (%s). Will remove them sequentially using (%d) batchSize ",
                allCount, type.name(), limit));

        int offset = 0;

        while (true) {

            Map<String, List<ContentletVersionInfo>> batch = nextBatch(type, limit, offset);
            if (batch.isEmpty()) {
                //We're done lets get out of here
                Logger.info(getClass(), "We're done collecting contentlets to destroy.");
                break;
            }

            Logger.info(getClass(), String.format(" For (%s) This batch is (%d) Big. ",type.name(), batch.size()));

            batch.forEach((identifier, versions) -> destroy(identifier, versions, type, user));

            offset += limit;
            Logger.debug(getClass(),
                    String.format(" Offset is (%d) of (%d) total. ", offset, allCount));
        }

        final long leftovers = countByType(type);
        Logger.info(getClass(),String.format(" :: for (%s) allCount is (%d) with leftovers (%d) :: ", type.name(), allCount, leftovers));

        internalDestroy(type);

        Logger.info(getClass(),
                String.format(" I'm done destroying (%d) pieces of Content from type (%s). ", allCount, type.variable()));
    }

    /**
     * This the only method meant to be transaction here. We want to make sure we address transactional smaller batches of contentlets.
     * @param type
     * @throws DotDataException
     */
    @WrapInTransaction
    private void internalDestroy(ContentType type) throws DotDataException{
        try {
            Logger.info(getClass(),
                    String.format("Destroying Content-Type with inode: [%s] and var: [%s].",
                            type.inode(), type.variable()));

            final ContentTypeFactory contentTypeFactory = FactoryLocator.getContentTypeFactory();
            contentTypeFactory.delete(type);
            CacheLocator.getContentTypeCache2().remove(type);
            HibernateUtil.addCommitListener(()-> broadcastEvents(type, APILocator.systemUser()));

        } catch (DotDataException e) {
            Logger.error(getClass(),
                    String.format("Error Removing CT [%s],[%s].", type.inode(), type.variable()),
                    e);
            throw new DotDataException(e);
        }

    }

    /**
     * Broadcasts the events to the system events API and the local system events API.
     * @param type
     * @param user
     */
    void broadcastEvents(final ContentType type, final User user)  {
        try {
            final String actionUrl = ContentTypeUtil.getInstance().getActionUrl(type, user);
            ContentTypePayloadDataWrapper contentTypePayloadDataWrapper = new ContentTypePayloadDataWrapper(actionUrl, type);
            APILocator.getSystemEventsAPI().pushAsync(SystemEventType.DELETE_BASE_CONTENT_TYPE, new Payload(
                    contentTypePayloadDataWrapper, Visibility.PERMISSION, String.valueOf(
                    PermissionAPI.PERMISSION_READ)));
        } catch (DotStateException | DotDataException e) {
            Logger.error(ContentType.class, e.getMessage(), e);
            throw new BaseRuntimeInternationalizationException(e);
        }
        final LocalSystemEventsAPI localSystemEventsAPI = APILocator.getLocalSystemEventsAPI();
        localSystemEventsAPI.notify(new ContentTypeDeletedEvent(type.variable()));
        notifyContentTypeDestroyed(type);
    }


    /**
     * Middle man that basically calls the delegate
     * @param identifier
     * @param inodeAndLanguages
     * @param type
     */
    void destroy(final String identifier, final List<ContentletVersionInfo> inodeAndLanguages, final ContentType type, final User user) {
        Logger.info(getClass(), String.format(" For (%s) Destroying (%d) contentlets for identifier (%s) ", type.name(), inodeAndLanguages.size(), identifier));
        final List<Contentlet> contentlets = makeContentlets(identifier, inodeAndLanguages, type);
        delegate.get().destroy(contentlets, user);
    }

    /**
     * Since all contetlets were moved under a disposable field-less content type, we need to create a list of contentlets including all the basic information
     * @param identifier
     * @param contentletVersionInfos
     * @param type
     * @return
     */
    List<Contentlet> makeContentlets(final String identifier,
            final List<ContentletVersionInfo> contentletVersionInfos, final ContentType type) {
        return contentletVersionInfos.stream().map(contentletVersionInfo -> {
            final Contentlet contentlet = new Contentlet();
            contentlet.setInode(contentletVersionInfo.getInode());
            contentlet.setIdentifier(identifier);
            contentlet.setLanguageId(contentletVersionInfo.getLanguageId().longValue());
            contentlet.setHost(contentletVersionInfo.getHostInode());
            contentlet.setFolder(contentletVersionInfo.getFolderInode());
            contentlet.setContentTypeId(type.id());
            return contentlet;
        }).collect(Collectors.toList());
    }


    /**
     * This method takes an input of contentlet and partitions it into lists that can be used to
     * feed threads as their assigned workload
     *
     * @param contents
     * @return
     */
    private List<Map<String, List<ContentletVersionInfo>>> partitionInput(
            Map<String, List<ContentletVersionInfo>> contents) {

        return Lists.partition(new ArrayList<>(contents.entrySet()), 1).stream().map(
                partition -> partition.stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        ).collect(Collectors.toList());

    }

    /**
     * Contentlet and version info DTO
     */
    static class ContentletVersionInfo {
        final String inode;
        final Number languageId;

        final String hostInode;

        final String folderInode;

        private ContentletVersionInfo(final String inode, final Number languageId, final String hostInode, final String folderInode) {
            this.inode = inode;
            this.languageId = languageId;
            this.hostInode = hostInode;
            this.folderInode = folderInode;
        }

        public static ContentletVersionInfo of(final String inode, final Number languageId, final String hostId, final String folderId) {
            return new ContentletVersionInfo(inode, languageId, hostId, folderId);
        }

        @Override
        public String toString() {
            return "ContentletVersionInfo{" +
                    "inode='" + inode + '\'' +
                    ", languageId=" + languageId +
                    ", hostInode='" + hostInode + '\'' +
                    ", folderInode='" + folderInode + '\'' +
                    '}';
        }

        public String getInode() {
            return inode;
        }

        public Number getLanguageId() {
            return languageId;
        }

        public String getHostInode() {
            return hostInode;
        }
        public String getFolderInode() {
            return null == folderInode ? Host.SYSTEM_HOST : folderInode;
        }

    }

    /**
     * Send out a system-wide notification that a content type has been destroyed
     * @param contentType
     */
    private void notifyContentTypeDestroyed(final ContentType contentType) {
        try {
            final NotificationAPI notificationAPI = APILocator.getNotificationAPI();
            // Search for the CMS Admin role and System User
            final Role cmsAdminRole = APILocator.getRoleAPI().loadCMSAdminRole();
            final User systemUser = APILocator.systemUser();

            notificationAPI.generateNotification(
                    new I18NMessage("contenttype.destroy.complete.title"),
                    new I18NMessage("contenttype.destroy.complete.message", null,
                            contentType.name()), null,
                    // no actions
                    NotificationLevel.WARNING, NotificationType.GENERIC,
                    Visibility.ROLE, cmsAdminRole.getId(), systemUser.getUserId(),
                    systemUser.getLocale()
            );
        } catch (DotDataException e) {
            Logger.error(getClass(), String.format("Failed sending out notification for CT [%s]",contentType.name()), e);
        }
    }


}
