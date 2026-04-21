package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZipSpringBootService {

    private final PomXmlGeneratorService pomXmlGeneratorService;

    public Repertoire creerStructure(ZipSpringBootFormRequest request) {
        Repertoire racine = new Repertoire("zip");
        Repertoire documents = new Repertoire("HelloWorld");
        Fichier fichier = pomXmlGeneratorService.generatePomXml(request);
        documents.ajouterFichier(fichier);
        racine.ajouterSousRepertoire(documents);
        return racine;
    }
}
