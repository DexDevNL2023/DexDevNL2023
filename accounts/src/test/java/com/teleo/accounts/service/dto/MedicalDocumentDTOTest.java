package com.teleo.accounts.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalDocumentDTO.class);
        MedicalDocumentDTO medicalDocumentDTO1 = new MedicalDocumentDTO();
        medicalDocumentDTO1.setId(1L);
        MedicalDocumentDTO medicalDocumentDTO2 = new MedicalDocumentDTO();
        assertThat(medicalDocumentDTO1).isNotEqualTo(medicalDocumentDTO2);
        medicalDocumentDTO2.setId(medicalDocumentDTO1.getId());
        assertThat(medicalDocumentDTO1).isEqualTo(medicalDocumentDTO2);
        medicalDocumentDTO2.setId(2L);
        assertThat(medicalDocumentDTO1).isNotEqualTo(medicalDocumentDTO2);
        medicalDocumentDTO1.setId(null);
        assertThat(medicalDocumentDTO1).isNotEqualTo(medicalDocumentDTO2);
    }
}
