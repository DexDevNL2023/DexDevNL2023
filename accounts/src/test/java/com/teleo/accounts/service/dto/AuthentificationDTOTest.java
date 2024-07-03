package com.teleo.accounts.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthentificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthentificationDTO.class);
        AuthentificationDTO authentificationDTO1 = new AuthentificationDTO();
        authentificationDTO1.setId(1L);
        AuthentificationDTO authentificationDTO2 = new AuthentificationDTO();
        assertThat(authentificationDTO1).isNotEqualTo(authentificationDTO2);
        authentificationDTO2.setId(authentificationDTO1.getId());
        assertThat(authentificationDTO1).isEqualTo(authentificationDTO2);
        authentificationDTO2.setId(2L);
        assertThat(authentificationDTO1).isNotEqualTo(authentificationDTO2);
        authentificationDTO1.setId(null);
        assertThat(authentificationDTO1).isNotEqualTo(authentificationDTO2);
    }
}
