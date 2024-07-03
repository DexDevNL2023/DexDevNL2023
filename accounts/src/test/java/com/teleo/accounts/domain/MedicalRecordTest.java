package com.teleo.accounts.domain;

import static com.teleo.accounts.domain.MedicalRecordTestSamples.*;
import static com.teleo.accounts.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalRecord.class);
        MedicalRecord medicalRecord1 = getMedicalRecordSample1();
        MedicalRecord medicalRecord2 = new MedicalRecord();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);

        medicalRecord2.setId(medicalRecord1.getId());
        assertThat(medicalRecord1).isEqualTo(medicalRecord2);

        medicalRecord2 = getMedicalRecordSample2();
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);
    }

    @Test
    void utilisateurTest() {
        MedicalRecord medicalRecord = getMedicalRecordRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        medicalRecord.setUtilisateur(userExtraBack);
        assertThat(medicalRecord.getUtilisateur()).isEqualTo(userExtraBack);

        medicalRecord.utilisateur(null);
        assertThat(medicalRecord.getUtilisateur()).isNull();
    }
}
