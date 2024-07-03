package com.teleo.insurances.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssuranceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssuranceDTO.class);
        AssuranceDTO assuranceDTO1 = new AssuranceDTO();
        assuranceDTO1.setId(1L);
        AssuranceDTO assuranceDTO2 = new AssuranceDTO();
        assertThat(assuranceDTO1).isNotEqualTo(assuranceDTO2);
        assuranceDTO2.setId(assuranceDTO1.getId());
        assertThat(assuranceDTO1).isEqualTo(assuranceDTO2);
        assuranceDTO2.setId(2L);
        assertThat(assuranceDTO1).isNotEqualTo(assuranceDTO2);
        assuranceDTO1.setId(null);
        assertThat(assuranceDTO1).isNotEqualTo(assuranceDTO2);
    }
}
