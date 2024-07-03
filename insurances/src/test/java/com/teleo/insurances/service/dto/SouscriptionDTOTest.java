package com.teleo.insurances.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SouscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SouscriptionDTO.class);
        SouscriptionDTO souscriptionDTO1 = new SouscriptionDTO();
        souscriptionDTO1.setId(1L);
        SouscriptionDTO souscriptionDTO2 = new SouscriptionDTO();
        assertThat(souscriptionDTO1).isNotEqualTo(souscriptionDTO2);
        souscriptionDTO2.setId(souscriptionDTO1.getId());
        assertThat(souscriptionDTO1).isEqualTo(souscriptionDTO2);
        souscriptionDTO2.setId(2L);
        assertThat(souscriptionDTO1).isNotEqualTo(souscriptionDTO2);
        souscriptionDTO1.setId(null);
        assertThat(souscriptionDTO1).isNotEqualTo(souscriptionDTO2);
    }
}
