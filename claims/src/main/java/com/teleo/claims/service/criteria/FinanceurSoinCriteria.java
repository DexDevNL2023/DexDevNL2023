package com.teleo.claims.service.criteria;

import com.teleo.claims.domain.enumeration.FinanceurType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.FinanceurSoin} entity. This class is used
 * in {@link com.teleo.claims.web.rest.FinanceurSoinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /financeur-soins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinanceurSoinCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FinanceurType
     */
    public static class FinanceurTypeFilter extends Filter<FinanceurType> {

        public FinanceurTypeFilter() {}

        public FinanceurTypeFilter(FinanceurTypeFilter filter) {
            super(filter);
        }

        @Override
        public FinanceurTypeFilter copy() {
            return new FinanceurTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private FinanceurTypeFilter type;

    private StringFilter adresse;

    private StringFilter telephone;

    private StringFilter email;

    private StringFilter servicesFournis;

    private LongFilter prestationSoinId;

    private Boolean distinct;

    public FinanceurSoinCriteria() {}

    public FinanceurSoinCriteria(FinanceurSoinCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(FinanceurTypeFilter::copy).orElse(null);
        this.adresse = other.optionalAdresse().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.servicesFournis = other.optionalServicesFournis().map(StringFilter::copy).orElse(null);
        this.prestationSoinId = other.optionalPrestationSoinId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FinanceurSoinCriteria copy() {
        return new FinanceurSoinCriteria(this);
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

    public FinanceurTypeFilter getType() {
        return type;
    }

    public Optional<FinanceurTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public FinanceurTypeFilter type() {
        if (type == null) {
            setType(new FinanceurTypeFilter());
        }
        return type;
    }

    public void setType(FinanceurTypeFilter type) {
        this.type = type;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public Optional<StringFilter> optionalAdresse() {
        return Optional.ofNullable(adresse);
    }

    public StringFilter adresse() {
        if (adresse == null) {
            setAdresse(new StringFilter());
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public Optional<StringFilter> optionalTelephone() {
        return Optional.ofNullable(telephone);
    }

    public StringFilter telephone() {
        if (telephone == null) {
            setTelephone(new StringFilter());
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getServicesFournis() {
        return servicesFournis;
    }

    public Optional<StringFilter> optionalServicesFournis() {
        return Optional.ofNullable(servicesFournis);
    }

    public StringFilter servicesFournis() {
        if (servicesFournis == null) {
            setServicesFournis(new StringFilter());
        }
        return servicesFournis;
    }

    public void setServicesFournis(StringFilter servicesFournis) {
        this.servicesFournis = servicesFournis;
    }

    public LongFilter getPrestationSoinId() {
        return prestationSoinId;
    }

    public Optional<LongFilter> optionalPrestationSoinId() {
        return Optional.ofNullable(prestationSoinId);
    }

    public LongFilter prestationSoinId() {
        if (prestationSoinId == null) {
            setPrestationSoinId(new LongFilter());
        }
        return prestationSoinId;
    }

    public void setPrestationSoinId(LongFilter prestationSoinId) {
        this.prestationSoinId = prestationSoinId;
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
        final FinanceurSoinCriteria that = (FinanceurSoinCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(type, that.type) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(servicesFournis, that.servicesFournis) &&
            Objects.equals(prestationSoinId, that.prestationSoinId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, type, adresse, telephone, email, servicesFournis, prestationSoinId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinanceurSoinCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalAdresse().map(f -> "adresse=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalServicesFournis().map(f -> "servicesFournis=" + f + ", ").orElse("") +
            optionalPrestationSoinId().map(f -> "prestationSoinId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
