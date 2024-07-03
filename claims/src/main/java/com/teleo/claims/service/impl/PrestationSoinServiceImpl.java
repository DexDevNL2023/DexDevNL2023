package com.teleo.claims.service.impl;

import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.repository.PrestationSoinRepository;
import com.teleo.claims.service.PrestationSoinService;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import com.teleo.claims.service.mapper.PrestationSoinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.claims.domain.PrestationSoin}.
 */
@Service
@Transactional
public class PrestationSoinServiceImpl implements PrestationSoinService {

    private static final Logger log = LoggerFactory.getLogger(PrestationSoinServiceImpl.class);

    private final PrestationSoinRepository prestationSoinRepository;

    private final PrestationSoinMapper prestationSoinMapper;

    public PrestationSoinServiceImpl(PrestationSoinRepository prestationSoinRepository, PrestationSoinMapper prestationSoinMapper) {
        this.prestationSoinRepository = prestationSoinRepository;
        this.prestationSoinMapper = prestationSoinMapper;
    }

    @Override
    public PrestationSoinDTO save(PrestationSoinDTO prestationSoinDTO) {
        log.debug("Request to save PrestationSoin : {}", prestationSoinDTO);
        PrestationSoin prestationSoin = prestationSoinMapper.toEntity(prestationSoinDTO);
        prestationSoin = prestationSoinRepository.save(prestationSoin);
        return prestationSoinMapper.toDto(prestationSoin);
    }

    @Override
    public PrestationSoinDTO update(PrestationSoinDTO prestationSoinDTO) {
        log.debug("Request to update PrestationSoin : {}", prestationSoinDTO);
        PrestationSoin prestationSoin = prestationSoinMapper.toEntity(prestationSoinDTO);
        prestationSoin = prestationSoinRepository.save(prestationSoin);
        return prestationSoinMapper.toDto(prestationSoin);
    }

    @Override
    public Optional<PrestationSoinDTO> partialUpdate(PrestationSoinDTO prestationSoinDTO) {
        log.debug("Request to partially update PrestationSoin : {}", prestationSoinDTO);

        return prestationSoinRepository
            .findById(prestationSoinDTO.getId())
            .map(existingPrestationSoin -> {
                prestationSoinMapper.partialUpdate(existingPrestationSoin, prestationSoinDTO);

                return existingPrestationSoin;
            })
            .map(prestationSoinRepository::save)
            .map(prestationSoinMapper::toDto);
    }

    public Page<PrestationSoinDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prestationSoinRepository.findAllWithEagerRelationships(pageable).map(prestationSoinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrestationSoinDTO> findOne(Long id) {
        log.debug("Request to get PrestationSoin : {}", id);
        return prestationSoinRepository.findOneWithEagerRelationships(id).map(prestationSoinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrestationSoin : {}", id);
        prestationSoinRepository.deleteById(id);
    }
}
