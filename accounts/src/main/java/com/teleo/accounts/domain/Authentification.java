package com.teleo.accounts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Authentification.
 */
@Entity
@Table(name = "authentification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Authentification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_connexion", nullable = false)
    private Instant dateConnexion;

    @NotNull
    @Column(name = "ip_adresse", nullable = false)
    private String ipAdresse;

    @JsonIgnoreProperties(value = { "authentification" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authentification")
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Authentification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateConnexion() {
        return this.dateConnexion;
    }

    public Authentification dateConnexion(Instant dateConnexion) {
        this.setDateConnexion(dateConnexion);
        return this;
    }

    public void setDateConnexion(Instant dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public String getIpAdresse() {
        return this.ipAdresse;
    }

    public Authentification ipAdresse(String ipAdresse) {
        this.setIpAdresse(ipAdresse);
        return this;
    }

    public void setIpAdresse(String ipAdresse) {
        this.ipAdresse = ipAdresse;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        if (this.utilisateur != null) {
            this.utilisateur.setAuthentification(null);
        }
        if (utilisateur != null) {
            utilisateur.setAuthentification(this);
        }
        this.utilisateur = utilisateur;
    }

    public Authentification utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authentification)) {
            return false;
        }
        return getId() != null && getId().equals(((Authentification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authentification{" +
            "id=" + getId() +
            ", dateConnexion='" + getDateConnexion() + "'" +
            ", ipAdresse='" + getIpAdresse() + "'" +
            "}";
    }
}
