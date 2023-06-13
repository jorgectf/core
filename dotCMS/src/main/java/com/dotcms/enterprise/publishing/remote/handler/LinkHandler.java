package com.dotcms.enterprise.publishing.remote.handler;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.enterprise.publishing.remote.bundler.LinkBundler;
import com.dotcms.enterprise.publishing.remote.handler.HandlerUtil.HandlerType;
import com.dotcms.publisher.pusher.PushPublisherConfig;
import com.dotcms.publisher.pusher.wrapper.LinkWrapper;
import com.dotcms.publisher.receiver.handler.IHandler;
import com.dotcms.publishing.DotPublishingException;
import com.dotcms.publishing.PublisherConfig;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.VersionInfo;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.links.factories.LinkFactory;
import com.dotmarketing.portlets.links.model.Link;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.PushPublishLogger;
import com.dotmarketing.util.PushPublishLogger.PushPublishAction;
import com.dotmarketing.util.PushPublishLogger.PushPublishHandler;
import com.liferay.portal.model.User;
import com.liferay.util.FileUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LinkHandler implements IHandler {
	private UserAPI uAPI = APILocator.getUserAPI();
	private List<String> infoToRemove = new ArrayList<String>();
	private PublisherConfig config;

	public LinkHandler(PublisherConfig config) {
		this.config = config;
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void handle(File bundleFolder) throws Exception {
	    if(LicenseUtil.getLevel() < LicenseLevel.PROFESSIONAL.level)
	        throw new RuntimeException("need an enterprise pro license to run this");
		Collection<File> templates = FileUtil.listFilesRecursively(bundleFolder, new LinkBundler().getFileFilter());

		handleLinks(templates);
	}

	private void deleteLink(Link link) throws DotPublishingException, DotDataException{
		try {
			Link l = APILocator.getMenuLinkAPI().find(link.getInode(), APILocator.getUserAPI().getSystemUser(), false);
			if(l!=null && InodeUtils.isSet(l.getInode())){
				APILocator.getMenuLinkAPI().delete(link, APILocator.getUserAPI().getSystemUser(), false);
			}
		} catch (DotSecurityException e) {
			Logger.error(FolderHandler.class,"Unable to delete with system user " + e.getMessage(),e);
		} catch (Exception e) {
			Logger.error(LinkHandler.class,e.getMessage(),e);
		}
	}

	private void handleLinks(Collection<File> links) throws DotPublishingException, DotDataException {
	    if(LicenseUtil.getLevel() < LicenseLevel.PROFESSIONAL.level)
	        throw new RuntimeException("need an enterprise pro license to run this");
		boolean unpublish = false;
	    User systemUser = uAPI.getSystemUser();
		try{
	        XStream xstream=new XStream(new DomDriver());

	        for(File linkFile: links) {
	        	if(linkFile.isDirectory()) continue;
                LinkWrapper linkWrapper;
	        	try (final InputStream input = Files.newInputStream(linkFile.toPath())){
                     linkWrapper = (LinkWrapper)  xstream.fromXML(input);
                }

	        	for(Link link : linkWrapper.getLinks()){

	    			if(linkWrapper.getOperation().equals(PushPublisherConfig.Operation.UNPUBLISH)) {
	    				unpublish = true;
	    				String linkIden = link.getIdentifier();
	    				deleteLink(link);
						PushPublishLogger.log(getClass(), PushPublishHandler.LINK, PushPublishAction.UNPUBLISH,
								linkIden, link.getInode(), link.getName(), config.getId());
	    				continue;
	    			}

	    			Link existing = null;
	    			Identifier existingId = null;
	    			Identifier linkId = linkWrapper.getLinkId();
	    			Host localHost = APILocator.getHostAPI().find(linkId.getHostId(), systemUser, false);
	    			String modUser = link.getModUser();

	    			try{
	    				existing = APILocator.getMenuLinkAPI().find(link.getInode(), systemUser, false);
	    				existingId = APILocator.getIdentifierAPI().find(existing.getIdentifier());
	    			}catch (DotSecurityException | DotStateException e) {
	    				Logger.debug(getClass(), "Could not find existing Link or Identifier");
					}
	    			if(existing==null || !InodeUtils.isSet(existing.getIdentifier())) {
    		        	Host h = APILocator.getHostAPI().find(link.getHostId(), systemUser, false);
    		        	Folder destination = APILocator.getFolderAPI().findFolderByPath(link.getParent(), h, systemUser, false);
    		        	APILocator.getMenuLinkAPI().save(link, destination, systemUser, false);
						PushPublishLogger.log(getClass(), PushPublishHandler.LINK, PushPublishAction.PUBLISH,
								link.getIdentifier(), link.getInode(), link.getName(), config.getId());
	    			} else if(!linkId.getParentPath().equals(existingId.getParentPath())) {
	                	// if was moved to HOST
	    				if(linkId.getParentPath().equals("/")) {
	    					LinkFactory.moveLink( existing, localHost );
	    				} else { // if was moved to another FOLDER
	    					Folder newParentFolder = APILocator.getFolderAPI().findFolderByPath(linkId.getParentPath(), localHost, systemUser, false);
	    					LinkFactory.moveLink( existing, newParentFolder );
	    				}
	                }

	    			HandlerUtil.setModUser(link.getInode(), modUser, HandlerType.LINKS);
		        }
	        }
	        if(!unpublish){
		        for (File linkFile : links) {
		        	if(linkFile.isDirectory()) continue;
                    LinkWrapper linkWrapper;
                    try(final InputStream input = Files.newInputStream(linkFile.toPath())){
                        linkWrapper = (LinkWrapper) xstream.fromXML(input);
                    }

		        	VersionInfo info = linkWrapper.getVi();
		        	if(info.isLocked() && info.getLockedBy() != null){
		        		info.setLockedBy(systemUser.getUserId());
		        	}

	                infoToRemove.add(info.getIdentifier());
	                APILocator.getVersionableAPI().saveVersionInfo(info);
				}
	        }
	        try{
	            for (String ident : infoToRemove) {
	                APILocator.getVersionableAPI().removeVersionInfoFromCache(ident);
	            }
	        }catch (Exception e) {
	            throw new DotPublishingException("Unable to remove from cache version info", e);
	        }
    	}
    	catch(Exception e){
    		throw new DotPublishingException(e.getMessage(),e);
    	}
    }
}
