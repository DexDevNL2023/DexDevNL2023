package com.teleo.insurances.service.impl;

import com.teleo.insurances.domain.Garantie;
import com.teleo.insurances.repository.GarantieRepository;
import com.teleo.insurances.service.GarantieService;
import com.teleo.insurances.service.dto.GarantieDTO;
import com.teleo.insurances.service.mapper.GarantieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.insurances.domain.Garantie}.
 */
@Service
@Transactional
public class GarantieServiceImpl implements GarantieService {

    private static final Logger log = LoggerFactory.getLogger(GarantieServiceImpl.class);

    private final GarantieRepository garantieRepository;

    private final GarantieMapper garantieMapper;

    public GarantieServiceImpl(GarantieRepository garantieRepository, GarantieMapper garantieMapper) {
        this.garantieRepository = garantieRepository;
        this.garantieMapper = garantieMapper;
    }

    @Override
    public GarantieDTO save(GarantieDTO garantieDTO) {
        log.debug("Request to save Garantie : {}", garantieDTO);
        Garantie garantie = garantieMapper.toEntity(garantieDTO);
        garantie = garantieRepository.save(garantie);
        return garantieMapper.toDto(garantie);
    }

    @Override
    public GarantieDTO update(GarantieDTO garantieDTO) {
        log.debug("Request to update Garantie : {}", garantieDTO);
        Garantie garantie = garantieMapper.toEntity(garantieDTO);
        garantie = garantieRepository.save(garantie);
        return garantieMapper.toDto(garantie);
    }

    @Override
    public Optional<GarantieDTO> partialUpdate(GarantieDTO garantieDTO) {
        log.debug("Request to partially update Garantie : {}", garantieDTO);

        return garantieRepository
            .findById(garantieDTO.getId())
            .map(existingGarantie -> {
                garantieMapper.partialUpdate(existingGarantie, garantieDTO);

                return existingGarantie;
            })
            .map(garantieRepository::save)
            .map(garantieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GarantieDTO> findOne(Long id) {
        log.debug("Request to get Garantie : {}", id);
        return garantieRepository.findById(id).map(garantieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Garantie : {}", id);
        garantieRepository.deleteById(id);
    }
}
