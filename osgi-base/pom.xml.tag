<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dotcms-parent</artifactId>
        <groupId>com.dotcms</groupId>
        <version>1.0.0</version>
    </parent>

    <groupId>com.dotcms.plugins</groupId>
    <modelVersion>4.0.0</modelVersion>
    <packaging>pom</packaging>
    <artifactId>osgi-base</artifactId>

    <modules>
        <module>core-bundles</module>
        <module>system-bundles</module>
    </modules>

    <build>
        <plugins>
            <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-enforcer-plugin</artifactId>
            <configuration>
                <skip>true</skip>
            </configuration>
            </plugin>
        </plugins>
    </build>


</project>
