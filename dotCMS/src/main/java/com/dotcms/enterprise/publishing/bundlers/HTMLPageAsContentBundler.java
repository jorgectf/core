package com.dotcms.enterprise.publishing.bundlers;

import com.dotcms.content.elasticsearch.business.ESMappingAPIImpl;
import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.enterprise.publishing.staticpublishing.StaticDependencyBundler;
import com.dotcms.publishing.BundlerStatus;
import com.dotcms.publishing.BundlerUtil;
import com.dotcms.publishing.DotBundleException;
import com.dotcms.publishing.IBundler;
import com.dotcms.publishing.IPublisher;
import com.dotcms.publishing.PublisherConfig;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.common.model.ContentletSearch;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpageasset.model.IHTMLPage;
import com.dotmarketing.portlets.languagesmanager.business.LanguageAPI;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HTMLPageAsContentBundler implements IBundler {

	private PublisherConfig config;
	private LanguageAPI langAPI = null;
	private ContentletAPI conAPI = null;
	private UserAPI uAPI = null;
	private User systemUser = null;

	public final static String  HTMLPAGE_ASSET_EXTENSION = ".html.xml" ;
	
	
	@Override
	public String getName() {
		return "HTMLPage as Content Bundler";
	}
	
	@Override
	public void setConfig(PublisherConfig pc) {
		config = pc;
		langAPI = APILocator.getLanguageAPI();
		conAPI = APILocator.getContentletAPI();
		uAPI = APILocator.getUserAPI();
		try {
			systemUser = uAPI.getSystemUser();
		} catch (DotDataException e) {
			Logger.fatal(HTMLPageAsContentBundler.class,e.getMessage(),e);
		}
	}

    @Override
    public void setPublisher(IPublisher publisher) {
    }

	@Override
	public void generate(File bundleRoot,BundlerStatus status) throws DotBundleException {
	    if(LicenseUtil.getLevel()< LicenseLevel.STANDARD.level)
	        throw new RuntimeException("need an enterprise license to run this bundler");
	    
		
		StringBuilder bob = new StringBuilder("+structuretype:" + Structure.STRUCTURE_TYPE_HTMLPAGE + " ");
		
		if(config.getExcludePatterns() != null && config.getExcludePatterns().size()>0){
			bob.append("-(" );
			for (String p : config.getExcludePatterns()) {
				if(!UtilMethods.isSet(p)){
					continue;
				}
				//p = p.replace(" ", "+");
				bob.append("path:" + p + " ");
			}
			bob.append(")" );
		}else if(config.getIncludePatterns() != null && config.getIncludePatterns().size()>0){
			bob.append("+(" );
			for (String p : config.getIncludePatterns()) {
				if(!UtilMethods.isSet(p)){
					continue;
				}
				//p = p.replace(" ", "+");
				bob.append("path:" + p + " ");
			}
			bob.append(")" );
		} else {
        	// Ignore static-publishing over html-pages that are not part of include patterns (https://github.com/dotCMS/core/issues/10504)
        	if (config.isStatic()) {
        		return;
        	}
		}

		if(config.isIncremental()) {
			Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1900);
			
			Date start;
		    Date end;
		    
		    if(config.getStartDate() != null){
		        start = config.getStartDate();
		    } else {
                start = cal.getTime();                
	        }
		    
		    if(config.getEndDate() != null){
		    	end = config.getEndDate();
		    } else {
		    	end = cal.getTime();
		    }
            
	        	        
	        bob.append(" +versionTs:[" + ESMappingAPIImpl.datetimeFormat.format(start) 
	                + " TO " + ESMappingAPIImpl.datetimeFormat.format(end) +"] ");
		}
		
		
		if(config.getHosts() != null && config.getHosts().size() > 0){
			bob.append(" +(" );
			for(Host h : config.getHosts()){
				bob.append("conhost:" + h.getIdentifier() + " ");
			}
			bob.append(" ) " );
		}

		Logger.info(HTMLPageAsContentBundler.class,bob.toString());

		final long defaultLanguage = langAPI.getDefaultLanguage().getId();
		for (final Long languageId : getSortedConfigLanguages(this.config, defaultLanguage)) {

			String queryLanguage = "+languageid:" + languageId + " ";
			/*
			If we are defaulting to default language we need to unify the current language
			with the default language pages.
			 */
			if (APILocator.getLanguageAPI().canDefaultPageToDefaultLanguage()
					&& !languageId.equals(defaultLanguage)) {
				queryLanguage =
						"+(languageid:" + languageId + " languageid:" + defaultLanguage + ") ";
			}
			processHTMLPages(bundleRoot, bob.toString() + queryLanguage, status, languageId);
		}
	}

	private void processHTMLPages(File bundleRoot, String luceneQuery, BundlerStatus status,
			long languageToProcess) {

		int limit = 200;
		int page = 0;
		status.setTotal(0);
		while(true){

			List<ContentletSearch> contentletSearchList = new ArrayList<>();
		
			try {
				contentletSearchList.addAll(conAPI
						.searchIndex(luceneQuery + " +live:true", limit, page * limit, "inode",
								systemUser, true));
			}
			catch(Exception e){
				Logger.debug(HTMLPageAsContentBundler.class,e.getMessage(),e);
			}
			try{
				if (!config.liveOnly() || config.isIncremental()) {
					contentletSearchList.addAll(conAPI
							.searchIndex(luceneQuery + " +working:true", limit, page * limit,
									"inode", systemUser, true));
				}
			} catch (Exception e) {
				Logger.debug(HTMLPageAsContentBundler.class,e.getMessage(),e);
			}

			/*
			Depending if we are defaulting to default language we could have duplicated identifiers,
			meaning, same page in the current and default language.
			 */
			Set<String> identifiersToProcess = contentletSearchList.stream()
					.map(ContentletSearch::getIdentifier)
					.collect(Collectors.toSet());

			page++;
			
			// no more content
			if (contentletSearchList.size() == 0 || page > 100000000) {
				break;
			}

			status.setTotal(status.getTotal() + contentletSearchList.size());

			//Calculate dependencies for other bundles to use
			new StaticDependencyBundler().getDependencySet(config, "content");

			for (final String identifier : identifiersToProcess) {

                try {

					// Get the page taking into account the DEFAULT_PAGE_TO_DEFAULT_LANGUAGE property
					IHTMLPage htmlPage = APILocator.getHTMLPageAssetAPI().findByIdLanguageFallback
							(identifier, languageToProcess, config.liveOnly(), systemUser, false);

					try {
						Host host = APILocator.getHostAPI().find(((Contentlet) htmlPage).getHost(),
								APILocator.getUserAPI().getSystemUser(), true);

						HTMLPageAsContentWrapper wrap = new HTMLPageAsContentWrapper();
						wrap.setAsset(htmlPage);
						wrap.setInfo(APILocator.getVersionableAPI()
								.getContentletVersionInfo(htmlPage.getIdentifier(),
										((Contentlet) htmlPage).getLanguageId()));
						wrap.setId(APILocator.getIdentifierAPI().find(htmlPage.getIdentifier()));

						//Write the page to disk
						writeFileToDisk(host, languageToProcess, bundleRoot, wrap);
					} catch (Exception e) {
						throw new DotBundleException(
								"cant get host for " + htmlPage + " reason " + e.getMessage());
					}

                    /*///////////////////////////////////////////////
                    //TODO: Fow now we will always force push the HTML Pages. Avoid the case when content on the page
                    //TODO: is changed but not the page itself.
                    //We will check is already pushed only for static.
                    if (contents.isPresent()){
                        //If we are able to add to DependencySet means that page will be pushed and we need to write file.
                        //Send htmlPage.getIdentifier() because Push Publish standard.
                        if ( contents.get().addOrClean(htmlPage.getIdentifier(), htmlPage.getModDate()) ) {
                            writeHTMLPage(bundleRoot, htmlPage);
                        } else {
                            //If we are not able to add to DependencySet means that the file will not be generated and
                            //the status total will be decreased.
                            status.setTotal(status.getTotal() - 1);
                        }
                    } else {
                        //In case is it not static we will always write to disk.
                        writeHTMLPage(bundleRoot, htmlPage);
                    }
                    status.addCount();
                    //End of TODO.
                    ///////////////////////////////////////////////*/

                	status.addCount();

                } catch (Exception e) {
                    Logger.error(HTMLPageAsContentBundler.class,e.getMessage() + " : Unable to write file",e);
                    status.addFailure();
                }
			}
		}
	}

	private void writeFileToDisk(Host host, final long tryingLang, File bundleRoot, HTMLPageAsContentWrapper htmlPageWrapper) throws IOException, DotDataException, DotSecurityException {
		
		boolean live = (htmlPageWrapper.getAsset().getInode().equals(htmlPageWrapper.getInfo().getLiveInode() )) ;

		String liveworking = (htmlPageWrapper.getAsset().getInode()
				.equals(htmlPageWrapper.getInfo().getLiveInode())) ? "live" : "working";
		String myFile = bundleRoot.getPath() + File.separator
				+ liveworking + File.separator
				+ host.getHostname() + File.separator + tryingLang
				+ htmlPageWrapper.getAsset().getURI().replace("/", File.separator)
				+ HTMLPAGE_ASSET_EXTENSION;

		// Should we write or is the file already there:
		Calendar cal = Calendar.getInstance();
		cal.setTime(htmlPageWrapper.getInfo().getVersionTs());
		cal.set(Calendar.MILLISECOND, 0);
		
		String dir = myFile.substring(0, myFile.lastIndexOf(File.separator));
		new File(dir).mkdirs();

		//only write if changed
		File pageFile = new File(myFile);
		if (!pageFile.exists() || pageFile.lastModified() != cal.getTimeInMillis()) {
			if (pageFile.exists()) {
				pageFile.delete(); // unlink possible existing hard link
			}
			BundlerUtil.objectToXML(htmlPageWrapper, pageFile, true);
			pageFile.setLastModified(cal.getTimeInMillis());
		}
		
		boolean deleteFile=config.liveOnly() && config.isIncremental() && !htmlPageWrapper.getAsset().isLive();

		pageFile = new File(myFile.replaceAll(HTMLPAGE_ASSET_EXTENSION, ""));
		if(deleteFile) {
			if (pageFile.isFile()) {
				pageFile.delete();
		    }
			pageFile = new File(bundleRoot.getPath() + File.separator
					+ "live" + File.separator
					+ host.getHostname() + File.separator + tryingLang
					+ htmlPageWrapper.getAsset().getURI().replace("/", File.separator));
			if (pageFile.isFile()) {
				pageFile.delete();
			}
		} else {
			BufferedWriter out = null;
		    try{
				if (!pageFile.exists()) {
					pageFile.createNewFile();
				}
				FileWriter fstream = new FileWriter(pageFile);
				out = new BufferedWriter(fstream);
				final String html = APILocator.getHTMLPageAssetAPI()
						.getHTML(htmlPageWrapper.getAsset().getURI(),
								host, live, htmlPageWrapper.getAsset().getInode(),
								uAPI.getSystemUser(), tryingLang,
								getUserAgent(config));
				out.write(html);
				out.close();
		    }catch(Exception e){
		        try{
		            Logger.error(this, e.getMessage() + " Unable to get page : " + host.getInode() + htmlPageWrapper.getAsset().getURI());
		        }
		        catch(Exception ex){
		            Logger.error(this, e.getMessage(),e);
		        }
		    }
		    finally{
		        if(out !=null){
		            out.close();
		        }
		    }
		}
	}
	
	@Override
	public FileFilter getFileFilter(){
		return new HTMLPageAsContentBundlerFilter();
	}

	public class HTMLPageAsContentBundlerFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			return (pathname.isDirectory() || pathname.getName().endsWith(HTMLPAGE_ASSET_EXTENSION));
		}

	}
	
}