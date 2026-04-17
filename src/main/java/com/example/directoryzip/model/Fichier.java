package com.example.directoryzip.model;

import java.util.List;

public interface Fichier {

    List<String> getContenu();

    String getNom();

    String getExtension();

    Repertoire getRepertoire();

    default String getNomComplet() {
        return getNom() + "." + getExtension();
    }
}
