package com.example.directoryzip.service;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.FichierMarkdown;
import com.example.directoryzip.model.FichierTexte;
import com.example.directoryzip.model.Repertoire;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DirectoryStructureService {

    public Repertoire creerStructureExemple() {
        Repertoire racine = new Repertoire("zip", null);

        Repertoire documents = new Repertoire("documents", racine);
        racine.ajouterSousRepertoire(documents);

        Repertoire contrats = new Repertoire("contrats", documents);
        documents.ajouterSousRepertoire(contrats);

        Repertoire images = new Repertoire("images", racine);
        racine.ajouterSousRepertoire(images);

        Repertoire archives = new Repertoire("archives", racine);
        racine.ajouterSousRepertoire(archives);

        Repertoire annee2026 = new Repertoire("2026", archives);
        archives.ajouterSousRepertoire(annee2026);

        documents.ajouterFichier(new FichierTexte(
                List.of(
                        "Projet Spring Boot - génération de répertoires",
                        "Date: " + LocalDateTime.now(),
                        "Ce fichier illustre un contenu texte simple."
                ),
                "readme",
                documents
        ));

        contrats.ajouterFichier(new FichierMarkdown(
                List.of(
                        "# Contrat exemple",
                        "",
                        "- Client: Demo",
                        "- Statut: Brouillon",
                        "- Signature: En attente"
                ),
                "contrat-client",
                contrats
        ));

        annee2026.ajouterFichier(new FichierTexte(
                List.of(
                        "Archive système",
                        "Aucun incident détecté.",
                        "Sauvegarde effectuée avec succès."
                ),
                "journal-2026",
                annee2026
        ));

        return racine;
    }

    public Path genererStructureSurDisque(Path dossierTravail) {
        Repertoire racine = creerStructureExemple();
        Path racinePath = dossierTravail.resolve(racine.getNom());

        try {
            if (Files.exists(racinePath)) {
                deleteRecursively(racinePath);
            }
            Files.createDirectories(racinePath);
            ecrireRepertoire(racine, dossierTravail);
            return racinePath;
        } catch (IOException e) {
            throw new UncheckedIOException("Erreur lors de la génération de la structure", e);
        }
    }

    private void ecrireRepertoire(Repertoire repertoire, Path basePath) throws IOException {
        Path currentPath = basePath.resolve(repertoire.getCheminRelatif());
        Files.createDirectories(currentPath);

        for (Fichier fichier : repertoire.getFichiers()) {
            Path filePath = currentPath.resolve(fichier.getNomComplet());
            Files.write(
                    filePath,
                    fichier.getContenu(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }

        for (Repertoire sousRepertoire : repertoire.getSousRepertoires()) {
            ecrireRepertoire(sousRepertoire, basePath);
        }
    }

    private void deleteRecursively(Path path) throws IOException {
        try (var paths = Files.walk(path)) {
            paths.sorted((a, b) -> b.compareTo(a))
                    .forEach(current -> {
                        try {
                            Files.deleteIfExists(current);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }
}
