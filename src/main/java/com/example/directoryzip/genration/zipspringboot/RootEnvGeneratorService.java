package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RootEnvGeneratorService {

    public Fichier generate(ZipSpringBootFormRequest request, List<String> modules) {
        List<String> lines = List.of(
                "JAVA_VERSION=" + request.getJavaVersion(),
                "GATEWAY_PORT=" + (request.getDockerPort() != null ? request.getDockerPort() : 9000)
        );

        return new Fichier(".env", "", lines);
    }
}