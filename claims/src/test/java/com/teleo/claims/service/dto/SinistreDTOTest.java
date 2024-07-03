package com.teleo.claims.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SinistreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SinistreDTO.class);
        SinistreDTO sinistreDTO1 = new SinistreDTO();
        sinistreDTO1.setId(1L);
        SinistreDTO sinistreDTO2 = new SinistreDTO();
        assertThat(sinistreDTO1).isNotEqualTo(sinistreDTO2);
        sinistreDTO2.setId(sinistreDTO1.getId());
        assertThat(sinistreDTO1).isEqualTo(sinistreDTO2);
        sinistreDTO2.setId(2L);
        assertThat(sinistreDTO1).isNotEqualTo(sinistreDTO2);
        sinistreDTO1.setId(null);
        assertThat(sinistreDTO1).isNotEqualTo(sinistreDTO2);
    }
}
