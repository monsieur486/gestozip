package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringBootModuleGeneratorService {

    private final ModulePomGeneratorService modulePomGeneratorService;
    private final ModuleApplicationYamlGeneratorService moduleApplicationYamlGeneratorService;
    private final MainApplicationGeneratorService mainApplicationGeneratorService;
    private final DockerfileGeneratorService dockerfileGeneratorService;

    public Repertoire generateModule(ZipSpringBootFormRequest request, String moduleName) {
        Repertoire moduleRoot = new Repertoire(moduleName);

        Repertoire src = new Repertoire("src");
        Repertoire main = new Repertoire("main");
        Repertoire java = new Repertoire("java");
        Repertoire resources = new Repertoire("resources");

        String modulePackage = request.getGroupId() + "." + normalizePackage(moduleName);

        Fichier pom = modulePomGeneratorService.generate(request, moduleName);
        Fichier appYml = moduleApplicationYamlGeneratorService.generate(request, moduleName);
        Fichier dockerfile = dockerfileGeneratorService.generate(request, moduleName);
        Fichier mainClass = mainApplicationGeneratorService.generate(modulePackage, moduleName);

        Repertoire packageTree = buildPackageTree(modulePackage);
        packageTree.ajouterFichier(mainClass);

        java.ajouterSousRepertoire(packageTree);
        resources.ajouterFichier(appYml);

        main.ajouterSousRepertoire(java);
        main.ajouterSousRepertoire(resources);
        src.ajouterSousRepertoire(main);

        moduleRoot.ajouterFichier(pom);
        moduleRoot.ajouterFichier(dockerfile);
        moduleRoot.ajouterSousRepertoire(src);

        return moduleRoot;
    }

    private String normalizePackage(String moduleName) {
        return moduleName.replace("-", "").toLowerCase();
    }

    private Repertoire buildPackageTree(String packageName) {
        String[] parts = packageName.split("\\.");
        Repertoire root = new Repertoire(parts[0]);
        Repertoire current = root;

        for (int i = 1; i < parts.length; i++) {
            Repertoire child = new Repertoire(parts[i]);
            current.ajouterSousRepertoire(child);
            current = child;
        }

        return root;
    }
}