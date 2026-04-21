package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModulePomGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request, String moduleName) {
        List<String> lines = new ArrayList<>();

        lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        lines.add("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"");
        lines.add("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        lines.add("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">");
        lines.add("    <modelVersion>4.0.0</modelVersion>");

        lines.add("    <parent>");
        lines.add("        <groupId>org.springframework.boot</groupId>");
        lines.add("        <artifactId>spring-boot-starter-parent</artifactId>");
        lines.add("        <version>3.3.5</version>");
        lines.add("        <relativePath/>");
        lines.add("    </parent>");

        lines.add("    <groupId>" + request.getGroupId() + "</groupId>");
        lines.add("    <artifactId>" + moduleName + "</artifactId>");
        lines.add("    <version>" + request.getVersion() + "</version>");

        lines.add("    <properties>");
        lines.add("        <java.version>" + request.getJavaVersion() + "</java.version>");
        lines.add("        <spring-cloud.version>2023.0.3</spring-cloud.version>");
        lines.add("    </properties>");

        lines.add("    <dependencyManagement>");
        lines.add("        <dependencies>");
        lines.add("            <dependency>");
        lines.add("                <groupId>org.springframework.cloud</groupId>");
        lines.add("                <artifactId>spring-cloud-dependencies</artifactId>");
        lines.add("                <version>${spring-cloud.version}</version>");
        lines.add("                <type>pom</type>");
        lines.add("                <scope>import</scope>");
        lines.add("            </dependency>");
        lines.add("        </dependencies>");
        lines.add("    </dependencyManagement>");

        lines.add("    <dependencies>");

        if ("eureka-server".equals(moduleName)) {
            addDep(lines, "org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server");
            addDep(lines, "org.springframework.boot", "spring-boot-starter-web");
        } else if ("api-gateway".equals(moduleName)) {
            addDep(lines, "org.springframework.cloud", "spring-cloud-starter-gateway");
            addDep(lines, "org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client");
            addDep(lines, "org.springframework.boot", "spring-boot-starter-actuator");
        } else {
            addDep(lines, "org.springframework.boot", "spring-boot-starter-web");
            addDep(lines, "org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client");
            addDep(lines, "org.springframework.boot", "spring-boot-starter-actuator");
        }

        addDep(lines, "org.projectlombok", "lombok", true, null);
        addDep(lines, "org.springframework.boot", "spring-boot-starter-test", false, "test");

        lines.add("    </dependencies>");

        lines.add("    <build>");
        lines.add("        <plugins>");
        lines.add("            <plugin>");
        lines.add("                <groupId>org.springframework.boot</groupId>");
        lines.add("                <artifactId>spring-boot-maven-plugin</artifactId>");
        lines.add("            </plugin>");
        lines.add("        </plugins>");
        lines.add("    </build>");

        lines.add("</project>");

        return new Fichier("pom", "xml", lines);
    }

    private void addDep(List<String> lines, String groupId, String artifactId) {
        addDep(lines, groupId, artifactId, false, null);
    }

    private void addDep(List<String> lines, String groupId, String artifactId, boolean optional, String scope) {
        lines.add("        <dependency>");
        lines.add("            <groupId>" + groupId + "</groupId>");
        lines.add("            <artifactId>" + artifactId + "</artifactId>");
        if (optional) {
            lines.add("            <optional>true</optional>");
        }
        if (scope != null) {
            lines.add("            <scope>" + scope + "</scope>");
        }
        lines.add("        </dependency>");
    }
}