package com.teleo.claims.service.criteria;

import com.teleo.claims.domain.enumeration.ReclamationStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.Reclamation} entity. This class is used
 * in {@link com.teleo.claims.web.rest.ReclamationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reclamations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReclamationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReclamationStatus
     */
    public static class ReclamationStatusFilter extends Filter<ReclamationStatus> {

        public ReclamationStatusFilter() {}

        public ReclamationStatusFilter(ReclamationStatusFilter filter) {
            super(filter);
        }

        @Override
        public ReclamationStatusFilter copy() {
            return new ReclamationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroReclamation;

    private InstantFilter dateReclamation;

    private StringFilter description;

    private ReclamationStatusFilter status;

    private LongFilter sinistreId;

    private Boolean distinct;

    public ReclamationCriteria() {}

    public ReclamationCriteria(ReclamationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroReclamation = other.optionalNumeroReclamation().map(StringFilter::copy).orElse(null);
        this.dateReclamation = other.optionalDateReclamation().map(InstantFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ReclamationStatusFilter::copy).orElse(null);
        this.sinistreId = other.optionalSinistreId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReclamationCriteria copy() {
        return new ReclamationCriteria(this);
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

    public StringFilter getNumeroReclamation() {
        return numeroReclamation;
    }

    public Optional<StringFilter> optionalNumeroReclamation() {
        return Optional.ofNullable(numeroReclamation);
    }

    public StringFilter numeroReclamation() {
        if (numeroReclamation == null) {
            setNumeroReclamation(new StringFilter());
        }
        return numeroReclamation;
    }

    public void setNumeroReclamation(StringFilter numeroReclamation) {
        this.numeroReclamation = numeroReclamation;
    }

    public InstantFilter getDateReclamation() {
        return dateReclamation;
    }

    public Optional<InstantFilter> optionalDateReclamation() {
        return Optional.ofNullable(dateReclamation);
    }

    public InstantFilter dateReclamation() {
        if (dateReclamation == null) {
            setDateReclamation(new InstantFilter());
        }
        return dateReclamation;
    }

    public void setDateReclamation(InstantFilter dateReclamation) {
        this.dateReclamation = dateReclamation;
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

    public ReclamationStatusFilter getStatus() {
        return status;
    }

    public Optional<ReclamationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ReclamationStatusFilter status() {
        if (status == null) {
            setStatus(new ReclamationStatusFilter());
        }
        return status;
    }

    public void setStatus(ReclamationStatusFilter status) {
        this.status = status;
    }

    public LongFilter getSinistreId() {
        return sinistreId;
    }

    public Optional<LongFilter> optionalSinistreId() {
        return Optional.ofNullable(sinistreId);
    }

    public LongFilter sinistreId() {
        if (sinistreId == null) {
            setSinistreId(new LongFilter());
        }
        return sinistreId;
    }

    public void setSinistreId(LongFilter sinistreId) {
        this.sinistreId = sinistreId;
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
        final ReclamationCriteria that = (ReclamationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroReclamation, that.numeroReclamation) &&
            Objects.equals(dateReclamation, that.dateReclamation) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(sinistreId, that.sinistreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroReclamation, dateReclamation, description, status, sinistreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReclamationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroReclamation().map(f -> "numeroReclamation=" + f + ", ").orElse("") +
            optionalDateReclamation().map(f -> "dateReclamation=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSinistreId().map(f -> "sinistreId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
