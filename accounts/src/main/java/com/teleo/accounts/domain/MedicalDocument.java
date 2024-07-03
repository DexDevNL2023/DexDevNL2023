package com.teleo.accounts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MedicalDocument.
 */
@Table("medical_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @NotNull(message = "must not be null")
    @Column("url")
    private String url;

    @Transient
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private MedicalRecord dossierMedical;

    @Column("dossier_medical_id")
    private Long dossierMedicalId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public MedicalDocument nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return this.type;
    }

    public MedicalDocument type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public MedicalDocument url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MedicalRecord getDossierMedical() {
        return this.dossierMedical;
    }

    public void setDossierMedical(MedicalRecord medicalRecord) {
        this.dossierMedical = medicalRecord;
        this.dossierMedicalId = medicalRecord != null ? medicalRecord.getId() : null;
    }

    public MedicalDocument dossierMedical(MedicalRecord medicalRecord) {
        this.setDossierMedical(medicalRecord);
        return this;
    }

    public Long getDossierMedicalId() {
        return this.dossierMedicalId;
    }

    public void setDossierMedicalId(Long medicalRecord) {
        this.dossierMedicalId = medicalRecord;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalDocument{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
