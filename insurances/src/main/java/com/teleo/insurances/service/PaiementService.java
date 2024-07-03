package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.PaiementDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.Paiement}.
 */
public interface PaiementService {
    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementDTO save(PaiementDTO paiementDTO);

    /**
     * Updates a paiement.
     *
     * @param paiementDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementDTO update(PaiementDTO paiementDTO);

    /**
     * Partially updates a paiement.
     *
     * @param paiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementDTO> partialUpdate(PaiementDTO paiementDTO);

    /**
     * Get the "id" paiement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementDTO> findOne(Long id);

    /**
     * Delete the "id" paiement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
