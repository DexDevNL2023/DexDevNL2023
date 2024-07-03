package com.teleo.accounts.service;

import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    MedicalDocumentDTO save(MedicalDocumentDTO medicalDocumentDTO);

    /**
     * Updates a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    MedicalDocumentDTO update(MedicalDocumentDTO medicalDocumentDTO);

    /**
     * Partially updates a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicalDocumentDTO> partialUpdate(MedicalDocumentDTO medicalDocumentDTO);

    /**
     * Get all the medicalDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicalDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" medicalDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicalDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" medicalDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
