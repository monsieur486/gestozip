package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationDockerYamlGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request) {
        return new Fichier("application-docker", "yml", generateLines(request));
    }

    private List<String> generateLines(ZipSpringBootFormRequest request) {
        List<String> lines = new ArrayList<>();
        Set<String> deps = normalize(request.getDependencies());

        lines.add("spring:");
        lines.add("  application:");
        lines.add("    name: " + request.getArtifactId());

        if (deps.contains("webflux") && !deps.contains("web") && !deps.contains("webmvc")) {
            lines.add("  main:");
            lines.add("    web-application-type: reactive");
        }

        if (deps.contains("data-jpa")) {
            lines.add("  datasource:");
            lines.add("    url: jdbc:postgresql://postgres:5432/" + request.getArtifactId());
            lines.add("    username: postgres");
            lines.add("    password: postgres");
            lines.add("    driver-class-name: org.postgresql.Driver");
            lines.add("  jpa:");
            lines.add("    hibernate:");
            lines.add("      ddl-auto: update");
            lines.add("    show-sql: true");
            lines.add("    properties:");
            lines.add("      hibernate:");
            lines.add("        format_sql: true");
        }

        if (deps.contains("data-mongodb")) {
            lines.add("  data:");
            lines.add("    mongodb:");
            lines.add("      uri: mongodb://mongodb:27017/" + request.getArtifactId());
        }

        if (deps.contains("data-redis")) {
            lines.add("  data:");
            lines.add("    redis:");
            lines.add("      host: redis");
            lines.add("      port: 6379");
        }

        lines.add("");
        lines.add("server:");
        lines.add("  port: " + request.getLocalPort());

        lines.add("");
        lines.add("management:");
        lines.add("  endpoints:");
        lines.add("    web:");
        lines.add("      exposure:");
        lines.add("        include: health,info");

        return lines;
    }

    private Set<String> normalize(List<String> dependencies) {
        Set<String> result = new LinkedHashSet<>();
        if (dependencies == null) {
            return result;
        }
        for (String dep : dependencies) {
            if (dep != null && !dep.isBlank()) {
                result.add(dep.trim().toLowerCase());
            }
        }
        return result;
    }
}