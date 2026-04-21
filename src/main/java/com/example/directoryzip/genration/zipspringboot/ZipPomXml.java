package com.example.directoryzip.genration.zipspringboot;

import com.example.directoryzip.model.Fichier;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ZipPomXml {

    private static final String XML_VERSION = "1.0";
    private static final String ENCODING = "UTF-8";


    public Fichier createPomXml(ZipSpringBootFormRequest request) {
        Fichier result = new Fichier();
        result.setNom("pom");
        result.setExtension("xml");
        result.setContenu(contenu(request));
        return result;
    }

    private List<String> contenu(ZipSpringBootFormRequest request) {
        String sb = getVersion() +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n";
        return List.of(sb);
    }

    private String getVersion() {
        return "<?xml version=\"" +
                XML_VERSION +
                "\" encoding=\"" +
                ENCODING +
                "\"?>\n";
    }
}
