package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.GarantieDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.Garantie}.
 */
public interface GarantieService {
    /**
     * Save a garantie.
     *
     * @param garantieDTO the entity to save.
     * @return the persisted entity.
     */
    GarantieDTO save(GarantieDTO garantieDTO);

    /**
     * Updates a garantie.
     *
     * @param garantieDTO the entity to update.
     * @return the persisted entity.
     */
    GarantieDTO update(GarantieDTO garantieDTO);

    /**
     * Partially updates a garantie.
     *
     * @param garantieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GarantieDTO> partialUpdate(GarantieDTO garantieDTO);

    /**
     * Get all the garanties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GarantieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" garantie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GarantieDTO> findOne(Long id);

    /**
     * Delete the "id" garantie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
