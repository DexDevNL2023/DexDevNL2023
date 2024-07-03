package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.PolicesAssurancesTestSamples.*;
import static com.teleo.insurances.domain.SouscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SouscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Souscription.class);
        Souscription souscription1 = getSouscriptionSample1();
        Souscription souscription2 = new Souscription();
        assertThat(souscription1).isNotEqualTo(souscription2);

        souscription2.setId(souscription1.getId());
        assertThat(souscription1).isEqualTo(souscription2);

        souscription2 = getSouscriptionSample2();
        assertThat(souscription1).isNotEqualTo(souscription2);
    }

    @Test
    void policesAssurancesTest() {
        Souscription souscription = getSouscriptionRandomSampleGenerator();
        PolicesAssurances policesAssurancesBack = getPolicesAssurancesRandomSampleGenerator();

        souscription.setPolicesAssurances(policesAssurancesBack);
        assertThat(souscription.getPolicesAssurances()).isEqualTo(policesAssurancesBack);

        souscription.policesAssurances(null);
        assertThat(souscription.getPolicesAssurances()).isNull();
    }
}
