package com.teleo.claims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.claims.domain.enumeration.PrestationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrestationSoin.
 */
@Entity
@Table(name = "prestation_soin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationSoin implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PrestationType type;

    @NotNull
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_prestation_soin__financeur_soin",
        joinColumns = @JoinColumn(name = "prestation_soin_id"),
        inverseJoinColumns = @JoinColumn(name = "financeur_soin_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestationSoins" }, allowSetters = true)
    private FinanceurSoin financeurSoin;

    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_prestation_soin__financeur_soin",
        joinColumns = @JoinColumn(name = "prestation_soin_id"),
        inverseJoinColumns = @JoinColumn(name = "financeur_soin_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestationSoins" }, allowSetters = true)
    private Set<FinanceurSoin> financeurSoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrestationSoin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public PrestationSoin nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public PrestationSoin description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PrestationType getType() {
        return this.type;
    }

    public PrestationSoin type(PrestationType type) {
        this.setType(type);
        return this;
    }

    public void setType(PrestationType type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return this.montant;
    }

    public PrestationSoin montant(BigDecimal montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public FinanceurSoin getFinanceurSoin() {
        return this.financeurSoin;
    }

    public void setFinanceurSoin(FinanceurSoin financeurSoin) {
        this.financeurSoin = financeurSoin;
    }

    public PrestationSoin financeurSoin(FinanceurSoin financeurSoin) {
        this.setFinanceurSoin(financeurSoin);
        return this;
    }

    public Set<FinanceurSoin> getFinanceurSoins() {
        return this.financeurSoins;
    }

    public void setFinanceurSoins(Set<FinanceurSoin> financeurSoins) {
        this.financeurSoins = financeurSoins;
    }

    public PrestationSoin financeurSoins(Set<FinanceurSoin> financeurSoins) {
        this.setFinanceurSoins(financeurSoins);
        return this;
    }

    public PrestationSoin addFinanceurSoin(FinanceurSoin financeurSoin) {
        this.financeurSoins.add(financeurSoin);
        return this;
    }

    public PrestationSoin removeFinanceurSoin(FinanceurSoin financeurSoin) {
        this.financeurSoins.remove(financeurSoin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestationSoin)) {
            return false;
        }
        return getId() != null && getId().equals(((PrestationSoin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationSoin{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
