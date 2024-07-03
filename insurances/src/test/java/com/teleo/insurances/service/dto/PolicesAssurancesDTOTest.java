package com.teleo.insurances.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.insurances.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicesAssurancesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicesAssurancesDTO.class);
        PolicesAssurancesDTO policesAssurancesDTO1 = new PolicesAssurancesDTO();
        policesAssurancesDTO1.setId(1L);
        PolicesAssurancesDTO policesAssurancesDTO2 = new PolicesAssurancesDTO();
        assertThat(policesAssurancesDTO1).isNotEqualTo(policesAssurancesDTO2);
        policesAssurancesDTO2.setId(policesAssurancesDTO1.getId());
        assertThat(policesAssurancesDTO1).isEqualTo(policesAssurancesDTO2);
        policesAssurancesDTO2.setId(2L);
        assertThat(policesAssurancesDTO1).isNotEqualTo(policesAssurancesDTO2);
        policesAssurancesDTO1.setId(null);
        assertThat(policesAssurancesDTO1).isNotEqualTo(policesAssurancesDTO2);
    }
}
