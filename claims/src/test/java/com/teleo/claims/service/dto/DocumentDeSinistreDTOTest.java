package com.teleo.claims.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.claims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentDeSinistreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDeSinistreDTO.class);
        DocumentDeSinistreDTO documentDeSinistreDTO1 = new DocumentDeSinistreDTO();
        documentDeSinistreDTO1.setId(1L);
        DocumentDeSinistreDTO documentDeSinistreDTO2 = new DocumentDeSinistreDTO();
        assertThat(documentDeSinistreDTO1).isNotEqualTo(documentDeSinistreDTO2);
        documentDeSinistreDTO2.setId(documentDeSinistreDTO1.getId());
        assertThat(documentDeSinistreDTO1).isEqualTo(documentDeSinistreDTO2);
        documentDeSinistreDTO2.setId(2L);
        assertThat(documentDeSinistreDTO1).isNotEqualTo(documentDeSinistreDTO2);
        documentDeSinistreDTO1.setId(null);
        assertThat(documentDeSinistreDTO1).isNotEqualTo(documentDeSinistreDTO2);
    }
}
