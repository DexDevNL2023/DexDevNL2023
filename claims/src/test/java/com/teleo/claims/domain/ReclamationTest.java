package com.teleo.claims.domain;

import static com.teleo.claims.domain.ReclamationTestSamples.*;
import static com.teleo.claims.domain.SinistreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReclamationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reclamation.class);
        Reclamation reclamation1 = getReclamationSample1();
        Reclamation reclamation2 = new Reclamation();
        assertThat(reclamation1).isNotEqualTo(reclamation2);

        reclamation2.setId(reclamation1.getId());
        assertThat(reclamation1).isEqualTo(reclamation2);

        reclamation2 = getReclamationSample2();
        assertThat(reclamation1).isNotEqualTo(reclamation2);
    }

    @Test
    void sinistreTest() {
        Reclamation reclamation = getReclamationRandomSampleGenerator();
        Sinistre sinistreBack = getSinistreRandomSampleGenerator();

        reclamation.setSinistre(sinistreBack);
        assertThat(reclamation.getSinistre()).isEqualTo(sinistreBack);

        reclamation.sinistre(null);
        assertThat(reclamation.getSinistre()).isNull();
    }
}
