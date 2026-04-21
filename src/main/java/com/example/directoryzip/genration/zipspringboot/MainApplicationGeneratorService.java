package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainApplicationGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request) {
        String className = buildMainClassName(request.getArtifactId());

        List<String> lines = new ArrayList<>();
        lines.add("package " + request.getGroupId() + ";");
        lines.add("");
        lines.add("import org.springframework.boot.SpringApplication;");
        lines.add("import org.springframework.boot.autoconfigure.SpringBootApplication;");
        lines.add("");
        lines.add("@SpringBootApplication");
        lines.add("public class " + className + " {");
        lines.add("");
        lines.add("    public static void main(String[] args) {");
        lines.add("        SpringApplication.run(" + className + ".class, args);");
        lines.add("    }");
        lines.add("}");

        return new Fichier(className, "java", lines);
    }

    private String buildMainClassName(String artifactId) {
        if (artifactId == null || artifactId.isBlank()) {
            return "DemoApplication";
        }

        String cleaned = artifactId.replaceAll("[^a-zA-Z0-9\\-_.]", "");
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
}