package com.teleo.accounts.service;

import com.teleo.accounts.service.dto.MedicalRecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO);

    /**
     * Updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update.
     * @return the persisted entity.
     */
    MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO);

    /**
     * Partially updates a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO);

    /**
     * Get all the medicalRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicalRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" medicalRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicalRecordDTO> findOne(Long id);

    /**
     * Delete the "id" medicalRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
