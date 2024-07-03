package com.teleo.insurances.domain;

import static com.teleo.insurances.domain.AssuranceTestSamples.*;
import static com.teleo.insurances.domain.GarantieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AssuranceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assurance.class);
        Assurance assurance1 = getAssuranceSample1();
        Assurance assurance2 = new Assurance();
        assertThat(assurance1).isNotEqualTo(assurance2);

        assurance2.setId(assurance1.getId());
        assertThat(assurance1).isEqualTo(assurance2);

        assurance2 = getAssuranceSample2();
        assertThat(assurance1).isNotEqualTo(assurance2);
    }

    @Test
    void garantieTest() {
        Assurance assurance = getAssuranceRandomSampleGenerator();
        Garantie garantieBack = getGarantieRandomSampleGenerator();

        assurance.addGarantie(garantieBack);
        assertThat(assurance.getGaranties()).containsOnly(garantieBack);

        assurance.removeGarantie(garantieBack);
        assertThat(assurance.getGaranties()).doesNotContain(garantieBack);

        assurance.garanties(new HashSet<>(Set.of(garantieBack)));
        assertThat(assurance.getGaranties()).containsOnly(garantieBack);

        assurance.setGaranties(new HashSet<>());
        assertThat(assurance.getGaranties()).doesNotContain(garantieBack);
    }
}
