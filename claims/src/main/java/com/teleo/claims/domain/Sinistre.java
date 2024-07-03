package com.teleo.claims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.claims.domain.enumeration.ClaimStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entities for claim management microservice
 */
@Entity
@Table(name = "sinistre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sinistre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_sinistre", nullable = false, unique = true)
    private String numeroSinistre;

    @NotNull
    @Column(name = "date_declaration", nullable = false)
    private Instant dateDeclaration;

    @Column(name = "date_traitement")
    private Instant dateTraitement;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status;

    @NotNull
    @Column(name = "raison", nullable = false)
    private String raison;

    @NotNull
    @Column(name = "montant_reclame", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantReclame;

    @Column(name = "montant_approuve", precision = 21, scale = 2)
    private BigDecimal montantApprouve;

    @NotNull
    @Column(name = "souscription_id", nullable = false)
    private Long souscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Fournisseur fournisseur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financeurSoin", "financeurSoins" }, allowSetters = true)
    private PrestationSoin prestationsSoin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sinistre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSinistre() {
        return this.numeroSinistre;
    }

    public Sinistre numeroSinistre(String numeroSinistre) {
        this.setNumeroSinistre(numeroSinistre);
        return this;
    }

    public void setNumeroSinistre(String numeroSinistre) {
        this.numeroSinistre = numeroSinistre;
    }

    public Instant getDateDeclaration() {
        return this.dateDeclaration;
    }

    public Sinistre dateDeclaration(Instant dateDeclaration) {
        this.setDateDeclaration(dateDeclaration);
        return this;
    }

    public void setDateDeclaration(Instant dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public Instant getDateTraitement() {
        return this.dateTraitement;
    }

    public Sinistre dateTraitement(Instant dateTraitement) {
        this.setDateTraitement(dateTraitement);
        return this;
    }

    public void setDateTraitement(Instant dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public ClaimStatus getStatus() {
        return this.status;
    }

    public Sinistre status(ClaimStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getRaison() {
        return this.raison;
    }

    public Sinistre raison(String raison) {
        this.setRaison(raison);
        return this;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public BigDecimal getMontantReclame() {
        return this.montantReclame;
    }

    public Sinistre montantReclame(BigDecimal montantReclame) {
        this.setMontantReclame(montantReclame);
        return this;
    }

    public void setMontantReclame(BigDecimal montantReclame) {
        this.montantReclame = montantReclame;
    }

    public BigDecimal getMontantApprouve() {
        return this.montantApprouve;
    }

    public Sinistre montantApprouve(BigDecimal montantApprouve) {
        this.setMontantApprouve(montantApprouve);
        return this;
    }

    public void setMontantApprouve(BigDecimal montantApprouve) {
        this.montantApprouve = montantApprouve;
    }

    public Long getSouscriptionId() {
        return this.souscriptionId;
    }

    public Sinistre souscriptionId(Long souscriptionId) {
        this.setSouscriptionId(souscriptionId);
        return this;
    }

    public void setSouscriptionId(Long souscriptionId) {
        this.souscriptionId = souscriptionId;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Sinistre fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public PrestationSoin getPrestationsSoin() {
        return this.prestationsSoin;
    }

    public void setPrestationsSoin(PrestationSoin prestationSoin) {
        this.prestationsSoin = prestationSoin;
    }

    public Sinistre prestationsSoin(PrestationSoin prestationSoin) {
        this.setPrestationsSoin(prestationSoin);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sinistre)) {
            return false;
        }
        return getId() != null && getId().equals(((Sinistre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sinistre{" +
            "id=" + getId() +
            ", numeroSinistre='" + getNumeroSinistre() + "'" +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            ", status='" + getStatus() + "'" +
            ", raison='" + getRaison() + "'" +
            ", montantReclame=" + getMontantReclame() +
            ", montantApprouve=" + getMontantApprouve() +
            ", souscriptionId=" + getSouscriptionId() +
            "}";
    }
}
