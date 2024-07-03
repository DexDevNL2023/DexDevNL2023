package com.teleo.accounts.domain;

import static com.teleo.accounts.domain.MedicalDocumentTestSamples.*;
import static com.teleo.accounts.domain.MedicalRecordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalDocument.class);
        MedicalDocument medicalDocument1 = getMedicalDocumentSample1();
        MedicalDocument medicalDocument2 = new MedicalDocument();
        assertThat(medicalDocument1).isNotEqualTo(medicalDocument2);

        medicalDocument2.setId(medicalDocument1.getId());
        assertThat(medicalDocument1).isEqualTo(medicalDocument2);

        medicalDocument2 = getMedicalDocumentSample2();
        assertThat(medicalDocument1).isNotEqualTo(medicalDocument2);
    }

    @Test
    void dossierMedicalTest() {
        MedicalDocument medicalDocument = getMedicalDocumentRandomSampleGenerator();
        MedicalRecord medicalRecordBack = getMedicalRecordRandomSampleGenerator();

        medicalDocument.setDossierMedical(medicalRecordBack);
        assertThat(medicalDocument.getDossierMedical()).isEqualTo(medicalRecordBack);

        medicalDocument.dossierMedical(null);
        assertThat(medicalDocument.getDossierMedical()).isNull();
    }
}
