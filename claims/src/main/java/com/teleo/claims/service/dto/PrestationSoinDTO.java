package com.teleo.claims.service.dto;

import com.teleo.claims.domain.enumeration.PrestationType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.teleo.claims.domain.PrestationSoin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationSoinDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String description;

    @NotNull
    private PrestationType type;

    @NotNull
    private BigDecimal montant;

    private FinanceurSoinDTO financeurSoin;

    private Set<FinanceurSoinDTO> financeurSoins = new HashSet<>();

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

    public PrestationType getType() {
        return type;
    }

    public void setType(PrestationType type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public FinanceurSoinDTO getFinanceurSoin() {
        return financeurSoin;
    }

    public void setFinanceurSoin(FinanceurSoinDTO financeurSoin) {
        this.financeurSoin = financeurSoin;
    }

    public Set<FinanceurSoinDTO> getFinanceurSoins() {
        return financeurSoins;
    }

    public void setFinanceurSoins(Set<FinanceurSoinDTO> financeurSoins) {
        this.financeurSoins = financeurSoins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestationSoinDTO)) {
            return false;
        }

        PrestationSoinDTO prestationSoinDTO = (PrestationSoinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestationSoinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationSoinDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", montant=" + getMontant() +
            ", financeurSoin=" + getFinanceurSoin() +
            ", financeurSoins=" + getFinanceurSoins() +
            "}";
    }
}
