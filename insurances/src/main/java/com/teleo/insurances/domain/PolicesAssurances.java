package com.teleo.insurances.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PolicesAssurances.
 */
@Entity
@Table(name = "polices_assurances")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicesAssurances implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_police", nullable = false, unique = true)
    private String numeroPolice;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private Instant dateEmission;

    @NotNull
    @Column(name = "date_expiration", nullable = false)
    private Instant dateExpiration;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "montant_assure", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantAssure;

    @NotNull
    @Column(name = "plafond_assure", precision = 21, scale = 2, nullable = false)
    private BigDecimal plafondAssure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "garanties" }, allowSetters = true)
    private Assurance assurance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PolicesAssurances id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPolice() {
        return this.numeroPolice;
    }

    public PolicesAssurances numeroPolice(String numeroPolice) {
        this.setNumeroPolice(numeroPolice);
        return this;
    }

    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

    public Instant getDateEmission() {
        return this.dateEmission;
    }

    public PolicesAssurances dateEmission(Instant dateEmission) {
        this.setDateEmission(dateEmission);
        return this;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDateExpiration() {
        return this.dateExpiration;
    }

    public PolicesAssurances dateExpiration(Instant dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getDescription() {
        return this.description;
    }

    public PolicesAssurances description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontantAssure() {
        return this.montantAssure;
    }

    public PolicesAssurances montantAssure(BigDecimal montantAssure) {
        this.setMontantAssure(montantAssure);
        return this;
    }

    public void setMontantAssure(BigDecimal montantAssure) {
        this.montantAssure = montantAssure;
    }

    public BigDecimal getPlafondAssure() {
        return this.plafondAssure;
    }

    public PolicesAssurances plafondAssure(BigDecimal plafondAssure) {
        this.setPlafondAssure(plafondAssure);
        return this;
    }

    public void setPlafondAssure(BigDecimal plafondAssure) {
        this.plafondAssure = plafondAssure;
    }

    public Assurance getAssurance() {
        return this.assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    public PolicesAssurances assurance(Assurance assurance) {
        this.setAssurance(assurance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicesAssurances)) {
            return false;
        }
        return getId() != null && getId().equals(((PolicesAssurances) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicesAssurances{" +
            "id=" + getId() +
            ", numeroPolice='" + getNumeroPolice() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", description='" + getDescription() + "'" +
            ", montantAssure=" + getMontantAssure() +
            ", plafondAssure=" + getPlafondAssure() +
            "}";
    }
}
