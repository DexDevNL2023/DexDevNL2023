package com.teleo.insurances.service.impl;

import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.repository.AssuranceRepository;
import com.teleo.insurances.service.AssuranceService;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.mapper.AssuranceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.insurances.domain.Assurance}.
 */
@Service
@Transactional
public class AssuranceServiceImpl implements AssuranceService {

    private static final Logger log = LoggerFactory.getLogger(AssuranceServiceImpl.class);

    private final AssuranceRepository assuranceRepository;

    private final AssuranceMapper assuranceMapper;

    public AssuranceServiceImpl(AssuranceRepository assuranceRepository, AssuranceMapper assuranceMapper) {
        this.assuranceRepository = assuranceRepository;
        this.assuranceMapper = assuranceMapper;
    }

    @Override
    public AssuranceDTO save(AssuranceDTO assuranceDTO) {
        log.debug("Request to save Assurance : {}", assuranceDTO);
        Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
        assurance = assuranceRepository.save(assurance);
        return assuranceMapper.toDto(assurance);
    }

    @Override
    public AssuranceDTO update(AssuranceDTO assuranceDTO) {
        log.debug("Request to update Assurance : {}", assuranceDTO);
        Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
        assurance = assuranceRepository.save(assurance);
        return assuranceMapper.toDto(assurance);
    }

    @Override
    public Optional<AssuranceDTO> partialUpdate(AssuranceDTO assuranceDTO) {
        log.debug("Request to partially update Assurance : {}", assuranceDTO);

        return assuranceRepository
            .findById(assuranceDTO.getId())
            .map(existingAssurance -> {
                assuranceMapper.partialUpdate(existingAssurance, assuranceDTO);

                return existingAssurance;
            })
            .map(assuranceRepository::save)
            .map(assuranceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssuranceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assurances");
        return assuranceRepository.findAll(pageable).map(assuranceMapper::toDto);
    }

    public Page<AssuranceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assuranceRepository.findAllWithEagerRelationships(pageable).map(assuranceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssuranceDTO> findOne(Long id) {
        log.debug("Request to get Assurance : {}", id);
        return assuranceRepository.findOneWithEagerRelationships(id).map(assuranceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assurance : {}", id);
        assuranceRepository.deleteById(id);
    }
}
