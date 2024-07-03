package com.teleo.accounts.service;

import com.teleo.accounts.domain.criteria.MedicalRecordCriteria;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.teleo.accounts.domain.MedicalRecord}.
 */
public interface MedicalRecordService {
    /**
     * Save a medicalRecord.
     *
     * @param medicalRecordDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MedicalRecordDTO> save(MedicalRecordDTO medicalRecordDTO);

    /**
     * Updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MedicalRecordDTO> update(MedicalRecordDTO medicalRecordDTO);

    /**
     * Partially updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO);
    /**
     * Find medicalRecords by criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MedicalRecordDTO> findByCriteria(MedicalRecordCriteria criteria, Pageable pageable);

    /**
     * Find the count of medicalRecords by criteria.
     * @param criteria filtering criteria
     * @return the count of medicalRecords
     */
    public Mono<Long> countByCriteria(MedicalRecordCriteria criteria);

    /**
     * Returns the number of medicalRecords available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" medicalRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MedicalRecordDTO> findOne(Long id);

    /**
     * Delete the "id" medicalRecord.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
