package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerfileGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request) {
        List<String> lines = List.of(
                "FROM eclipse-temurin:" + request.getJavaVersion() + "-jdk AS build",
                "WORKDIR /app",
                "",
                "COPY pom.xml .",
                "COPY src ./src",
                "RUN chmod +x mvnw 2>/dev/null || true",
                "RUN ./mvnw clean package -DskipTests 2>/dev/null || mvn clean package -DskipTests",
                "",
                "FROM eclipse-temurin:" + request.getJavaVersion() + "-jre",
                "WORKDIR /app",
                "",
                "COPY --from=build /app/target/*.jar app.jar",
                "",
                "EXPOSE " + request.getLocalPort(),
                "",
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]"
        );

        return new Fichier("Dockerfile", "", lines);
    }
}