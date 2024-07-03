package com.teleo.accounts.service.dto;

import com.teleo.accounts.domain.enumeration.Gender;
import com.teleo.accounts.domain.enumeration.UserExtraType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.accounts.domain.UserExtra} entity.
 */
@Schema(description = "Entities for user account management microservice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserExtraDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String numNiu;

    @NotNull(message = "must not be null")
    private String nom;

    @NotNull(message = "must not be null")
    private String prenom;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @NotNull(message = "must not be null")
    private String motDePasse;

    @NotNull(message = "must not be null")
    private Instant dateNaissance;

    @NotNull(message = "must not be null")
    private String numCni;

    @NotNull(message = "must not be null")
    private Gender sexe;

    @NotNull(message = "must not be null")
    private String telephone;

    @NotNull(message = "must not be null")
    private String addresse;

    @NotNull(message = "must not be null")
    private String ville;

    @NotNull(message = "must not be null")
    private String pays;

    @NotNull(message = "must not be null")
    private UserExtraType type;

    @Lob
    private byte[] signature;

    private String signatureContentType;

    private UserDTO internalUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumNiu() {
        return numNiu;
    }

    public void setNumNiu(String numNiu) {
        this.numNiu = numNiu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Instant getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumCni() {
        return numCni;
    }

    public void setNumCni(String numCni) {
        this.numCni = numCni;
    }

    public Gender getSexe() {
        return sexe;
    }

    public void setSexe(Gender sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public UserExtraType getType() {
        return type;
    }

    public void setType(UserExtraType type) {
        this.type = type;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public UserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(UserDTO internalUser) {
        this.internalUser = internalUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserExtraDTO)) {
            return false;
        }

        UserExtraDTO userExtraDTO = (UserExtraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userExtraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraDTO{" +
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
            ", internalUser=" + getInternalUser() +
            "}";
    }
}
