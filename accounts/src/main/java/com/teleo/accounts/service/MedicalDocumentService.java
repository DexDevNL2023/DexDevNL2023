package com.teleo.accounts.service;

import com.teleo.accounts.domain.criteria.MedicalDocumentCriteria;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.teleo.accounts.domain.MedicalDocument}.
 */
public interface MedicalDocumentService {
    /**
     * Save a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MedicalDocumentDTO> save(MedicalDocumentDTO medicalDocumentDTO);

    /**
     * Updates a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MedicalDocumentDTO> update(MedicalDocumentDTO medicalDocumentDTO);

    /**
     * Partially updates a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MedicalDocumentDTO> partialUpdate(MedicalDocumentDTO medicalDocumentDTO);
    /**
     * Find medicalDocuments by criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MedicalDocumentDTO> findByCriteria(MedicalDocumentCriteria criteria, Pageable pageable);

    /**
     * Find the count of medicalDocuments by criteria.
     * @param criteria filtering criteria
     * @return the count of medicalDocuments
     */
    public Mono<Long> countByCriteria(MedicalDocumentCriteria criteria);

    /**
     * Returns the number of medicalDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" medicalDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MedicalDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" medicalDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
