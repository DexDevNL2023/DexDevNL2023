package com.teleo.claims.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.DocumentDeSinistre} entity. This class is used
 * in {@link com.teleo.claims.web.rest.DocumentDeSinistreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-de-sinistres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDeSinistreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter type;

    private StringFilter url;

    private InstantFilter dateTelechargement;

    private LongFilter sinistreId;

    private Boolean distinct;

    public DocumentDeSinistreCriteria() {}

    public DocumentDeSinistreCriteria(DocumentDeSinistreCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.dateTelechargement = other.optionalDateTelechargement().map(InstantFilter::copy).orElse(null);
        this.sinistreId = other.optionalSinistreId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentDeSinistreCriteria copy() {
        return new DocumentDeSinistreCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
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

    public InstantFilter getDateTelechargement() {
        return dateTelechargement;
    }

    public Optional<InstantFilter> optionalDateTelechargement() {
        return Optional.ofNullable(dateTelechargement);
    }

    public InstantFilter dateTelechargement() {
        if (dateTelechargement == null) {
            setDateTelechargement(new InstantFilter());
        }
        return dateTelechargement;
    }

    public void setDateTelechargement(InstantFilter dateTelechargement) {
        this.dateTelechargement = dateTelechargement;
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
        final DocumentDeSinistreCriteria that = (DocumentDeSinistreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(type, that.type) &&
            Objects.equals(url, that.url) &&
            Objects.equals(dateTelechargement, that.dateTelechargement) &&
            Objects.equals(sinistreId, that.sinistreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, type, url, dateTelechargement, sinistreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDeSinistreCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalDateTelechargement().map(f -> "dateTelechargement=" + f + ", ").orElse("") +
            optionalSinistreId().map(f -> "sinistreId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
