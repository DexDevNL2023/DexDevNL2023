package com.teleo.accounts.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.accounts.domain.MedicalRecord} entity. This class is used
 * in {@link com.teleo.accounts.web.rest.MedicalRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medical-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numMedicalRecord;

    private StringFilter details;

    private InstantFilter dateUpdated;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public MedicalRecordCriteria() {}

    public MedicalRecordCriteria(MedicalRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numMedicalRecord = other.optionalNumMedicalRecord().map(StringFilter::copy).orElse(null);
        this.details = other.optionalDetails().map(StringFilter::copy).orElse(null);
        this.dateUpdated = other.optionalDateUpdated().map(InstantFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicalRecordCriteria copy() {
        return new MedicalRecordCriteria(this);
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

    public StringFilter getNumMedicalRecord() {
        return numMedicalRecord;
    }

    public Optional<StringFilter> optionalNumMedicalRecord() {
        return Optional.ofNullable(numMedicalRecord);
    }

    public StringFilter numMedicalRecord() {
        if (numMedicalRecord == null) {
            setNumMedicalRecord(new StringFilter());
        }
        return numMedicalRecord;
    }

    public void setNumMedicalRecord(StringFilter numMedicalRecord) {
        this.numMedicalRecord = numMedicalRecord;
    }

    public StringFilter getDetails() {
        return details;
    }

    public Optional<StringFilter> optionalDetails() {
        return Optional.ofNullable(details);
    }

    public StringFilter details() {
        if (details == null) {
            setDetails(new StringFilter());
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public InstantFilter getDateUpdated() {
        return dateUpdated;
    }

    public Optional<InstantFilter> optionalDateUpdated() {
        return Optional.ofNullable(dateUpdated);
    }

    public InstantFilter dateUpdated() {
        if (dateUpdated == null) {
            setDateUpdated(new InstantFilter());
        }
        return dateUpdated;
    }

    public void setDateUpdated(InstantFilter dateUpdated) {
        this.dateUpdated = dateUpdated;
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
        final MedicalRecordCriteria that = (MedicalRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numMedicalRecord, that.numMedicalRecord) &&
            Objects.equals(details, that.details) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numMedicalRecord, details, dateUpdated, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumMedicalRecord().map(f -> "numMedicalRecord=" + f + ", ").orElse("") +
            optionalDetails().map(f -> "details=" + f + ", ").orElse("") +
            optionalDateUpdated().map(f -> "dateUpdated=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
