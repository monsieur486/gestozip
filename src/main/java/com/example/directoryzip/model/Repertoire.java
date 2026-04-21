package com.example.directoryzip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repertoire {

    private String nom;
    private Repertoire parent;
    private List<Repertoire> sousRepertoires = new ArrayList<>();
    private List<Fichier> fichiers = new ArrayList<>();

    public Repertoire(String nom) {
        this.nom = nom;
    }

    public void ajouterSousRepertoire(Repertoire repertoire) {
        repertoire.setParent(this);
        this.sousRepertoires.add(repertoire);
    }

    private void setParent(Repertoire repertoire) {
        this.parent = repertoire;
    }

    public void ajouterFichier(Fichier fichier) {
        fichier.setParent(this);
        this.fichiers.add(fichier);
    }

    public String getCheminRelatif() {
        if (parent == null) {
            return nom;
        }
        return parent.getCheminRelatif() + "/" + nom;
    }
}
