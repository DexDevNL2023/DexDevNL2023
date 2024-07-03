package com.teleo.claims.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "addresse", nullable = false)
    private String addresse;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "pays", nullable = false)
    private String pays;

    @NotNull
    @Column(name = "services_fournis", nullable = false)
    private String servicesFournis;

    @NotNull
    @Column(name = "date_debut_contrat", nullable = false)
    private Instant dateDebutContrat;

    @Column(name = "date_fin_contrat")
    private Instant dateFinContrat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fournisseur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Fournisseur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Fournisseur adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Fournisseur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public Fournisseur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddresse() {
        return this.addresse;
    }

    public Fournisseur addresse(String addresse) {
        this.setAddresse(addresse);
        return this;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getVille() {
        return this.ville;
    }

    public Fournisseur ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return this.pays;
    }

    public Fournisseur pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getServicesFournis() {
        return this.servicesFournis;
    }

    public Fournisseur servicesFournis(String servicesFournis) {
        this.setServicesFournis(servicesFournis);
        return this;
    }

    public void setServicesFournis(String servicesFournis) {
        this.servicesFournis = servicesFournis;
    }

    public Instant getDateDebutContrat() {
        return this.dateDebutContrat;
    }

    public Fournisseur dateDebutContrat(Instant dateDebutContrat) {
        this.setDateDebutContrat(dateDebutContrat);
        return this;
    }

    public void setDateDebutContrat(Instant dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public Instant getDateFinContrat() {
        return this.dateFinContrat;
    }

    public Fournisseur dateFinContrat(Instant dateFinContrat) {
        this.setDateFinContrat(dateFinContrat);
        return this;
    }

    public void setDateFinContrat(Instant dateFinContrat) {
        this.dateFinContrat = dateFinContrat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return getId() != null && getId().equals(((Fournisseur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
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
