package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.AssuranceTestSamples.*;
import static com.teleo.insurances.domain.GarantieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GarantieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Garantie.class);
        Garantie garantie1 = getGarantieSample1();
        Garantie garantie2 = new Garantie();
        assertThat(garantie1).isNotEqualTo(garantie2);

        garantie2.setId(garantie1.getId());
        assertThat(garantie1).isEqualTo(garantie2);

        garantie2 = getGarantieSample2();
        assertThat(garantie1).isNotEqualTo(garantie2);
    }

    @Test
    void assuranceTest() {
        Garantie garantie = getGarantieRandomSampleGenerator();
        Assurance assuranceBack = getAssuranceRandomSampleGenerator();

        garantie.addAssurance(assuranceBack);
        assertThat(garantie.getAssurances()).containsOnly(assuranceBack);
        assertThat(assuranceBack.getGaranties()).containsOnly(garantie);

        garantie.removeAssurance(assuranceBack);
        assertThat(garantie.getAssurances()).doesNotContain(assuranceBack);
        assertThat(assuranceBack.getGaranties()).doesNotContain(garantie);

        garantie.assurances(new HashSet<>(Set.of(assuranceBack)));
        assertThat(garantie.getAssurances()).containsOnly(assuranceBack);
        assertThat(assuranceBack.getGaranties()).containsOnly(garantie);

        garantie.setAssurances(new HashSet<>());
        assertThat(garantie.getAssurances()).doesNotContain(assuranceBack);
        assertThat(assuranceBack.getGaranties()).doesNotContain(garantie);
    }
}
