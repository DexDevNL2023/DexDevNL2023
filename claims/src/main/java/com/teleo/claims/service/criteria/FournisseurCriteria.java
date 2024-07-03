package com.teleo.claims.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.claims.domain.Fournisseur} entity. This class is used
 * in {@link com.teleo.claims.web.rest.FournisseurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fournisseurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FournisseurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter adresse;

    private StringFilter telephone;

    private StringFilter email;

    private StringFilter addresse;

    private StringFilter ville;

    private StringFilter pays;

    private StringFilter servicesFournis;

    private InstantFilter dateDebutContrat;

    private InstantFilter dateFinContrat;

    private Boolean distinct;

    public FournisseurCriteria() {}

    public FournisseurCriteria(FournisseurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.adresse = other.optionalAdresse().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.addresse = other.optionalAddresse().map(StringFilter::copy).orElse(null);
        this.ville = other.optionalVille().map(StringFilter::copy).orElse(null);
        this.pays = other.optionalPays().map(StringFilter::copy).orElse(null);
        this.servicesFournis = other.optionalServicesFournis().map(StringFilter::copy).orElse(null);
        this.dateDebutContrat = other.optionalDateDebutContrat().map(InstantFilter::copy).orElse(null);
        this.dateFinContrat = other.optionalDateFinContrat().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FournisseurCriteria copy() {
        return new FournisseurCriteria(this);
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

    public StringFilter getAddresse() {
        return addresse;
    }

    public Optional<StringFilter> optionalAddresse() {
        return Optional.ofNullable(addresse);
    }

    public StringFilter addresse() {
        if (addresse == null) {
            setAddresse(new StringFilter());
        }
        return addresse;
    }

    public void setAddresse(StringFilter addresse) {
        this.addresse = addresse;
    }

    public StringFilter getVille() {
        return ville;
    }

    public Optional<StringFilter> optionalVille() {
        return Optional.ofNullable(ville);
    }

    public StringFilter ville() {
        if (ville == null) {
            setVille(new StringFilter());
        }
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getPays() {
        return pays;
    }

    public Optional<StringFilter> optionalPays() {
        return Optional.ofNullable(pays);
    }

    public StringFilter pays() {
        if (pays == null) {
            setPays(new StringFilter());
        }
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
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

    public InstantFilter getDateDebutContrat() {
        return dateDebutContrat;
    }

    public Optional<InstantFilter> optionalDateDebutContrat() {
        return Optional.ofNullable(dateDebutContrat);
    }

    public InstantFilter dateDebutContrat() {
        if (dateDebutContrat == null) {
            setDateDebutContrat(new InstantFilter());
        }
        return dateDebutContrat;
    }

    public void setDateDebutContrat(InstantFilter dateDebutContrat) {
        this.dateDebutContrat = dateDebutContrat;
    }

    public InstantFilter getDateFinContrat() {
        return dateFinContrat;
    }

    public Optional<InstantFilter> optionalDateFinContrat() {
        return Optional.ofNullable(dateFinContrat);
    }

    public InstantFilter dateFinContrat() {
        if (dateFinContrat == null) {
            setDateFinContrat(new InstantFilter());
        }
        return dateFinContrat;
    }

    public void setDateFinContrat(InstantFilter dateFinContrat) {
        this.dateFinContrat = dateFinContrat;
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
        final FournisseurCriteria that = (FournisseurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(addresse, that.addresse) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(servicesFournis, that.servicesFournis) &&
            Objects.equals(dateDebutContrat, that.dateDebutContrat) &&
            Objects.equals(dateFinContrat, that.dateFinContrat) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nom,
            adresse,
            telephone,
            email,
            addresse,
            ville,
            pays,
            servicesFournis,
            dateDebutContrat,
            dateFinContrat,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FournisseurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalAdresse().map(f -> "adresse=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalAddresse().map(f -> "addresse=" + f + ", ").orElse("") +
            optionalVille().map(f -> "ville=" + f + ", ").orElse("") +
            optionalPays().map(f -> "pays=" + f + ", ").orElse("") +
            optionalServicesFournis().map(f -> "servicesFournis=" + f + ", ").orElse("") +
            optionalDateDebutContrat().map(f -> "dateDebutContrat=" + f + ", ").orElse("") +
            optionalDateFinContrat().map(f -> "dateFinContrat=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
