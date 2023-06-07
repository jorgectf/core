<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dotcms-bom</artifactId>
        <groupId>com.dotcms</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>logging-bom</artifactId>
    <packaging>pom</packaging>

    <properties>
        <log4j.version>2.20.0</log4j.version>
        <slf4j.version>2.0.7</slf4j.version>
    </properties>

    <dependencyManagement>
        <dependencies>

            <!-- Logging framework support Log4j2 as base logger
             very specific artifacts are required to support other frameworks.
             Do not include incompatible artifacts e.g. -->
            <dependency>
                <groupId>org.apache.logging.log4j</groupId>
                <artifactId>log4j-core</artifactId>
                <version>${log4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Support log4j1.2.x API in Log4j2 -->
                <groupId>org.apache.logging.log4j</groupId>
                <artifactId>log4j-1.2-api</artifactId>
                <version>${log4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Support Java Commons Logging API in Log4j2 -->
                <groupId>org.apache.logging.log4j</groupId>
                <artifactId>log4j-jcl</artifactId>
                <version>${log4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Support SLF4j2 API in Log4j2 -->
                <groupId>org.apache.logging.log4j</groupId>
                <artifactId>log4j-slf4j2-impl</artifactId>
                <version>${log4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>${slf4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Servlet container initialization of Log4j2 -->
                <groupId>org.apache.logging.log4j</groupId>
                <artifactId>log4j-web</artifactId>
                <version>${log4j.version}</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <!-- Async support for Log4J 2.x -->
                <groupId>com.lmax</groupId>
                <artifactId>disruptor</artifactId>
                <version>3.3.4</version>
                <scope>compile</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>
</project>
