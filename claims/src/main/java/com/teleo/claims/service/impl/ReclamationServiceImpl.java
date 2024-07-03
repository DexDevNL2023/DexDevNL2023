package com.teleo.claims.service.impl;

import com.teleo.claims.domain.Reclamation;
import com.teleo.claims.repository.ReclamationRepository;
import com.teleo.claims.service.ReclamationService;
import com.teleo.claims.service.dto.ReclamationDTO;
import com.teleo.claims.service.mapper.ReclamationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.claims.domain.Reclamation}.
 */
@Service
@Transactional
public class ReclamationServiceImpl implements ReclamationService {

    private static final Logger log = LoggerFactory.getLogger(ReclamationServiceImpl.class);

    private final ReclamationRepository reclamationRepository;

    private final ReclamationMapper reclamationMapper;

    public ReclamationServiceImpl(ReclamationRepository reclamationRepository, ReclamationMapper reclamationMapper) {
        this.reclamationRepository = reclamationRepository;
        this.reclamationMapper = reclamationMapper;
    }

    @Override
    public ReclamationDTO save(ReclamationDTO reclamationDTO) {
        log.debug("Request to save Reclamation : {}", reclamationDTO);
        Reclamation reclamation = reclamationMapper.toEntity(reclamationDTO);
        reclamation = reclamationRepository.save(reclamation);
        return reclamationMapper.toDto(reclamation);
    }

    @Override
    public ReclamationDTO update(ReclamationDTO reclamationDTO) {
        log.debug("Request to update Reclamation : {}", reclamationDTO);
        Reclamation reclamation = reclamationMapper.toEntity(reclamationDTO);
        reclamation = reclamationRepository.save(reclamation);
        return reclamationMapper.toDto(reclamation);
    }

    @Override
    public Optional<ReclamationDTO> partialUpdate(ReclamationDTO reclamationDTO) {
        log.debug("Request to partially update Reclamation : {}", reclamationDTO);

        return reclamationRepository
            .findById(reclamationDTO.getId())
            .map(existingReclamation -> {
                reclamationMapper.partialUpdate(existingReclamation, reclamationDTO);

                return existingReclamation;
            })
            .map(reclamationRepository::save)
            .map(reclamationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReclamationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reclamations");
        return reclamationRepository.findAll(pageable).map(reclamationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReclamationDTO> findOne(Long id) {
        log.debug("Request to get Reclamation : {}", id);
        return reclamationRepository.findById(id).map(reclamationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reclamation : {}", id);
        reclamationRepository.deleteById(id);
    }
}
