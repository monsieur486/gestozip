package com.example.directoryzip.model;

import java.util.ArrayList;
import java.util.List;

public class Repertoire {

    private final String nom;
    private final Repertoire parent;
    private final List<Repertoire> sousRepertoires = new ArrayList<>();
    private final List<Fichier> fichiers = new ArrayList<>();

    public Repertoire(String nom, Repertoire parent) {
        this.nom = nom;
        this.parent = parent;
    }

    public String getNom() {
        return nom;
    }

    public Repertoire getParent() {
        return parent;
    }

    public List<Repertoire> getSousRepertoires() {
        return sousRepertoires;
    }

    public List<Fichier> getFichiers() {
        return fichiers;
    }

    public void ajouterSousRepertoire(Repertoire repertoire) {
        this.sousRepertoires.add(repertoire);
    }

    public void ajouterFichier(Fichier fichier) {
        this.fichiers.add(fichier);
    }

    public String getCheminRelatif() {
        if (parent == null) {
            return nom;
        }
        return parent.getCheminRelatif() + "/" + nom;
    }
}
