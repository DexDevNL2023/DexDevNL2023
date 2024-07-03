package com.teleo.insurances.service.impl;

import com.teleo.insurances.domain.PolicesAssurances;
import com.teleo.insurances.repository.PolicesAssurancesRepository;
import com.teleo.insurances.service.PolicesAssurancesService;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import com.teleo.insurances.service.mapper.PolicesAssurancesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.insurances.domain.PolicesAssurances}.
 */
@Service
@Transactional
public class PolicesAssurancesServiceImpl implements PolicesAssurancesService {

    private static final Logger log = LoggerFactory.getLogger(PolicesAssurancesServiceImpl.class);

    private final PolicesAssurancesRepository policesAssurancesRepository;

    private final PolicesAssurancesMapper policesAssurancesMapper;

    public PolicesAssurancesServiceImpl(
        PolicesAssurancesRepository policesAssurancesRepository,
        PolicesAssurancesMapper policesAssurancesMapper
    ) {
        this.policesAssurancesRepository = policesAssurancesRepository;
        this.policesAssurancesMapper = policesAssurancesMapper;
    }

    @Override
    public PolicesAssurancesDTO save(PolicesAssurancesDTO policesAssurancesDTO) {
        log.debug("Request to save PolicesAssurances : {}", policesAssurancesDTO);
        PolicesAssurances policesAssurances = policesAssurancesMapper.toEntity(policesAssurancesDTO);
        policesAssurances = policesAssurancesRepository.save(policesAssurances);
        return policesAssurancesMapper.toDto(policesAssurances);
    }

    @Override
    public PolicesAssurancesDTO update(PolicesAssurancesDTO policesAssurancesDTO) {
        log.debug("Request to update PolicesAssurances : {}", policesAssurancesDTO);
        PolicesAssurances policesAssurances = policesAssurancesMapper.toEntity(policesAssurancesDTO);
        policesAssurances = policesAssurancesRepository.save(policesAssurances);
        return policesAssurancesMapper.toDto(policesAssurances);
    }

    @Override
    public Optional<PolicesAssurancesDTO> partialUpdate(PolicesAssurancesDTO policesAssurancesDTO) {
        log.debug("Request to partially update PolicesAssurances : {}", policesAssurancesDTO);

        return policesAssurancesRepository
            .findById(policesAssurancesDTO.getId())
            .map(existingPolicesAssurances -> {
                policesAssurancesMapper.partialUpdate(existingPolicesAssurances, policesAssurancesDTO);

                return existingPolicesAssurances;
            })
            .map(policesAssurancesRepository::save)
            .map(policesAssurancesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PolicesAssurancesDTO> findOne(Long id) {
        log.debug("Request to get PolicesAssurances : {}", id);
        return policesAssurancesRepository.findById(id).map(policesAssurancesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PolicesAssurances : {}", id);
        policesAssurancesRepository.deleteById(id);
    }
}
