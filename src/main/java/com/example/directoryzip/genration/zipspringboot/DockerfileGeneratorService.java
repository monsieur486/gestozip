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
                "COPY . .",
                "RUN ./mvnw clean package -DskipTests 2>/dev/null || mvn clean package -DskipTests",
                "",
                "FROM eclipse-temurin:" + request.getJavaVersion() + "-jre",
                "WORKDIR /app",
                "COPY --from=build /app/target/*.jar app.jar",
                "EXPOSE " + request.getLocalPort(),
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]"
        );

        return new Fichier("Dockerfile", "", lines);
    }

    public Fichier generate(ZipSpringBootFormRequest request, String moduleName) {
        List<String> lines = List.of(
                "FROM eclipse-temurin:" + request.getJavaVersion() + "-jdk AS build",
                "WORKDIR /app",
                "COPY . .",
                "RUN ./mvnw clean package -DskipTests 2>/dev/null || mvn clean package -DskipTests",
                "",
                "FROM eclipse-temurin:" + request.getJavaVersion() + "-jre",
                "WORKDIR /app",
                "COPY --from=build /app/target/*.jar app.jar",
                "EXPOSE " + resolveExposePort(moduleName, request.getLocalPort()),
                "ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]"
        );

        return new Fichier("Dockerfile", "", lines);
    }

    private int resolveExposePort(String moduleName, Integer defaultPort) {
        return switch (moduleName) {
            case "eureka-server" -> 8761;
            case "api-gateway" -> 9000;
            case "service-a" -> defaultPort != null ? defaultPort : 8081;
            case "service-b" -> defaultPort != null ? defaultPort + 1 : 8082;
            default -> defaultPort != null ? defaultPort : 8080;
        };
    }
}