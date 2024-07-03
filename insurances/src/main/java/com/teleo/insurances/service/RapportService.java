package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.RapportDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.Rapport}.
 */
public interface RapportService {
    /**
     * Save a rapport.
     *
     * @param rapportDTO the entity to save.
     * @return the persisted entity.
     */
    RapportDTO save(RapportDTO rapportDTO);

    /**
     * Updates a rapport.
     *
     * @param rapportDTO the entity to update.
     * @return the persisted entity.
     */
    RapportDTO update(RapportDTO rapportDTO);

    /**
     * Partially updates a rapport.
     *
     * @param rapportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RapportDTO> partialUpdate(RapportDTO rapportDTO);

    /**
     * Get the "id" rapport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RapportDTO> findOne(Long id);

    /**
     * Delete the "id" rapport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
