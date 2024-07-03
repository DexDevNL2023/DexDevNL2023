package com.teleo.claims.service;

import com.teleo.claims.service.dto.ReclamationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.claims.domain.Reclamation}.
 */
public interface ReclamationService {
    /**
     * Save a reclamation.
     *
     * @param reclamationDTO the entity to save.
     * @return the persisted entity.
     */
    ReclamationDTO save(ReclamationDTO reclamationDTO);

    /**
     * Updates a reclamation.
     *
     * @param reclamationDTO the entity to update.
     * @return the persisted entity.
     */
    ReclamationDTO update(ReclamationDTO reclamationDTO);

    /**
     * Partially updates a reclamation.
     *
     * @param reclamationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReclamationDTO> partialUpdate(ReclamationDTO reclamationDTO);

    /**
     * Get the "id" reclamation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReclamationDTO> findOne(Long id);

    /**
     * Delete the "id" reclamation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
