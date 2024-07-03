package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.AssuranceTestSamples.*;
import static com.teleo.insurances.domain.RapportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rapport.class);
        Rapport rapport1 = getRapportSample1();
        Rapport rapport2 = new Rapport();
        assertThat(rapport1).isNotEqualTo(rapport2);

        rapport2.setId(rapport1.getId());
        assertThat(rapport1).isEqualTo(rapport2);

        rapport2 = getRapportSample2();
        assertThat(rapport1).isNotEqualTo(rapport2);
    }

    @Test
    void assuranceTest() {
        Rapport rapport = getRapportRandomSampleGenerator();
        Assurance assuranceBack = getAssuranceRandomSampleGenerator();

        rapport.setAssurance(assuranceBack);
        assertThat(rapport.getAssurance()).isEqualTo(assuranceBack);

        rapport.assurance(null);
        assertThat(rapport.getAssurance()).isNull();
    }
}
