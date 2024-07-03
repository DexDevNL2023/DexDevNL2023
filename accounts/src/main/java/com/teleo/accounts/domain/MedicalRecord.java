package com.teleo.accounts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MedicalRecord.
 */
@Table("medical_record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("num_medical_record")
    private String numMedicalRecord;

    @NotNull(message = "must not be null")
    @Column("details")
    private String details;

    @NotNull(message = "must not be null")
    @Column("date_updated")
    private Instant dateUpdated;

    @Transient
    @JsonIgnoreProperties(value = { "internalUser" }, allowSetters = true)
    private UserExtra utilisateur;

    @Column("utilisateur_id")
    private Long utilisateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumMedicalRecord() {
        return this.numMedicalRecord;
    }

    public MedicalRecord numMedicalRecord(String numMedicalRecord) {
        this.setNumMedicalRecord(numMedicalRecord);
        return this;
    }

    public void setNumMedicalRecord(String numMedicalRecord) {
        this.numMedicalRecord = numMedicalRecord;
    }

    public String getDetails() {
        return this.details;
    }

    public MedicalRecord details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getDateUpdated() {
        return this.dateUpdated;
    }

    public MedicalRecord dateUpdated(Instant dateUpdated) {
        this.setDateUpdated(dateUpdated);
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public UserExtra getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(UserExtra userExtra) {
        this.utilisateur = userExtra;
        this.utilisateurId = userExtra != null ? userExtra.getId() : null;
    }

    public MedicalRecord utilisateur(UserExtra userExtra) {
        this.setUtilisateur(userExtra);
        return this;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public void setUtilisateurId(Long userExtra) {
        this.utilisateurId = userExtra;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecord{" +
            "id=" + getId() +
            ", numMedicalRecord='" + getNumMedicalRecord() + "'" +
            ", details='" + getDetails() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
