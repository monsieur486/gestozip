package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitignoreGeneratorService {

    public Fichier generate() {
        List<String> lines = List.of(
                "target/",
                ".idea/",
                "*.iml",
                ".classpath",
                ".project",
                ".settings/",
                ".mvn/wrapper/maven-wrapper.jar",
                "logs/",
                "*.log",
                ".DS_Store",
                "",
                "# OS",
                "Thumbs.db",
                "",
                "# Env",
                ".env",
                "",
                "# Generated archives",
                "*.zip"
        );

        return new Fichier(".gitignore", "", lines);
    }
}