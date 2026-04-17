package com.example.directoryzip.controller;

import com.example.directoryzip.service.DirectoryStructureService;
import com.example.directoryzip.service.ZipService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/zip")
public class ZipController {

    private final DirectoryStructureService directoryStructureService;
    private final ZipService zipService;

    public ZipController(DirectoryStructureService directoryStructureService, ZipService zipService) {
        this.directoryStructureService = directoryStructureService;
        this.zipService = zipService;
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public ResponseEntity<ByteArrayResource> downloadZip() {
        try {
            Path workingDir = Path.of("generated");
            Path generatedDirectory = directoryStructureService.genererStructureSurDisque(workingDir);
            Path zipPath = zipService.zipDirectory(generatedDirectory, workingDir.resolve("zip.zip"));

            byte[] zipBytes = Files.readAllBytes(zipPath);
            ByteArrayResource resource = new ByteArrayResource(zipBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=zip.zip")
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .contentLength(zipBytes.length)
                    .body(resource);
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de lire le ZIP généré", e);
        }
    }
}
