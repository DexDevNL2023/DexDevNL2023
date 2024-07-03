package com.teleo.claims.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.claims.domain.Fournisseur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FournisseurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String adresse;

    @NotNull
    private String telephone;

    @NotNull
    private String email;

    @NotNull
    private String addresse;

    @NotNull
    private String ville;

    @NotNull
    private String pays;

    @NotNull
    private String servicesFournis;

    @NotNull
    private Instant dateDebutContrat;

    private Instant dateFinContrat;

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

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getServicesFournis() {
        return servicesFournis;
    }

    public void setServicesFournis(String servicesFournis) {
        this.servicesFournis = servicesFournis;
    }

    public Instant getDateDebutContrat() {
        return dateDebutContrat;
    }

    public void setDateDebutContrat(Instant dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public Instant getDateFinContrat() {
        return dateFinContrat;
    }

    public void setDateFinContrat(Instant dateFinContrat) {
        this.dateFinContrat = dateFinContrat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FournisseurDTO)) {
            return false;
        }

        FournisseurDTO fournisseurDTO = (FournisseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fournisseurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FournisseurDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", addresse='" + getAddresse() + "'" +
            ", ville='" + getVille() + "'" +
            ", pays='" + getPays() + "'" +
            ", servicesFournis='" + getServicesFournis() + "'" +
            ", dateDebutContrat='" + getDateDebutContrat() + "'" +
            ", dateFinContrat='" + getDateFinContrat() + "'" +
            "}";
    }
}
