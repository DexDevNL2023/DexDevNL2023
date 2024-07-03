package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.Authentification;
import com.teleo.accounts.repository.AuthentificationRepository;
import com.teleo.accounts.service.AuthentificationService;
import com.teleo.accounts.service.dto.AuthentificationDTO;
import com.teleo.accounts.service.mapper.AuthentificationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.accounts.domain.Authentification}.
 */
@Service
@Transactional
public class AuthentificationServiceImpl implements AuthentificationService {

    private static final Logger log = LoggerFactory.getLogger(AuthentificationServiceImpl.class);

    private final AuthentificationRepository authentificationRepository;

    private final AuthentificationMapper authentificationMapper;

    public AuthentificationServiceImpl(
        AuthentificationRepository authentificationRepository,
        AuthentificationMapper authentificationMapper
    ) {
        this.authentificationRepository = authentificationRepository;
        this.authentificationMapper = authentificationMapper;
    }

    @Override
    public AuthentificationDTO save(AuthentificationDTO authentificationDTO) {
        log.debug("Request to save Authentification : {}", authentificationDTO);
        Authentification authentification = authentificationMapper.toEntity(authentificationDTO);
        authentification = authentificationRepository.save(authentification);
        return authentificationMapper.toDto(authentification);
    }

    @Override
    public AuthentificationDTO update(AuthentificationDTO authentificationDTO) {
        log.debug("Request to update Authentification : {}", authentificationDTO);
        Authentification authentification = authentificationMapper.toEntity(authentificationDTO);
        authentification = authentificationRepository.save(authentification);
        return authentificationMapper.toDto(authentification);
    }

    @Override
    public Optional<AuthentificationDTO> partialUpdate(AuthentificationDTO authentificationDTO) {
        log.debug("Request to partially update Authentification : {}", authentificationDTO);

        return authentificationRepository
            .findById(authentificationDTO.getId())
            .map(existingAuthentification -> {
                authentificationMapper.partialUpdate(existingAuthentification, authentificationDTO);

                return existingAuthentification;
            })
            .map(authentificationRepository::save)
            .map(authentificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthentificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Authentifications");
        return authentificationRepository.findAll(pageable).map(authentificationMapper::toDto);
    }

    /**
     *  Get all the authentifications where Utilisateur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthentificationDTO> findAllWhereUtilisateurIsNull() {
        log.debug("Request to get all authentifications where Utilisateur is null");
        return StreamSupport.stream(authentificationRepository.findAll().spliterator(), false)
            .filter(authentification -> authentification.getUtilisateur() == null)
            .map(authentificationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthentificationDTO> findOne(Long id) {
        log.debug("Request to get Authentification : {}", id);
        return authentificationRepository.findById(id).map(authentificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Authentification : {}", id);
        authentificationRepository.deleteById(id);
    }
}
