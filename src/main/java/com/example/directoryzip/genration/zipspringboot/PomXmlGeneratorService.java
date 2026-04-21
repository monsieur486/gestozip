package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class PomXmlGeneratorService {

    private static final String SPRING_BOOT_VERSION = "4.0.5";

    public Fichier generatePomXml(ZipSpringBootFormRequest request) {
        List<String> xml = generateXml(request);

        return new Fichier("pom", "xml", xml);
    }

    private List<String> generateXml(ZipSpringBootFormRequest request) {
        List<String> xml = new ArrayList<>();

        xml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.add("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"");
        xml.add("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        xml.add("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">");

        xml.add("    <modelVersion>4.0.0</modelVersion>");

        xml.add("    <parent>");
        xml.add("        <groupId>org.springframework.boot</groupId>");
        xml.add("        <artifactId>spring-boot-starter-parent</artifactId>");
        xml.add("        <version>" + SPRING_BOOT_VERSION + "</version>");
        xml.add("        <relativePath/>");
        xml.add("    </parent>");

        xml.add("    <groupId>" + escapeXml(request.getGroupId()) + "</groupId>");
        xml.add("    <artifactId>" + escapeXml(request.getArtifactId()) + "</artifactId>");
        xml.add("    <version>" + escapeXml(request.getVersion()) + "</version>");
        xml.add("    <name>" + escapeXml(request.getName()) + "</name>");
        xml.add("    <description>" + escapeXml(request.getName()) + "</description>");

        xml.add("    <properties>");
        xml.add("        <java.version>" + request.getJavaVersion() + "</java.version>");
        xml.add("    </properties>");

        xml.add("    <dependencies>");

        Set<String> dependencies = normalizeDependencies(request.getDependencies());

        for (String dependency : dependencies) {
            xml.addAll(buildDependency(dependency));
        }

        xml.add("        <dependency>");
        xml.add("            <groupId>org.springframework.boot</groupId>");
        xml.add("            <artifactId>spring-boot-starter-test</artifactId>");
        xml.add("            <scope>test</scope>");
        xml.add("        </dependency>");

        xml.add("    </dependencies>");

        xml.add("    <build>");
        xml.add("        <plugins>");

        xml.add("            <plugin>");
        xml.add("                <groupId>org.springframework.boot</groupId>");
        xml.add("                <artifactId>spring-boot-maven-plugin</artifactId>");

        if (dependencies.contains("lombok")) {
            xml.add("                <configuration>");
            xml.add("                    <excludes>");
            xml.add("                        <exclude>");
            xml.add("                            <groupId>org.projectlombok</groupId>");
            xml.add("                            <artifactId>lombok</artifactId>");
            xml.add("                        </exclude>");
            xml.add("                    </excludes>");
            xml.add("                </configuration>");
        }

        xml.add("            </plugin>");

        if (dependencies.contains("lombok")) {
            xml.add("            <plugin>");
            xml.add("                <groupId>org.apache.maven.plugins</groupId>");
            xml.add("                <artifactId>maven-compiler-plugin</artifactId>");
            xml.add("                <configuration>");
            xml.add("                    <annotationProcessorPaths>");
            xml.add("                        <path>");
            xml.add("                            <groupId>org.projectlombok</groupId>");
            xml.add("                            <artifactId>lombok</artifactId>");
            xml.add("                        </path>");
            xml.add("                    </annotationProcessorPaths>");
            xml.add("                </configuration>");
            xml.add("            </plugin>");
        }

        xml.add("        </plugins>");
        xml.add("    </build>");

        xml.add("</project>");

        return xml;
    }

    private Set<String> normalizeDependencies(List<String> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return new LinkedHashSet<>();
        }

        Set<String> result = new LinkedHashSet<>();
        for (String dependency : dependencies) {
            if (dependency != null && !dependency.isBlank()) {
                result.add(dependency.trim().toLowerCase());
            }
        }
        return result;
    }

    private List<String> buildDependency(String dependency) {
        return switch (dependency) {
            case "web" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-web");
            case "webmvc" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-webmvc");
            case "webflux" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-webflux");
            case "thymeleaf" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-thymeleaf");
            case "security" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-security");
            case "validation" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-validation");
            case "data-jpa" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-data-jpa");
            case "data-mongodb" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-data-mongodb");
            case "data-redis" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-data-redis");
            case "amqp", "rabbitmq" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-amqp");
            case "actuator" -> dependencyBlock("org.springframework.boot", "spring-boot-starter-actuator");
            case "lombok" -> optionalDependencyBlock("org.projectlombok", "lombok");
            case "devtools" -> runtimeOptionalDependencyBlock("org.springframework.boot", "spring-boot-devtools");
            case "postgresql" -> runtimeDependencyBlock("org.postgresql", "postgresql");
            case "mysql" -> runtimeDependencyBlock("com.mysql", "mysql-connector-j");
            case "h2" -> runtimeDependencyBlock("com.h2database", "h2");
            default -> List.of(
                    "        <!-- Dépendance inconnue ignorée : " + escapeXml(dependency) + " -->"
            );
        };
    }

    private List<String> dependencyBlock(String groupId, String artifactId) {
        return List.of(
                "        <dependency>",
                "            <groupId>" + groupId + "</groupId>",
                "            <artifactId>" + artifactId + "</artifactId>",
                "        </dependency>"
        );
    }

    private List<String> optionalDependencyBlock(String groupId, String artifactId) {
        return List.of(
                "        <dependency>",
                "            <groupId>" + groupId + "</groupId>",
                "            <artifactId>" + artifactId + "</artifactId>",
                "            <optional>true</optional>",
                "        </dependency>"
        );
    }

    private List<String> runtimeDependencyBlock(String groupId, String artifactId) {
        return List.of(
                "        <dependency>",
                "            <groupId>" + groupId + "</groupId>",
                "            <artifactId>" + artifactId + "</artifactId>",
                "            <scope>runtime</scope>",
                "        </dependency>"
        );
    }

    private List<String> runtimeOptionalDependencyBlock(String groupId, String artifactId) {
        return List.of(
                "        <dependency>",
                "            <groupId>" + groupId + "</groupId>",
                "            <artifactId>" + artifactId + "</artifactId>",
                "            <scope>runtime</scope>",
                "            <optional>true</optional>",
                "        </dependency>"
        );
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
