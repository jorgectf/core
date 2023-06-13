package com.dotcms.enterprise.publishing.remote.bundler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.publisher.business.DotPublisherException;
import com.dotcms.publisher.business.PublisherAPI;
import com.dotcms.publisher.pusher.PushPublisherConfig;
import com.dotcms.publisher.pusher.wrapper.ContainerWrapper;
import com.dotcms.publishing.BundlerStatus;
import com.dotcms.publishing.BundlerUtil;
import com.dotcms.publishing.DotBundleException;
import com.dotcms.publishing.IBundler;
import com.dotcms.publishing.IPublisher;
import com.dotcms.publishing.PublisherConfig;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.PushPublishLogger;
import com.liferay.portal.model.User;

public class ContainerBundler implements IBundler {
	private PushPublisherConfig config;
	private User systemUser;
	ContentletAPI conAPI = null;
	UserAPI uAPI = null;

	PublisherAPI pubAPI = null;

	public final static String CONTAINER_EXTENSION = ".container.xml" ;

	@Override
	public String getName() {
		return "Container bundler";
	}

	@Override
	public void setConfig(PublisherConfig pc) {
		config = (PushPublisherConfig) pc;
		conAPI = APILocator.getContentletAPI();
		uAPI = APILocator.getUserAPI();
		pubAPI = PublisherAPI.getInstance();
		try {
			systemUser = uAPI.getSystemUser();
		} catch (DotDataException e) {
			Logger.fatal(ContainerBundler.class,e.getMessage(),e);
		}
	}

    @Override
    public void setPublisher(IPublisher publisher) {
    }

	@Override
	public void generate(File bundleRoot, BundlerStatus status)
			throws DotBundleException {
	    if(LicenseUtil.getLevel() < LicenseLevel.PROFESSIONAL.level)
	        throw new RuntimeException("need an enterprise pro license to run this bundler");

		//Get containers linked with the content
		Set<String> containerIds = config.getContainers();

		try {
			Set<Container> containers = new HashSet<Container>();

			for (String containerId : containerIds) {
				Container working = APILocator.getContainerAPI().
                        getWorkingContainerById(containerId, systemUser, false);
				Container live = APILocator.getContainerAPI().
						getLiveContainerById(containerId, systemUser, false);

			    containers.add(working);

				if(live!=null && InodeUtils.isSet(live.getInode())
				        && !live.getInode().equals(working.getInode())) {
				    // add the live if exists and is different than the working
				    containers.add(live);
				}
			}

			for(Container container : containers) {
				writeContainer(bundleRoot, container);
			}
		} catch (Exception e) {
			status.addFailure();

			throw new DotBundleException(this.getClass().getName() + " : " + "generate()"
			+ e.getMessage() + ": Unable to pull content", e);
		}

	}



	private void writeContainer(File bundleRoot, Container container)
			throws IOException, DotBundleException, DotDataException,
			DotSecurityException, DotPublisherException
	{
		Identifier containerId = APILocator.getIdentifierAPI().find(container.getIdentifier());
		ContainerWrapper wrapper =
				new ContainerWrapper(containerId, container);
		wrapper.setOperation(config.getOperation());
		wrapper.setCvi(APILocator.getVersionableAPI().getVersionInfo(containerId.getId()));

		wrapper.setCsList(APILocator.getContainerAPI().getContainerStructures(container));
		
		String liveworking = container.isLive() ? "live" :  "working";

		String uri = APILocator.getIdentifierAPI()
				.find(container).getURI().replace("/", File.separator);
		if(!uri.endsWith(CONTAINER_EXTENSION)){
			uri.replace(CONTAINER_EXTENSION, "");
			uri.trim();
			uri += CONTAINER_EXTENSION;
		}

		Host h = APILocator.getHostAPI().find(containerId.getHostId(), systemUser, false);

		String myFileUrl = bundleRoot.getPath() + File.separator
				+liveworking + File.separator
				+ h.getHostname() + uri;

		File containerFile = new File(myFileUrl);
		containerFile.mkdirs();

		BundlerUtil.objectToXML(wrapper, containerFile, true);
		containerFile.setLastModified(Calendar.getInstance().getTimeInMillis());

		if(Config.getBooleanProperty("PUSH_PUBLISHING_LOG_DEPENDENCIES", false)) {
			PushPublishLogger.log(getClass(), "Container bundled for pushing. Operation: "+config.getOperation()+", Identifier: "+ container.getIdentifier(), config.getId());
		}
	}

	@Override
	public FileFilter getFileFilter(){
		return new ContainerBundlerFilter();
	}

	public class ContainerBundlerFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {

			return (pathname.isDirectory() || pathname.getName().endsWith(CONTAINER_EXTENSION));
		}

	}

}
