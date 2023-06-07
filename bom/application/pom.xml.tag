<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dotcms-bom</artifactId>
        <groupId>com.dotcms</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <packaging>pom</packaging>
    <artifactId>application-bom</artifactId>

    <properties>
        <aws-java-sdk.version>1.12.429</aws-java-sdk.version>
        <twelvemonkeys.version>3.7.0</twelvemonkeys.version>
        <swagger.version>2.2.0</swagger.version>
        <bytebuddy.version>1.12.6</bytebuddy.version>
        <batik.version>1.16</batik.version>
        <bouncy-castle.version>1.70</bouncy-castle.version>
        <awaitility.version>3.0.0</awaitility.version>
        <shedlock.version>4.33.0</shedlock.version>
        <glowroot.version>0.13.1</glowroot.version>
        <jackson.version>2.13.4</jackson.version>
        <jersey.version>2.22.1</jersey.version>
        <dotcms.tika-api.version>1.0.0-SNAPSHOT</dotcms.tika-api.version>
    </properties>
    <dependencyManagement>



        <!--
        The Bill of Materials (BOM) is a special type of Project Object Model (POM) in Maven that centralizes the management of a project's dependencies and their versions. It can be used not only to control the versions of dependencies but also plugins.

        Importantly, merely defining a dependency in the dependencyManagement section of the POM doesn't automatically include it in the project. For a dependency to be included in the classpath, it must be explicitly added to the project's dependencies. By managing dependencies through a BOM, you can avoid having to declare a dependency version each time in the project's dependencies section or manually override the version of transitive dependencies every time they are used.

        In Maven, the order in which dependencies are declared matters:

        1. Dependencies are first resolved in the order they are declared in the POM.
        2. Next, dependencies are resolved in the order they are declared in the dependencyManagement section.
        3. Finally, dependencies are resolved in the order they are declared in the parent POM.

        For example, if you wish to override a dependency declared in the parent POM, you must declare it in the dependencyManagement section of the child POM.

        When importing third-party BOMs, follow these rules:

        1. Analyze the project's direct dependencies.
        2. Examine the dependencyManagement section of the project. If a dependency version is defined here, it will override the version from the direct dependencies.
        3. Investigate the BOMs (import scope) in the dependencyManagement section in the order they are listed. If a version for a dependency is defined in a BOM, it will override any versions from the previous steps. If the same dependency is declared in multiple BOMs, the version from the first BOM is applied.

        In summary, when you import multiple BOMs, the version from the first BOM takes precedence if there are overlapping managed dependencies. As such, it's crucial to manage the order of BOM imports with care, considering which BOM should have priority in the case of overlapping dependencies.

        -->
        <dependencies>

            <dependency>
                <groupId>com.dotcms.core.plugins</groupId>
                <artifactId>tika-api</artifactId>
                <version>1.0.0</version>
            </dependency>

            <dependency>
                <groupId>com.dotcms</groupId>
                <artifactId>logging-bom</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>


            <!-- Asynchronous NIO client server framework for the jvm -->
            <!-- <dependency>

                 <groupId>io.netty</groupId>
                 <artifactId>netty-all</artifactId>
             </dependency>-->


            <!--
              *****************************
                 dotcms repackaged dependencies
              *****************************
            -->

            <dependency>
                <groupId>com.dotcms</groupId>
                <artifactId>ant-tooling</artifactId>
                <version>1.3.2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.aopalliance-repackaged</artifactId>
                <version>2.4.0-b10_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.asm</artifactId>
                <version>3.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.axiom-api</artifactId>
                <version>1.2.5_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.axiom-impl</artifactId>
                <version>1.2.5_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.bsf</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.bsh</artifactId>
                <version>2.0b4_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.cactus.integration.ant</artifactId>
                <version>1.8.0_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.cargo-ant</artifactId>
                <version>0.9_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.cargo-core-uberjar</artifactId>
                <version>0.9_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.com.dotmarketing.jhlabs.images.filters</artifactId>
                <version>ukv_2</version>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-dbcp</artifactId>
                <version>1.3_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-fileupload</artifactId>
                <version>1.3.3_1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-httpclient</artifactId>
                <version>3.1_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-jci-core</artifactId>
                <version>1.0.406301_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-jci-eclipse</artifactId>
                <version>3.2.0.666_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-net</artifactId>
                <version>3.3_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-pool</artifactId>
                <version>1.5.4_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.compression-filter</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.concurrent</artifactId>
                <version>1.3.4_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.core-renderer-modified</artifactId>
                <version>ukv_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.cos</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.counter-ejb</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.dwr</artifactId>
                <version>3rc2modified_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.fileupload-ext</artifactId>
                <version>ukv_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.gif89</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.google</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.guava</artifactId>
                <version>11.0.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.hibernate</artifactId>
                <version>2.1.7_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.hibernate-validator</artifactId>
                <version>4.3.2.Final_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.httpbridge</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.httpclient</artifactId>
                <version>4.2.2_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.iText</artifactId>
                <version>2.1.0_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jamm</artifactId>
                <version>0.2.5_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.javacsv</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.javax.annotation-api</artifactId>
                <version>1.2_2</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jazzy-core</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jboss-logging</artifactId>
                <version>3.3.0.Final_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jempbox</artifactId>
                <version>1.6.0_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jep</artifactId>
                <version>2.4.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jsoup</artifactId>
                <version>1.6.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jstl</artifactId>
                <version>1.0.5_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jta</artifactId>
                <version>1.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jxl</artifactId>
                <version>2.6_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.ldap</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.lesscss</artifactId>
                <version>1.5.1-SNAPSHOT_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.mail-ejb</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.maxmind-db</artifactId>
                <version>1.0.0_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.msnm</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.myspell</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.odmg</artifactId>
                <version>ukv_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.confluence.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.mediawiki.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.textile.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.tracwiki.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.org.eclipse.mylyn.wikitext.twiki.core</artifactId>
                <version>1.8.0.I20120918-1109_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.persistence-api</artifactId>
                <version>1.0_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.platform</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.portlet</artifactId>
                <version>1.0_2</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.rhino</artifactId>
                <version>1.7R4_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.secure-filter</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.snappy-java</artifactId>
                <version>1.0.4.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.sslext</artifactId>
                <version>1.2-0_1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.stax2-api</artifactId>
                <version>3.1.1_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.struts</artifactId>
                <version>1.2.10_1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.stxx</artifactId>
                <version>1.3_3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.Tidy</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.trove</artifactId>
                <version>1.0.2_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.twitter4j-core</artifactId>
                <version>3.0.3_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.txtmark</artifactId>
                <version>0.14-SNAPSHOT_1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.util-taglib</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.validation-api</artifactId>
                <version>1.1.0.Final_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.wbmp</artifactId>
                <version>ukv_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>io.github.darkxanter</groupId>
                <artifactId>webp-imageio</artifactId>
                <version>0.3.2</version>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.woodstox-core-lgpl</artifactId>
                <version>4.2.0_2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.xpp3-min</artifactId>
                <version>1.1.4c_2</version>
                <scope>compile</scope>
            </dependency>

       <dependency>
           <groupId>com.dotcms.lib</groupId>
           <artifactId>dot.quartz-all</artifactId>
           <version>1.8.6_2</version>
           <scope>compile</scope>
       </dependency>


         <dependency>
          <groupId>com.dotcms.lib</groupId>
          <artifactId>dot.slf4j-api</artifactId>
          <version>1.7.12_2</version>
          <scope>compile</scope>
      </dependency>
      <dependency>
          <groupId>com.dotcms.lib</groupId>
          <artifactId>dot.slf4j-jcl</artifactId>
          <version>1.7.12_2</version>
          <scope>compile</scope>
      </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jaxb-api</artifactId>
                <version>2.2.7_4</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jaxb-core</artifactId>
                <version>2.2.7_4</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.jaxb-impl</artifactId>
                <version>2.2.7_4</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.dotcms.lib</groupId>
                <artifactId>dot.commons-cli</artifactId>
                <version>1.2_2</version>
                <scope>compile</scope>
            </dependency>

            <!--
               *****************************
                  Job Scheduling
               *****************************
            -->

            <dependency>
                <!-- replaces dot.quartz-all -->
                <groupId>org.quartz-scheduler</groupId>
                <artifactId>quartz</artifactId>
                <version>1.8.6</version>
            </dependency>


            <!--
               *****************************
                  WebDav Support
               *****************************
            -->
            <dependency>
                <groupId>com.ettrema</groupId>
                <artifactId>milton-api</artifactId>
                <version>1.8.1.4</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>*</groupId>
                        <artifactId>*</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>com.ettrema</groupId>
                <artifactId>milton-servlet</artifactId>
                <version>1.8.1.4</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>*</groupId>
                        <artifactId>*</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>



            <dependency>
                <groupId>com.github.ben-manes.caffeine</groupId>
                <artifactId>caffeine</artifactId>
                <version>2.9.2</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.checkerframework</groupId>
                        <artifactId>checker-qual</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <!--
             *****************************
                Coding Utilities
             *****************************
           -->
            <dependency>
                <!-- Google Guava: Google Core Libraries -->
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>27.0.1-android</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- SneakyThrow: A Java library that allows to throw checked exceptions without declaration -->
                <groupId>com.rainerhahnekamp</groupId>
                <artifactId>sneakythrow</artifactId>
                <version>1.1.0</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- Functional programming utilities including Try, Tuple, Option, Either, Collections, etc. -->
                <groupId>io.vavr</groupId>
                <artifactId>vavr</artifactId>
                <version>0.10.3</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- Text processing utilities and regex-->
                <groupId>oro</groupId>
                <artifactId>oro</artifactId>
                <version>2.0.8</version>
                <scope>compile</scope>
            </dependency>

            <!--
             *****************************
                Apache Commons Utilities
             *****************************
           -->
            <dependency>
                <groupId>commons-beanutils</groupId>
                <artifactId>commons-beanutils</artifactId>
                <version>1.9.4</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-collections</groupId>
                <artifactId>commons-collections</artifactId>
                <version>3.2.2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-configuration</groupId>
                <artifactId>commons-configuration</artifactId>
                <version>1.10</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-digester</groupId>
                <artifactId>commons-digester</artifactId>
                <version>2.1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-fileupload</groupId>
                <artifactId>commons-fileupload</artifactId>
                <version>1.5</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-io</groupId>
                <artifactId>commons-io</artifactId>
                <version>2.11.0</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-lang</groupId>
                <artifactId>commons-lang</artifactId>
                <version>2.6</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-validator</groupId>
                <artifactId>commons-validator</artifactId>
                <version>1.6</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-compress</artifactId>
                <version>1.21</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-lang3</artifactId>
                <version>3.12.0</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-math3</artifactId>
                <version>3.6.1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-numbers-gamma</artifactId>
                <version>1.0</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-pool2</artifactId>
                <version>2.9.0</version>
                <scope>compile</scope>
            </dependency>


            <!--
             *****************************
                File and HTTP Processing
             *****************************
           -->
            <dependency>
                <!-- mozilla encoding detector -->
                <groupId>com.googlecode.juniversalchardet</groupId>
                <artifactId>juniversalchardet</artifactId>
                <version>1.0.3</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- UserAgentUtils: Utility for parsing a user agent string -->
                <groupId>eu.bitwalker</groupId>
                <artifactId>UserAgentUtils</artifactId>
                <version>1.19</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>eu.medsea.mimeutil</groupId>
                <artifactId>mime-util</artifactId>
                <version>2.1.3</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.slf4j</groupId>
                        <artifactId>slf4j-log4j12</artifactId>
                    </exclusion>
                    <exclusion>
                        <groupId>log4j</groupId>
                        <artifactId>log4j</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <dependency>
                <!-- Geo Location -->
                <groupId>com.maxmind.geoip2</groupId>
                <artifactId>geoip2</artifactId>
                <version>2.1.0</version>
                <scope>compile</scope>
            </dependency>

            <!--
             *****************************
                GraphQL Support
             *****************************
           -->

            <dependency>
                <groupId>com.graphql-java</groupId>
                <artifactId>graphql-java</artifactId>
                <version>13.0</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.slf4j</groupId>
                        <artifactId>slf4j-api</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>com.graphql-java</groupId>
                <artifactId>graphql-java-extended-scalars</artifactId>
                <version>1.0.1</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.graphql-java-kickstart</groupId>
                <artifactId>graphql-java-servlet</artifactId>
                <version>9.1.0</version>
                <scope>compile</scope>
            </dependency>

            <!--
             *****************************
                Database Support
             *****************************
           -->
            <dependency>
                <!-- Connection Pool -->
                <groupId>com.zaxxer</groupId>
                <artifactId>HikariCP</artifactId>
                <version>3.4.2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.postgresql</groupId>
                <artifactId>postgresql</artifactId>
                <version>42.5.1</version>
                <scope>provided</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.checkerframework</groupId>
                        <artifactId>checker-qual</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <version>1.3.176</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- MSSQL Driver, MSSQL support is deprecated -->
                <groupId>com.microsoft.sqlserver</groupId>
                <artifactId>mssql-jdbc</artifactId>
                <version>7.4.1.jre8</version>
                <scope>provided</scope>
            </dependency>


            <dependency>
                <groupId>com.impossibl.pgjdbc-ng</groupId>
                <artifactId>pgjdbc-ng</artifactId>
                <version>0.8.9</version>
                <scope>compile</scope>
            </dependency>

            <!--
             *****************************
               Cluster Support
             *****************************
           -->
            <dependency>
                <groupId>com.hazelcast</groupId>
                <artifactId>hazelcast-all</artifactId>
                <version>3.12.13</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.hazelcast</groupId>
                <artifactId>hazelcast-kubernetes</artifactId>
                <version>1.2.2</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>com.hazelcast</groupId>
                        <artifactId>hazelcast</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <dependency>
                <!-- Shedlock Distributed Locking -->
                <groupId>net.javacrumbs.shedlock</groupId>
                <artifactId>shedlock-core</artifactId>
                <version>${shedlock.version}</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>net.javacrumbs.shedlock</groupId>
                <artifactId>shedlock-provider-jdbc</artifactId>
                <version>${shedlock.version}</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <!-- JGroups: Reliable Multicast Communication -->
                <groupId>org.jgroups</groupId>
                <artifactId>jgroups</artifactId>
                <version>3.6.1.Final</version>
                <scope>compile</scope>
            </dependency>


            <!--
             *****************************
                API Client Support
             *****************************
           -->


            <dependency>
                <!-- general HTTP Client -->
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpclient</artifactId>
                <version>4.5.13</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>com.amazonaws</groupId>
                <artifactId>aws-java-sdk-bom</artifactId>
                <version>${aws-java-sdk.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <!-- Redis Client -->
                <groupId>io.lettuce</groupId>
                <artifactId>lettuce-core</artifactId>
                <version>6.1.1.RELEASE</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>redis.clients</groupId>
                <artifactId>jedis</artifactId>
                <version>2.7.3</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.elasticsearch.client</groupId>
                <artifactId>elasticsearch-rest-high-level-client</artifactId>
                <version>7.10.2</version>
                <scope>compile</scope>
            </dependency>


            <!--
             *****************************
                JavaX/Jakarta/J2EE Support
             *****************************
           -->
            <dependency>
                <groupId>com.sun.activation</groupId>
                <artifactId>javax.activation</artifactId>
                <version>1.2.0</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.sun.mail</groupId>
                <artifactId>jakarta.mail</artifactId>
                <version>1.6.7</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>jakarta.xml.bind</groupId>
                <artifactId>jakarta.xml.bind-api</artifactId>
                <version>2.3.3</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>javax.annotation</groupId>
                <artifactId>javax.annotation-api</artifactId>
                <version>1.2</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>javax.validation</groupId>
                <artifactId>validation-api</artifactId>
                <version>1.1.0.Final</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>javax.servlet-api</artifactId>
                <version>3.1.0</version>
                <scope>provided</scope>
            </dependency>
            <dependency>
                <groupId>javax.servlet.jsp</groupId>
                <artifactId>javax.servlet.jsp-api</artifactId>
                <version>2.2.1</version>
                <scope>provided</scope>
            </dependency>

            <dependency>
                <groupId>javax.websocket</groupId>
                <artifactId>javax.websocket-api</artifactId>
                <version>1.1</version>
                <scope>compile</scope>
            </dependency>

            <!--
             *****************************
                XML Support
             *****************************
           -->
            <dependency>
                <groupId>com.thoughtworks.xstream</groupId>
                <artifactId>xstream</artifactId>
                <version>1.4.8</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>xpp3</groupId>
                        <artifactId>xpp3_min</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <!-- JaxB -->

            <dependency>
                <groupId>org.glassfish.jaxb</groupId>
                <artifactId>jaxb-runtime</artifactId>
                <version>2.3.8</version>
                <scope>provided</scope>
            </dependency>


            <!-- Castor:  Replace with JaxB -->
           <dependency>
               <groupId>org.codehaus.castor</groupId>
               <artifactId>castor-core</artifactId>
               <version>1.4.1</version>
               <scope>compile</scope>
           </dependency>
           <dependency>
               <groupId>org.codehaus.castor</groupId>
               <artifactId>castor-xml</artifactId>
               <version>1.4.1</version>
               <scope>compile</scope>
               <exclusions>
                   <exclusion>
                       <groupId>javax.inject</groupId>
                       <artifactId>javax.inject</artifactId>
                   </exclusion>
                   <exclusion>
                       <groupId>org.springframework</groupId>
                       <artifactId>spring-context</artifactId>
                   </exclusion>
               </exclusions>
           </dependency>


            <!--
             *****************************
                XPath Support
             *****************************
           -->
            <dependency>
                <groupId>jaxen</groupId>
                <artifactId>jaxen</artifactId>
                <version>1.2.0</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>werken-xpath</groupId>
                <artifactId>werken-xpath</artifactId>
                <version>0.9.4</version>
                <scope>compile</scope>
            </dependency>

            <!--
              *****************************
                 JSON
              *****************************
            -->

            <dependency>
                <!-- Jackson Json Object Mapper -->
                <groupId>com.fasterxml.jackson</groupId>
                <artifactId>jackson-bom</artifactId>
                <version>${jackson.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>com.github.jonpeterson</groupId>
                <artifactId>jackson-module-model-versioning</artifactId>
                <version>1.2.2</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <groupId>com.jayway.jsonpath</groupId>
                <artifactId>json-path</artifactId>
                <version>2.4.0</version>
                <scope>compile</scope>
            </dependency>




            <!-- Image Processing -->
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-batik</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-bmp</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-clippath</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-core</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-hdr</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-icns</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-iff</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-jpeg</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-metadata</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-pcx</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-pdf</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-pict</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-pnm</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-psd</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-sgi</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-tga</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-thumbsdb</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-tiff</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.imageio</groupId>
                <artifactId>imageio-webp</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.twelvemonkeys.servlet</groupId>
                <artifactId>servlet</artifactId>
                <version>${twelvemonkeys.version}</version>
                <scope>compile</scope>
            </dependency>


            <!-- FOP XSL based Print Formatter -->
            <dependency>
                <groupId>fop</groupId>
                <artifactId>fop</artifactId>
                <version>0.20.3</version>
                <scope>compile</scope>
            </dependency>

            <!-- Security -->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>0.9.1</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcpkix-jdk15on</artifactId>
                <version>${bouncy-castle.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcprov-jdk15on</artifactId>
                <version>${bouncy-castle.version}</version>
                <scope>compile</scope>
            </dependency>


            <!-- JAX-RS Rest API -->

            <dependency>
                <groupId>javax.ws.rs</groupId>
                <artifactId>javax.ws.rs-api</artifactId>
                <version>2.0.1</version>
                <scope>compile</scope>
            </dependency>

            <!-- Rest API Documentation -->
            <dependency>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-jaxrs2</artifactId>
                <version>${swagger.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>io.swagger.core.v3</groupId>
                <artifactId>swagger-jaxrs2-servlet-initializer</artifactId>
                <version>${swagger.version}</version>
                <scope>compile</scope>
            </dependency>


            <!--
                 *****************************
                    Bytecode Processing
                 *****************************
            -->

            <dependency>
                <groupId>net.bytebuddy</groupId>
                <artifactId>byte-buddy</artifactId>
                <version>${bytebuddy.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Bytebuddy is preferred over cglib and other bytecode manipulation-->
                <groupId>net.bytebuddy</groupId>
                <artifactId>byte-buddy-agent</artifactId>
                <version>${bytebuddy.version}</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <groupId>cglib</groupId>
                <artifactId>cglib-nodep</artifactId>
                <version>3.2.6</version>
            </dependency>

            <dependency>
                <!-- deprecated replaced with Bytebuddy -->
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjrt</artifactId>
                <version>1.9.2</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- Bytecode manipulation library needed for mocking.  Old try to remove -->
                <groupId>org.javassist</groupId>
                <artifactId>javassist</artifactId>
                <version>3.29.2-GA</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <!-- Failure handling e.g. Circuit Breaker-->
                <groupId>net.jodah</groupId>
                <artifactId>failsafe</artifactId>
                <version>1.1.1</version>
                <scope>compile</scope>
            </dependency>


            <dependency>
                <!-- ANTLR (ANother Tool for Language Recognition)  -->
                <groupId>org.antlr</groupId>
                <artifactId>antlr</artifactId>
                <version>3.1.1</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.antlr</groupId>
                        <artifactId>stringtemplate</artifactId>
                    </exclusion>
                    <exclusion>
                        <groupId>antlr</groupId>
                        <artifactId>antlr</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>


            <!-- OSGi Keep Bundles out of here.  This is for core
            framework and interfaces required in parent classloader-->
            <dependency>
                <groupId>org.apache.felix</groupId>
                <artifactId>org.apache.felix.http.proxy</artifactId>
                <version>3.0.6</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.apache.felix</groupId>
                <artifactId>org.apache.felix.main</artifactId>
                <version>7.0.5</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.apache.felix</groupId>
                <artifactId>org.apache.felix.http.api</artifactId>
                <version>2.3.2</version>
                <scope>compile</scope>
            </dependency>




            <!-- File Format Processing -->

            <!-- XML Processing -->
            <dependency>
                <groupId>org.dom4j</groupId>
                <artifactId>dom4j</artifactId>
                <version>2.1.3</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.jdom</groupId>
                <artifactId>jdom</artifactId>
                <version>1.1.3</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- XML Stream Processing like Sax -->
                <groupId>xmlpull</groupId>
                <artifactId>xmlpull</artifactId>
                <version>1.1.3.1</version>
                <scope>compile</scope>
            </dependency>


            <!-- HTML Processing -->
            <dependency>
                <groupId>org.ccil.cowan.tagsoup</groupId>
                <artifactId>tagsoup</artifactId>
                <version>1.2.1</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <!-- PDF Processing -->
                <groupId>org.apache.pdfbox</groupId>
                <artifactId>pdfbox</artifactId>
                <version>2.0.28</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Microsoft Document Processing -->
                <groupId>org.apache.poi</groupId>
                <artifactId>poi</artifactId>
                <version>3.17</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Batik SVG Processing -->
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-anim</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-awt-util</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-bridge</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-codec</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-constants</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-css</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-dom</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-ext</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-gvt</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-i18n</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-parser</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-script</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-svg-dom</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-transcoder</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.apache.xmlgraphics</groupId>
                        <artifactId>batik-svggen</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-util</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>batik-xml</artifactId>
                <version>${batik.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.xmlgraphics</groupId>
                <artifactId>xmlgraphics-commons</artifactId>
                <version>2.6</version>
                <scope>compile</scope>
            </dependency>


            <!-- JAX-RS Rest Implementation Jersey -->

            <dependency>
                <groupId>org.glassfish.jersey</groupId>
                <artifactId>jersey-bom</artifactId>
                <version>${jersey.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>jakarta.inject</groupId>
                <artifactId>jakarta.inject-api</artifactId>
            </dependency>

            <!-- IDE Support -->
            <dependency>
                <groupId>org.jetbrains</groupId>
                <artifactId>annotations</artifactId>
                <version>16.0.1</version>
                <scope>provided</scope>
            </dependency>

            <dependency>
                <!-- Immutables is a Java annotation processor to eliminate the burden of writing immutable types.
                 Is only needed during compilation and no runtime component required -->
                <groupId>org.immutables</groupId>
                <artifactId>value</artifactId>
                <version>2.9.0</version>
                <scope>provided</scope>
            </dependency>

            <!--

            Are we using this?
            <dependency>
                 <groupId>org.openjdk.jmh</groupId>
                 <artifactId>jmh-core</artifactId>
                 <version>0.1</version>
                 <scope>compile</scope>
             </dependency>-->


            <dependency>
                <!-- Helps to load native libraries from JAR files -->
                <groupId>org.scijava</groupId>
                <artifactId>native-lib-loader</artifactId>
                <version>2.3.2</version>
                <scope>compile</scope>
            </dependency>


            <!-- Servlet Container support -->
            <dependency>
                <!-- web filter rewrite of urls -->
                <groupId>org.tuckey</groupId>
                <artifactId>urlrewritefilter</artifactId>
                <version>4.0.4</version>
                <scope>compile</scope>
            </dependency>


            <!-- Apache Tomcat -->

            <dependency>
                <!-- Jasper is the Tomcat JSP Engine -->
                <groupId>org.apache.tomcat</groupId>
                <artifactId>tomcat-jasper</artifactId>
                <version>9.0.73</version>
                <scope>provided</scope>
            </dependency>


            <dependency>
                <groupId>org.apache.tomcat</groupId>
                <artifactId>tomcat-jdbc</artifactId>
                <version>9.0.73</version>
                <scope>test</scope>
            </dependency>



            <dependency>
                <groupId>org.glowroot</groupId>
                <artifactId>glowroot-agent</artifactId>
                <version>${glowroot.version}</version>
                <!-- TODO: May need zip
                <type>zip</type>
                <classifier>dist</classifier>
                -->
            </dependency>



            <!-- Test Dependencies -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.13.2</version>
                <scope>test</scope>
            </dependency>

            <dependency>
                <groupId>org.awaitility</groupId>
                <artifactId>awaitility</artifactId>
                <version>${awaitility.version}</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.awaitility</groupId>
                <artifactId>awaitility-proxy</artifactId>
                <version>${awaitility.version}</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.mockito</groupId>
                <artifactId>mockito-core</artifactId>
                <version>2.28.2</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <!-- Deprecated need to remove replace with core mockito-->
                <groupId>org.powermock</groupId>
                <artifactId>powermock-api-mockito2</artifactId>
                <version>2.0.9</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <!-- Deprecated need to remove replace with core mockito-->
                <groupId>org.powermock</groupId>
                <artifactId>powermock-module-junit4</artifactId>
                <version>2.0.9</version>
                <scope>test</scope>
            </dependency>

            <dependency>
                <!-- Hamcrest is a framework for writing matcher objects allowing 'match' rules to be defined declaratively. -->
                <groupId>org.hamcrest</groupId>
                <artifactId>hamcrest-all</artifactId>
                <version>1.3</version>
                <!-- often just used for testing we have in production code
                Note, scope does not pass down to child, must declare scope explicitly-->
                <scope>compile</scope>
            </dependency>

            <dependency>
                <groupId>com.tngtech.junit.dataprovider</groupId>
                <artifactId>junit4-dataprovider</artifactId>
                <version>2.6</version>
                <scope>test</scope>
            </dependency>


        </dependencies>
    </dependencyManagement>
</project>
