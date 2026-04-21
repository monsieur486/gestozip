package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import org.springframework.stereotype.Service;

@Service
public class ZipSpringBootService {

    public Repertoire creerStructure(ZipSpringBootFormRequest request) {
        Repertoire racine = new Repertoire("zip");
        Repertoire documents = new Repertoire("HelloWorld");
        Fichier fichier = new ZipPomXml().createPomXml(request);
        documents.ajouterFichier(fichier);
        racine.ajouterSousRepertoire(documents);
        return racine;
    }
}
