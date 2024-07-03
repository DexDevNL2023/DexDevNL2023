package com.teleo.claims.service.criteria;

import com.teleo.claims.domain.enumeration.PrestationType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.PrestationSoin} entity. This class is used
 * in {@link com.teleo.claims.web.rest.PrestationSoinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prestation-soins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationSoinCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PrestationType
     */
    public static class PrestationTypeFilter extends Filter<PrestationType> {

        public PrestationTypeFilter() {}

        public PrestationTypeFilter(PrestationTypeFilter filter) {
            super(filter);
        }

        @Override
        public PrestationTypeFilter copy() {
            return new PrestationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private PrestationTypeFilter type;

    private BigDecimalFilter montant;

    private LongFilter financeurSoinId;

    private LongFilter financeurSoinId;

    private Boolean distinct;

    public PrestationSoinCriteria() {}

    public PrestationSoinCriteria(PrestationSoinCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(PrestationTypeFilter::copy).orElse(null);
        this.montant = other.optionalMontant().map(BigDecimalFilter::copy).orElse(null);
        this.financeurSoinId = other.optionalFinanceurSoinId().map(LongFilter::copy).orElse(null);
        this.financeurSoinId = other.optionalFinanceurSoinId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PrestationSoinCriteria copy() {
        return new PrestationSoinCriteria(this);
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

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
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

    public PrestationTypeFilter getType() {
        return type;
    }

    public Optional<PrestationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public PrestationTypeFilter type() {
        if (type == null) {
            setType(new PrestationTypeFilter());
        }
        return type;
    }

    public void setType(PrestationTypeFilter type) {
        this.type = type;
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

    public LongFilter getFinanceurSoinId() {
        return financeurSoinId;
    }

    public Optional<LongFilter> optionalFinanceurSoinId() {
        return Optional.ofNullable(financeurSoinId);
    }

    public LongFilter financeurSoinId() {
        if (financeurSoinId == null) {
            setFinanceurSoinId(new LongFilter());
        }
        return financeurSoinId;
    }

    public void setFinanceurSoinId(LongFilter financeurSoinId) {
        this.financeurSoinId = financeurSoinId;
    }

    public LongFilter getFinanceurSoinId() {
        return financeurSoinId;
    }

    public Optional<LongFilter> optionalFinanceurSoinId() {
        return Optional.ofNullable(financeurSoinId);
    }

    public LongFilter financeurSoinId() {
        if (financeurSoinId == null) {
            setFinanceurSoinId(new LongFilter());
        }
        return financeurSoinId;
    }

    public void setFinanceurSoinId(LongFilter financeurSoinId) {
        this.financeurSoinId = financeurSoinId;
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
        final PrestationSoinCriteria that = (PrestationSoinCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(financeurSoinId, that.financeurSoinId) &&
            Objects.equals(financeurSoinId, that.financeurSoinId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, type, montant, financeurSoinId, financeurSoinId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationSoinCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalMontant().map(f -> "montant=" + f + ", ").orElse("") +
            optionalFinanceurSoinId().map(f -> "financeurSoinId=" + f + ", ").orElse("") +
            optionalFinanceurSoinId().map(f -> "financeurSoinId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
