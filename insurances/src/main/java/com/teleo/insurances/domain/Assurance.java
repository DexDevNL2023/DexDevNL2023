package com.teleo.insurances.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.insurances.domain.enumeration.InsuranceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entities for insurance management microservice
 */
@Entity
@Table(name = "assurance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assurance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "conditions")
    private String conditions;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InsuranceType type;

    @NotNull
    @Column(name = "prix_assurance", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixAssurance;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_assurance__garantie",
        joinColumns = @JoinColumn(name = "assurance_id"),
        inverseJoinColumns = @JoinColumn(name = "garantie_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assurances" }, allowSetters = true)
    private Set<Garantie> garanties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assurance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Assurance nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Assurance description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditions() {
        return this.conditions;
    }

    public Assurance conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public InsuranceType getType() {
        return this.type;
    }

    public Assurance type(InsuranceType type) {
        this.setType(type);
        return this;
    }

    public void setType(InsuranceType type) {
        this.type = type;
    }

    public BigDecimal getPrixAssurance() {
        return this.prixAssurance;
    }

    public Assurance prixAssurance(BigDecimal prixAssurance) {
        this.setPrixAssurance(prixAssurance);
        return this;
    }

    public void setPrixAssurance(BigDecimal prixAssurance) {
        this.prixAssurance = prixAssurance;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Assurance dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Assurance dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Set<Garantie> getGaranties() {
        return this.garanties;
    }

    public void setGaranties(Set<Garantie> garanties) {
        this.garanties = garanties;
    }

    public Assurance garanties(Set<Garantie> garanties) {
        this.setGaranties(garanties);
        return this;
    }

    public Assurance addGarantie(Garantie garantie) {
        this.garanties.add(garantie);
        return this;
    }

    public Assurance removeGarantie(Garantie garantie) {
        this.garanties.remove(garantie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assurance)) {
            return false;
        }
        return getId() != null && getId().equals(((Assurance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assurance{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", type='" + getType() + "'" +
            ", prixAssurance=" + getPrixAssurance() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
