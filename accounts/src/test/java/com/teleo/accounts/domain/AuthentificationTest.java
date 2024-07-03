package com.teleo.accounts.domain;

import static com.teleo.accounts.domain.AuthentificationTestSamples.*;
import static com.teleo.accounts.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthentificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authentification.class);
        Authentification authentification1 = getAuthentificationSample1();
        Authentification authentification2 = new Authentification();
        assertThat(authentification1).isNotEqualTo(authentification2);

        authentification2.setId(authentification1.getId());
        assertThat(authentification1).isEqualTo(authentification2);

        authentification2 = getAuthentificationSample2();
        assertThat(authentification1).isNotEqualTo(authentification2);
    }

    @Test
    void utilisateurTest() {
        Authentification authentification = getAuthentificationRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        authentification.setUtilisateur(utilisateurBack);
        assertThat(authentification.getUtilisateur()).isEqualTo(utilisateurBack);
        assertThat(utilisateurBack.getAuthentification()).isEqualTo(authentification);

        authentification.utilisateur(null);
        assertThat(authentification.getUtilisateur()).isNull();
        assertThat(utilisateurBack.getAuthentification()).isNull();
    }
}
