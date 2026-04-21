package com.example.directoryzip.genration.zipspringboot;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZipSpringBootFormRequest {

    @NotNull
    private Integer javaVersion;

    @NotNull
    private String nomProjet;
}
