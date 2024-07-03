package com.teleo.insurances.service.dto;

import com.teleo.insurances.domain.enumeration.RapportType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.insurances.domain.Rapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private RapportType type;

    @NotNull
    private Instant dateGeneration;

    @NotNull
    private String url;

    private AssuranceDTO assurance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RapportType getType() {
        return type;
    }

    public void setType(RapportType type) {
        this.type = type;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AssuranceDTO getAssurance() {
        return assurance;
    }

    public void setAssurance(AssuranceDTO assurance) {
        this.assurance = assurance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportDTO)) {
            return false;
        }

        RapportDTO rapportDTO = (RapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", url='" + getUrl() + "'" +
            ", assurance=" + getAssurance() +
            "}";
    }
}
