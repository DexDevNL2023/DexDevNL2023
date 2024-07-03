package com.teleo.claims.domain;

import static com.teleo.claims.domain.FournisseurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FournisseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fournisseur.class);
        Fournisseur fournisseur1 = getFournisseurSample1();
        Fournisseur fournisseur2 = new Fournisseur();
        assertThat(fournisseur1).isNotEqualTo(fournisseur2);

        fournisseur2.setId(fournisseur1.getId());
        assertThat(fournisseur1).isEqualTo(fournisseur2);

        fournisseur2 = getFournisseurSample2();
        assertThat(fournisseur1).isNotEqualTo(fournisseur2);
    }
}
