package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelloControllerGeneratorService {

    public Fichier generateSingleApp(ZipSpringBootFormRequest request) {
        String packageName = buildSingleAppPackageName(request);
        String appName = request.getArtifactId();

        return generateController(packageName, appName, "/api/hello", "Hello from " + appName + " !");
    }

    public Fichier generateModule(ZipSpringBootFormRequest request, String moduleName) {
        String packageName = buildModulePackageName(request, moduleName);

        String path = "/api/hello";
        String message = "Hello from " + moduleName + " !";

        if ("service-a".equals(moduleName)) {
            path = "/api/service-a/hello";
            message = "Hello from service-a !";
        } else if ("service-b".equals(moduleName)) {
            path = "/api/service-b/hello";
            message = "Hello from service-b !";
        }

        return generateController(packageName, moduleName, path, message);
    }

    public boolean shouldGenerateForModule(String moduleName) {
        return "service-a".equals(moduleName) || "service-b".equals(moduleName);
    }

    private Fichier generateController(String packageName, String sourceName, String requestPath, String message) {
        String className = "HelloController";

        List<String> lines = new ArrayList<>();
        lines.add("package " + packageName + ";");
        lines.add("");
        lines.add("import org.springframework.web.bind.annotation.GetMapping;");
        lines.add("import org.springframework.web.bind.annotation.RequestMapping;");
        lines.add("import org.springframework.web.bind.annotation.RestController;");
        lines.add("");
        lines.add("@RestController");
        lines.add("@RequestMapping(\"" + requestPath + "\")");
        lines.add("public class " + className + " {");
        lines.add("");
        lines.add("    @GetMapping");
        lines.add("    public String hello() {");
        lines.add("        return \"" + escapeJava(message) + "\";");
        lines.add("    }");
        lines.add("}");

        return new Fichier(className, "java", lines);
    }

    private String buildControllerClassName(String value) {
        if (value == null || value.isBlank()) {
            return "HelloController";
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
            return "HelloController";
        }

        return sb.append("HelloController").toString();
    }

    private String buildSingleAppPackageName(ZipSpringBootFormRequest request) {
        return request.getGroupId() + "." + sanitizePackagePart(request.getArtifactId());
    }

    private String buildModulePackageName(ZipSpringBootFormRequest request, String moduleName) {
        return request.getGroupId() + "." + sanitizePackagePart(moduleName);
    }

    private String sanitizePackagePart(String value) {
        if (value == null || value.isBlank()) {
            return "app";
        }

        String cleaned = value.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        if (cleaned.isBlank()) {
            return "app";
        }

        if (Character.isDigit(cleaned.charAt(0))) {
            cleaned = "app" + cleaned;
        }

        return cleaned;
    }

    private String escapeJava(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}