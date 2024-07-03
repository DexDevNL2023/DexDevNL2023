package com.teleo.claims.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinanceurSoinDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinanceurSoinDTO.class);
        FinanceurSoinDTO financeurSoinDTO1 = new FinanceurSoinDTO();
        financeurSoinDTO1.setId(1L);
        FinanceurSoinDTO financeurSoinDTO2 = new FinanceurSoinDTO();
        assertThat(financeurSoinDTO1).isNotEqualTo(financeurSoinDTO2);
        financeurSoinDTO2.setId(financeurSoinDTO1.getId());
        assertThat(financeurSoinDTO1).isEqualTo(financeurSoinDTO2);
        financeurSoinDTO2.setId(2L);
        assertThat(financeurSoinDTO1).isNotEqualTo(financeurSoinDTO2);
        financeurSoinDTO1.setId(null);
        assertThat(financeurSoinDTO1).isNotEqualTo(financeurSoinDTO2);
    }
}
