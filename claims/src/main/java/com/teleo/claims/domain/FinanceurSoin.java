package com.teleo.claims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.claims.domain.enumeration.FinanceurType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FinanceurSoin.
 */
@Entity
@Table(name = "financeur_soin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinanceurSoin implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FinanceurType type;

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
    @Column(name = "services_fournis", nullable = false)
    private String servicesFournis;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "financeurSoins")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "financeurSoin", "financeurSoins" }, allowSetters = true)
    private Set<PrestationSoin> prestationSoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinanceurSoin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public FinanceurSoin nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public FinanceurType getType() {
        return this.type;
    }

    public FinanceurSoin type(FinanceurType type) {
        this.setType(type);
        return this;
    }

    public void setType(FinanceurType type) {
        this.type = type;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public FinanceurSoin adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public FinanceurSoin telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public FinanceurSoin email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServicesFournis() {
        return this.servicesFournis;
    }

    public FinanceurSoin servicesFournis(String servicesFournis) {
        this.setServicesFournis(servicesFournis);
        return this;
    }

    public void setServicesFournis(String servicesFournis) {
        this.servicesFournis = servicesFournis;
    }

    public Set<PrestationSoin> getPrestationSoins() {
        return this.prestationSoins;
    }

    public void setPrestationSoins(Set<PrestationSoin> prestationSoins) {
        if (this.prestationSoins != null) {
            this.prestationSoins.forEach(i -> i.removeFinanceurSoin(this));
        }
        if (prestationSoins != null) {
            prestationSoins.forEach(i -> i.addFinanceurSoin(this));
        }
        this.prestationSoins = prestationSoins;
    }

    public FinanceurSoin prestationSoins(Set<PrestationSoin> prestationSoins) {
        this.setPrestationSoins(prestationSoins);
        return this;
    }

    public FinanceurSoin addPrestationSoin(PrestationSoin prestationSoin) {
        this.prestationSoins.add(prestationSoin);
        prestationSoin.getFinanceurSoins().add(this);
        return this;
    }

    public FinanceurSoin removePrestationSoin(PrestationSoin prestationSoin) {
        this.prestationSoins.remove(prestationSoin);
        prestationSoin.getFinanceurSoins().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinanceurSoin)) {
            return false;
        }
        return getId() != null && getId().equals(((FinanceurSoin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinanceurSoin{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", servicesFournis='" + getServicesFournis() + "'" +
            "}";
    }
}
