package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiResponseGeneratorService {

    public Fichier generateSingleApp(ZipSpringBootFormRequest request) {
        String packageName = buildSingleAppPackageName(request);
        return generate(packageName);
    }

    public Fichier generateModule(ZipSpringBootFormRequest request, String moduleName) {
        String packageName = buildModulePackageName(request, moduleName);
        return generate(packageName);
    }

    private Fichier generate(String packageName) {
        List<String> lines = new ArrayList<>();
        lines.add("package " + packageName + ";");
        lines.add("");
        lines.add("import lombok.AllArgsConstructor;");
        lines.add("import lombok.Data;");
        lines.add("import lombok.NoArgsConstructor;");
        lines.add("");
        lines.add("@Data");
        lines.add("@NoArgsConstructor");
        lines.add("@AllArgsConstructor");
        lines.add("public class ApiResponse<T> {");
        lines.add("");
        lines.add("    private boolean success;");
        lines.add("    private String message;");
        lines.add("    private T data;");
        lines.add("}");

        return new Fichier("ApiResponse", "java", lines);
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
}