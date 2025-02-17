package com.dotmarketing.portlets.templates.business;

import com.dotcms.IntegrationTestBase;
import com.dotcms.datagen.ContainerDataGen;
import com.dotcms.datagen.ContentletDataGen;
import com.dotcms.datagen.HTMLPageDataGen;
import com.dotcms.datagen.LanguageDataGen;
import com.dotcms.datagen.MultiTreeDataGen;
import com.dotcms.datagen.SiteDataGen;
import com.dotcms.datagen.TemplateAsFileDataGen;
import com.dotcms.datagen.TemplateDataGen;
import com.dotcms.datagen.TestDataUtils;
import com.dotcms.datagen.TestUserUtils;
import com.dotcms.datagen.ThemeDataGen;
import com.dotcms.datagen.UserDataGen;
import com.dotcms.datagen.VariantDataGen;
import com.dotcms.rest.api.v1.template.TemplateView;
import com.dotcms.util.IntegrationTestInitService;
import com.dotcms.variant.VariantAPI;
import com.dotcms.variant.model.Variant;
import com.dotmarketing.beans.ContainerStructure;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.Inode;
import com.dotmarketing.beans.MultiTree;
import com.dotmarketing.beans.Permission;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.business.PermissionAPI.PermissionableType;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.business.VersionableAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.AssetUtil;
import com.dotmarketing.portlets.containers.business.ContainerAPI;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.model.ContentletVersionInfo;
import com.dotmarketing.portlets.fileassets.business.FileAssetAPIImpl;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.htmlpageasset.business.HTMLPageAssetAPI;
import com.dotmarketing.portlets.htmlpageasset.model.HTMLPageAsset;
import com.dotmarketing.portlets.htmlpageasset.model.IHTMLPage;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.portlets.templates.business.TemplateFactory.HTMLPageVersion;
import com.dotmarketing.portlets.templates.design.bean.ContainerUUID;
import com.dotmarketing.portlets.templates.model.FileAssetTemplate;
import com.dotmarketing.portlets.templates.model.Template;
import com.dotmarketing.util.Constants;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.PaginatedArrayList;
import com.dotmarketing.util.UUIDGenerator;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Note: If you dont find a test over here, check {@link com.dotcms.rest.api.v1.template.TemplateResourceTest}
 * several tests were created over there, since the Resource calls the api.
 */
public class TemplateAPITest extends IntegrationTestBase {

    private static ContainerAPI containerAPI;
    private static HostAPI hostAPI;
    private static TemplateAPI templateAPI;
    private static User user;
    private static UserAPI userAPI;
    private static VersionableAPI versionableAPI;
    private static Host host;

    @BeforeClass
    public static void prepare () throws Exception {
    	
        //Setting web app environment
        IntegrationTestInitService.getInstance().init();

        containerAPI   = APILocator.getContainerAPI();
        hostAPI        = APILocator.getHostAPI();
        templateAPI    = APILocator.getTemplateAPI();
        userAPI        = APILocator.getUserAPI();
        user           = userAPI.getSystemUser();
        versionableAPI = APILocator.getVersionableAPI();
        host           = hostAPI.findDefaultHost(user, false);
    }

    @Test
    public void getContainersUUIDFromDrawTemplateBodyTest() throws Exception {

        final String templateBody = "  This is just test<br/>  \n" +
                                    "  #parseContainer   ('f4a02846-7ca4-4e08-bf07-a61366bbacbb','1552493847863')  \n" +
                                    "  <p>This is just test</p>  \n" +
                                    "  #parseContainer   ('/application/containers/test1/','1552493847864')  \n" +
                                    "#parseContainer('/application/containers/test2/','1552493847868')\n" +
                                    "#parseContainer('/application/containers/test3/'     ,'1552493847869'       )\n" +
                                    "#parseContainer(    '/application/containers/test4/',    '1552493847870')\n";

        final TemplateAPI templateAPI = APILocator.getTemplateAPI();
        final List<ContainerUUID> containerUUIDS = templateAPI.getContainersUUIDFromDrawTemplateBody(templateBody);

        assertNotNull(containerUUIDS);
        assertEquals(5, containerUUIDS.size());

        final ContainerUUID containerUUID1 = containerUUIDS.get(0);
        assertEquals("f4a02846-7ca4-4e08-bf07-a61366bbacbb", containerUUID1.getIdentifier());
        assertEquals("1552493847863", containerUUID1.getUUID());

        final ContainerUUID containerUUID2 = containerUUIDS.get(1);
        assertEquals("/application/containers/test1/", containerUUID2.getIdentifier());
        assertEquals("1552493847864", containerUUID2.getUUID());

        final ContainerUUID containerUUID3 = containerUUIDS.get(2);
        assertEquals("/application/containers/test2/", containerUUID3.getIdentifier());
        assertEquals("1552493847868", containerUUID3.getUUID());

        final ContainerUUID containerUUID4 = containerUUIDS.get(3);
        assertEquals("/application/containers/test3/", containerUUID4.getIdentifier());
        assertEquals("1552493847869", containerUUID4.getUUID());

        final ContainerUUID containerUUID5 = containerUUIDS.get(4);
        assertEquals("/application/containers/test4/", containerUUID5.getIdentifier());
        assertEquals("1552493847870", containerUUID5.getUUID());
    }

    @Test
    public void getContainersUUIDFromDrawTemplateBodyDoubleCommasTest() throws Exception {

        final String templateBody = "  This is just test<br/>  \n" +
                "  #parseContainer   (\"f4a02846-7ca4-4e08-bf07-a61366bbacbb\",\"1552493847863\")  \n" +
                "  <p>This is just test</p>  \n" +
                "  #parseContainer   (\"/application/containers/test1/\",'1552493847864\")  \n" +
                "#parseContainer(\"/application/containers/test2/\",'1552493847868')\n" +
                "#parseContainer('/application/containers/test3/'     ,'1552493847869'       )\n" +
                "#parseContainer(    '/application/containers/test4/',    '1552493847870')\n";

        final TemplateAPI templateAPI = APILocator.getTemplateAPI();
        final List<ContainerUUID> containerUUIDS = templateAPI.getContainersUUIDFromDrawTemplateBody(templateBody);

        assertNotNull(containerUUIDS);
        assertEquals(5, containerUUIDS.size());

        final ContainerUUID containerUUID1 = containerUUIDS.get(0);
        assertEquals("f4a02846-7ca4-4e08-bf07-a61366bbacbb", containerUUID1.getIdentifier());
        assertEquals("1552493847863", containerUUID1.getUUID());

        final ContainerUUID containerUUID2 = containerUUIDS.get(1);
        assertEquals("/application/containers/test1/", containerUUID2.getIdentifier());
        assertEquals("1552493847864", containerUUID2.getUUID());

        final ContainerUUID containerUUID3 = containerUUIDS.get(2);
        assertEquals("/application/containers/test2/", containerUUID3.getIdentifier());
        assertEquals("1552493847868", containerUUID3.getUUID());

        final ContainerUUID containerUUID4 = containerUUIDS.get(3);
        assertEquals("/application/containers/test3/", containerUUID4.getIdentifier());
        assertEquals("1552493847869", containerUUID4.getUUID());

        final ContainerUUID containerUUID5 = containerUUIDS.get(4);
        assertEquals("/application/containers/test4/", containerUUID5.getIdentifier());
        assertEquals("1552493847870", containerUUID5.getUUID());
    }

