package com.example.directoryzip;

import com.example.directoryzip.service.DirectoryStructureService;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryStructureServiceTest {

    private final DirectoryStructureService service = new DirectoryStructureService();

    @Test
    void shouldGenerateExpectedStructure() throws Exception {
        Path tempDir = Files.createTempDirectory("directory-zip-demo-test-");
        Path root = service.genererStructureSurDisque(tempDir);

        assertTrue(Files.exists(root.resolve("documents/readme.txt")));
        assertTrue(Files.exists(root.resolve("documents/contrats/contrat-client.md")));
        assertTrue(Files.exists(root.resolve("archives/2026/journal-2026.txt")));
    }
}
