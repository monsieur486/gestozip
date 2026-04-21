package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Repertoire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZipSpringBootService {

    private final SingleAppStructureGeneratorService singleAppStructureGeneratorService;
    private final MicroservicesStructureGeneratorService microservicesStructureGeneratorService;

    public Repertoire creerStructure(ZipSpringBootFormRequest request) {
        if (request.isMicroservices()) {
            return microservicesStructureGeneratorService.generate(request);
        }
        return singleAppStructureGeneratorService.generate(request);
    }
}