    @Test
    public void getContainersUUIDFromDrawTemplateBodyLegacyTest() throws Exception {

        final String templateBody = "  This is just test<br/>  \n" +
                "  #parseContainer   (\"f4a02846-7ca4-4e08-bf07-a61366bbacbb\")  \n" +
                "  <p>This is just test</p>  \n" +
                "  #parseContainer   (\"/application/containers/test1/\",'1552493847864\")  \n" +
                "#parseContainer(\"/application/containers/test2/\",'1552493847868')\n" +
                "#parseContainer('/application/containers/test3/'    )\n" +
                "#parseContainer(    '/application/containers/test4/',    '1552493847870')\n";

        final TemplateAPI templateAPI = APILocator.getTemplateAPI();
        final List<ContainerUUID> containerUUIDS = templateAPI.getContainersUUIDFromDrawTemplateBody(templateBody);

        assertNotNull(containerUUIDS);
        assertEquals(5, containerUUIDS.size());

        final ContainerUUID containerUUID1 = containerUUIDS.get(0);
        assertEquals("f4a02846-7ca4-4e08-bf07-a61366bbacbb", containerUUID1.getIdentifier());
        assertEquals(ContainerUUID.UUID_LEGACY_VALUE, containerUUID1.getUUID());

        final ContainerUUID containerUUID2 = containerUUIDS.get(1);
        assertEquals("/application/containers/test1/", containerUUID2.getIdentifier());
        assertEquals("1552493847864", containerUUID2.getUUID());

        final ContainerUUID containerUUID3 = containerUUIDS.get(2);
        assertEquals("/application/containers/test2/", containerUUID3.getIdentifier());
        assertEquals("1552493847868", containerUUID3.getUUID());

        final ContainerUUID containerUUID4 = containerUUIDS.get(3);
        assertEquals("/application/containers/test3/", containerUUID4.getIdentifier());
        assertEquals(ContainerUUID.UUID_LEGACY_VALUE, containerUUID4.getUUID());

        final ContainerUUID containerUUID5 = containerUUIDS.get(4);
        assertEquals("/application/containers/test4/", containerUUID5.getIdentifier());
        assertEquals("1552493847870", containerUUID5.getUUID());
    }


    @Test
    public void getContainersUUIDFromDrawTemplateBodyTestNullInput() throws Exception {

        final String templateBody = null;

        final TemplateAPI templateAPI = APILocator.getTemplateAPI();
        final List<ContainerUUID> containerUUIDS = templateAPI.getContainersUUIDFromDrawTemplateBody(templateBody);

        assertNotNull(containerUUIDS);
        assertEquals(0, containerUUIDS.size());
    }

    /**
     * Method to test: saveTemplate
     * Given Scenario: Create a template and save it
     * ExpectedResult: Template should be save successfully
     *
     */
    @Test
    public void test_saveTemplate_newTemplate() throws Exception {
        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();
        final String body="<html><body> I'm mostly empty </body></html>";

        Template template=new Template();
        template.setTitle(title);
        template.setBody(body);
        template = templateAPI.saveTemplate(template, newHostA, user, false);
        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        assertEquals(template.getBody(), body);
        assertEquals(template.getTitle(), title);
    }

    /**
     * Method to test: saveTemplate
     * Given Scenario: Create a template and save it, update the body and save it again.
     * ExpectedResult: Template should be save successfully
     *
     */
    @Test
    public void test_saveTemplate_editExistingTemplate() throws Exception {
        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();
        final String body="<html><body> I'm mostly empty </body></html>";

        Template template=new Template();
        template.setTitle(title);
        template.setBody(body);
        template = templateAPI.saveTemplate(template, newHostA, user, false);
        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        final String templateInode = template.getInode();
        final String templateIdentifier = template.getIdentifier();
        assertEquals(template.getBody(), body);
        assertEquals(template.getTitle(), title);

        final String updatedBody = "updated body!";
        template = templateAPI.findWorkingTemplate(template.getIdentifier(), user, false);
        template.setBody(updatedBody);
        template.setInode("");
        template = templateAPI.saveTemplate(template, newHostA, user, false);
        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        assertEquals(templateIdentifier, template.getIdentifier());
        assertNotEquals(templateInode, template.getInode());
        assertEquals(updatedBody,template.getBody());
        assertEquals(title,template.getTitle());
    }

    /**
     * Method to test: publishTemplate
     * Given Scenario: Create a template, publish it
     * ExpectedResult: Template should be live true
     *
     */
    @Test
    public void publishTemplate_expects_live_true() throws Exception {

        final Host host    = hostAPI.findDefaultHost(user, false);
        final String body  = "<html><body> I'm mostly empty </body></html>";
        final String title = "empty test template "+UUIDGenerator.generateUuid();
        final Template template = new Template();
        template.setTitle(title);
        template.setBody(body);
        final Template templateSaved = templateAPI.saveTemplate(template, host, user, false);
        assertTrue(UtilMethods.isSet(templateSaved.getInode()));
        assertTrue(UtilMethods.isSet(templateSaved.getIdentifier()));
        assertEquals(templateSaved.getBody(), body);
        assertEquals(templateSaved.getTitle(), title);
        assertFalse(templateSaved.isLive());

        templateAPI.publishTemplate(templateSaved, user, false);
        assertTrue(templateSaved.isLive());
    }

    /**
     * Method to test: unpublishTemplate
     * Given Scenario: Create a template, publish and unpublish it
     * ExpectedResult: Template should be live false at the end
     *
     */
    @Test
    public void unpublishTemplate_expects_live_false() throws Exception {

        final Host host    = hostAPI.findDefaultHost(user, false);
        final String body  = "<html><body> I'm mostly empty </body></html>";
        final String title = "empty test template "+UUIDGenerator.generateUuid();
        final Template template = new Template();
        template.setTitle(title);
        template.setBody(body);
        final Template templateSaved = templateAPI.saveTemplate(template, host, user, false);
        assertTrue(UtilMethods.isSet(templateSaved.getInode()));
        assertTrue(UtilMethods.isSet(templateSaved.getIdentifier()));
        assertEquals(templateSaved.getBody(), body);
        assertEquals(templateSaved.getTitle(), title);
        assertFalse(templateSaved.isLive());

        templateAPI.publishTemplate(templateSaved, user, false);
        assertTrue(templateSaved.isLive());

        templateAPI.unpublishTemplate(templateSaved, user, false);
        assertFalse(templateSaved.isLive());
    }

