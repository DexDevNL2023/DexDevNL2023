package com.teleo.accounts.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.accounts.domain.MedicalDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalDocumentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nom;

    @NotNull(message = "must not be null")
    private String type;

    @NotNull(message = "must not be null")
    private String url;

    private MedicalRecordDTO dossierMedical;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MedicalRecordDTO getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(MedicalRecordDTO dossierMedical) {
        this.dossierMedical = dossierMedical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalDocumentDTO)) {
            return false;
        }

        MedicalDocumentDTO medicalDocumentDTO = (MedicalDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalDocumentDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", dossierMedical=" + getDossierMedical() +
            "}";
    }
}
