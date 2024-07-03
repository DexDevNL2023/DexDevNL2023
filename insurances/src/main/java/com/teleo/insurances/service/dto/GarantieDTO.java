package com.teleo.insurances.service.dto;

import com.teleo.insurances.domain.enumeration.GarantieStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.teleo.insurances.domain.Garantie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GarantieDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateFin;

    @NotNull
    private GarantieStatus status;

    private Set<AssuranceDTO> assurances = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public GarantieStatus getStatus() {
        return status;
    }

    public void setStatus(GarantieStatus status) {
        this.status = status;
    }

    public Set<AssuranceDTO> getAssurances() {
        return assurances;
    }

    public void setAssurances(Set<AssuranceDTO> assurances) {
        this.assurances = assurances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GarantieDTO)) {
            return false;
        }

        GarantieDTO garantieDTO = (GarantieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, garantieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GarantieDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", status='" + getStatus() + "'" +
            ", assurances=" + getAssurances() +
            "}";
    }
}
