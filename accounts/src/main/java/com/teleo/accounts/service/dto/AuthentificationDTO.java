package com.teleo.accounts.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.teleo.accounts.domain.Authentification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthentificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateConnexion;

    @NotNull
    private String ipAdresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(Instant dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public String getIpAdresse() {
        return ipAdresse;
    }

    public void setIpAdresse(String ipAdresse) {
        this.ipAdresse = ipAdresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthentificationDTO)) {
            return false;
        }

        AuthentificationDTO authentificationDTO = (AuthentificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authentificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthentificationDTO{" +
            "id=" + getId() +
            ", dateConnexion='" + getDateConnexion() + "'" +
            ", ipAdresse='" + getIpAdresse() + "'" +
            "}";
    }
}
