package com.teleo.insurances.service.criteria;

import com.teleo.insurances.domain.enumeration.RapportType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.insurances.domain.Rapport} entity. This class is used
 * in {@link com.teleo.insurances.web.rest.RapportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rapports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RapportType
     */
    public static class RapportTypeFilter extends Filter<RapportType> {

        public RapportTypeFilter() {}

        public RapportTypeFilter(RapportTypeFilter filter) {
            super(filter);
        }

        @Override
        public RapportTypeFilter copy() {
            return new RapportTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titre;

    private StringFilter description;

    private RapportTypeFilter type;

    private InstantFilter dateGeneration;

    private StringFilter url;

    private LongFilter assuranceId;

    private Boolean distinct;

    public RapportCriteria() {}

    public RapportCriteria(RapportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(RapportTypeFilter::copy).orElse(null);
        this.dateGeneration = other.optionalDateGeneration().map(InstantFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.assuranceId = other.optionalAssuranceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RapportCriteria copy() {
        return new RapportCriteria(this);
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

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
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

    public RapportTypeFilter getType() {
        return type;
    }

    public Optional<RapportTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public RapportTypeFilter type() {
        if (type == null) {
            setType(new RapportTypeFilter());
        }
        return type;
    }

    public void setType(RapportTypeFilter type) {
        this.type = type;
    }

    public InstantFilter getDateGeneration() {
        return dateGeneration;
    }

    public Optional<InstantFilter> optionalDateGeneration() {
        return Optional.ofNullable(dateGeneration);
    }

    public InstantFilter dateGeneration() {
        if (dateGeneration == null) {
            setDateGeneration(new InstantFilter());
        }
        return dateGeneration;
    }

    public void setDateGeneration(InstantFilter dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
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
        final RapportCriteria that = (RapportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dateGeneration, that.dateGeneration) &&
            Objects.equals(url, that.url) &&
            Objects.equals(assuranceId, that.assuranceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, description, type, dateGeneration, url, assuranceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDateGeneration().map(f -> "dateGeneration=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalAssuranceId().map(f -> "assuranceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
