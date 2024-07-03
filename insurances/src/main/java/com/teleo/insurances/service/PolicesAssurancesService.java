package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.PolicesAssurances}.
 */
public interface PolicesAssurancesService {
    /**
     * Save a policesAssurances.
     *
     * @param policesAssurancesDTO the entity to save.
     * @return the persisted entity.
     */
    PolicesAssurancesDTO save(PolicesAssurancesDTO policesAssurancesDTO);

    /**
     * Updates a policesAssurances.
     *
     * @param policesAssurancesDTO the entity to update.
     * @return the persisted entity.
     */
    PolicesAssurancesDTO update(PolicesAssurancesDTO policesAssurancesDTO);

    /**
     * Partially updates a policesAssurances.
     *
     * @param policesAssurancesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PolicesAssurancesDTO> partialUpdate(PolicesAssurancesDTO policesAssurancesDTO);

    /**
     * Get the "id" policesAssurances.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PolicesAssurancesDTO> findOne(Long id);

    /**
     * Delete the "id" policesAssurances.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
