package com.teleo.claims.service;

import com.teleo.claims.service.dto.SinistreDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.claims.domain.Sinistre}.
 */
public interface SinistreService {
    /**
     * Save a sinistre.
     *
     * @param sinistreDTO the entity to save.
     * @return the persisted entity.
     */
    SinistreDTO save(SinistreDTO sinistreDTO);

    /**
     * Updates a sinistre.
     *
     * @param sinistreDTO the entity to update.
     * @return the persisted entity.
     */
    SinistreDTO update(SinistreDTO sinistreDTO);

    /**
     * Partially updates a sinistre.
     *
     * @param sinistreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SinistreDTO> partialUpdate(SinistreDTO sinistreDTO);

    /**
     * Get the "id" sinistre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SinistreDTO> findOne(Long id);

    /**
     * Delete the "id" sinistre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
