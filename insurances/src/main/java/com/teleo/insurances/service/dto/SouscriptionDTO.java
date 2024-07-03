package com.teleo.insurances.service.dto;

import com.teleo.insurances.domain.enumeration.PaymentFrequency;
import com.teleo.insurances.domain.enumeration.SubscriptionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.insurances.domain.Souscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SouscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroSouscription;

    @NotNull
    private Instant dateSouscription;

    @NotNull
    private SubscriptionStatus status;

    @NotNull
    private BigDecimal cumulSinistres;

    @NotNull
    private PaymentFrequency frequencePaiement;

    @NotNull
    private Long utilisateurId;

    private PolicesAssurancesDTO policesAssurances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSouscription() {
        return numeroSouscription;
    }

    public void setNumeroSouscription(String numeroSouscription) {
        this.numeroSouscription = numeroSouscription;
    }

    public Instant getDateSouscription() {
        return dateSouscription;
    }

    public void setDateSouscription(Instant dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public BigDecimal getCumulSinistres() {
        return cumulSinistres;
    }

    public void setCumulSinistres(BigDecimal cumulSinistres) {
        this.cumulSinistres = cumulSinistres;
    }

    public PaymentFrequency getFrequencePaiement() {
        return frequencePaiement;
    }

    public void setFrequencePaiement(PaymentFrequency frequencePaiement) {
        this.frequencePaiement = frequencePaiement;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public PolicesAssurancesDTO getPolicesAssurances() {
        return policesAssurances;
    }

    public void setPolicesAssurances(PolicesAssurancesDTO policesAssurances) {
        this.policesAssurances = policesAssurances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SouscriptionDTO)) {
            return false;
        }

        SouscriptionDTO souscriptionDTO = (SouscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, souscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SouscriptionDTO{" +
            "id=" + getId() +
            ", numeroSouscription='" + getNumeroSouscription() + "'" +
            ", dateSouscription='" + getDateSouscription() + "'" +
            ", status='" + getStatus() + "'" +
            ", cumulSinistres=" + getCumulSinistres() +
            ", frequencePaiement='" + getFrequencePaiement() + "'" +
            ", utilisateurId=" + getUtilisateurId() +
            ", policesAssurances=" + getPolicesAssurances() +
            "}";
    }
}
