package com.teleo.insurances.service.criteria;

import com.teleo.insurances.domain.enumeration.GarantieStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.Garantie} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.GarantieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /garanties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GarantieCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GarantieStatus
     */
    public static class GarantieStatusFilter extends Filter<GarantieStatus> {

        public GarantieStatusFilter() {}

        public GarantieStatusFilter(GarantieStatusFilter filter) {
            super(filter);
        }

        @Override
        public GarantieStatusFilter copy() {
            return new GarantieStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private GarantieStatusFilter status;

    private LongFilter assuranceId;

    private Boolean distinct;

    public GarantieCriteria() {}

    public GarantieCriteria(GarantieCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(InstantFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(GarantieStatusFilter::copy).orElse(null);
        this.assuranceId = other.optionalAssuranceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GarantieCriteria copy() {
        return new GarantieCriteria(this);
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

    public GarantieStatusFilter getStatus() {
        return status;
    }

    public Optional<GarantieStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public GarantieStatusFilter status() {
        if (status == null) {
            setStatus(new GarantieStatusFilter());
        }
        return status;
    }

    public void setStatus(GarantieStatusFilter status) {
        this.status = status;
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
        final GarantieCriteria that = (GarantieCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(status, that.status) &&
            Objects.equals(assuranceId, that.assuranceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dateDebut, dateFin, status, assuranceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GarantieCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAssuranceId().map(f -> "assuranceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
