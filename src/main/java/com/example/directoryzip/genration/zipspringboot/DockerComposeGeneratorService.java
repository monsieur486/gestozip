package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class DockerComposeGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request) {
        return new Fichier("docker-compose", "yml", generateLines(request));
    }

    private List<String> generateLines(ZipSpringBootFormRequest request) {
        Set<String> deps = normalize(request.getDependencies());
        String appName = request.getArtifactId().toLowerCase();

        List<String> lines = new ArrayList<>();

        lines.add("services:");
        lines.add("  " + appName + ":");
        lines.add("    build:");
        lines.add("      context: .");
        lines.add("      dockerfile: Dockerfile");
        lines.add("    container_name: " + appName);
        if (request.getDockerPort() != null) {
            lines.add("    ports:");
            lines.add("      - \"" + request.getDockerPort() + ":" + request.getLocalPort() + "\"");
        }
        lines.add("    environment:");
        lines.add("      SPRING_PROFILES_ACTIVE: docker");
        lines.add("      SERVER_PORT: " + request.getLocalPort());

        boolean hasDependsOn = deps.contains("data-jpa") || deps.contains("postgresql")
                || deps.contains("data-mongodb") || deps.contains("data-redis");

        if (hasDependsOn) {
            lines.add("    depends_on:");
            if (deps.contains("data-jpa") || deps.contains("postgresql")) {
                lines.add("      - postgres");
            }
            if (deps.contains("data-mongodb")) {
                lines.add("      - mongodb");
            }
            if (deps.contains("data-redis")) {
                lines.add("      - redis");
            }
        }

        if (deps.contains("data-jpa") || deps.contains("postgresql")) {
            lines.add("");
            lines.add("  postgres:");
            lines.add("    image: postgres:16");
            lines.add("    container_name: postgres");
            lines.add("    environment:");
            lines.add("      POSTGRES_DB: " + appName);
            lines.add("      POSTGRES_USER: postgres");
            lines.add("      POSTGRES_PASSWORD: postgres");
            lines.add("    ports:");
            lines.add("      - \"5432:5432\"");
            lines.add("    volumes:");
            lines.add("      - postgres_data:/var/lib/postgresql/data");
        }

        if (deps.contains("data-mongodb")) {
            lines.add("");
            lines.add("  mongodb:");
            lines.add("    image: mongo:7");
            lines.add("    container_name: mongodb");
            lines.add("    ports:");
            lines.add("      - \"27017:27017\"");
            lines.add("    volumes:");
            lines.add("      - mongodb_data:/data/db");
        }

        if (deps.contains("data-redis")) {
            lines.add("");
            lines.add("  redis:");
            lines.add("    image: redis:7");
            lines.add("    container_name: redis");
            lines.add("    ports:");
            lines.add("      - \"6379:6379\"");
        }

        boolean hasVolumes = (deps.contains("data-jpa") || deps.contains("postgresql"))
                || deps.contains("data-mongodb");

        if (hasVolumes) {
            lines.add("");
            lines.add("volumes:");
            if (deps.contains("data-jpa") || deps.contains("postgresql")) {
                lines.add("  postgres_data:");
            }
            if (deps.contains("data-mongodb")) {
                lines.add("  mongodb_data:");
            }
        }

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