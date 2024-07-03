package com.teleo.accounts.service;

import com.teleo.accounts.domain.criteria.UserExtraCriteria;
import com.teleo.accounts.service.dto.UserExtraDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.teleo.accounts.domain.UserExtra}.
 */
public interface UserExtraService {
    /**
     * Save a userExtra.
     *
     * @param userExtraDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserExtraDTO> save(UserExtraDTO userExtraDTO);

    /**
     * Updates a userExtra.
     *
     * @param userExtraDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserExtraDTO> update(UserExtraDTO userExtraDTO);

    /**
     * Partially updates a userExtra.
     *
     * @param userExtraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserExtraDTO> partialUpdate(UserExtraDTO userExtraDTO);
    /**
     * Find userExtras by criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserExtraDTO> findByCriteria(UserExtraCriteria criteria, Pageable pageable);

    /**
     * Find the count of userExtras by criteria.
     * @param criteria filtering criteria
     * @return the count of userExtras
     */
    public Mono<Long> countByCriteria(UserExtraCriteria criteria);

    /**
     * Get all the userExtras with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserExtraDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of userExtras available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" userExtra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserExtraDTO> findOne(Long id);

    /**
     * Delete the "id" userExtra.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
