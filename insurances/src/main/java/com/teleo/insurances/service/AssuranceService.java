package com.teleo.insurances.service;

import com.teleo.insurances.service.dto.AssuranceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.insurances.domain.Assurance}.
 */
public interface AssuranceService {
    /**
     * Save a assurance.
     *
     * @param assuranceDTO the entity to save.
     * @return the persisted entity.
     */
    AssuranceDTO save(AssuranceDTO assuranceDTO);

    /**
     * Updates a assurance.
     *
     * @param assuranceDTO the entity to update.
     * @return the persisted entity.
     */
    AssuranceDTO update(AssuranceDTO assuranceDTO);

    /**
     * Partially updates a assurance.
     *
     * @param assuranceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssuranceDTO> partialUpdate(AssuranceDTO assuranceDTO);

    /**
     * Get all the assurances with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssuranceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assurance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssuranceDTO> findOne(Long id);

    /**
     * Delete the "id" assurance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
