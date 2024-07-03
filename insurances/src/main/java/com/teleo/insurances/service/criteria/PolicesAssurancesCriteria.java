package com.teleo.insurances.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.PolicesAssurances} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.PolicesAssurancesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /polices-assurances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PolicesAssurancesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroPolice;

    private InstantFilter dateEmission;

    private InstantFilter dateExpiration;

    private StringFilter description;

    private BigDecimalFilter montantAssure;

    private BigDecimalFilter plafondAssure;

    private LongFilter assuranceId;

    private Boolean distinct;

    public PolicesAssurancesCriteria() {}

    public PolicesAssurancesCriteria(PolicesAssurancesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroPolice = other.optionalNumeroPolice().map(StringFilter::copy).orElse(null);
        this.dateEmission = other.optionalDateEmission().map(InstantFilter::copy).orElse(null);
        this.dateExpiration = other.optionalDateExpiration().map(InstantFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.montantAssure = other.optionalMontantAssure().map(BigDecimalFilter::copy).orElse(null);
        this.plafondAssure = other.optionalPlafondAssure().map(BigDecimalFilter::copy).orElse(null);
        this.assuranceId = other.optionalAssuranceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PolicesAssurancesCriteria copy() {
        return new PolicesAssurancesCriteria(this);
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

    public StringFilter getNumeroPolice() {
        return numeroPolice;
    }

    public Optional<StringFilter> optionalNumeroPolice() {
        return Optional.ofNullable(numeroPolice);
    }

    public StringFilter numeroPolice() {
        if (numeroPolice == null) {
            setNumeroPolice(new StringFilter());
        }
        return numeroPolice;
    }

    public void setNumeroPolice(StringFilter numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

    public InstantFilter getDateEmission() {
        return dateEmission;
    }

    public Optional<InstantFilter> optionalDateEmission() {
        return Optional.ofNullable(dateEmission);
    }

    public InstantFilter dateEmission() {
        if (dateEmission == null) {
            setDateEmission(new InstantFilter());
        }
        return dateEmission;
    }

    public void setDateEmission(InstantFilter dateEmission) {
        this.dateEmission = dateEmission;
    }

    public InstantFilter getDateExpiration() {
        return dateExpiration;
    }

    public Optional<InstantFilter> optionalDateExpiration() {
        return Optional.ofNullable(dateExpiration);
    }

    public InstantFilter dateExpiration() {
        if (dateExpiration == null) {
            setDateExpiration(new InstantFilter());
        }
        return dateExpiration;
    }

    public void setDateExpiration(InstantFilter dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getMontantAssure() {
        return montantAssure;
    }

    public Optional<BigDecimalFilter> optionalMontantAssure() {
        return Optional.ofNullable(montantAssure);
    }

    public BigDecimalFilter montantAssure() {
        if (montantAssure == null) {
            setMontantAssure(new BigDecimalFilter());
        }
        return montantAssure;
    }

    public void setMontantAssure(BigDecimalFilter montantAssure) {
        this.montantAssure = montantAssure;
    }

    public BigDecimalFilter getPlafondAssure() {
        return plafondAssure;
    }

    public Optional<BigDecimalFilter> optionalPlafondAssure() {
        return Optional.ofNullable(plafondAssure);
    }

    public BigDecimalFilter plafondAssure() {
        if (plafondAssure == null) {
            setPlafondAssure(new BigDecimalFilter());
        }
        return plafondAssure;
    }

    public void setPlafondAssure(BigDecimalFilter plafondAssure) {
        this.plafondAssure = plafondAssure;
    }

    public LongFilter getAssuranceId() {
        return assuranceId;
    }

    public Optional<LongFilter> optionalAssuranceId() {
        return Optional.ofNullable(assuranceId);
    }

    public LongFilter assuranceId() {
        if (assuranceId == null) {
            setAssuranceId(new LongFilter());
        }
        return assuranceId;
    }

    public void setAssuranceId(LongFilter assuranceId) {
        this.assuranceId = assuranceId;
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
        final PolicesAssurancesCriteria that = (PolicesAssurancesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroPolice, that.numeroPolice) &&
            Objects.equals(dateEmission, that.dateEmission) &&
            Objects.equals(dateExpiration, that.dateExpiration) &&
            Objects.equals(description, that.description) &&
            Objects.equals(montantAssure, that.montantAssure) &&
            Objects.equals(plafondAssure, that.plafondAssure) &&
            Objects.equals(assuranceId, that.assuranceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroPolice,
            dateEmission,
            dateExpiration,
            description,
            montantAssure,
            plafondAssure,
            assuranceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicesAssurancesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroPolice().map(f -> "numeroPolice=" + f + ", ").orElse("") +
            optionalDateEmission().map(f -> "dateEmission=" + f + ", ").orElse("") +
            optionalDateExpiration().map(f -> "dateExpiration=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalMontantAssure().map(f -> "montantAssure=" + f + ", ").orElse("") +
            optionalPlafondAssure().map(f -> "plafondAssure=" + f + ", ").orElse("") +
            optionalAssuranceId().map(f -> "assuranceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
