package com.teleo.claims.service.dto;

import com.teleo.claims.domain.enumeration.ReclamationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.claims.domain.Reclamation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReclamationDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroReclamation;

    @NotNull
    private Instant dateReclamation;

    @NotNull
    private String description;

    @NotNull
    private ReclamationStatus status;

    private SinistreDTO sinistre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroReclamation() {
        return numeroReclamation;
    }

    public void setNumeroReclamation(String numeroReclamation) {
        this.numeroReclamation = numeroReclamation;
    }

    public Instant getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Instant dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReclamationStatus getStatus() {
        return status;
    }

    public void setStatus(ReclamationStatus status) {
        this.status = status;
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
        if (!(o instanceof ReclamationDTO)) {
            return false;
        }

        ReclamationDTO reclamationDTO = (ReclamationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reclamationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReclamationDTO{" +
            "id=" + getId() +
            ", numeroReclamation='" + getNumeroReclamation() + "'" +
            ", dateReclamation='" + getDateReclamation() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", sinistre=" + getSinistre() +
            "}";
    }
}
