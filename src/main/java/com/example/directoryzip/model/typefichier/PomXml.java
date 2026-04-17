package com.example.directoryzip.model.typefichier;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;

import java.util.List;

public record PomXml(List<String> contenu, String nom, Repertoire repertoire) implements Fichier {

    @Override
    public String getExtension() {
        return "xml";
    }
}
