package com.teleo.claims.domain;

import static com.teleo.claims.domain.FournisseurTestSamples.*;
import static com.teleo.claims.domain.PrestationSoinTestSamples.*;
import static com.teleo.claims.domain.SinistreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SinistreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sinistre.class);
        Sinistre sinistre1 = getSinistreSample1();
        Sinistre sinistre2 = new Sinistre();
        assertThat(sinistre1).isNotEqualTo(sinistre2);

        sinistre2.setId(sinistre1.getId());
        assertThat(sinistre1).isEqualTo(sinistre2);

        sinistre2 = getSinistreSample2();
        assertThat(sinistre1).isNotEqualTo(sinistre2);
    }

    @Test
    void fournisseurTest() {
        Sinistre sinistre = getSinistreRandomSampleGenerator();
        Fournisseur fournisseurBack = getFournisseurRandomSampleGenerator();

        sinistre.setFournisseur(fournisseurBack);
        assertThat(sinistre.getFournisseur()).isEqualTo(fournisseurBack);

        sinistre.fournisseur(null);
        assertThat(sinistre.getFournisseur()).isNull();
    }

    @Test
    void prestationsSoinTest() {
        Sinistre sinistre = getSinistreRandomSampleGenerator();
        PrestationSoin prestationSoinBack = getPrestationSoinRandomSampleGenerator();

        sinistre.setPrestationsSoin(prestationSoinBack);
        assertThat(sinistre.getPrestationsSoin()).isEqualTo(prestationSoinBack);

        sinistre.prestationsSoin(null);
        assertThat(sinistre.getPrestationsSoin()).isNull();
    }
}
