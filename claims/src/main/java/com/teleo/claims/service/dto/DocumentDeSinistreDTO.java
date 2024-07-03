package com.teleo.claims.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.claims.domain.DocumentDeSinistre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDeSinistreDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private String type;

    @NotNull
    private String url;

    @NotNull
    private Instant dateTelechargement;

    private SinistreDTO sinistre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getDateTelechargement() {
        return dateTelechargement;
    }

    public void setDateTelechargement(Instant dateTelechargement) {
        this.dateTelechargement = dateTelechargement;
    }

    public SinistreDTO getSinistre() {
        return sinistre;
    }

    public void setSinistre(SinistreDTO sinistre) {
        this.sinistre = sinistre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDeSinistreDTO)) {
            return false;
        }

        DocumentDeSinistreDTO documentDeSinistreDTO = (DocumentDeSinistreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDeSinistreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDeSinistreDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", dateTelechargement='" + getDateTelechargement() + "'" +
            ", sinistre=" + getSinistre() +
            "}";
    }
}
