package com.teleo.insurances.service.criteria;

import com.teleo.insurances.domain.enumeration.PaymentFrequency;
import com.teleo.insurances.domain.enumeration.SubscriptionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.Souscription} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.SouscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /souscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SouscriptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SubscriptionStatus
     */
    public static class SubscriptionStatusFilter extends Filter<SubscriptionStatus> {

        public SubscriptionStatusFilter() {}

        public SubscriptionStatusFilter(SubscriptionStatusFilter filter) {
            super(filter);
        }

        @Override
        public SubscriptionStatusFilter copy() {
            return new SubscriptionStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentFrequency
     */
    public static class PaymentFrequencyFilter extends Filter<PaymentFrequency> {

        public PaymentFrequencyFilter() {}

        public PaymentFrequencyFilter(PaymentFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public PaymentFrequencyFilter copy() {
            return new PaymentFrequencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroSouscription;

    private InstantFilter dateSouscription;

    private SubscriptionStatusFilter status;

    private BigDecimalFilter cumulSinistres;

    private PaymentFrequencyFilter frequencePaiement;

    private LongFilter utilisateurId;

    private LongFilter policesAssurancesId;

    private Boolean distinct;

    public SouscriptionCriteria() {}

    public SouscriptionCriteria(SouscriptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroSouscription = other.optionalNumeroSouscription().map(StringFilter::copy).orElse(null);
        this.dateSouscription = other.optionalDateSouscription().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(SubscriptionStatusFilter::copy).orElse(null);
        this.cumulSinistres = other.optionalCumulSinistres().map(BigDecimalFilter::copy).orElse(null);
        this.frequencePaiement = other.optionalFrequencePaiement().map(PaymentFrequencyFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.policesAssurancesId = other.optionalPolicesAssurancesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SouscriptionCriteria copy() {
        return new SouscriptionCriteria(this);
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

    public StringFilter getNumeroSouscription() {
        return numeroSouscription;
    }

    public Optional<StringFilter> optionalNumeroSouscription() {
        return Optional.ofNullable(numeroSouscription);
    }

    public StringFilter numeroSouscription() {
        if (numeroSouscription == null) {
            setNumeroSouscription(new StringFilter());
        }
        return numeroSouscription;
    }

    public void setNumeroSouscription(StringFilter numeroSouscription) {
        this.numeroSouscription = numeroSouscription;
    }

    public InstantFilter getDateSouscription() {
        return dateSouscription;
    }

    public Optional<InstantFilter> optionalDateSouscription() {
        return Optional.ofNullable(dateSouscription);
    }

    public InstantFilter dateSouscription() {
        if (dateSouscription == null) {
            setDateSouscription(new InstantFilter());
        }
        return dateSouscription;
    }

    public void setDateSouscription(InstantFilter dateSouscription) {
        this.dateSouscription = dateSouscription;
    }

    public SubscriptionStatusFilter getStatus() {
        return status;
    }

    public Optional<SubscriptionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public SubscriptionStatusFilter status() {
        if (status == null) {
            setStatus(new SubscriptionStatusFilter());
        }
        return status;
    }

    public void setStatus(SubscriptionStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getCumulSinistres() {
        return cumulSinistres;
    }

    public Optional<BigDecimalFilter> optionalCumulSinistres() {
        return Optional.ofNullable(cumulSinistres);
    }

    public BigDecimalFilter cumulSinistres() {
        if (cumulSinistres == null) {
            setCumulSinistres(new BigDecimalFilter());
        }
        return cumulSinistres;
    }

    public void setCumulSinistres(BigDecimalFilter cumulSinistres) {
        this.cumulSinistres = cumulSinistres;
    }

    public PaymentFrequencyFilter getFrequencePaiement() {
        return frequencePaiement;
    }

    public Optional<PaymentFrequencyFilter> optionalFrequencePaiement() {
        return Optional.ofNullable(frequencePaiement);
    }

    public PaymentFrequencyFilter frequencePaiement() {
        if (frequencePaiement == null) {
            setFrequencePaiement(new PaymentFrequencyFilter());
        }
        return frequencePaiement;
    }

    public void setFrequencePaiement(PaymentFrequencyFilter frequencePaiement) {
        this.frequencePaiement = frequencePaiement;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public LongFilter getPolicesAssurancesId() {
        return policesAssurancesId;
    }

    public Optional<LongFilter> optionalPolicesAssurancesId() {
        return Optional.ofNullable(policesAssurancesId);
    }

    public LongFilter policesAssurancesId() {
        if (policesAssurancesId == null) {
            setPolicesAssurancesId(new LongFilter());
        }
        return policesAssurancesId;
    }

    public void setPolicesAssurancesId(LongFilter policesAssurancesId) {
        this.policesAssurancesId = policesAssurancesId;
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
        final SouscriptionCriteria that = (SouscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroSouscription, that.numeroSouscription) &&
            Objects.equals(dateSouscription, that.dateSouscription) &&
            Objects.equals(status, that.status) &&
            Objects.equals(cumulSinistres, that.cumulSinistres) &&
            Objects.equals(frequencePaiement, that.frequencePaiement) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(policesAssurancesId, that.policesAssurancesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroSouscription,
            dateSouscription,
            status,
            cumulSinistres,
            frequencePaiement,
            utilisateurId,
            policesAssurancesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SouscriptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroSouscription().map(f -> "numeroSouscription=" + f + ", ").orElse("") +
            optionalDateSouscription().map(f -> "dateSouscription=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCumulSinistres().map(f -> "cumulSinistres=" + f + ", ").orElse("") +
            optionalFrequencePaiement().map(f -> "frequencePaiement=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalPolicesAssurancesId().map(f -> "policesAssurancesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
