package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RootDockerComposeGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request, List<String> modules) {
        List<String> lines = new ArrayList<>();
        lines.add("services:");

        for (String module : modules) {
            lines.add("  " + module + ":");
            lines.add("    build:");
            lines.add("      context: ./" + module);
            lines.add("      dockerfile: Dockerfile");
            lines.add("    container_name: " + module);

            if ("eureka-server".equals(module)) {
                lines.add("    ports:");
                lines.add("      - \"8761:8761\"");
            } else if ("api-gateway".equals(module)) {
                lines.add("    ports:");
                lines.add("      - \"" + (request.getDockerPort() != null ? request.getDockerPort() : 9000) + ":9000\"");
                lines.add("    depends_on:");
                lines.add("      - eureka-server");
            } else {
                lines.add("    depends_on:");
                lines.add("      - eureka-server");
            }

            lines.add("    environment:");
            lines.add("      SPRING_PROFILES_ACTIVE: docker");
            lines.add("");
        }

        return new Fichier("docker-compose", "yml", lines);
    }
}