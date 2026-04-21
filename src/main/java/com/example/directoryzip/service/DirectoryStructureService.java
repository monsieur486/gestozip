package com.example.directoryzip.service;

import com.example.directoryzip.model.Fichier;
import com.example.directoryzip.model.Repertoire;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class DirectoryStructureService {

    public Path genererStructureSurDisque(Path dossierTravail, Repertoire racine) {
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
