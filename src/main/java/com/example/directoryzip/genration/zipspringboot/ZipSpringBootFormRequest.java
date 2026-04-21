package com.example.directoryzip.genration.zipspringboot;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZipSpringBootFormRequest {

    @NotNull
    private Integer javaVersion;

    @NotNull
    private String name;

    @NotNull
    private String groupId;

    @NotNull
    private String artifactId;

    @NotNull
    private String version;

    private List<String> dependencies = new ArrayList<>();

    @NotNull
    private Integer localPort;

    private Integer dockerPort;

    private boolean microservices;

    private List<String> modules = new ArrayList<>();
}