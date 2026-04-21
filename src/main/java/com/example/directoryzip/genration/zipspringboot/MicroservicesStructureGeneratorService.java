package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MicroservicesStructureGeneratorService {

    private final SpringBootModuleGeneratorService springBootModuleGeneratorService;
    private final RootDockerComposeGeneratorService rootDockerComposeGeneratorService;
    private final RootEnvGeneratorService rootEnvGeneratorService;

    public Repertoire generate(ZipSpringBootFormRequest request) {
        String rootName = request.getArtifactId();
        Repertoire root = new Repertoire(rootName);

        List<String> modules = request.getModules() == null || request.getModules().isEmpty()
                ? List.of("eureka-server", "api-gateway", "service-a", "service-b")
                : request.getModules();

        for (String module : modules) {
            root.ajouterSousRepertoire(
                    springBootModuleGeneratorService.generateModule(request, module)
            );
        }

        Fichier compose = rootDockerComposeGeneratorService.generate(request, modules);
        Fichier env = rootEnvGeneratorService.generate(request, modules);

        root.ajouterFichier(compose);
        root.ajouterFichier(env);

        return root;
    }
}