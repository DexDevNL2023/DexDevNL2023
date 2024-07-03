package com.teleo.insurances.service.dto;

import com.teleo.insurances.domain.enumeration.InsuranceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.teleo.insurances.domain.Assurance} entity.
 */
@Schema(description = "Entities for insurance management microservice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssuranceDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String description;

    private String conditions;

    @NotNull
    private InsuranceType type;

    @NotNull
    private BigDecimal prixAssurance;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateFin;

    private Set<GarantieDTO> garanties = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public InsuranceType getType() {
        return type;
    }

    public void setType(InsuranceType type) {
        this.type = type;
    }

    public BigDecimal getPrixAssurance() {
        return prixAssurance;
    }

    public void setPrixAssurance(BigDecimal prixAssurance) {
        this.prixAssurance = prixAssurance;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Set<GarantieDTO> getGaranties() {
        return garanties;
    }

    public void setGaranties(Set<GarantieDTO> garanties) {
        this.garanties = garanties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssuranceDTO)) {
            return false;
        }

        AssuranceDTO assuranceDTO = (AssuranceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assuranceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssuranceDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", type='" + getType() + "'" +
            ", prixAssurance=" + getPrixAssurance() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", garanties=" + getGaranties() +
            "}";
    }
}
