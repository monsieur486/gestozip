package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingleAppStructureGeneratorService {

    private final PomXmlGeneratorService pomXmlGeneratorService;
    private final ApplicationYamlGeneratorService applicationYamlGeneratorService;
    private final DockerfileGeneratorService dockerfileGeneratorService;
    private final DockerComposeGeneratorService dockerComposeGeneratorService;
    private final MainApplicationGeneratorService mainApplicationGeneratorService;

    public Repertoire generate(ZipSpringBootFormRequest request) {
        String projectName = request.getArtifactId();

        Repertoire racine = new Repertoire(projectName);

        Repertoire src = new Repertoire("src");
        Repertoire main = new Repertoire("main");
        Repertoire java = new Repertoire("java");
        Repertoire resources = new Repertoire("resources");

        Fichier pomXml = pomXmlGeneratorService.generatePomXml(request);
        Fichier applicationYml = applicationYamlGeneratorService.generate(request);
        Fichier dockerfile = dockerfileGeneratorService.generate(request);
        Fichier dockerCompose = dockerComposeGeneratorService.generate(request);
        Fichier mainApplication = mainApplicationGeneratorService.generate(request);

        Repertoire packageTree = buildPackageTree(request.getGroupId());
        packageTree.ajouterFichier(mainApplication);

        java.ajouterSousRepertoire(packageTree);
        resources.ajouterFichier(applicationYml);

        main.ajouterSousRepertoire(java);
        main.ajouterSousRepertoire(resources);
        src.ajouterSousRepertoire(main);

        racine.ajouterFichier(pomXml);
        racine.ajouterFichier(dockerfile);
        racine.ajouterFichier(dockerCompose);
        racine.ajouterSousRepertoire(src);

        return racine;
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