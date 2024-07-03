package com.teleo.accounts.domain.criteria;

import com.teleo.accounts.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teleo.accounts.domain.Notification} entity. This class is used
 * in {@link com.teleo.accounts.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private InstantFilter dateEnvoi;

    private NotificationTypeFilter type;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.dateEnvoi = other.optionalDateEnvoi().map(InstantFilter::copy).orElse(null);
        this.type = other.optionalType().map(NotificationTypeFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public InstantFilter getDateEnvoi() {
        return dateEnvoi;
    }

    public Optional<InstantFilter> optionalDateEnvoi() {
        return Optional.ofNullable(dateEnvoi);
    }

    public InstantFilter dateEnvoi() {
        if (dateEnvoi == null) {
            setDateEnvoi(new InstantFilter());
        }
        return dateEnvoi;
    }

    public void setDateEnvoi(InstantFilter dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public NotificationTypeFilter getType() {
        return type;
    }

    public Optional<NotificationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public NotificationTypeFilter type() {
        if (type == null) {
            setType(new NotificationTypeFilter());
        }
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(dateEnvoi, that.dateEnvoi) &&
            Objects.equals(type, that.type) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, dateEnvoi, type, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalDateEnvoi().map(f -> "dateEnvoi=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
