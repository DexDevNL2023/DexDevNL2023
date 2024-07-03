package com.teleo.insurances.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.insurances.domain.enumeration.PaymentFrequency;
import com.teleo.insurances.domain.enumeration.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Souscription.
 */
@Entity
@Table(name = "souscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Souscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_souscription", nullable = false, unique = true)
    private String numeroSouscription;

    @NotNull
    @Column(name = "date_souscription", nullable = false)
    private Instant dateSouscription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @NotNull
    @Column(name = "cumul_sinistres", precision = 21, scale = 2, nullable = false)
    private BigDecimal cumulSinistres;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequence_paiement", nullable = false)
    private PaymentFrequency frequencePaiement;

    @NotNull
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "assurance" }, allowSetters = true)
    private PolicesAssurances policesAssurances;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Souscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSouscription() {
        return this.numeroSouscription;
    }

    public Souscription numeroSouscription(String numeroSouscription) {
        this.setNumeroSouscription(numeroSouscription);
        return this;
    }

    public void setNumeroSouscription(String numeroSouscription) {
        this.numeroSouscription = numeroSouscription;
    }

    public Instant getDateSouscription() {
        return this.dateSouscription;
    }

    public Souscription dateSouscription(Instant dateSouscription) {
        this.setDateSouscription(dateSouscription);
        return this;
    }

    public void setDateSouscription(Instant dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public Souscription status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public BigDecimal getCumulSinistres() {
        return this.cumulSinistres;
    }

    public Souscription cumulSinistres(BigDecimal cumulSinistres) {
        this.setCumulSinistres(cumulSinistres);
        return this;
    }

    public void setCumulSinistres(BigDecimal cumulSinistres) {
        this.cumulSinistres = cumulSinistres;
    }

    public PaymentFrequency getFrequencePaiement() {
        return this.frequencePaiement;
    }

    public Souscription frequencePaiement(PaymentFrequency frequencePaiement) {
        this.setFrequencePaiement(frequencePaiement);
        return this;
    }

    public void setFrequencePaiement(PaymentFrequency frequencePaiement) {
        this.frequencePaiement = frequencePaiement;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public Souscription utilisateurId(Long utilisateurId) {
        this.setUtilisateurId(utilisateurId);
        return this;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public PolicesAssurances getPolicesAssurances() {
        return this.policesAssurances;
    }

    public void setPolicesAssurances(PolicesAssurances policesAssurances) {
        this.policesAssurances = policesAssurances;
    }

    public Souscription policesAssurances(PolicesAssurances policesAssurances) {
        this.setPolicesAssurances(policesAssurances);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Souscription)) {
            return false;
        }
        return getId() != null && getId().equals(((Souscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Souscription{" +
            "id=" + getId() +
            ", numeroSouscription='" + getNumeroSouscription() + "'" +
            ", dateSouscription='" + getDateSouscription() + "'" +
            ", status='" + getStatus() + "'" +
            ", cumulSinistres=" + getCumulSinistres() +
            ", frequencePaiement='" + getFrequencePaiement() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            "}";
    }
}
