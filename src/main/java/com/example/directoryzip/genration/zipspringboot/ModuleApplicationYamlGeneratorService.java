package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleApplicationYamlGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request, String moduleName) {
        List<String> lines = new ArrayList<>();

        lines.add("spring:");
        lines.add("  application:");
        lines.add("    name: " + moduleName);

        if ("api-gateway".equals(moduleName)) {
            lines.add("  main:");
            lines.add("    web-application-type: reactive");
            lines.add("  cloud:");
            lines.add("    gateway:");
            lines.add("      discovery:");
            lines.add("        locator:");
            lines.add("          enabled: true");
            lines.add("          lower-case-service-id: true");
        }

        lines.add("");
        lines.add("server:");
        lines.add("  port: " + resolveLocalPort(moduleName, request.getLocalPort()));

        lines.add("");
        lines.add("eureka:");
        lines.add("  client:");

        if ("eureka-server".equals(moduleName)) {
            lines.add("    register-with-eureka: false");
            lines.add("    fetch-registry: false");
        } else {
            lines.add("    service-url:");
            lines.add("      defaultZone: http://eureka-server:8761/eureka");
        }

        return new Fichier("application", "yml", lines);
    }

    private int resolveLocalPort(String moduleName, Integer defaultPort) {
        return switch (moduleName) {
            case "eureka-server" -> 8761;
            case "api-gateway" -> 9000;
            case "service-a" -> defaultPort != null ? defaultPort : 8081;
            case "service-b" -> defaultPort != null ? defaultPort + 1 : 8082;
            default -> defaultPort != null ? defaultPort : 8080;
        };
    }
}