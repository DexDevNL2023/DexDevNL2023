package com.teleo.claims.service.impl;

import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.repository.SinistreRepository;
import com.teleo.claims.service.SinistreService;
import com.teleo.claims.service.dto.SinistreDTO;
import com.teleo.claims.service.mapper.SinistreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.claims.domain.Sinistre}.
 */
@Service
@Transactional
public class SinistreServiceImpl implements SinistreService {

    private static final Logger log = LoggerFactory.getLogger(SinistreServiceImpl.class);

    private final SinistreRepository sinistreRepository;

    private final SinistreMapper sinistreMapper;

    public SinistreServiceImpl(SinistreRepository sinistreRepository, SinistreMapper sinistreMapper) {
        this.sinistreRepository = sinistreRepository;
        this.sinistreMapper = sinistreMapper;
    }

    @Override
    public SinistreDTO save(SinistreDTO sinistreDTO) {
        log.debug("Request to save Sinistre : {}", sinistreDTO);
        Sinistre sinistre = sinistreMapper.toEntity(sinistreDTO);
        sinistre = sinistreRepository.save(sinistre);
        return sinistreMapper.toDto(sinistre);
    }

    @Override
    public SinistreDTO update(SinistreDTO sinistreDTO) {
        log.debug("Request to update Sinistre : {}", sinistreDTO);
        Sinistre sinistre = sinistreMapper.toEntity(sinistreDTO);
        sinistre = sinistreRepository.save(sinistre);
        return sinistreMapper.toDto(sinistre);
    }

    @Override
    public Optional<SinistreDTO> partialUpdate(SinistreDTO sinistreDTO) {
        log.debug("Request to partially update Sinistre : {}", sinistreDTO);

        return sinistreRepository
            .findById(sinistreDTO.getId())
            .map(existingSinistre -> {
                sinistreMapper.partialUpdate(existingSinistre, sinistreDTO);

                return existingSinistre;
            })
            .map(sinistreRepository::save)
            .map(sinistreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SinistreDTO> findOne(Long id) {
        log.debug("Request to get Sinistre : {}", id);
        return sinistreRepository.findById(id).map(sinistreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sinistre : {}", id);
        sinistreRepository.deleteById(id);
    }
}
