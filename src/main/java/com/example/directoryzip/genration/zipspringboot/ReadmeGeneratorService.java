package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReadmeGeneratorService {

    public Fichier generateSingleApp(ZipSpringBootFormRequest request) {
        List<String> lines = new ArrayList<>();
        Set<String> deps = normalize(request.getDependencies());

        lines.add("# " + request.getName());
        lines.add("");
        lines.add("Projet généré automatiquement avec GestoZip.");
        lines.add("");
        lines.add("## Informations");
        lines.add("");
        lines.add("- GroupId : `" + request.getGroupId() + "`");
        lines.add("- ArtifactId : `" + request.getArtifactId() + "`");
        lines.add("- Version : `" + request.getVersion() + "`");
        lines.add("- Java : `" + request.getJavaVersion() + "`");
        lines.add("- Port local : `" + request.getLocalPort() + "`");

        if (request.getDockerPort() != null) {
            lines.add("- Port Docker exposé : `" + request.getDockerPort() + "`");
        } else {
            lines.add("- Port Docker exposé : `non exposé`");
        }

        lines.add("");
        lines.add("## Dépendances");
        lines.add("");

        if (deps.isEmpty()) {
            lines.add("- aucune");
        } else {
            for (String dep : deps) {
                lines.add("- " + dep);
            }
        }

        lines.add("");
        lines.add("## Lancement local");
        lines.add("");
        lines.add("```bash");
        lines.add("mvn spring-boot:run");
        lines.add("```");
        lines.add("");
        lines.add("## Lancement Docker");
        lines.add("");
        lines.add("```bash");
        lines.add("docker compose up --build");
        lines.add("```");
        lines.add("");
        lines.add("## Endpoint de test");
        lines.add("");
        lines.add("```");
        lines.add("GET /api/hello");
        lines.add("```");

        return new Fichier("README", "md", lines);
    }

    public Fichier generateMicroservices(ZipSpringBootFormRequest request, List<String> modules) {
        List<String> lines = new ArrayList<>();

        lines.add("# " + request.getName());
        lines.add("");
        lines.add("Architecture microservices générée automatiquement avec GestoZip.");
        lines.add("");
        lines.add("## Modules");
        lines.add("");

        for (String module : modules) {
            lines.add("- " + module);
        }

        lines.add("");
        lines.add("## Lancement");
        lines.add("");
        lines.add("```bash");
        lines.add("docker compose up --build");
        lines.add("```");
        lines.add("");
        lines.add("## Accès");
        lines.add("");
        lines.add("- Eureka : `http://localhost:8761`");
        lines.add("- Gateway : `http://localhost:" + (request.getDockerPort() != null ? request.getDockerPort() : 9000) + "`");
        lines.add("");
        lines.add("## Endpoints de test");
        lines.add("");
        if (modules.contains("service-a")) {
            lines.add("- `GET /api/service-a/hello`");
        }
        if (modules.contains("service-b")) {
            lines.add("- `GET /api/service-b/hello`");
        }

        return new Fichier("README", "md", lines);
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