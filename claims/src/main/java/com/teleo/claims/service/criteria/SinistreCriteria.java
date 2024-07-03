package com.teleo.claims.service.criteria;

import com.teleo.claims.domain.enumeration.ClaimStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.Sinistre} entity. This class is used
 * in {@link com.teleo.claims.web.rest.SinistreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sinistres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SinistreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ClaimStatus
     */
    public static class ClaimStatusFilter extends Filter<ClaimStatus> {

        public ClaimStatusFilter() {}

        public ClaimStatusFilter(ClaimStatusFilter filter) {
            super(filter);
        }

        @Override
        public ClaimStatusFilter copy() {
            return new ClaimStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroSinistre;

    private InstantFilter dateDeclaration;

    private InstantFilter dateTraitement;

    private ClaimStatusFilter status;

    private StringFilter raison;

    private BigDecimalFilter montantReclame;

    private BigDecimalFilter montantApprouve;

    private LongFilter souscriptionId;

    private LongFilter fournisseurId;

    private LongFilter prestationsSoinId;

    private Boolean distinct;

    public SinistreCriteria() {}

    public SinistreCriteria(SinistreCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroSinistre = other.optionalNumeroSinistre().map(StringFilter::copy).orElse(null);
        this.dateDeclaration = other.optionalDateDeclaration().map(InstantFilter::copy).orElse(null);
        this.dateTraitement = other.optionalDateTraitement().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ClaimStatusFilter::copy).orElse(null);
        this.raison = other.optionalRaison().map(StringFilter::copy).orElse(null);
        this.montantReclame = other.optionalMontantReclame().map(BigDecimalFilter::copy).orElse(null);
        this.montantApprouve = other.optionalMontantApprouve().map(BigDecimalFilter::copy).orElse(null);
        this.souscriptionId = other.optionalSouscriptionId().map(LongFilter::copy).orElse(null);
        this.fournisseurId = other.optionalFournisseurId().map(LongFilter::copy).orElse(null);
        this.prestationsSoinId = other.optionalPrestationsSoinId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SinistreCriteria copy() {
        return new SinistreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumeroSinistre() {
        return numeroSinistre;
    }

    public Optional<StringFilter> optionalNumeroSinistre() {
        return Optional.ofNullable(numeroSinistre);
    }

    public StringFilter numeroSinistre() {
        if (numeroSinistre == null) {
            setNumeroSinistre(new StringFilter());
        }
        return numeroSinistre;
    }

    public void setNumeroSinistre(StringFilter numeroSinistre) {
        this.numeroSinistre = numeroSinistre;
    }

    public InstantFilter getDateDeclaration() {
        return dateDeclaration;
    }

    public Optional<InstantFilter> optionalDateDeclaration() {
        return Optional.ofNullable(dateDeclaration);
    }

    public InstantFilter dateDeclaration() {
        if (dateDeclaration == null) {
            setDateDeclaration(new InstantFilter());
        }
        return dateDeclaration;
    }

    public void setDateDeclaration(InstantFilter dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public InstantFilter getDateTraitement() {
        return dateTraitement;
    }

    public Optional<InstantFilter> optionalDateTraitement() {
        return Optional.ofNullable(dateTraitement);
    }

    public InstantFilter dateTraitement() {
        if (dateTraitement == null) {
            setDateTraitement(new InstantFilter());
        }
        return dateTraitement;
    }

    public void setDateTraitement(InstantFilter dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public ClaimStatusFilter getStatus() {
        return status;
    }

    public Optional<ClaimStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ClaimStatusFilter status() {
        if (status == null) {
            setStatus(new ClaimStatusFilter());
        }
        return status;
    }

    public void setStatus(ClaimStatusFilter status) {
        this.status = status;
    }

    public StringFilter getRaison() {
        return raison;
    }

    public Optional<StringFilter> optionalRaison() {
        return Optional.ofNullable(raison);
    }

    public StringFilter raison() {
        if (raison == null) {
            setRaison(new StringFilter());
        }
        return raison;
    }

    public void setRaison(StringFilter raison) {
        this.raison = raison;
    }

    public BigDecimalFilter getMontantReclame() {
        return montantReclame;
    }

    public Optional<BigDecimalFilter> optionalMontantReclame() {
        return Optional.ofNullable(montantReclame);
    }

    public BigDecimalFilter montantReclame() {
        if (montantReclame == null) {
            setMontantReclame(new BigDecimalFilter());
        }
        return montantReclame;
    }

    public void setMontantReclame(BigDecimalFilter montantReclame) {
        this.montantReclame = montantReclame;
    }

    public BigDecimalFilter getMontantApprouve() {
        return montantApprouve;
    }

    public Optional<BigDecimalFilter> optionalMontantApprouve() {
        return Optional.ofNullable(montantApprouve);
    }

    public BigDecimalFilter montantApprouve() {
        if (montantApprouve == null) {
            setMontantApprouve(new BigDecimalFilter());
        }
        return montantApprouve;
    }

    public void setMontantApprouve(BigDecimalFilter montantApprouve) {
        this.montantApprouve = montantApprouve;
    }

    public LongFilter getSouscriptionId() {
        return souscriptionId;
    }

    public Optional<LongFilter> optionalSouscriptionId() {
        return Optional.ofNullable(souscriptionId);
    }

    public LongFilter souscriptionId() {
        if (souscriptionId == null) {
            setSouscriptionId(new LongFilter());
        }
        return souscriptionId;
    }

    public void setSouscriptionId(LongFilter souscriptionId) {
        this.souscriptionId = souscriptionId;
    }

    public LongFilter getFournisseurId() {
        return fournisseurId;
    }

    public Optional<LongFilter> optionalFournisseurId() {
        return Optional.ofNullable(fournisseurId);
    }

    public LongFilter fournisseurId() {
        if (fournisseurId == null) {
            setFournisseurId(new LongFilter());
        }
        return fournisseurId;
    }

    public void setFournisseurId(LongFilter fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public LongFilter getPrestationsSoinId() {
        return prestationsSoinId;
    }

    public Optional<LongFilter> optionalPrestationsSoinId() {
        return Optional.ofNullable(prestationsSoinId);
    }

    public LongFilter prestationsSoinId() {
        if (prestationsSoinId == null) {
            setPrestationsSoinId(new LongFilter());
        }
        return prestationsSoinId;
    }

    public void setPrestationsSoinId(LongFilter prestationsSoinId) {
        this.prestationsSoinId = prestationsSoinId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SinistreCriteria that = (SinistreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroSinistre, that.numeroSinistre) &&
            Objects.equals(dateDeclaration, that.dateDeclaration) &&
            Objects.equals(dateTraitement, that.dateTraitement) &&
            Objects.equals(status, that.status) &&
            Objects.equals(raison, that.raison) &&
            Objects.equals(montantReclame, that.montantReclame) &&
            Objects.equals(montantApprouve, that.montantApprouve) &&
            Objects.equals(souscriptionId, that.souscriptionId) &&
            Objects.equals(fournisseurId, that.fournisseurId) &&
            Objects.equals(prestationsSoinId, that.prestationsSoinId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroSinistre,
            dateDeclaration,
            dateTraitement,
            status,
            raison,
            montantReclame,
            montantApprouve,
            souscriptionId,
            fournisseurId,
            prestationsSoinId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SinistreCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroSinistre().map(f -> "numeroSinistre=" + f + ", ").orElse("") +
            optionalDateDeclaration().map(f -> "dateDeclaration=" + f + ", ").orElse("") +
            optionalDateTraitement().map(f -> "dateTraitement=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalRaison().map(f -> "raison=" + f + ", ").orElse("") +
            optionalMontantReclame().map(f -> "montantReclame=" + f + ", ").orElse("") +
            optionalMontantApprouve().map(f -> "montantApprouve=" + f + ", ").orElse("") +
            optionalSouscriptionId().map(f -> "souscriptionId=" + f + ", ").orElse("") +
            optionalFournisseurId().map(f -> "fournisseurId=" + f + ", ").orElse("") +
            optionalPrestationsSoinId().map(f -> "prestationsSoinId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
