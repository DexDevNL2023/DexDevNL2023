package com.teleo.accounts.domain;

import static com.teleo.accounts.domain.AuthentificationTestSamples.*;
import static com.teleo.accounts.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void authentificationTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Authentification authentificationBack = getAuthentificationRandomSampleGenerator();

        utilisateur.setAuthentification(authentificationBack);
        assertThat(utilisateur.getAuthentification()).isEqualTo(authentificationBack);

        utilisateur.authentification(null);
        assertThat(utilisateur.getAuthentification()).isNull();
    }
}
