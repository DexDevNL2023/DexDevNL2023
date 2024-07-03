package com.teleo.accounts.domain.criteria;

import com.teleo.accounts.domain.enumeration.Gender;
import com.teleo.accounts.domain.enumeration.UserExtraType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.accounts.domain.UserExtra} entity. This class is used
 * in {@link com.teleo.accounts.web.rest.UserExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserExtraCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering UserExtraType
     */
    public static class UserExtraTypeFilter extends Filter<UserExtraType> {

        public UserExtraTypeFilter() {}

        public UserExtraTypeFilter(UserExtraTypeFilter filter) {
            super(filter);
        }

        @Override
        public UserExtraTypeFilter copy() {
            return new UserExtraTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numNiu;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter email;

    private StringFilter motDePasse;

    private InstantFilter dateNaissance;

    private StringFilter numCni;

    private GenderFilter sexe;

    private StringFilter telephone;

    private StringFilter addresse;

    private StringFilter ville;

    private StringFilter pays;

    private UserExtraTypeFilter type;

    private LongFilter internalUserId;

    private Boolean distinct;

    public UserExtraCriteria() {}

    public UserExtraCriteria(UserExtraCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numNiu = other.optionalNumNiu().map(StringFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.motDePasse = other.optionalMotDePasse().map(StringFilter::copy).orElse(null);
        this.dateNaissance = other.optionalDateNaissance().map(InstantFilter::copy).orElse(null);
        this.numCni = other.optionalNumCni().map(StringFilter::copy).orElse(null);
        this.sexe = other.optionalSexe().map(GenderFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.addresse = other.optionalAddresse().map(StringFilter::copy).orElse(null);
        this.ville = other.optionalVille().map(StringFilter::copy).orElse(null);
        this.pays = other.optionalPays().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(UserExtraTypeFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserExtraCriteria copy() {
        return new UserExtraCriteria(this);
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

    public StringFilter getNumNiu() {
        return numNiu;
    }

    public Optional<StringFilter> optionalNumNiu() {
        return Optional.ofNullable(numNiu);
    }

    public StringFilter numNiu() {
        if (numNiu == null) {
            setNumNiu(new StringFilter());
        }
        return numNiu;
    }

    public void setNumNiu(StringFilter numNiu) {
        this.numNiu = numNiu;
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

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public StringFilter getMotDePasse() {
        return motDePasse;
    }

    public Optional<StringFilter> optionalMotDePasse() {
        return Optional.ofNullable(motDePasse);
    }

    public StringFilter motDePasse() {
        if (motDePasse == null) {
            setMotDePasse(new StringFilter());
        }
        return motDePasse;
    }

    public void setMotDePasse(StringFilter motDePasse) {
        this.motDePasse = motDePasse;
    }

    public InstantFilter getDateNaissance() {
        return dateNaissance;
    }

    public Optional<InstantFilter> optionalDateNaissance() {
        return Optional.ofNullable(dateNaissance);
    }

    public InstantFilter dateNaissance() {
        if (dateNaissance == null) {
            setDateNaissance(new InstantFilter());
        }
        return dateNaissance;
    }

    public void setDateNaissance(InstantFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getNumCni() {
        return numCni;
    }

    public Optional<StringFilter> optionalNumCni() {
        return Optional.ofNullable(numCni);
    }

    public StringFilter numCni() {
        if (numCni == null) {
            setNumCni(new StringFilter());
        }
        return numCni;
    }

    public void setNumCni(StringFilter numCni) {
        this.numCni = numCni;
    }

    public GenderFilter getSexe() {
        return sexe;
    }

    public Optional<GenderFilter> optionalSexe() {
        return Optional.ofNullable(sexe);
    }

    public GenderFilter sexe() {
        if (sexe == null) {
            setSexe(new GenderFilter());
        }
        return sexe;
    }

    public void setSexe(GenderFilter sexe) {
        this.sexe = sexe;
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

    public UserExtraTypeFilter getType() {
        return type;
    }

    public Optional<UserExtraTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public UserExtraTypeFilter type() {
        if (type == null) {
            setType(new UserExtraTypeFilter());
        }
        return type;
    }

    public void setType(UserExtraTypeFilter type) {
        this.type = type;
    }

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public Optional<LongFilter> optionalInternalUserId() {
        return Optional.ofNullable(internalUserId);
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            setInternalUserId(new LongFilter());
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
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
        final UserExtraCriteria that = (UserExtraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numNiu, that.numNiu) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(email, that.email) &&
            Objects.equals(motDePasse, that.motDePasse) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(numCni, that.numCni) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(addresse, that.addresse) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(type, that.type) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numNiu,
            nom,
            prenom,
            email,
            motDePasse,
            dateNaissance,
            numCni,
            sexe,
            telephone,
            addresse,
            ville,
            pays,
            type,
            internalUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumNiu().map(f -> "numNiu=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalMotDePasse().map(f -> "motDePasse=" + f + ", ").orElse("") +
            optionalDateNaissance().map(f -> "dateNaissance=" + f + ", ").orElse("") +
            optionalNumCni().map(f -> "numCni=" + f + ", ").orElse("") +
            optionalSexe().map(f -> "sexe=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalAddresse().map(f -> "addresse=" + f + ", ").orElse("") +
            optionalVille().map(f -> "ville=" + f + ", ").orElse("") +
            optionalPays().map(f -> "pays=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
