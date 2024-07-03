package com.teleo.accounts.service;

import com.teleo.accounts.domain.criteria.NotificationCriteria;
import com.teleo.accounts.service.dto.NotificationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.teleo.accounts.domain.Notification}.
 */
public interface NotificationService {
    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<NotificationDTO> save(NotificationDTO notificationDTO);

    /**
     * Updates a notification.
     *
     * @param notificationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<NotificationDTO> update(NotificationDTO notificationDTO);

    /**
     * Partially updates a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NotificationDTO> partialUpdate(NotificationDTO notificationDTO);
    /**
     * Find notifications by criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NotificationDTO> findByCriteria(NotificationCriteria criteria, Pageable pageable);

    /**
     * Find the count of notifications by criteria.
     * @param criteria filtering criteria
     * @return the count of notifications
     */
    public Mono<Long> countByCriteria(NotificationCriteria criteria);

    /**
     * Returns the number of notifications available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NotificationDTO> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
