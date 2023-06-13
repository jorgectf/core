package com.dotcms.enterprise.publishing.remote.bundler;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.publisher.business.PublishQueueElement;
import com.dotcms.publisher.pusher.PushPublisherConfig;
import com.dotcms.publisher.pusher.wrapper.UserWrapper;
import com.dotcms.publishing.*;
import com.dotmarketing.beans.UserProxy;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.Role;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.business.UserProxyAPI;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.PushPublishLogger;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.User;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.List;

/**
 * @author Jonathan Gamba
 *         Date: 5/29/13
 */
public class UserBundler implements IBundler {

    private PushPublisherConfig config;
    private UserAPI userAPI;
    private UserProxyAPI userProxyAPI;

    public final static String USER_PREFIX = "user_";
    public final static String FOLDER_USERS = "users";
    public final static String WRAPPER_DESCRIPTOR_EXTENSION = ".user.xml";

    @Override
    public String getName () {
        return "User Bundler";
    }

    @Override
    public void setConfig ( PublisherConfig pc ) {
        config = (PushPublisherConfig) pc;
        userAPI = APILocator.getUserAPI();
        userProxyAPI = APILocator.getUserProxyAPI();
    }

    @Override
    public void setPublisher(IPublisher publisher) {
    }

    /**
     * Prepares all the bundler structure in order to push a user
     *
     * @param bundleRoot
     * @param status
     * @throws com.dotcms.publishing.DotBundleException
     *
     */
    @Override
    public void generate ( File bundleRoot, BundlerStatus status ) throws DotBundleException {

        if ( LicenseUtil.getLevel() < LicenseLevel.PROFESSIONAL.level ) {
            throw new RuntimeException( "need an enterprise pro license to run this bundler" );
        }

        PushPublisherConfig.Operation operation = config.getOperation();

        try {
            List<PublishQueueElement> assets = config.getAssets();
            for ( PublishQueueElement element : assets ) {

                if(!element.getType().equals("user"))
                    continue;
                
                //Getting the user id to publish
                String asset = element.getAsset();
                String userId = asset.replace( USER_PREFIX, "" );

                //Load all the related info to this user id
                User user = userAPI.loadUserById( userId );
                UserProxy userProxy = null;
                List<Address> addresses = null;
                if ( operation.equals( PushPublisherConfig.Operation.PUBLISH ) ) {//We don't need to do unnecessary calls for an UNPUBLISH
                    userProxy = userProxyAPI.getUserProxy( userId, userAPI.getSystemUser(), false );
                    addresses = userAPI.loadUserAddresses( user, userAPI.getSystemUser(), false );
                }
                
                Role userRole = APILocator.getRoleAPI().getUserRole(user);

                //Prepare and Bundle the found user
                UserWrapper wrapper = new UserWrapper( user, userProxy, addresses, userRole );
                wrapper.setOperation( operation );

                //Prepare the file where we are going to write all this user information
                String uri = user.getUserId();
                if ( !uri.endsWith( WRAPPER_DESCRIPTOR_EXTENSION ) ) {
                    uri = uri.replace( WRAPPER_DESCRIPTOR_EXTENSION, "" ).trim();
                    uri += WRAPPER_DESCRIPTOR_EXTENSION;
                }
                String myFileUrl = bundleRoot.getPath() + File.separator + FOLDER_USERS + File.separator + uri;

                File templateFile = new File( myFileUrl );
                templateFile.mkdirs();

                BundlerUtil.objectToXML( wrapper, templateFile, true );
                templateFile.setLastModified( Calendar.getInstance().getTimeInMillis() );

                if ( Config.getBooleanProperty( "PUSH_PUBLISHING_LOG_DEPENDENCIES", false ) ) {
                    PushPublishLogger.log( getClass(), "User bundled for pushing. Operation : " + config.getOperation() + ", User id: " + userId, config.getId() );
                }
            }
        } catch ( Exception e ) {
            status.addFailure();
            throw new DotBundleException( this.getClass().getName() + " : " + "generate()" + e.getMessage() + ": Unable to pull content", e );
        }
    }

    @Override
    public FileFilter getFileFilter () {
        return new UserBundlerFilter();
    }

    public class UserBundlerFilter implements FileFilter {

        @Override
        public boolean accept ( File pathName ) {
            return (pathName.isDirectory() || pathName.getName().endsWith( WRAPPER_DESCRIPTOR_EXTENSION ));
        }

    }

}