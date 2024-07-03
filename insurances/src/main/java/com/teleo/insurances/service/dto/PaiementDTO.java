package com.teleo.insurances.service.dto;

import com.teleo.insurances.domain.enumeration.PaymentStatus;
import com.teleo.insurances.domain.enumeration.PaymentType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.insurances.domain.Paiement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant datePaiement;

    @NotNull
    private BigDecimal montant;

    @NotNull
    private PaymentType type;

    @NotNull
    private PaymentStatus status;

    private SouscriptionDTO souscription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public SouscriptionDTO getSouscription() {
        return souscription;
    }

    public void setSouscription(SouscriptionDTO souscription) {
        this.souscription = souscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementDTO)) {
            return false;
        }

        PaiementDTO paiementDTO = (PaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + getId() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", montant=" + getMontant() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", souscription=" + getSouscription() +
            "}";
    }
}
