package com.teleo.insurances.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.insurances.domain.enumeration.GarantieStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Garantie.
 */
@Entity
@Table(name = "garantie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Garantie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GarantieStatus status;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "garanties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "garanties" }, allowSetters = true)
    private Set<Assurance> assurances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Garantie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Garantie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Garantie dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Garantie dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public GarantieStatus getStatus() {
        return this.status;
    }

    public Garantie status(GarantieStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(GarantieStatus status) {
        this.status = status;
    }

    public Set<Assurance> getAssurances() {
        return this.assurances;
    }

    public void setAssurances(Set<Assurance> assurances) {
        if (this.assurances != null) {
            this.assurances.forEach(i -> i.removeGarantie(this));
        }
        if (assurances != null) {
            assurances.forEach(i -> i.addGarantie(this));
        }
        this.assurances = assurances;
    }

    public Garantie assurances(Set<Assurance> assurances) {
        this.setAssurances(assurances);
        return this;
    }

    public Garantie addAssurance(Assurance assurance) {
        this.assurances.add(assurance);
        assurance.getGaranties().add(this);
        return this;
    }

    public Garantie removeAssurance(Assurance assurance) {
        this.assurances.remove(assurance);
        assurance.getGaranties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Garantie)) {
            return false;
        }
        return getId() != null && getId().equals(((Garantie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Garantie{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
