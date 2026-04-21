package com.example.directoryzip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fichier {

    private String nom;
    private String extension;
    private Repertoire parent;
    private List<String> contenu = new ArrayList<>();

    public Fichier(String nom, String extension, List<String> contenu) {
        this.nom = nom;
        this.extension = extension;
        this.contenu = contenu;
        this.parent = null;
    }

    public String getNomComplet() {
        if (extension == null || extension.isBlank()) {
            return nom;
        }
        return nom + "." + extension;
    }

}
