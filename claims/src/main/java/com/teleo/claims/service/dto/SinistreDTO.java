package com.teleo.claims.service.dto;

import com.teleo.claims.domain.enumeration.ClaimStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.claims.domain.Sinistre} entity.
 */
@Schema(description = "Entities for claim management microservice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SinistreDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroSinistre;

    @NotNull
    private Instant dateDeclaration;

    private Instant dateTraitement;

    @NotNull
    private ClaimStatus status;

    @NotNull
    private String raison;

    @NotNull
    private BigDecimal montantReclame;

    private BigDecimal montantApprouve;

    @NotNull
    private Long souscriptionId;

    private FournisseurDTO fournisseur;

    private PrestationSoinDTO prestationsSoin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSinistre() {
        return numeroSinistre;
    }

    public void setNumeroSinistre(String numeroSinistre) {
        this.numeroSinistre = numeroSinistre;
    }

    public Instant getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(Instant dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public Instant getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(Instant dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public BigDecimal getMontantReclame() {
        return montantReclame;
    }

    public void setMontantReclame(BigDecimal montantReclame) {
        this.montantReclame = montantReclame;
    }

    public BigDecimal getMontantApprouve() {
        return montantApprouve;
    }

    public void setMontantApprouve(BigDecimal montantApprouve) {
        this.montantApprouve = montantApprouve;
    }

    public Long getSouscriptionId() {
        return souscriptionId;
    }

    public void setSouscriptionId(Long souscriptionId) {
        this.souscriptionId = souscriptionId;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public PrestationSoinDTO getPrestationsSoin() {
        return prestationsSoin;
    }

    public void setPrestationsSoin(PrestationSoinDTO prestationsSoin) {
        this.prestationsSoin = prestationsSoin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SinistreDTO)) {
            return false;
        }

        SinistreDTO sinistreDTO = (SinistreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sinistreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SinistreDTO{" +
            "id=" + getId() +
            ", numeroSinistre='" + getNumeroSinistre() + "'" +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            ", status='" + getStatus() + "'" +
            ", raison='" + getRaison() + "'" +
            ", montantReclame=" + getMontantReclame() +
            ", montantApprouve=" + getMontantApprouve() +
            ", souscriptionId=" + getSouscriptionId() +
            ", fournisseur=" + getFournisseur() +
            ", prestationsSoin=" + getPrestationsSoin() +
            "}";
    }
}
