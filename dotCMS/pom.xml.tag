<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.dotcms</groupId>
        <artifactId>build-parent</artifactId>
        <version>1.0.0</version>
        <relativePath>../build-parent/pom.xml</relativePath>
    </parent>
    <artifactId>dotcms-core</artifactId>
    <packaging>jar</packaging>
    <properties>
        <servlet.port>8080</servlet.port>
        <palantirJavaFormat.version>2.29.0</palantirJavaFormat.version>
        <src.dir>${project.basedir}/src/main/java</src.dir>
        <test.src.dir>${project.basedir}/src/test/java</test.src.dir>
        <version.spotless.plugin>2.36.0</version.spotless.plugin>
        <jackson.version>2.13.4</jackson.version>
        <jersey.version>2.27</jersey.version>
        <batik.version>1.16</batik.version>
        <glowroot.version>0.13.1</glowroot.version>
        <version.cargo.plugin>1.10.6</version.cargo.plugin>
        <assembly-directory>${basedir}/target/dist</assembly-directory>
        <skipDockerEnv>false</skipDockerEnv>
        <skipITs>true</skipITs>
        <skipOwasp>true</skipOwasp>
        <starter.filename>starter.zip</starter.filename>
        <starter.path>${project.build.directory}/starter</starter.path>
        <starter.full.path>${starter.path}/${starter.filename}</starter.full.path>


        <clean.docker.volumes>false</clean.docker.volumes>
        <!-- Tomcat -->
        <tomcat.version>9.0.53</tomcat.version>
        <enableDebug>true</enableDebug>
        <debugPort>8000</debugPort>
        <debugHost>localhost</debugHost>
        <debugWait>false</debugWait>
        <buildDocker>true</buildDocker>
        <buildJib>false</buildJib>
        <tomcat-dist-folder>dotserver/tomcat-${tomcat.version}</tomcat-dist-folder>
        <tomcat9-overrides>${project.basedir}/src/main/resources/container/tomcat9</tomcat9-overrides>

        <docker.base.image>tomcat:9.0.74-jdk11</docker.base.image>
        <docker.output.image.name>dotcms/dotcms-test</docker.output.image.name>

    </properties>
    <dependencies>
        <dependency>
            <groupId>com.dotcms.core.plugins</groupId>
            <artifactId>tika-api</artifactId>
        </dependency>
        <!--
          *****************************
             dotcms repackaged dependencies
          *****************************
        -->

        <dependency>
            <groupId>com.dotcms</groupId>
            <artifactId>ant-tooling</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.aopalliance-repackaged</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.asm</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.axiom-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.axiom-impl</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.bsf</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.bsh</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.cactus.integration.ant</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.cargo-ant</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.cargo-core-uberjar</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.com.dotmarketing.jhlabs.images.filters</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.commons-dbcp</artifactId>
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
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.commons-jci-core</artifactId>
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
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.concurrent</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.core-renderer-modified</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.cos</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.counter-ejb</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.dwr</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.fileupload-ext</artifactId>
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
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.guava</artifactId>
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
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.httpbridge</artifactId>
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
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jamm</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.javacsv</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.javax.annotation-api</artifactId>
        </dependency>


        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jazzy-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jboss-logging</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jempbox</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jep</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jsoup</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jstl</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jta</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jxl</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.ldap</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.lesscss</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.mail-ejb</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.maxmind-db</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.msnm</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.myspell</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.odmg</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.confluence.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.mediawiki.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.textile.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.tracwiki.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.org.eclipse.mylyn.wikitext.twiki.core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.persistence-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.platform</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.portlet</artifactId>
        </dependency>

        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.rhino</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.secure-filter</artifactId>
        </dependency>


        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.snappy-java</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.sslext</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.stax2-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.struts</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.stxx</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.Tidy</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.trove</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.twitter4j-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.txtmark</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.util-taglib</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.validation-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.wbmp</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.darkxanter</groupId>
            <artifactId>webp-imageio</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.woodstox-core-lgpl</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.xpp3-min</artifactId>
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
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.slf4j-jcl</artifactId>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jaxb-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jaxb-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.jaxb-impl</artifactId>
        </dependency>

        <dependency>
            <!-- possible remove ? -->
            <groupId>com.dotcms.lib</groupId>
            <artifactId>dot.commons-cli</artifactId>
            <scope>compile</scope>
        </dependency>

        <!--
           *****************************
              WebDav Support
           *****************************
        -->
        <dependency>
            <groupId>com.ettrema</groupId>
            <artifactId>milton-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.ettrema</groupId>
            <artifactId>milton-servlet</artifactId>
        </dependency>



        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
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
        </dependency>

        <dependency>
            <!-- SneakyThrow: A Java library that allows to throw checked exceptions without declaration -->
            <groupId>com.rainerhahnekamp</groupId>
            <artifactId>sneakythrow</artifactId>
        </dependency>

        <dependency>
            <!-- Functional programming utilities including Try, Tuple, Option, Either, Collections, etc. -->
            <groupId>io.vavr</groupId>
            <artifactId>vavr</artifactId>
        </dependency>
        <dependency>
            <groupId>jakarta.inject</groupId>
            <artifactId>jakarta.inject-api</artifactId>
            <version>1.0.3</version>
        </dependency>

        <dependency>
            <!-- Text processing utilities and regex-->
            <groupId>oro</groupId>
            <artifactId>oro</artifactId>
        </dependency>

        <!--
         *****************************
            Apache Commons Utilities
         *****************************
       -->
        <dependency>
            <groupId>commons-beanutils</groupId>
            <artifactId>commons-beanutils</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-collections</groupId>
            <artifactId>commons-collections</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-configuration</groupId>
            <artifactId>commons-configuration</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-digester</groupId>
            <artifactId>commons-digester</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-validator</groupId>
            <artifactId>commons-validator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-compress</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-math3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-numbers-gamma</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
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
        </dependency>

        <dependency>
            <!-- UserAgentUtils: Utility for parsing a user agent string -->
            <groupId>eu.bitwalker</groupId>
            <artifactId>UserAgentUtils</artifactId>
        </dependency>
        <dependency>
            <groupId>eu.medsea.mimeutil</groupId>
            <artifactId>mime-util</artifactId>
        </dependency>

        <dependency>
            <!-- Geo Location -->
            <groupId>com.maxmind.geoip2</groupId>
            <artifactId>geoip2</artifactId>
        </dependency>

        <!--
         *****************************
            GraphQL Support
         *****************************
       -->

        <dependency>
            <groupId>com.graphql-java</groupId>
            <artifactId>graphql-java</artifactId>
        </dependency>
        <dependency>
            <groupId>com.graphql-java</groupId>
            <artifactId>graphql-java-extended-scalars</artifactId>
        </dependency>
        <dependency>
            <groupId>com.graphql-java-kickstart</groupId>
            <artifactId>graphql-java-servlet</artifactId>
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
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
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
        </dependency>

        <dependency>
            <!-- MSSQL Driver, MSSQL support is deprecated -->
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
        </dependency>


        <dependency>
            <groupId>com.impossibl.pgjdbc-ng</groupId>
            <artifactId>pgjdbc-ng</artifactId>
        </dependency>

        <!--
         *****************************
           Cluster Support
         *****************************
       -->
        <dependency>
            <groupId>com.hazelcast</groupId>
            <artifactId>hazelcast-all</artifactId>
        </dependency>
        <dependency>
            <groupId>com.hazelcast</groupId>
            <artifactId>hazelcast-kubernetes</artifactId>
        </dependency>

        <dependency>
            <!-- Shedlock Distributed Locking -->
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-core</artifactId>
        </dependency>

        <dependency>
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-provider-jdbc</artifactId>
        </dependency>


        <dependency>
            <!-- JGroups: Reliable Multicast Communication -->
            <groupId>org.jgroups</groupId>
            <artifactId>jgroups</artifactId>
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
        </dependency>


        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-kms</artifactId>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-rekognition</artifactId>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-s3</artifactId>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>jmespath-java</artifactId>
        </dependency>

        <dependency>
            <!-- Redis Client -->
            <groupId>io.lettuce</groupId>
            <artifactId>lettuce-core</artifactId>
        </dependency>

        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-high-level-client</artifactId>
        </dependency>


        <!--
         *****************************
            JavaX/Jakarta/J2EE Support
         *****************************
       -->
        <dependency>
            <groupId>com.sun.activation</groupId>
            <artifactId>javax.activation</artifactId>
        </dependency>
        <dependency>
            <groupId>com.sun.mail</groupId>
            <artifactId>jakarta.mail</artifactId>
        </dependency>
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
        </dependency>

        <dependency>
            <groupId>javax.websocket</groupId>
            <artifactId>javax.websocket-api</artifactId>
        </dependency>

        <!--
         *****************************
            XML Support
         *****************************
       -->
        <dependency>
            <groupId>com.thoughtworks.xstream</groupId>
            <artifactId>xstream</artifactId>
        </dependency>

        <!-- JaxB -->

        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <scope>provided</scope>
        </dependency>

        <!-- Castor: Replace with JaxB -->
       <dependency>
           <groupId>org.codehaus.castor</groupId>
           <artifactId>castor-core</artifactId>
           <scope>compile</scope>
       </dependency>

       <dependency>
           <groupId>org.codehaus.castor</groupId>
           <artifactId>castor-xml</artifactId>
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
        </dependency>

        <dependency>
            <groupId>werken-xpath</groupId>
            <artifactId>werken-xpath</artifactId>
        </dependency>

        <!--
          *****************************
             JSON
          *****************************
        -->

       <!-- <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
        -->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-guava</artifactId>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jdk8</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.jaxrs</groupId>
            <artifactId>jackson-jaxrs-base</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-parameter-names</artifactId>
        </dependency>

        <dependency>
            <groupId>com.github.jonpeterson</groupId>
            <artifactId>jackson-module-model-versioning</artifactId>
        </dependency>


        <dependency>
            <groupId>com.jayway.jsonpath</groupId>
            <artifactId>json-path</artifactId>
        </dependency>




        <!-- Image Processing -->
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-batik</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-bmp</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-clippath</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-hdr</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-icns</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-iff</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-jpeg</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-metadata</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-pcx</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-pdf</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-pict</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-pnm</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-psd</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-sgi</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-tga</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-thumbsdb</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-tiff</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.imageio</groupId>
            <artifactId>imageio-webp</artifactId>
        </dependency>
        <dependency>
            <groupId>com.twelvemonkeys.servlet</groupId>
            <artifactId>servlet</artifactId>
        </dependency>


        <!-- FOP XSL based Print Formatter -->
        <dependency>
            <groupId>fop</groupId>
            <artifactId>fop</artifactId>
        </dependency>

        <!-- Security -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
        </dependency>

        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
        </dependency>
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk15on</artifactId>
        </dependency>


        <!-- JAX-RS Rest API -->

        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <scope>compile</scope>
        </dependency>

        <!-- Rest API Documentation -->
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-jaxrs2-servlet-initializer</artifactId>
        </dependency>


        <!--
             *****************************
                Bytecode Processing
             *****************************
        -->

        <dependency>
            <groupId>net.bytebuddy</groupId>
            <artifactId>byte-buddy</artifactId>
        </dependency>
        <dependency>
            <!-- Bytebuddy is preferred over cglib and other bytecode manipulation-->
            <groupId>net.bytebuddy</groupId>
            <artifactId>byte-buddy-agent</artifactId>
        </dependency>


        <dependency>
            <!-- deprecated prefer Bytebuddy, used bycom.dotcms.lib:dot.hibernate:2.1.7_3 -->
            <groupId>cglib</groupId>
            <artifactId>cglib-nodep</artifactId>
            <scope>compile</scope>
        </dependency>


        <dependency>
            <!-- Failure handling e.g. Circuit Breaker-->
            <groupId>net.jodah</groupId>
            <artifactId>failsafe</artifactId>
        </dependency>

        <dependency>
            <!-- ANTLR (ANother Tool for Language Recognition)  -->
            <groupId>org.antlr</groupId>
            <artifactId>antlr</artifactId>
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
        </dependency>

        <dependency>
            <groupId>org.apache.felix</groupId>
            <artifactId>org.apache.felix.main</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.felix</groupId>
            <artifactId>org.apache.felix.http.api</artifactId>
            <version>3.0.0</version>
            <scope>compile</scope>
        </dependency>


        <!-- File Format Processing -->

        <!-- XML Processing -->
        <dependency>
            <groupId>org.dom4j</groupId>
            <artifactId>dom4j</artifactId>
        </dependency>

        <dependency>
            <groupId>org.jdom</groupId>
            <artifactId>jdom</artifactId>
        </dependency>

        <dependency>
            <!-- XML Stream Processing like Sax -->
            <groupId>xmlpull</groupId>
            <artifactId>xmlpull</artifactId>
        </dependency>


        <!-- HTML Processing -->
        <dependency>
            <groupId>org.ccil.cowan.tagsoup</groupId>
            <artifactId>tagsoup</artifactId>
        </dependency>

        <dependency>
            <!-- PDF Processing -->
            <groupId>org.apache.pdfbox</groupId>
            <artifactId>pdfbox</artifactId>
        </dependency>
        <dependency>
            <!-- Microsoft Document Processing -->
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
        </dependency>
        <dependency>
            <!-- Batik SVG Processing -->
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-anim</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-awt-util</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-bridge</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-codec</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-constants</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-css</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-dom</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>xml-apis</groupId>
                    <artifactId>xml-apis</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-ext</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-gvt</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-i18n</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-parser</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-script</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-svg-dom</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-transcoder</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-util</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-xml</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>xmlgraphics-commons</artifactId>
        </dependency>


        <!-- JAX-RS Rest Implementation Jersey -->

        <dependency>
            <groupId>org.glassfish.jersey.connectors</groupId>
            <artifactId>jersey-apache-connector</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-common</artifactId>
        </dependency>

        <!--
        <dependency>
         Newer jersey requires choosing injection framework
            <groupId>org.glassfish.jersey.inject</groupId>
            <artifactId>jersey-cdi2-se</artifactId>
            <artifactId>jersey-hk2</artifactId>
        </dependency>
        -->

        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.ext</groupId>
            <artifactId>jersey-entity-filtering</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-jaxb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-jackson</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-multipart</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-sse</artifactId>
        </dependency>

        <dependency>
            <!-- Immutables is a Java annotation processor to eliminate the burden of writing immutable types.
             Is only needed during compilation and no runtime component required -->
            <groupId>org.immutables</groupId>
            <artifactId>value</artifactId>
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
        </dependency>


        <!-- Servlet Container support -->
        <dependency>
            <!-- web filter rewrite of urls -->
            <groupId>org.tuckey</groupId>
            <artifactId>urlrewritefilter</artifactId>
        </dependency>


        <!-- Apache Tomcat -->

        <dependency>
            <!-- Jasper is the Tomcat JSP Engine -->
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper</artifactId>
        </dependency>


        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jdbc</artifactId>
        </dependency>


        <!--
                <dependency>
                    <groupId>org.glowroot</groupId>
                    <artifactId>glowroot-agent</artifactId>
                </dependency>
        -->


        <!-- Test Dependencies -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.awaitility</groupId>
            <artifactId>awaitility</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.awaitility</groupId>
            <artifactId>awaitility-proxy</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <!-- Deprecated need to remove replace with core mockito-->
            <groupId>org.powermock</groupId>
            <artifactId>powermock-api-mockito2</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <!-- Deprecated need to remove replace with core mockito-->
            <groupId>org.powermock</groupId>
            <artifactId>powermock-module-junit4</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <!-- Hamcrest is a framework for writing matcher objects allowing 'match' rules to be defined declaratively. -->
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest-all</artifactId>
            <!-- often just used for testing we have in production code -->
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>com.tngtech.junit.dataprovider</groupId>
            <artifactId>junit4-dataprovider</artifactId>
        </dependency>

        <!-- explicitly add all the declared logging dependencies -->
        <!-- Logging framework support Log4j2 as base logger
            very specific artifacts are required to support other frameworks.
            Do not include incompatible artifacts e.g. -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
        </dependency>
        <dependency>
            <!-- Support log4j1.2.x API in Log4j2 -->
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-1.2-api</artifactId>
        </dependency>
        <dependency>
            <!-- Support Java Commons Logging API in Log4j2 -->
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-jcl</artifactId>
        </dependency>
        <dependency>
            <!-- Support SLF4j2 API in Log4j2 -->
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </dependency>
        <dependency>
            <!-- Servlet container initialization of Log4j2 -->
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-web</artifactId>
        </dependency>
        <dependency>
            <!-- Async support for Log4J 2.x -->
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
        </dependency>


    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.dotcms</groupId>
                <artifactId>application-bom</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <build>
        <sourceDirectory>${src.dir}</sourceDirectory>
        <testSourceDirectory>${test.src.dir}</testSourceDirectory>
        <resources>
            <resource>
                <filtering>false</filtering>
                <directory>src/main/resources</directory>
                <excludes>
                    <exclude>GeoLite2-City.mmdb</exclude>
                </excludes>
            </resource>
            <!--
            <resource>
                <filtering>false</filtering>
                <directory>src/main/webapp</directory>
            </resource>
            -->
        </resources>
        <plugins>


            <plugin>
                <groupId>org.owasp</groupId>
                <artifactId>dependency-check-maven</artifactId>
                <configuration>
                    <skip>${skipOwasp}</skip>
                    <suppressionFiles>
                        <suppressionFile>${maven.multiModuleProjectDirectory}/owasp-suppressions.xml</suppressionFile>
                    </suppressionFiles>
                    <!-- Turn down when we clean up or have suppressions-->
                    <failBuildOnCVSS>11</failBuildOnCVSS>
                    <assemblyAnalyzerEnabled>false</assemblyAnalyzerEnabled>
                    <failOnError>false</failOnError>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>com.dotcms</groupId>
                <artifactId>generate-gradle-dependencies</artifactId>
                <version>${project.version}</version>
                <executions>
                    <execution>
                        <id>generate-deps</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.basedir}</outputDirectory>
                            <outputFileName>dependencies.gradle</outputFileName>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>com.diffplug.spotless</groupId>
                <artifactId>spotless-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>format-core</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <skip>${skipFormat}</skip>
                            <!-- These are the defaults, you can override if you want -->
                            <!-- optional: limit format enforcement to just the files changed by this feature branch -->
                            <!-- <ratchetFrom>origin/main</ratchetFrom>-->
                            <formats>
                                <!-- you can define as many formats as you want, each is independent -->
                                <format>
                                    <!-- define the files to apply to -->
                                    <includes>
                                        <include>*.md</include>
                                        <include>.gitignore</include>
                                    </includes>
                                    <!-- define the steps to apply to those files -->
                                    <trimTrailingWhitespace />
                                    <endWithNewline />
                                    <toggleOffOn />
                                    <indent>
                                        <tabs>true</tabs>
                                        <spacesPerTab>4</spacesPerTab>
                                    </indent>
                                </format>
                            </formats>
                            <!-- define a language-specific format -->
                            <java>
                                <includes>
                                    <include>src/main/java/**/*.java</include>
                                    <include>src/test/java/**/*.java</include>
                                    <include>src/enterprise/java/**/*.java</include>
                                </includes>
                                <palantirJavaFormat>
                                    <version>${palantirJavaFormat.version}</version>
                                </palantirJavaFormat>
                                <!--
                                <googleJavaFormat>
                                    <version>1.8</version>
                                    <style>AOSP</style>
                                    <reflowLongStrings>true</reflowLongStrings>
                                </googleJavaFormat>
                                -->
                                <!--
                                <eclipse>
                                    <file>../docs/styleguide/eclipse-java-google-style.xml</file>
                                    <version>4.10</version>
                                </eclipse>
                                -->
                                <removeUnusedImports />
                                <trimTrailingWhitespace />
                                <endWithNewline />
                                <importOrder />
                                <!--<importOrder>
                                    <order>java,javax,org,com,net,com.dotcms,com.dotmarketing</order>
                                </importOrder>-->
                            </java>
                            <pom>
                                <!-- These are the defaults, you can override if you want -->
                                <includes>
                                    <include>pom.xml</include>
                                </includes>
                                <sortPom>
                                    <encoding>UTF-8</encoding>
                                    <!-- The encoding of the pom files -->
                                    <lineSeparator>${line.separator}</lineSeparator>
                                    <!-- line separator to use -->
                                    <expandEmptyElements>false</expandEmptyElements>
                                    <!-- Should empty elements be expanded-->
                                    <spaceBeforeCloseEmptyElement>false</spaceBeforeCloseEmptyElement>
                                    <!-- Should a space be added inside self-closing elements-->
                                    <keepBlankLines>false</keepBlankLines>
                                    <!-- Keep empty lines -->
                                    <nrOfIndentSpace>4</nrOfIndentSpace>
                                    <!-- Indentation -->
                                    <indentBlankLines>false</indentBlankLines>
                                    <!-- Should empty lines be indented -->
                                    <indentSchemaLocation>false</indentSchemaLocation>
                                    <!-- Should schema locations be indented -->
                                    <predefinedSortOrder>custom_1</predefinedSortOrder>
                                    <!-- Sort order of elements: https://github.com/Ekryd/sortpom/wiki/PredefinedSortOrderProfiles-->
                                    <sortOrderFile />
                                    <!-- Custom sort order of elements: https://raw.githubusercontent.com/Ekryd/sortpom/master/sorter/src/main/resources/custom_1.xml -->
                                    <sortDependencies>scope,groupId,artifactId</sortDependencies>
                                    <!-- Sort dependencies: https://github.com/Ekryd/sortpom/wiki/SortDependencies-->
                                    <sortDependencyExclusions />
                                    <!-- Sort dependency exclusions: https://github.com/Ekryd/sortpom/wiki/SortDependencies-->
                                    <sortPlugins />
                                    <!-- Sort plugins: https://github.com/Ekryd/sortpom/wiki/SortPlugins -->
                                    <sortProperties>true</sortProperties>
                                    <!-- Sort properties -->
                                    <sortModules>false</sortModules>
                                    <!-- Sort modules -->
                                    <sortExecutions>false</sortExecutions>
                                    <!-- Sort plugin executions -->
                                </sortPom>
                            </pom>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>add-source</id>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <phase>generate-sources</phase>
                        <configuration>
                            <sources>
                                <source>${basedir}/src/enterprise/java</source>
                            </sources>
                        </configuration>
                    </execution>
                    <execution>
                        <id>add-resource</id>
                        <goals>
                            <goal>add-resource</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <resources>
                                <resource>
                                    <directory>${basedir}/src/enterprise/resources</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>test-jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <systemPropertyVariables>
                        <DOT_ASSET_REAL_PATH>${test.assets.dir}</DOT_ASSET_REAL_PATH>
                        <DOT_DYNAMIC_CONTENT_PATH>${test.dynamic.dir}</DOT_DYNAMIC_CONTENT_PATH>
                        <workingDirectory>${test.working.dir}</workingDirectory>
                        <java.io.tmpdir>${test.temp.dir}</java.io.tmpdir>
                    </systemPropertyVariables>
                    <trimStackTrace>false</trimStackTrace>
                </configuration>
            </plugin>

        </plugins>
    </build>

    <reporting>
        <plugins>
            <plugin>
                <groupId>org.owasp</groupId>
                <artifactId>dependency-check-maven</artifactId>
                <reportSets>
                    <reportSet>
                        <reports>
                            <report>aggregate</report>
                        </reports>
                    </reportSet>
                </reportSets>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-report-plugin</artifactId>
            </plugin>
        </plugins>
    </reporting>
</project>

