package com.teleo.insurances.service.criteria;

import com.teleo.insurances.domain.enumeration.PaymentStatus;
import com.teleo.insurances.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.Paiement} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.PaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentType
     */
    public static class PaymentTypeFilter extends Filter<PaymentType> {

        public PaymentTypeFilter() {}

        public PaymentTypeFilter(PaymentTypeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentTypeFilter copy() {
            return new PaymentTypeFilter(this);
        }
    }

    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {

        public PaymentStatusFilter() {}

        public PaymentStatusFilter(PaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaymentStatusFilter copy() {
            return new PaymentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter datePaiement;

    private BigDecimalFilter montant;

    private PaymentTypeFilter type;

    private PaymentStatusFilter status;

    private LongFilter souscriptionId;

    private Boolean distinct;

    public PaiementCriteria() {}

    public PaiementCriteria(PaiementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.datePaiement = other.optionalDatePaiement().map(InstantFilter::copy).orElse(null);
        this.montant = other.optionalMontant().map(BigDecimalFilter::copy).orElse(null);
        this.type = other.optionalType().map(PaymentTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PaymentStatusFilter::copy).orElse(null);
        this.souscriptionId = other.optionalSouscriptionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaiementCriteria copy() {
        return new PaiementCriteria(this);
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

    public InstantFilter getDatePaiement() {
        return datePaiement;
    }

    public Optional<InstantFilter> optionalDatePaiement() {
        return Optional.ofNullable(datePaiement);
    }

    public InstantFilter datePaiement() {
        if (datePaiement == null) {
            setDatePaiement(new InstantFilter());
        }
        return datePaiement;
    }

    public void setDatePaiement(InstantFilter datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimalFilter getMontant() {
        return montant;
    }

    public Optional<BigDecimalFilter> optionalMontant() {
        return Optional.ofNullable(montant);
    }

    public BigDecimalFilter montant() {
        if (montant == null) {
            setMontant(new BigDecimalFilter());
        }
        return montant;
    }

    public void setMontant(BigDecimalFilter montant) {
        this.montant = montant;
    }

    public PaymentTypeFilter getType() {
        return type;
    }

    public Optional<PaymentTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public PaymentTypeFilter type() {
        if (type == null) {
            setType(new PaymentTypeFilter());
        }
        return type;
    }

    public void setType(PaymentTypeFilter type) {
        this.type = type;
    }

    public PaymentStatusFilter getStatus() {
        return status;
    }

    public Optional<PaymentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PaymentStatusFilter status() {
        if (status == null) {
            setStatus(new PaymentStatusFilter());
        }
        return status;
    }

    public void setStatus(PaymentStatusFilter status) {
        this.status = status;
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
        final PaiementCriteria that = (PaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datePaiement, that.datePaiement) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(souscriptionId, that.souscriptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datePaiement, montant, type, status, souscriptionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDatePaiement().map(f -> "datePaiement=" + f + ", ").orElse("") +
            optionalMontant().map(f -> "montant=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSouscriptionId().map(f -> "souscriptionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
