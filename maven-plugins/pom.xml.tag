<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dotcms-parent</artifactId>
        <groupId>com.dotcms</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>maven-plugins</artifactId>
    <packaging>pom</packaging>
    <modules>
        <module>generate-gradle-dependencies</module>
    </modules>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.apache.maven</groupId>
                <artifactId>maven-plugin-api</artifactId>
                <version>3.9.1</version>
                <scope>provided</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.maven.plugin-tools</groupId>
                <artifactId>maven-plugin-annotations</artifactId>
                <version>3.8.1</version>
                <scope>provided</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.maven</groupId>
                <artifactId>maven-project</artifactId>
                <version>2.2.1</version>
                <scope>provided</scope>
            </dependency>

            <dependency>
                <groupId>org.apache.maven</groupId>
                <artifactId>maven-core</artifactId>
                <version>3.9.1</version>
                <scope>provided</scope>
            </dependency>
            <!-- Add any other dependencies required by your plugin -->
        </dependencies>
    </dependencyManagement>
</project>