    /**
     * Method to test: archive
     * Given Scenario: Create a template, archive
     * ExpectedResult: Template should be archive true
     *
     */
    @Test
    public void archiveTemplate_expects_archive_true() throws Exception {

        final Host host    = hostAPI.findDefaultHost(user, false);
        final String body  = "<html><body> I'm mostly empty </body></html>";
        final String title = "empty test template "+UUIDGenerator.generateUuid();
        final Template template = new Template();
        template.setTitle(title);
        template.setBody(body);
        final Template templateSaved = templateAPI.saveTemplate(template, host, user, false);
        assertTrue(UtilMethods.isSet(templateSaved.getInode()));
        assertTrue(UtilMethods.isSet(templateSaved.getIdentifier()));
        assertEquals(templateSaved.getBody(), body);
        assertEquals(templateSaved.getTitle(), title);
        assertFalse(templateSaved.isLive());

        templateAPI.archive(templateSaved, user, false);
        assertTrue(templateSaved.isArchived());
    }

    /**
     * Method to test:  unarchive
     * Given Scenario: Create a template, archive and unarchive
     * ExpectedResult: Template should be archive false
     *
     */
    @Test
    public void archiveTemplate_expects_unarchive_true() throws Exception {

        final Host host    = hostAPI.findDefaultHost(user, false);
        final String body  = "<html><body> I'm mostly empty </body></html>";
        final String title = "empty test template "+UUIDGenerator.generateUuid();
        final Template template = new Template();
        template.setTitle(title);
        template.setBody(body);
        final Template templateSaved = templateAPI.saveTemplate(template, host, user, false);
        assertTrue(UtilMethods.isSet(templateSaved.getInode()));
        assertTrue(UtilMethods.isSet(templateSaved.getIdentifier()));
        assertEquals(templateSaved.getBody(), body);
        assertEquals(templateSaved.getTitle(), title);
        assertFalse(templateSaved.isLive());

        templateAPI.archive(templateSaved, user, false);
        assertTrue(templateSaved.isArchived());

        templateAPI.unarchive(templateSaved,user);
        assertFalse(templateSaved.isArchived());
    }

    @Test
    public void delete() throws Exception {
        Host host= hostAPI.findDefaultHost(user, false);

        // a container to use inside the template
        Container container = new Container();
        container.setFriendlyName("test container");
        container.setTitle("his is the title");
        container.setMaxContentlets(5);
        container.setPreLoop("preloop code");
        container.setPostLoop("postloop code");
        Structure st=CacheLocator.getContentTypeCache().getStructureByVelocityVarName("host");

        List<ContainerStructure> csList = new ArrayList<>();
        ContainerStructure cs = new ContainerStructure();
        cs.setStructureId(st.getInode());
        cs.setCode("this is the code");
        csList.add(cs);
        container = containerAPI.save(container, csList, host, user, false);


        String body="<html><body> #parseContainer('"+container.getIdentifier()+"') </body></html>";
        String title="empty test template "+UUIDGenerator.generateUuid();

        Template template=new Template();
        template.setTitle(title);
        template.setBody(body);

        final Template saved= templateAPI.saveTemplate(template, host, user, false);

        final String tInode=template.getInode(),tIdent=template.getIdentifier();

        templateAPI.delete(saved, user, false);

        AssetUtil.assertDeleted(tInode, tIdent, "template");

        containerAPI.delete(container, user, false);

        AssetUtil.assertDeleted(container.getInode(),container.getIdentifier(), Inode.Type.CONTAINERS.getValue());
    }

    @Test
    public void findPagesByTemplate() throws Exception {
        User user=APILocator.getUserAPI().getSystemUser();
        Host host=APILocator.getHostAPI().findDefaultHost(user, false);

        Template template = null;
        Folder folder = null;
        HTMLPageAsset page = null;

        try {
            //Create a Template
            template = new Template();
            template.setTitle("Title");
            template.setBody("Body");
            template = APILocator.getTemplateAPI().saveTemplate(template, host, user, false);

            //Create a Folder
            folder = APILocator.getFolderAPI().createFolders(
                    "/test_junit/test_" + UUIDGenerator.generateUuid().replaceAll("-", "_"), host,
                    user, false);

            //Create a Page inside the Folder assigned to the newly created Template
            page = new HTMLPageDataGen(folder, template).nextPersisted();


            //wait a second before attempting to search the pages with elastic search
            //APILocator.getContentletAPI().isInodeIndexed(page.getInode());

            //Find pages by template
            List<Contentlet> pages = APILocator.getHTMLPageAssetAPI()
                    .findPagesByTemplate(template, user, false);
            assertFalse(pages.isEmpty()); //Should contain dependencies
            assertTrue(pages.size() == 1); //Only one dependency, the page we just created
            assertEquals(page.getInode(), pages.get(0).getInode()); //Page inode should be the same

            //Delete the page
            HTMLPageDataGen.remove(page);
            page = null;
            //Now find again pages by template
            pages = APILocator.getHTMLPageAssetAPI().findPagesByTemplate(template, user, false);
            assertTrue(pages.isEmpty()); //Should NOT contain dependencies

        } finally {
            //Clean up
            if (page != null) {
                HTMLPageDataGen.remove(page);
            }
            if (template != null) {
                APILocator.getTemplateAPI().delete(template, user, false);
            }
            if (folder != null) {
                APILocator.getFolderAPI().delete(folder, user, false);
            }
        }

    }

    /**
     * Method to test: {@link TemplateAPI#findLiveTemplate(String, User, boolean)}
     * Given Scenario: Saves a new template and checks if is Live (false), then publish the template
     * and check again if is live (true).
     * ExpectedResult: template after been published should be live = true
     */
    @Test
    public void findLiveTemplate() throws Exception {

        Template template=new Template();
        template.setTitle("empty test template "+UUIDGenerator.generateUuid());
        template.setBody("<html><body> I'm mostly empty </body></html>");
        template= templateAPI.saveTemplate(template, host, user, false);

        Template live = templateAPI
                .findLiveTemplate(template.getIdentifier(), user, false);
        assertNull(live);//template has not been published

        //Publish template
        versionableAPI.setLive(template);

        live = templateAPI.findLiveTemplate(template.getIdentifier(), user, false);
        assertNotNull(live);//template has been published
        assertEquals(template.getInode(),live.getInode());//inode live is the template inode
        assertFalse(live.getOwner().isEmpty());//check owner was pulled
        assertFalse(live.getIDate().toString().isEmpty());//check idate was pulled

        templateAPI.delete(template, user, false);
    }

    /**
     * Method to test: {@link TemplateAPI#findTemplates(User, boolean, Map, String, String, String, String, int, int, String)}
     * Given Scenario: Saves a new template and finds all the templates the user can use
     * ExpectedResult: list of templates the user can use, it must contain the template created for this test
     */
    @Test
    public void testFindTemplates() throws DotDataException, DotSecurityException {
        final String title = "testTemplate_" + System.currentTimeMillis();
        final Template template = new TemplateDataGen().title(title).nextPersisted();

        final List<Template> result = templateAPI
                .findTemplates(user, false, null, host.getIdentifier(), null, null, null, 0, -1,
                        null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(template));
        assertFalse(result.get(0).getOwner().isEmpty());//check owner was pulled
        assertFalse(result.get(0).getIDate().toString().isEmpty());//check idate was pulled

    }

