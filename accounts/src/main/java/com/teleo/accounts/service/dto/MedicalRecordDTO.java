package com.teleo.accounts.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.accounts.domain.MedicalRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecordDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String numMedicalRecord;

    @NotNull(message = "must not be null")
    private String details;

    @NotNull(message = "must not be null")
    private Instant dateUpdated;

    private UserExtraDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumMedicalRecord() {
        return numMedicalRecord;
    }

    public void setNumMedicalRecord(String numMedicalRecord) {
        this.numMedicalRecord = numMedicalRecord;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public UserExtraDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UserExtraDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecordDTO)) {
            return false;
        }

        MedicalRecordDTO medicalRecordDTO = (MedicalRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecordDTO{" +
            "id=" + getId() +
            ", numMedicalRecord='" + getNumMedicalRecord() + "'" +
            ", details='" + getDetails() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
