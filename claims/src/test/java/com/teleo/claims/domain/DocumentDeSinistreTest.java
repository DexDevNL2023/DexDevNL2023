package com.teleo.claims.domain;

import static com.teleo.claims.domain.DocumentDeSinistreTestSamples.*;
import static com.teleo.claims.domain.SinistreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentDeSinistreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDeSinistre.class);
        DocumentDeSinistre documentDeSinistre1 = getDocumentDeSinistreSample1();
        DocumentDeSinistre documentDeSinistre2 = new DocumentDeSinistre();
        assertThat(documentDeSinistre1).isNotEqualTo(documentDeSinistre2);

        documentDeSinistre2.setId(documentDeSinistre1.getId());
        assertThat(documentDeSinistre1).isEqualTo(documentDeSinistre2);

        documentDeSinistre2 = getDocumentDeSinistreSample2();
        assertThat(documentDeSinistre1).isNotEqualTo(documentDeSinistre2);
    }

    @Test
    void sinistreTest() {
        DocumentDeSinistre documentDeSinistre = getDocumentDeSinistreRandomSampleGenerator();
        Sinistre sinistreBack = getSinistreRandomSampleGenerator();

        documentDeSinistre.setSinistre(sinistreBack);
        assertThat(documentDeSinistre.getSinistre()).isEqualTo(sinistreBack);

        documentDeSinistre.sinistre(null);
        assertThat(documentDeSinistre.getSinistre()).isNull();
    }
}
