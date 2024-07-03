package com.teleo.claims.domain;

import static com.teleo.claims.domain.FinanceurSoinTestSamples.*;
import static com.teleo.claims.domain.PrestationSoinTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PrestationSoinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationSoin.class);
        PrestationSoin prestationSoin1 = getPrestationSoinSample1();
        PrestationSoin prestationSoin2 = new PrestationSoin();
        assertThat(prestationSoin1).isNotEqualTo(prestationSoin2);

        prestationSoin2.setId(prestationSoin1.getId());
        assertThat(prestationSoin1).isEqualTo(prestationSoin2);

        prestationSoin2 = getPrestationSoinSample2();
        assertThat(prestationSoin1).isNotEqualTo(prestationSoin2);
    }

    @Test
    void financeurSoinTest() {
        PrestationSoin prestationSoin = getPrestationSoinRandomSampleGenerator();
        FinanceurSoin financeurSoinBack = getFinanceurSoinRandomSampleGenerator();

        prestationSoin.setFinanceurSoin(financeurSoinBack);
        assertThat(prestationSoin.getFinanceurSoin()).isEqualTo(financeurSoinBack);

        prestationSoin.financeurSoin(null);
        assertThat(prestationSoin.getFinanceurSoin()).isNull();
    }

    @Test
    void financeurSoinTest() {
        PrestationSoin prestationSoin = getPrestationSoinRandomSampleGenerator();
        FinanceurSoin financeurSoinBack = getFinanceurSoinRandomSampleGenerator();

        prestationSoin.addFinanceurSoin(financeurSoinBack);
        assertThat(prestationSoin.getFinanceurSoins()).containsOnly(financeurSoinBack);

        prestationSoin.removeFinanceurSoin(financeurSoinBack);
        assertThat(prestationSoin.getFinanceurSoins()).doesNotContain(financeurSoinBack);

        prestationSoin.financeurSoins(new HashSet<>(Set.of(financeurSoinBack)));
        assertThat(prestationSoin.getFinanceurSoins()).containsOnly(financeurSoinBack);

        prestationSoin.setFinanceurSoins(new HashSet<>());
        assertThat(prestationSoin.getFinanceurSoins()).doesNotContain(financeurSoinBack);
    }
}
