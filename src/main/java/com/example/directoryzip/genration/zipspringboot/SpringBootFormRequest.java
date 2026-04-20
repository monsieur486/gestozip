package com.example.directoryzip.genration.zipspringboot;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpringBootFormRequest {

    @NotNull
    private Integer javaVersion;
}
