package com.example.directoryzip.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {

    public Path zipDirectory(Path sourceDirectory, Path outputZip) {
        try {
            Files.createDirectories(outputZip.getParent());
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(outputZip))) {
                Files.walk(sourceDirectory)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> addFileToZip(sourceDirectory, path, zipOutputStream));
            }
            return outputZip;
        } catch (IOException e) {
            throw new UncheckedIOException("Erreur lors de la création du ZIP", e);
        }
    }

    private void addFileToZip(Path sourceDirectory, Path file, ZipOutputStream zipOutputStream) {
        ZipEntry zipEntry = new ZipEntry(sourceDirectory.relativize(file).toString());
        try {
            zipOutputStream.putNextEntry(zipEntry);
            Files.copy(file, zipOutputStream);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
