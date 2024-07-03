package com.teleo.insurances.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GarantieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GarantieDTO.class);
        GarantieDTO garantieDTO1 = new GarantieDTO();
        garantieDTO1.setId(1L);
        GarantieDTO garantieDTO2 = new GarantieDTO();
        assertThat(garantieDTO1).isNotEqualTo(garantieDTO2);
        garantieDTO2.setId(garantieDTO1.getId());
        assertThat(garantieDTO1).isEqualTo(garantieDTO2);
        garantieDTO2.setId(2L);
        assertThat(garantieDTO1).isNotEqualTo(garantieDTO2);
        garantieDTO1.setId(null);
        assertThat(garantieDTO1).isNotEqualTo(garantieDTO2);
    }
}
