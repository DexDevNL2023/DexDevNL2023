package com.teleo.insurances.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.insurances.domain.PolicesAssurances} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicesAssurancesDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroPolice;

    @NotNull
    private Instant dateEmission;

    @NotNull
    private Instant dateExpiration;

    private String description;

    @NotNull
    private BigDecimal montantAssure;

    @NotNull
    private BigDecimal plafondAssure;

    private AssuranceDTO assurance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPolice() {
        return numeroPolice;
    }

    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontantAssure() {
        return montantAssure;
    }

    public void setMontantAssure(BigDecimal montantAssure) {
        this.montantAssure = montantAssure;
    }

    public BigDecimal getPlafondAssure() {
        return plafondAssure;
    }

    public void setPlafondAssure(BigDecimal plafondAssure) {
        this.plafondAssure = plafondAssure;
    }

    public AssuranceDTO getAssurance() {
        return assurance;
    }

    public void setAssurance(AssuranceDTO assurance) {
        this.assurance = assurance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicesAssurancesDTO)) {
            return false;
        }

        PolicesAssurancesDTO policesAssurancesDTO = (PolicesAssurancesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, policesAssurancesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicesAssurancesDTO{" +
            "id=" + getId() +
            ", numeroPolice='" + getNumeroPolice() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", description='" + getDescription() + "'" +
            ", montantAssure=" + getMontantAssure() +
            ", plafondAssure=" + getPlafondAssure() +
            ", assurance=" + getAssurance() +
            "}";
    }
}
