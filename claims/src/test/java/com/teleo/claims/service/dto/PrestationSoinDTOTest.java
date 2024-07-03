package com.teleo.claims.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestationSoinDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationSoinDTO.class);
        PrestationSoinDTO prestationSoinDTO1 = new PrestationSoinDTO();
        prestationSoinDTO1.setId(1L);
        PrestationSoinDTO prestationSoinDTO2 = new PrestationSoinDTO();
        assertThat(prestationSoinDTO1).isNotEqualTo(prestationSoinDTO2);
        prestationSoinDTO2.setId(prestationSoinDTO1.getId());
        assertThat(prestationSoinDTO1).isEqualTo(prestationSoinDTO2);
        prestationSoinDTO2.setId(2L);
        assertThat(prestationSoinDTO1).isNotEqualTo(prestationSoinDTO2);
        prestationSoinDTO1.setId(null);
        assertThat(prestationSoinDTO1).isNotEqualTo(prestationSoinDTO2);
    }
}
