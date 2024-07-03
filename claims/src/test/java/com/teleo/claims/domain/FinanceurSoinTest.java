package com.teleo.claims.domain;

import static com.teleo.claims.domain.FinanceurSoinTestSamples.*;
import static com.teleo.claims.domain.PrestationSoinTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FinanceurSoinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinanceurSoin.class);
        FinanceurSoin financeurSoin1 = getFinanceurSoinSample1();
        FinanceurSoin financeurSoin2 = new FinanceurSoin();
        assertThat(financeurSoin1).isNotEqualTo(financeurSoin2);

        financeurSoin2.setId(financeurSoin1.getId());
        assertThat(financeurSoin1).isEqualTo(financeurSoin2);

        financeurSoin2 = getFinanceurSoinSample2();
        assertThat(financeurSoin1).isNotEqualTo(financeurSoin2);
    }

    @Test
    void prestationSoinTest() {
        FinanceurSoin financeurSoin = getFinanceurSoinRandomSampleGenerator();
        PrestationSoin prestationSoinBack = getPrestationSoinRandomSampleGenerator();

        financeurSoin.addPrestationSoin(prestationSoinBack);
        assertThat(financeurSoin.getPrestationSoins()).containsOnly(prestationSoinBack);
        assertThat(prestationSoinBack.getFinanceurSoins()).containsOnly(financeurSoin);

        financeurSoin.removePrestationSoin(prestationSoinBack);
        assertThat(financeurSoin.getPrestationSoins()).doesNotContain(prestationSoinBack);
        assertThat(prestationSoinBack.getFinanceurSoins()).doesNotContain(financeurSoin);

        financeurSoin.prestationSoins(new HashSet<>(Set.of(prestationSoinBack)));
        assertThat(financeurSoin.getPrestationSoins()).containsOnly(prestationSoinBack);
        assertThat(prestationSoinBack.getFinanceurSoins()).containsOnly(financeurSoin);

        financeurSoin.setPrestationSoins(new HashSet<>());
        assertThat(financeurSoin.getPrestationSoins()).doesNotContain(prestationSoinBack);
        assertThat(prestationSoinBack.getFinanceurSoins()).doesNotContain(financeurSoin);
    }
}
