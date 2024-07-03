package com.teleo.claims.service;

import com.teleo.claims.service.dto.PrestationSoinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.claims.domain.PrestationSoin}.
 */
public interface PrestationSoinService {
    /**
     * Save a prestationSoin.
     *
     * @param prestationSoinDTO the entity to save.
     * @return the persisted entity.
     */
    PrestationSoinDTO save(PrestationSoinDTO prestationSoinDTO);

    /**
     * Updates a prestationSoin.
     *
     * @param prestationSoinDTO the entity to update.
     * @return the persisted entity.
     */
    PrestationSoinDTO update(PrestationSoinDTO prestationSoinDTO);

    /**
     * Partially updates a prestationSoin.
     *
     * @param prestationSoinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrestationSoinDTO> partialUpdate(PrestationSoinDTO prestationSoinDTO);

    /**
     * Get all the prestationSoins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrestationSoinDTO> findAll(Pageable pageable);

    /**
     * Get all the prestationSoins with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrestationSoinDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prestationSoin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrestationSoinDTO> findOne(Long id);

    /**
     * Delete the "id" prestationSoin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
