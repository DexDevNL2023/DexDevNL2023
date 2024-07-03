package com.teleo.accounts.domain;

import com.teleo.accounts.domain.enumeration.Gender;
import com.teleo.accounts.domain.enumeration.UserExtraType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entities for user account management microservice
 */
@Table("user_extra")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("num_niu")
    private String numNiu;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    @NotNull(message = "must not be null")
    @Column("prenom")
    private String prenom;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column("email")
    private String email;

    @NotNull(message = "must not be null")
    @Column("mot_de_passe")
    private String motDePasse;

    @NotNull(message = "must not be null")
    @Column("date_naissance")
    private Instant dateNaissance;

    @NotNull(message = "must not be null")
    @Column("num_cni")
    private String numCni;

    @NotNull(message = "must not be null")
    @Column("sexe")
    private Gender sexe;

    @NotNull(message = "must not be null")
    @Column("telephone")
    private String telephone;

    @NotNull(message = "must not be null")
    @Column("addresse")
    private String addresse;

    @NotNull(message = "must not be null")
    @Column("ville")
    private String ville;

    @NotNull(message = "must not be null")
    @Column("pays")
    private String pays;

    @NotNull(message = "must not be null")
    @Column("type")
    private UserExtraType type;

    @Column("signature")
    private byte[] signature;

    @NotNull
    @Column("signature_content_type")
    private String signatureContentType;

    @Transient
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserExtra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumNiu() {
        return this.numNiu;
    }

    public UserExtra numNiu(String numNiu) {
        this.setNumNiu(numNiu);
        return this;
    }

    public void setNumNiu(String numNiu) {
        this.numNiu = numNiu;
    }

    public String getNom() {
        return this.nom;
    }

    public UserExtra nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public UserExtra prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public UserExtra email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public UserExtra motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public UserExtra dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumCni() {
        return this.numCni;
    }

    public UserExtra numCni(String numCni) {
        this.setNumCni(numCni);
        return this;
    }

    public void setNumCni(String numCni) {
        this.numCni = numCni;
    }

    public Gender getSexe() {
        return this.sexe;
    }

    public UserExtra sexe(Gender sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Gender sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public UserExtra telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddresse() {
        return this.addresse;
    }

    public UserExtra addresse(String addresse) {
        this.setAddresse(addresse);
        return this;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getVille() {
        return this.ville;
    }

    public UserExtra ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return this.pays;
    }

    public UserExtra pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public UserExtraType getType() {
        return this.type;
    }

    public UserExtra type(UserExtraType type) {
        this.setType(type);
        return this;
    }

    public void setType(UserExtraType type) {
        this.type = type;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public UserExtra signature(byte[] signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return this.signatureContentType;
    }

    public UserExtra signatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
        return this;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public UserExtra internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserExtra)) {
            return false;
        }
        return getId() != null && getId().equals(((UserExtra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtra{" +
            "id=" + getId() +
            ", numNiu='" + getNumNiu() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", numCni='" + getNumCni() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", addresse='" + getAddresse() + "'" +
            ", ville='" + getVille() + "'" +
            ", pays='" + getPays() + "'" +
            ", type='" + getType() + "'" +
            ", signature='" + getSignature() + "'" +
            ", signatureContentType='" + getSignatureContentType() + "'" +
            "}";
    }
}
