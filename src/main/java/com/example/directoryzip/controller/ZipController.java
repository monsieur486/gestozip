package com.example.directoryzip.controller;

import com.example.directoryzip.genration.zipspringboot.ZipSpringBootFormRequest;
import com.example.directoryzip.genration.zipspringboot.ZipSpringBootService;
import com.example.directoryzip.service.DirectoryStructureService;
import com.example.directoryzip.service.ZipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/zip")
@RequiredArgsConstructor
public class ZipController {

    private final DirectoryStructureService directoryStructureService;
    private final ZipSpringBootService zipSpringBootService;
    private final ZipService zipService;

    @PostMapping(value = "/springboot", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ByteArrayResource> download(@Valid @ModelAttribute ZipSpringBootFormRequest request) {
        try {
            Path workingDir = Path.of("generated");
            Path generatedDirectory = directoryStructureService.genererStructureSurDisque(workingDir, zipSpringBootService.creerStructure(request));
            Path zipPath = zipService.zipDirectory(generatedDirectory, workingDir.resolve("generation.zip"));

            byte[] zipBytes = Files.readAllBytes(zipPath);
            ByteArrayResource resource = new ByteArrayResource(zipBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generation.zip")
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .contentLength(zipBytes.length)
                    .body(resource);
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de lire le ZIP généré", e);
        }
    }
}
