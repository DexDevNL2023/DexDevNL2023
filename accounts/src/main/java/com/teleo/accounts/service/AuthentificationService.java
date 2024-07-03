package com.teleo.accounts.service;

import com.teleo.accounts.service.dto.AuthentificationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.teleo.accounts.domain.Authentification}.
 */
public interface AuthentificationService {
    /**
     * Save a authentification.
     *
     * @param authentificationDTO the entity to save.
     * @return the persisted entity.
     */
    AuthentificationDTO save(AuthentificationDTO authentificationDTO);

    /**
     * Updates a authentification.
     *
     * @param authentificationDTO the entity to update.
     * @return the persisted entity.
     */
    AuthentificationDTO update(AuthentificationDTO authentificationDTO);

    /**
     * Partially updates a authentification.
     *
     * @param authentificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuthentificationDTO> partialUpdate(AuthentificationDTO authentificationDTO);

    /**
     * Get all the authentifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuthentificationDTO> findAll(Pageable pageable);

    /**
     * Get all the AuthentificationDTO where Utilisateur is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AuthentificationDTO> findAllWhereUtilisateurIsNull();

    /**
     * Get the "id" authentification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuthentificationDTO> findOne(Long id);

    /**
     * Delete the "id" authentification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
