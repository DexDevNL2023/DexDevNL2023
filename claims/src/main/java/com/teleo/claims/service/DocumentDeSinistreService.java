package com.teleo.claims.service;

import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.claims.domain.DocumentDeSinistre}.
 */
public interface DocumentDeSinistreService {
    /**
     * Save a documentDeSinistre.
     *
     * @param documentDeSinistreDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentDeSinistreDTO save(DocumentDeSinistreDTO documentDeSinistreDTO);

    /**
     * Updates a documentDeSinistre.
     *
     * @param documentDeSinistreDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentDeSinistreDTO update(DocumentDeSinistreDTO documentDeSinistreDTO);

    /**
     * Partially updates a documentDeSinistre.
     *
     * @param documentDeSinistreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentDeSinistreDTO> partialUpdate(DocumentDeSinistreDTO documentDeSinistreDTO);

    /**
     * Get all the documentDeSinistres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentDeSinistreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentDeSinistre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentDeSinistreDTO> findOne(Long id);

    /**
     * Delete the "id" documentDeSinistre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
