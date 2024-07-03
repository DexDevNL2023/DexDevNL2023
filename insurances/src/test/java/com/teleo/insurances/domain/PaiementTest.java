package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.PaiementTestSamples.*;
import static com.teleo.insurances.domain.SouscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paiement.class);
        Paiement paiement1 = getPaiementSample1();
        Paiement paiement2 = new Paiement();
        assertThat(paiement1).isNotEqualTo(paiement2);

        paiement2.setId(paiement1.getId());
        assertThat(paiement1).isEqualTo(paiement2);

        paiement2 = getPaiementSample2();
        assertThat(paiement1).isNotEqualTo(paiement2);
    }

    @Test
    void souscriptionTest() {
        Paiement paiement = getPaiementRandomSampleGenerator();
        Souscription souscriptionBack = getSouscriptionRandomSampleGenerator();

        paiement.setSouscription(souscriptionBack);
        assertThat(paiement.getSouscription()).isEqualTo(souscriptionBack);

        paiement.souscription(null);
        assertThat(paiement.getSouscription()).isNull();
    }
}
