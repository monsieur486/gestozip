package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainApplicationGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request) {
        String packageName = buildSingleAppPackageName(request);
        return generate(packageName, request.getArtifactId());
    }

    public Fichier generate(String packageName, String moduleName) {
        String className = buildMainClassName(moduleName);

        List<String> lines = new ArrayList<>();
        lines.add("package " + packageName + ";");
        lines.add("");

        lines.add("import org.springframework.boot.SpringApplication;");
        lines.add("import org.springframework.boot.autoconfigure.SpringBootApplication;");

        boolean isGateway = "api-gateway".equals(moduleName);
        boolean isEureka = "eureka-server".equals(moduleName);

        if (isGateway) {
            lines.add("import org.springframework.cloud.client.discovery.EnableDiscoveryClient;");
        }

        if (isEureka) {
            lines.add("import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;");
        }

        lines.add("");
        lines.add("@SpringBootApplication");

        if (isGateway) {
            lines.add("@EnableDiscoveryClient");
        }

        if (isEureka) {
            lines.add("@EnableEurekaServer");
        }

        lines.add("public class " + className + " {");
        lines.add("");
        lines.add("    public static void main(String[] args) {");
        lines.add("        SpringApplication.run(" + className + ".class, args);");
        lines.add("    }");
        lines.add("}");

        return new Fichier(className, "java", lines);
    }

    public String buildSingleAppPackageName(ZipSpringBootFormRequest request) {
        return request.getGroupId() + "." + sanitizePackagePart(request.getArtifactId());
    }

    public String buildModulePackageName(ZipSpringBootFormRequest request, String moduleName) {
        return request.getGroupId() + "." + sanitizePackagePart(moduleName);
    }

    private String buildMainClassName(String value) {
        if (value == null || value.isBlank()) {
            return "DemoApplication";
        }

        String cleaned = value.replaceAll("[^a-zA-Z0-9\\-_.]", "");
        String[] parts = cleaned.split("[\\-_.]+");

        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part == null || part.isBlank()) {
                continue;
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1).toLowerCase());
            }
        }

        if (sb.isEmpty()) {
            return "DemoApplication";
        }

        return sb.append("Application").toString();
    }

    private String sanitizePackagePart(String value) {
        if (value == null || value.isBlank()) {
            return "app";
        }

        String cleaned = value
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase();

        if (cleaned.isBlank()) {
            return "app";
        }

        if (Character.isDigit(cleaned.charAt(0))) {
            cleaned = "app" + cleaned;
        }

        return cleaned;
    }
}