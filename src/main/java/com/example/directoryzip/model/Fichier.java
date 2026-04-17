package com.example.directoryzip.model;

import java.util.List;

public interface Fichier {

    List<String> contenu();

    String nom();

    String getExtension();

    Repertoire repertoire();

    default String getNomComplet() {
        return nom() + "." + getExtension();
    }
}
