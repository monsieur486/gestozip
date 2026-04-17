package com.example.directoryzip.model;

import java.util.List;

public class FichierTexte implements Fichier {

    private final List<String> contenu;
    private final String nom;
    private final Repertoire repertoire;

    public FichierTexte(List<String> contenu, String nom, Repertoire repertoire) {
        this.contenu = contenu;
        this.nom = nom;
        this.repertoire = repertoire;
    }

    @Override
    public List<String> getContenu() {
        return contenu;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getExtension() {
        return "txt";
    }

    @Override
    public Repertoire getRepertoire() {
        return repertoire;
    }
}
