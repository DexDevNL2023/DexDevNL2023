package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.SouscriptionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.Souscription}.
 */
public interface SouscriptionService {
    /**
     * Save a souscription.
     *
     * @param souscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    SouscriptionDTO save(SouscriptionDTO souscriptionDTO);

    /**
     * Updates a souscription.
     *
     * @param souscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    SouscriptionDTO update(SouscriptionDTO souscriptionDTO);

    /**
     * Partially updates a souscription.
     *
     * @param souscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SouscriptionDTO> partialUpdate(SouscriptionDTO souscriptionDTO);

    /**
     * Get the "id" souscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SouscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" souscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
