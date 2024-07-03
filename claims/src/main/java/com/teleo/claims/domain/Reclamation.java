package com.teleo.claims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.claims.domain.enumeration.ReclamationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reclamation.
 */
@Entity
@Table(name = "reclamation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reclamation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_reclamation", nullable = false, unique = true)
    private String numeroReclamation;

    @NotNull
    @Column(name = "date_reclamation", nullable = false)
    private Instant dateReclamation;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReclamationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fournisseur", "prestationsSoin" }, allowSetters = true)
    private Sinistre sinistre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reclamation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroReclamation() {
        return this.numeroReclamation;
    }

    public Reclamation numeroReclamation(String numeroReclamation) {
        this.setNumeroReclamation(numeroReclamation);
        return this;
    }

    public void setNumeroReclamation(String numeroReclamation) {
        this.numeroReclamation = numeroReclamation;
    }

    public Instant getDateReclamation() {
        return this.dateReclamation;
    }

    public Reclamation dateReclamation(Instant dateReclamation) {
        this.setDateReclamation(dateReclamation);
        return this;
    }

    public void setDateReclamation(Instant dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getDescription() {
        return this.description;
    }

    public Reclamation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReclamationStatus getStatus() {
        return this.status;
    }

    public Reclamation status(ReclamationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReclamationStatus status) {
        this.status = status;
    }

    public Sinistre getSinistre() {
        return this.sinistre;
    }

    public void setSinistre(Sinistre sinistre) {
        this.sinistre = sinistre;
    }

    public Reclamation sinistre(Sinistre sinistre) {
        this.setSinistre(sinistre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reclamation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reclamation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reclamation{" +
            "id=" + getId() +
            ", numeroReclamation='" + getNumeroReclamation() + "'" +
            ", dateReclamation='" + getDateReclamation() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
