package com.teleo.insurances.service.criteria;

import com.teleo.insurances.domain.enumeration.InsuranceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.Assurance} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.AssuranceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assurances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssuranceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering InsuranceType
     */
    public static class InsuranceTypeFilter extends Filter<InsuranceType> {

        public InsuranceTypeFilter() {}

        public InsuranceTypeFilter(InsuranceTypeFilter filter) {
            super(filter);
        }

        @Override
        public InsuranceTypeFilter copy() {
            return new InsuranceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private StringFilter conditions;

    private InsuranceTypeFilter type;

    private BigDecimalFilter prixAssurance;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private LongFilter garantieId;

    private Boolean distinct;

    public AssuranceCriteria() {}

    public AssuranceCriteria(AssuranceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.conditions = other.optionalConditions().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(InsuranceTypeFilter::copy).orElse(null);
        this.prixAssurance = other.optionalPrixAssurance().map(BigDecimalFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(InstantFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(InstantFilter::copy).orElse(null);
        this.garantieId = other.optionalGarantieId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AssuranceCriteria copy() {
        return new AssuranceCriteria(this);
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

    public StringFilter getConditions() {
        return conditions;
    }

    public Optional<StringFilter> optionalConditions() {
        return Optional.ofNullable(conditions);
    }

    public StringFilter conditions() {
        if (conditions == null) {
            setConditions(new StringFilter());
        }
        return conditions;
    }

    public void setConditions(StringFilter conditions) {
        this.conditions = conditions;
    }

    public InsuranceTypeFilter getType() {
        return type;
    }

    public Optional<InsuranceTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public InsuranceTypeFilter type() {
        if (type == null) {
            setType(new InsuranceTypeFilter());
        }
        return type;
    }

    public void setType(InsuranceTypeFilter type) {
        this.type = type;
    }

    public BigDecimalFilter getPrixAssurance() {
        return prixAssurance;
    }

    public Optional<BigDecimalFilter> optionalPrixAssurance() {
        return Optional.ofNullable(prixAssurance);
    }

    public BigDecimalFilter prixAssurance() {
        if (prixAssurance == null) {
            setPrixAssurance(new BigDecimalFilter());
        }
        return prixAssurance;
    }

    public void setPrixAssurance(BigDecimalFilter prixAssurance) {
        this.prixAssurance = prixAssurance;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public Optional<InstantFilter> optionalDateDebut() {
        return Optional.ofNullable(dateDebut);
    }

    public InstantFilter dateDebut() {
        if (dateDebut == null) {
            setDateDebut(new InstantFilter());
        }
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public Optional<InstantFilter> optionalDateFin() {
        return Optional.ofNullable(dateFin);
    }

    public InstantFilter dateFin() {
        if (dateFin == null) {
            setDateFin(new InstantFilter());
        }
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
    }

    public LongFilter getGarantieId() {
        return garantieId;
    }

    public Optional<LongFilter> optionalGarantieId() {
        return Optional.ofNullable(garantieId);
    }

    public LongFilter garantieId() {
        if (garantieId == null) {
            setGarantieId(new LongFilter());
        }
        return garantieId;
    }

    public void setGarantieId(LongFilter garantieId) {
        this.garantieId = garantieId;
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
        final AssuranceCriteria that = (AssuranceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(conditions, that.conditions) &&
            Objects.equals(type, that.type) &&
            Objects.equals(prixAssurance, that.prixAssurance) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(garantieId, that.garantieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, conditions, type, prixAssurance, dateDebut, dateFin, garantieId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssuranceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalConditions().map(f -> "conditions=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPrixAssurance().map(f -> "prixAssurance=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalGarantieId().map(f -> "garantieId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
