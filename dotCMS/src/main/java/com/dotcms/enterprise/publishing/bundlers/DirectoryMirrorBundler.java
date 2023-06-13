package com.dotcms.enterprise.publishing.bundlers;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.publishing.BundlerStatus;
import com.dotcms.publishing.BundlerUtil;
import com.dotcms.publishing.DotBundleException;
import com.dotcms.publishing.IBundler;
import com.dotcms.publishing.IPublisher;
import com.dotcms.publishing.PublisherConfig;
import com.dotmarketing.util.Logger;
import com.liferay.util.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * This bundle will make a copy of a directory using hard links when
 * configuration is "incremental"
 * 
 * @author Michele Mastrogiovanni
 */
public class DirectoryMirrorBundler implements IBundler {

	private PublisherConfig config;
	
	@Override
	public String getName() {
		return "Directory Mirror Bundler";
	}

	@Override
	public void setConfig(PublisherConfig pc) {
		this.config = pc;
	}

    @Override
    public void setPublisher(IPublisher publisher) {
    }

	
	public File getDestinationFile(String destination){
		
		return BundlerUtil.getBundleRoot(destination);
	}


    @Override
    public void generate(File bundleRoot, BundlerStatus status) throws DotBundleException {
        if (LicenseUtil.getLevel() < LicenseLevel.STANDARD.level) {
            throw new RuntimeException("need an enterprise license to run this");
        }

        String destination = config.getDestinationBundle();
        File destinationBundle = getDestinationFile(destination);

        final List<FileFilter> bundlerFilters = new ArrayList<FileFilter>();
        try {
            for (Class clazz : config.getPublishers()) {
                for (Class bundler : ((IPublisher) clazz.newInstance()).getBundlers()) {
                    if (!bundler.equals(DirectoryMirrorBundler.class)) {
                        bundlerFilters.add(((IBundler) bundler.newInstance()).getFileFilter());
                    }
                }
            }
            FileUtil.copyDirectory(bundleRoot, destinationBundle, this.config.isIncremental(), new FileFilter() {
                @Override
                public boolean accept(File file) {
                    try {
                        if (!file.isDirectory()) {
                            if (file.length()==0L){
                                return false;
                            }

                            for (FileFilter filter : bundlerFilters) {
                                if (filter.accept(file)) {
                                    return false;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Logger.warn(this, ex.getMessage(), ex);
                    }
                    return true;
                }
            });
        } catch (Exception ex) {
            throw new DotBundleException(ex.getMessage(), ex);
        }
    }

	@Override
	public FileFilter getFileFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return false;
			}
		};
	}
	
}
