package com.teleo.claims.service.dto;

import com.teleo.claims.domain.enumeration.FinanceurType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.teleo.claims.domain.FinanceurSoin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinanceurSoinDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private FinanceurType type;

    @NotNull
    private String adresse;

    @NotNull
    private String telephone;

    @NotNull
    private String email;

    @NotNull
    private String servicesFournis;

    private Set<PrestationSoinDTO> prestationSoins = new HashSet<>();

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

    public FinanceurType getType() {
        return type;
    }

    public void setType(FinanceurType type) {
        this.type = type;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServicesFournis() {
        return servicesFournis;
    }

    public void setServicesFournis(String servicesFournis) {
        this.servicesFournis = servicesFournis;
    }

    public Set<PrestationSoinDTO> getPrestationSoins() {
        return prestationSoins;
    }

    public void setPrestationSoins(Set<PrestationSoinDTO> prestationSoins) {
        this.prestationSoins = prestationSoins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinanceurSoinDTO)) {
            return false;
        }

        FinanceurSoinDTO financeurSoinDTO = (FinanceurSoinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financeurSoinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinanceurSoinDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", servicesFournis='" + getServicesFournis() + "'" +
            ", prestationSoins=" + getPrestationSoins() +
            "}";
    }
}
