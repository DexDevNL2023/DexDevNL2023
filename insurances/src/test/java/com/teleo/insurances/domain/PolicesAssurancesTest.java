package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.AssuranceTestSamples.*;
import static com.teleo.insurances.domain.PolicesAssurancesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicesAssurancesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicesAssurances.class);
        PolicesAssurances policesAssurances1 = getPolicesAssurancesSample1();
        PolicesAssurances policesAssurances2 = new PolicesAssurances();
        assertThat(policesAssurances1).isNotEqualTo(policesAssurances2);

        policesAssurances2.setId(policesAssurances1.getId());
        assertThat(policesAssurances1).isEqualTo(policesAssurances2);

        policesAssurances2 = getPolicesAssurancesSample2();
        assertThat(policesAssurances1).isNotEqualTo(policesAssurances2);
    }

    @Test
    void assuranceTest() {
        PolicesAssurances policesAssurances = getPolicesAssurancesRandomSampleGenerator();
        Assurance assuranceBack = getAssuranceRandomSampleGenerator();

        policesAssurances.setAssurance(assuranceBack);
        assertThat(policesAssurances.getAssurance()).isEqualTo(assuranceBack);

        policesAssurances.assurance(null);
        assertThat(policesAssurances.getAssurance()).isNull();
    }
}