    /**
     * Method to test: {@link TemplateFactory#findWorkingTemplateByName(String, Host)}
     * Given Scenario: Saves a new template and find the working version of it using the template.title and host
     * ExpectedResult: working version of the template
     */
    @Test
    public void testFindWorkingTemplateByName() throws Exception {
        final String title = "testTemplate_" + System.currentTimeMillis();
        final Template template_version1 = new TemplateDataGen().title(title).host(host).nextPersisted();
        Thread.sleep(1000);
        template_version1.setTitle(title + "_1");
        template_version1.setInode("");
        final Template template_version2 = TemplateDataGen.save(template_version1);

        final Template result = FactoryLocator.getTemplateFactory().findWorkingTemplateByName(title + "_1", host);
        assertNotNull(result);
        assertEquals(template_version2.getInode(), result.getInode());
        assertFalse(result.getOwner().isEmpty());//check owner was pulled
        assertFalse(result.getIDate().toString().isEmpty());//check idate was pulled
    }


    /**
     * Method to test: {@link TemplateAPI#findWorkingTemplate(String, User, boolean)}
     * Given Scenario: Tries to find a non-existent working template
     * ExpectedResult: null template
     */
    @Test
    public void testFindWorkingTemplateNoNPE() throws DotDataException, DotSecurityException {
        try {
            final Template template = templateAPI.findWorkingTemplate("NO_TEMPLATE",
                    APILocator.getUserAPI().getSystemUser(), false);

            assertNull(template);

        }
        catch(NullPointerException e) {
            Logger.error(this, "getting non-existent template should not throw an NPE", e);
            assertTrue("getting non-existent template should not throw an NPE", false);
        }
    }

    /**
     * Method to test: {@link TemplateAPI#findWorkingTemplate(String, User, boolean)}
     * Given Scenario: Tries to find a non-existent live template
     * ExpectedResult: null template
     */
    @Test
    public void testFindLiveTemplateNoNPE() throws DotDataException, DotSecurityException {


        try {
            final Template template = templateAPI.findLiveTemplate("NO_TEMPLATE",
                    APILocator.getUserAPI().getSystemUser(), false);

            assertNull(template);
            
        }
        catch(NullPointerException e) {
            Logger.error(this, "getting non-existent template should not throw an NPE", e);
            assertTrue("getting non-existent template should not throw an NPE", false);
        }
    }

