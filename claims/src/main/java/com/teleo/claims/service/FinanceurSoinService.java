package com.teleo.claims.service;

import com.teleo.claims.service.dto.FinanceurSoinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.claims.domain.FinanceurSoin}.
 */
public interface FinanceurSoinService {
    /**
     * Save a financeurSoin.
     *
     * @param financeurSoinDTO the entity to save.
     * @return the persisted entity.
     */
    FinanceurSoinDTO save(FinanceurSoinDTO financeurSoinDTO);

    /**
     * Updates a financeurSoin.
     *
     * @param financeurSoinDTO the entity to update.
     * @return the persisted entity.
     */
    FinanceurSoinDTO update(FinanceurSoinDTO financeurSoinDTO);

    /**
     * Partially updates a financeurSoin.
     *
     * @param financeurSoinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FinanceurSoinDTO> partialUpdate(FinanceurSoinDTO financeurSoinDTO);

    /**
     * Get all the financeurSoins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FinanceurSoinDTO> findAll(Pageable pageable);

    /**
     * Get the "id" financeurSoin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinanceurSoinDTO> findOne(Long id);

    /**
     * Delete the "id" financeurSoin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