    /**
     * Method to test: {@link TemplateAPI#findTemplatesAssignedTo(Host,boolean)}
     * Given Scenario: Finds all templates under a host
     * ExpectedResult: list of templates that live under a host
     */
    @Test
    public void testFindTemplatesAssignedTo() throws DotDataException {
        final String title = "testTemplate_" + System.currentTimeMillis();
        final Template template = new TemplateDataGen().title(title).nextPersisted();

        final List<Template> result = templateAPI.findTemplatesAssignedTo(host);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(template));
        assertFalse(result.get(0).getOwner().isEmpty());//check owner was pulled
        assertFalse(result.get(0).getIDate().toString().isEmpty());//check idate was pulled
    }

    /**
     * Method to test: {@link TemplateAPI#copy(Template, User)}
     * Given Scenario: Creates a template and then a copy of it
     * ExpectedResult: template copied successfully
     */
    @Test
    public void copyTemplate() throws Exception {
        Template oldTemplate = null;
        Template newTemplate = null;

        try {
            //Create a Template.
            oldTemplate = new Template();
            oldTemplate.setTitle("Title");
            oldTemplate.setBody("<html><body> I'm mostly empty </body></html>");
            oldTemplate = templateAPI.saveTemplate(oldTemplate, host, user, false);

            //Copy Template.
            newTemplate = templateAPI.copy(oldTemplate, user);

            //Body should be the same.
            Assert.assertEquals(oldTemplate.getBody(), newTemplate.getBody());

            //Name, identifier, inode and mod_date shouldn't be the same.
            Assert.assertNotEquals(oldTemplate.getTitle(), newTemplate.getTitle());
            Assert.assertNotEquals(oldTemplate.getIdentifier(), newTemplate.getIdentifier());
            Assert.assertNotEquals(oldTemplate.getInode(), newTemplate.getInode());
            Assert.assertNotEquals(oldTemplate.getModDate(), newTemplate.getModDate());

        } finally {
            //Clean up.
            if (oldTemplate != null) {
                templateAPI.delete(oldTemplate, user, false);
            }
            if (newTemplate != null) {
                templateAPI.delete(newTemplate, user, false);
            }
        }
    }

    @Test
    public void testFindTemplatesNoLayout () throws Exception {
        Template template = null;
        Template layout = null;
        try {

            template = new Template();
            template.setTitle("Template Title");
            template.setBody("<html><body> Empty Template </body></html>");
            template = templateAPI.saveTemplate(template, host, user, false);

            layout = new Template();
            layout.setTitle(Template.ANONYMOUS_PREFIX + System.currentTimeMillis());
            layout.setBody("<html><body> Empty Layout </body></html>");
            layout = templateAPI.saveTemplate(layout, host, user, false);

            //This method should only return Templates, no Layouts
            List<Template> templates = APILocator.getTemplateAPI().findTemplates(user, false, null, null, null,
                                                        null, null, 0, 1000, null);

            assertFalse(templates.isEmpty());
            for (final Template temp : this.removeSystemTemplate(templates)) {
                assertTrue(temp.isTemplate());
            }

            //This method should only return Templates, no Layouts
            templates = templateAPI.findTemplatesUserCanUse(user, null, null, false,
                                                        0, 1000);

            assertFalse(templates.isEmpty());
            for (final Template temp : this.removeSystemTemplate(templates)) {
                assertTrue(temp.isTemplate());
            }

        } finally {
            if (template != null) {
                templateAPI.delete(template, user, false);
            }
            if (layout != null) {
                templateAPI.delete(layout, user, false);
            }
        }
    }

    private List<Template> removeSystemTemplate(final List<Template> paginatedArrayList) {

        final List<Template> paginatedArrayListWithoutSystemTemplate = new ArrayList<>();

        for(Template templateObject : paginatedArrayList) {

            if (!Template.SYSTEM_TEMPLATE.equals(templateObject.getIdentifier())) {

                paginatedArrayListWithoutSystemTemplate.add(templateObject);
            }
        }

        return paginatedArrayListWithoutSystemTemplate;
    }

    /**
     * Method to test: {@link TemplateAPI#findTemplatesUserCanUse(User, String, String, boolean, int, int)}
     * Given Scenario: Finds all the templates an user can use that matches the filter
     * ExpectedResult: list of templates that live under a host
     */
    @Test
    public void testFindTemplatesUserCanUse_IncludeUniqueFilter_ShouldListOnlyOneResult() throws Exception {
        Template template = null;
        Template anotherTemplate = null;
        try {

            template = new Template();
            final String uniqueString = UUIDGenerator.generateUuid();
            final String uniqueTitle =  uniqueString + " This one will show up";
            template.setTitle(uniqueTitle);
            template.setBody("<html><body> Empty Template </body></html>");
            template = templateAPI.saveTemplate(template, host, user, false);

            anotherTemplate = new Template();
            anotherTemplate.setTitle("I am not invited");
            anotherTemplate.setBody("<html><body> Empty Template </body></html>");
            anotherTemplate = templateAPI.saveTemplate(anotherTemplate, host, user, false);

            final List<Template> filteredTemplates = APILocator.getTemplateAPI().findTemplatesUserCanUse(user, host.getIdentifier(), uniqueString, true,0, 1000);
            final List<Template> filteredTemplatesWithoutSystemTemplate = this.removeSystemTemplate(filteredTemplates);
            assertEquals(1, filteredTemplatesWithoutSystemTemplate.size());
            assertEquals(uniqueTitle, filteredTemplatesWithoutSystemTemplate.get(0).getTitle());
            assertFalse(filteredTemplatesWithoutSystemTemplate.get(0).getOwner().isEmpty());//check owner was pulled
            assertFalse(filteredTemplatesWithoutSystemTemplate.get(0).getIDate().toString().isEmpty());//check idate was pulled

        } finally {
            if (template != null) {
                templateAPI.delete(template, user, false);
            }
            if (anotherTemplate != null) {
                templateAPI.delete(anotherTemplate, user, false);
            }
        }
    }

    /**
     * Method to test: {@link TemplateAPI#find(String, User, boolean)}
     * Given Scenario: find a template by inode
     * ExpectedResult: the template of the specific inode
     */
    @Test
    public void test_find_success() throws Exception{
        final String title = "testFindTemplate_" + System.currentTimeMillis();
        final Template template = new TemplateDataGen().title(title).nextPersisted();

        //Remove template cache so it's has to search in the DB
        CacheLocator.getTemplateCache().clearCache();

        final Template templateFound = templateAPI.find(template.getInode(),user,false);

        assertEquals(title,templateFound.getTitle());
        assertFalse(templateFound.getOwner().isEmpty());//check owner was pulled
        assertFalse(templateFound.getIDate().toString().isEmpty());//check idate was pulled
    }

    /**
     * Method to test: {@link TemplateAPI#find(String, User, boolean)}
     * Given Scenario: tries to find a template, but the inode does not exists.
     * ExpectedResult: null
     */
    @Test
    public void test_find_inode_not_exist_return_Null() throws Exception{
       final Template templateFound = templateAPI.find(UUIDGenerator.generateUuid(),user,false);
       assertNull(templateFound);
    }

    /**
     * Method to test: {@link TemplateAPI#findAllVersions(Identifier, User, boolean)}
     * Given Scenario: brings all the versions of a template, using the identifier as reference
     * ExpectedResult: list of templates, the size must be 3 since its the amount of versions
     */
    @Test
    public void test_findAllVersions_success() throws Exception {
        final String title = "testFindTemplate_" + System.currentTimeMillis();
        Template template = new TemplateDataGen().title(title).nextPersisted();
        template.setTitle(title + "_1");
        template.setInode("");
        template = TemplateDataGen.save(template);
        template.setTitle(title + "_2");
        template.setInode("");
        template = TemplateDataGen.save(template);

        final Identifier identifier = APILocator.getIdentifierAPI().find(template.getIdentifier());
        final List<Template> templateAllVersions = templateAPI.findAllVersions(identifier,user,false);
        assertNotNull(templateAllVersions);
        assertEquals(3,templateAllVersions.size());
        assertFalse(templateAllVersions.get(0).getOwner().isEmpty());//check owner was pulled
        assertFalse(templateAllVersions.get(0).getIDate().toString().isEmpty());//check idate was pulled
    }

    /**
     * Method to test: {@link TemplateAPI#findAllVersions(Identifier, User, boolean)}
     * Given Scenario: brings all the versions of a template, using the identifier as reference,
     * since the identifier does not belong to any template, the list must be empty
     * ExpectedResult: empty list of templates
     */
    @Test
    public void test_findAllVersions_IdentifierNotBelongToTemplate_returnEmptyList() throws Exception {
        final Identifier identifier = new Identifier();
        identifier.setId(UUIDGenerator.generateUuid());
        final List<Template> templateAllVersions = templateAPI.findAllVersions(identifier,user,false);
        assertTrue(templateAllVersions.isEmpty());
    }

    /**
     * Method to test: {@link TemplateAPI#saveTemplate(Template, Host, User, boolean)}
     * Given Scenario: Saves a new template and checks that the owner and create_date
     * is being save on the identifier table
     * ExpectedResult: owner and create_date columns on the identifier tables must have values
     */
    @Test
    public void test_newTemplate_checkIfSavesOwnerAndCreateDateOnIdentifier() throws Exception {
        final String title = "testTemplate_" + System.currentTimeMillis();
        final Template template = new TemplateDataGen().title(title).nextPersisted();
        final Identifier identifier = APILocator.getIdentifierAPI().find(template.getIdentifier());
        assertNotNull(identifier);
        assertNotNull(identifier.getOwner());
        assertNotNull(identifier.getCreateDate());
        assertFalse(identifier.getOwner().isEmpty());
        assertFalse(identifier.getCreateDate().toString().isEmpty());
    }

    /**
     * Method to test: {@link TemplateAPI#saveTemplate(Template, Host, User, boolean)}
     * Given Scenario: Tries to save a new template using a limited user that does not have the required permissions
     * ExpectedResult: template is not saved and a DotSecurityException is thrown.
     */
    @Test(expected = DotSecurityException.class)
    public void test_saveTemplate_limitedUser_noEnoughPermissions_fail() throws Exception{
        //Create the limited user
        final User limitedUser = new UserDataGen().roles(TestUserUtils.getFrontendRole(), TestUserUtils.getBackendRole()).nextPersisted();
        APILocator.getUserAPI().save(limitedUser,APILocator.systemUser(),false);

        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();
        //Create template
        Template templateA = new TemplateDataGen().title(title).next();
        templateA = APILocator.getTemplateAPI().saveTemplate(templateA,newHostA,limitedUser,false);
    }

    /**
     * Method to test: {@link TemplateAPI#saveTemplate(Template, Host, User, boolean)}
     * Given Scenario: Tries to save a new template using a limited user that have the required permissions
     * ExpectedResult: template is saved.
     */
    @Test
    public void test_saveTemplate_limitedUser_success() throws Exception{
        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();

        //Create the limited user
        final User limitedUser = new UserDataGen().roles(TestUserUtils.getFrontendRole(), TestUserUtils.getBackendRole()).nextPersisted();
        APILocator.getUserAPI().save(limitedUser,APILocator.systemUser(),false);

        //Give Permissions Over the Host Can Add children
        Permission permissions = new Permission(PermissionAPI.INDIVIDUAL_PERMISSION_TYPE,
                newHostA.getPermissionId(),
                APILocator.getRoleAPI().loadRoleByKey(limitedUser.getUserId()).getId(),
                PermissionAPI.PERMISSION_CAN_ADD_CHILDREN, true);
        APILocator.getPermissionAPI().save(permissions, newHostA, user, false);
        //Give Permissions Over the Host READ/EDIT Templates
        permissions = new Permission(PermissionableType.TEMPLATES.getCanonicalName(),
                newHostA.getPermissionId(),
                APILocator.getRoleAPI().loadRoleByKey(limitedUser.getUserId()).getId(),
                PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT, true);
        APILocator.getPermissionAPI().save(permissions, newHostA, user, false);

        //Create template
        Template templateA = new TemplateDataGen().title(title).next();
        templateA = APILocator.getTemplateAPI().saveTemplate(templateA,newHostA,limitedUser,false);

        assertEquals(1,templateAPI.findTemplatesAssignedTo(newHostA).size());
        assertEquals(title,templateAPI.findTemplatesAssignedTo(newHostA).get(0).getTitle());
    }

    /**
     * Method to test: {@link TemplateAPI#getTemplateHost(Template)}
     * Given Scenario: Saves a new template as a File and tries to get the Host of it.
     * ExpectedResult: host where the template lives.
     */
    @Test
    public void getTemplateHost_fileTemplate_success() throws Exception {

        final Host host = new SiteDataGen().nextPersisted();

        final FileAssetTemplate fileAssetTemplate = new TemplateAsFileDataGen()
                .host(host)
                .nextPersisted();

        final FileAssetTemplate template = FileAssetTemplate.class.cast(APILocator.getTemplateAPI()
                .findWorkingTemplate(fileAssetTemplate.getIdentifier(),user,false));

        assertTrue(template.getIdentifier().contains(Constants.TEMPLATE_FOLDER_PATH));

        final Host templateHost = templateAPI.getTemplateHost(template);

        assertNotNull(templateHost);
        assertEquals(host.getIdentifier(),templateHost.getIdentifier());
        assertEquals(fileAssetTemplate.getIdentifier(),template.getIdentifier());
    }

    /**
     * Method to test: {@link TemplateAPI#findWorkingTemplate(String, User, boolean)}
     * Given Scenario: Saves a new template as a File and tries to get the working version of it.
     * ExpectedResult: working version of a fileTemplate
     */
    @Test
    public void findWorkingTemplate_fileTemplate_success() throws Exception {

        final Host host = new SiteDataGen().nextPersisted();

        final FileAssetTemplate fileAssetTemplate = new TemplateAsFileDataGen()
                .host(host)
                .nextPersisted();

        final Template template = templateAPI
                .findWorkingTemplate(fileAssetTemplate.getIdentifier(),user,false);

        assertNotNull(template);
        assertTrue(template.getIdentifier().contains(Constants.TEMPLATE_FOLDER_PATH));
        assertEquals(fileAssetTemplate.getIdentifier(),template.getIdentifier());
    }

    /**
     * Method to test: {@link TemplateAPI#findLiveTemplate(String, User, boolean)}
     * Given Scenario: Saves a new template as a File, make it live and tries to get the live version of it.
     * ExpectedResult: live version of a fileTemplate
     */
    @Test
    public void findLiveTemplate_fileTemplate_success() throws Exception {

        final Host host = new SiteDataGen().nextPersisted();

        final FileAssetTemplate fileAssetTemplate = new TemplateAsFileDataGen()
                .host(host)
                .nextPersisted();
        templateAPI.setLive(fileAssetTemplate);

        final Template template = templateAPI
                .findLiveTemplate(fileAssetTemplate.getIdentifier(),user,false);

        assertNotNull(template);
        assertTrue(template.getIdentifier().contains(Constants.TEMPLATE_FOLDER_PATH));
        assertEquals(fileAssetTemplate.getIdentifier(),template.getIdentifier());
    }

    /**
     * Method to test: {@link TemplateAPI#getTemplateByFolder(Folder, Host, User, boolean)}
     * Given Scenario: Saves a new template as a File, and tries to get the template using the folder
     * where the files lives.
     * ExpectedResult: the fileTemplate created
     */
    @Test
    public void getTemplateByFolder_fileTemplate_success() throws Exception {

        final Host host = new SiteDataGen().nextPersisted();

        final FileAssetTemplate fileAssetTemplate = new TemplateAsFileDataGen()
                .host(host)
                .nextPersisted();

        final Folder templateFolder = APILocator.getFolderAPI()
                .findFolderByPath(fileAssetTemplate.getPath(), host, user, false);

        final Template template = templateAPI
                .getTemplateByFolder(templateFolder,host,user,false);

        assertNotNull(template);
        assertTrue(template.getIdentifier().contains(Constants.TEMPLATE_FOLDER_PATH));
        assertEquals(fileAssetTemplate.getIdentifier(),template.getIdentifier());
    }

    /**
     * Method to test: {@link TemplateAPI#saveDraftTemplate(Template, Host, User, boolean)}
     * Given Scenario: Saves a new template if the identifier does not exists.
     * ExpectedResult: Template successfully saved.
     */
    @Test
    public void saveDraftTemplate_identifierNotExists_success()
            throws DotSecurityException, DotDataException {
        final Host newHost = new SiteDataGen().nextPersisted();
        final String title = "Template" + System.currentTimeMillis();
        final String body="<html><body> I'm mostly empty </body></html>";

        Template template=new Template();
        template.setTitle(title);
        template.setBody(body);
        template = templateAPI.saveDraftTemplate(template, newHost, user, false);

        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        assertEquals(template.getBody(), body);
        assertEquals(template.getTitle(), title);
    }

    /**
     * Method to test: {@link TemplateAPI#saveDraftTemplate(Template, Host, User, boolean)}
     * Given Scenario: Saves a new template version if the inode does not exists.
     * ExpectedResult: Template version successfully saved.
     */
    @Test
    public void saveDraftTemplate_inodeNotExists_success()
            throws DotSecurityException, DotDataException {
        final Host newHost = new SiteDataGen().nextPersisted();
        final String title = "Template" + System.currentTimeMillis();
        final String body="<html><body> I'm mostly empty </body></html>";

        Template template=new Template();
        template.setTitle(title);
        template.setBody(body);
        template = templateAPI.saveDraftTemplate(template, newHost, user, false);
        final String templateOriginalInode = template.getInode();

        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        assertEquals(template.getBody(), body);
        assertEquals(template.getTitle(), title);

        //new version
        template.setTitle(title + "_UPDATED");
        template.setInode("");
        template = templateAPI.saveDraftTemplate(template, newHost, user, false);

        assertTrue(UtilMethods.isSet(template.getInode()));
        assertTrue(UtilMethods.isSet(template.getIdentifier()));
        assertEquals(template.getTitle(), title + "_UPDATED");
        assertNotEquals(templateOriginalInode,template.getInode());
    }

    /**
     * Method to test: {@link TemplateAPI#saveDraftTemplate(Template, Host, User, boolean)}
     * Given Scenario: If the user calling the saveDraftTemplate method is not the same that last
     *                  updated the template a new version will be created.
     * ExpectedResult: Template version successfully saved.
     */
    @Test
    public void saveDraftTemplate_lastUpdatedUserIsNotTheSame_success()
            throws DotDataException, DotSecurityException {
        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();

        //Create the limited user
        final User limitedUser = new UserDataGen().roles(TestUserUtils.getFrontendRole(), TestUserUtils.getBackendRole()).nextPersisted();
        APILocator.getUserAPI().save(limitedUser,APILocator.systemUser(),false);

        //Give Permissions Over the Host Can Add children
        Permission permissions = new Permission(PermissionAPI.INDIVIDUAL_PERMISSION_TYPE,
                newHostA.getPermissionId(),
                APILocator.getRoleAPI().loadRoleByKey(limitedUser.getUserId()).getId(),
                PermissionAPI.PERMISSION_CAN_ADD_CHILDREN, true);
        APILocator.getPermissionAPI().save(permissions, newHostA, user, false);
        //Give Permissions Over the Host READ/EDIT Templates
        permissions = new Permission(PermissionableType.TEMPLATES.getCanonicalName(),
                newHostA.getPermissionId(),
                APILocator.getRoleAPI().loadRoleByKey(limitedUser.getUserId()).getId(),
                PermissionAPI.PERMISSION_READ | PermissionAPI.PERMISSION_EDIT, true);
        APILocator.getPermissionAPI().save(permissions, newHostA, user, false);

        //Create template
        Template templateA = new TemplateDataGen().title(title).next();
        templateA = APILocator.getTemplateAPI().saveTemplate(templateA,newHostA,limitedUser,false);
        final String templateOriginalInode = templateA.getInode();

        //new version using system user
        templateA.setTitle(title + "_UPDATED");
        templateA.setInode("");
        templateA = templateAPI.saveDraftTemplate(templateA, newHostA, user, false);

        assertTrue(UtilMethods.isSet(templateA.getInode()));
        assertTrue(UtilMethods.isSet(templateA.getIdentifier()));
        assertEquals(templateA.getTitle(), title + "_UPDATED");
        assertNotEquals(templateOriginalInode,templateA.getInode());
    }

    /**
     * Method to test: {@link TemplateAPI#saveDraftTemplate(Template, Host, User, boolean)}
     * Given Scenario: Calling the saveDraftTemplate using the same user, id and inode, shouldn't
     *                  create a new version.
     * ExpectedResult: Template saved successfully but keep the inode
     */
    @Test
    public void saveDraftTemplate_success() throws DotSecurityException, DotDataException {
        final String title = "Template" + System.currentTimeMillis();
        final Host newHostA = new SiteDataGen().nextPersisted();

        //Create template
        Template templateA = new TemplateDataGen().title(title).next();
        templateA = APILocator.getTemplateAPI().saveDraftTemplate(templateA,newHostA,user,false);
        final String templateOriginalInode = templateA.getInode();

        //new Draft should keep the same inode
        templateA.setTitle(title + "_UPDATED");
        templateA = templateAPI.saveDraftTemplate(templateA, newHostA, user, false);

        assertTrue(UtilMethods.isSet(templateA.getInode()));
        assertTrue(UtilMethods.isSet(templateA.getIdentifier()));
        assertEquals(templateA.getTitle(), title + "_UPDATED");
        assertEquals(templateOriginalInode,templateA.getInode());
    }

    /**
     * Method to test: {@link TemplateAPI#getContainersInTemplate(Template, User, boolean)}
     * Given Scenario: Finds the containers that are being used by the template.
     * ExpectedResult: list of containers.
     */
    @Test
    public void test_getContainersInTemplate_success() throws Exception{

        final Host newHost = new SiteDataGen().nextPersisted();

        //Create a container for the given contentlet
        final Container container = new ContainerDataGen().site(newHost)
                .nextPersisted();

        //Create a template
        final Template template = new TemplateDataGen().site(newHost)
                .withContainer(container.getIdentifier())
                .nextPersisted();

        //Create page
        final HTMLPageAsset page = new HTMLPageDataGen(newHost, template)
                .languageId(APILocator.getLanguageAPI().getDefaultLanguage().getId()).nextPersisted();

        //Create content
        final Contentlet contentlet = TestDataUtils
                .getGenericContentContent(true,APILocator.getLanguageAPI().getDefaultLanguage().getId(),newHost);

        //Add content to page
        new MultiTreeDataGen()
                .setPage(page)
                .setContainer(container)
                .setContentlet(contentlet)
                .setInstanceID(UUIDGenerator.shorty())
                .setPersonalization(MultiTree.DOT_PERSONALIZATION_DEFAULT)
                .setTreeOrder(1)
                .nextPersisted();

        assertEquals(1,APILocator.getTemplateAPI().getContainersInTemplate(template,user,false).size());
    }

    /**
     * Method to test: {@link TemplateAPIImpl#setThemeName(Template, User, boolean)}
     * When: The theme exists
     * Should: Call the {@link Template#setThemeName(String)}
     *
     * @throws DotDataException
     * @throws DotSecurityException
     */
    @Test
    public void setThemeName() throws DotDataException, DotSecurityException {
        final Template template = new TemplateDataGen().nextPersisted();
        final Contentlet contentlet = new ThemeDataGen().nextPersisted();

        template.setTheme(contentlet.getFolder());

        ((TemplateAPIImpl) APILocator.getTemplateAPI()).setThemeName(template, APILocator.systemUser(), false);

        final Folder themeFolder = APILocator.getFolderAPI()
                .find(contentlet.getFolder(), APILocator.systemUser(), false);

        assertEquals(template.getThemeName(), themeFolder.getName());
    }

    /**
     * Method to test: {@link TemplateAPIImpl#setThemeName(Template, User, boolean)}
     * When: The theme does not exist
     * Should: not call the {@link Template#setThemeName(String)}
     *
     * @throws DotDataException
     * @throws DotSecurityException
     */
    @Test
    public void setThemeNameDoesNotExists() throws DotDataException, DotSecurityException {
        final Template template = new TemplateDataGen().nextPersisted();
        template.setTheme("not_exists");

        ((TemplateAPIImpl) APILocator.getTemplateAPI()).setThemeName(template, APILocator.systemUser(), false);
        assertNull(template.getThemeName());
    }

    /**
     * Method to test: {@link TemplateAPIImpl#setThemeName(Template, User, boolean)}
     * When: The theme is null
     * Should: not call the {@link Template#setThemeName(String)}
     *
     * @throws DotDataException
     * @throws DotSecurityException
     */
    @Test
    public void setThemeNameNull() throws DotDataException, DotSecurityException {
        final Template template = new TemplateDataGen().nextPersisted();
        template.setTheme(null);

        ((TemplateAPIImpl) APILocator.getTemplateAPI()).setThemeName(template, APILocator.systemUser(), false);
        assertNull(template.getThemeName());
    }

    /**
     * Method to test: {@link TemplateAPIImpl#getPages(String)}
     * When: A Template is used by one Page's Version
     * Should: Return the Inode of this page
     */
    @Test
    public void justOnePageVersion() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Template template = new TemplateDataGen().host(host).nextPersisted();

        final HTMLPageAsset htmlPageAsset = new HTMLPageDataGen(host, template).nextPersisted();

        final List<HTMLPageVersion> pages = APILocator.getTemplateAPI().getPages(template.getIdentifier());

        assertFalse(pages.isEmpty());
        assertEquals(1, pages.size());
        assertEquals(htmlPageAsset.getInode(), pages.get(0).getInode());
        assertEquals(htmlPageAsset.getLanguageId(), pages.get(0).getLanguageId());
        assertEquals(htmlPageAsset.getVariantId(), pages.get(0).getVariantName());
        assertEquals(htmlPageAsset.getIdentifier(), pages.get(0).getIdentifier());
    }

    /**
     * Method to test: {@link TemplateAPIImpl#getPages(String)}
     * When: Create three different version of the same page but in different languages
     * - 2 of them are going to use the same Template
     * - and the last one is going to use a different template
     * Should:
     * - Return the 2 firsts inodes for the first Template.
     * - Return 3 Inodes for the second Template: The third Page's version ans two more create for the
     * first two languages.
     */
    @Test
    public void severalPageVersionLang() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Template template_1 = new TemplateDataGen().host(host).nextPersisted();
        final Template template_2 = new TemplateDataGen().host(host).nextPersisted();

        final Language language_1 = new LanguageDataGen().nextPersisted();
        final Language language_2 = new LanguageDataGen().nextPersisted();

        final HTMLPageAsset htmlPageAsset_1 = new HTMLPageDataGen(host, template_1).nextPersisted();
        final HTMLPageAsset htmlPageAsset_2 = createNewVersion(language_1, htmlPageAsset_1, template_1);
        final HTMLPageAsset htmlPageAsset_3 = createNewVersion(language_2, htmlPageAsset_1, template_2);

        final List<HTMLPageVersion> pagesTemplate1 = APILocator.getTemplateAPI().getPages(template_1.getIdentifier());
        checkFor(pagesTemplate1, htmlPageAsset_1, htmlPageAsset_2);


        final List<HTMLPageVersion> pagesTemplate2 = APILocator.getTemplateAPI().getPages(template_2.getIdentifier());
        final HTMLPageAsset lastWorkingVersion1 = getLastWorkingVersion(htmlPageAsset_1);
        final HTMLPageAsset lastWorkingVersion2 = getLastWorkingVersion(htmlPageAsset_2);
        checkFor(pagesTemplate2, htmlPageAsset_3, lastWorkingVersion1, lastWorkingVersion2);
    }


    /**
     * Method to test: {@link TemplateAPIImpl#getPages(String)}
     * When: Create three different version of the same page but in different {@link com.dotcms.variant.model.Variant}
     * - 2 of them are going to use the same Template
     * - and the last one is going to use a different template
     * Should:
     * - Return the 2 firsts inodes for the first Template.
     * - Return the last inodes for the second {@link Template}
     */
    @Test
    public void severalPageVersionVariant() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Template template_1 = new TemplateDataGen().host(host).nextPersisted();
        final Template template_2 = new TemplateDataGen().host(host).nextPersisted();

        final Variant variant_1 = new VariantDataGen().nextPersisted();
        final Variant variant_2 = new VariantDataGen().nextPersisted();

        final HTMLPageAsset htmlPageAsset_1 = new HTMLPageDataGen(host, template_1).nextPersisted();
        final HTMLPageAsset htmlPageAsset_2 = createNewVersion(variant_1, htmlPageAsset_1, template_1);
        final HTMLPageAsset htmlPageAsset_3 = createNewVersion(variant_2, htmlPageAsset_1, template_2);

        final List<HTMLPageVersion> pagesTemplate1 = APILocator.getTemplateAPI().getPages(template_1.getIdentifier());
        checkFor(pagesTemplate1, htmlPageAsset_1, htmlPageAsset_2);

        final List<HTMLPageVersion> pagesTemplate2 = APILocator.getTemplateAPI().getPages(template_2.getIdentifier());
        checkFor(pagesTemplate2, htmlPageAsset_3);
    }

    /**
     * Method to test: {@link TemplateAPIImpl#getPages(String)}
     * When: Create three different pages no matter the lang, all of them are going to use the same
     * {@link Template}.
     * Should:
     * - Return all the pages.
     */
    @Test
    public void severalPages() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Template template = new TemplateDataGen().host(host).nextPersisted();

        final Language language_1 = new LanguageDataGen().nextPersisted();
        final Language language_2 = new LanguageDataGen().nextPersisted();

        final HTMLPageAsset htmlPageAsset_1 = new HTMLPageDataGen(host, template)
                .languageId(language_1.getId())
                .nextPersisted();

        final HTMLPageAsset htmlPageAsset_2 = new HTMLPageDataGen(host, template)
                .languageId(language_2.getId())
                .nextPersisted();

        final List<HTMLPageVersion> pagesTemplate1 = APILocator.getTemplateAPI().getPages(template.getIdentifier());
        checkFor(pagesTemplate1, htmlPageAsset_1, htmlPageAsset_2);

    }

    private void checkFor(final List<HTMLPageVersion> pagesTemplate, final HTMLPageAsset... htmlPageAssets) {
        assertFalse(pagesTemplate.isEmpty());
        assertEquals(htmlPageAssets.length, pagesTemplate.size());

        final List<String> inodesFromTemplate = pagesTemplate.stream()
                .map(pageVersion -> pageVersion.getInode()).collect(Collectors.toList());

        for (final HTMLPageAsset htmlPageAsset : htmlPageAssets) {
            assertTrue(inodesFromTemplate.contains(htmlPageAsset.getInode()));
        }
    }

    private HTMLPageAsset getLastWorkingVersion(final HTMLPageAsset htmlPageAsset) {
        final ContentletVersionInfo contentletVersionInfo = APILocator.getVersionableAPI()
                .getContentletVersionInfo(htmlPageAsset.getIdentifier(),
                        htmlPageAsset.getLanguageId(),
                        VariantAPI.DEFAULT_VARIANT.name())
                .orElseThrow(() -> new AssertionError());
        final HTMLPageAsset htmlPageAssetFromDataBase = getFromDataBase(
                contentletVersionInfo.getWorkingInode());
        return htmlPageAssetFromDataBase;
    }

    private HTMLPageAsset getFromDataBase(final String inode) {

        final Contentlet contentlet = FactoryLocator.getContentletFactory()
                .findInDb(inode).orElseThrow(() -> new AssertionError("Contentlet should exists:" + inode));

        return APILocator.getHTMLPageAssetAPI().fromContentlet(contentlet);
    }

    private HTMLPageAsset createNewVersion(final Language language, final HTMLPageAsset htmlPageAsset,
            final Template template) {

        final Contentlet checkout = HTMLPageDataGen.checkout(htmlPageAsset);

        checkout.setLanguageId(language.getId());
        checkout.setStringProperty(HTMLPageAssetAPI.TEMPLATE_FIELD, template.getIdentifier());

        final Contentlet checking = HTMLPageDataGen.checkin(checkout);
        return APILocator.getHTMLPageAssetAPI()
                .fromContentlet(checking);
    }

    private HTMLPageAsset createNewVersion(final Variant variant, final HTMLPageAsset htmlPageAsset,
            final Template template) {

        final Contentlet checkout = HTMLPageDataGen.checkout(htmlPageAsset);

        checkout.setVariantId(variant.name());
        checkout.setStringProperty(HTMLPageAssetAPI.TEMPLATE_FIELD, template.getIdentifier());

        final Contentlet checking = HTMLPageDataGen.checkin(checkout);
        return APILocator.getHTMLPageAssetAPI()
                .fromContentlet(checking);
    }
}
