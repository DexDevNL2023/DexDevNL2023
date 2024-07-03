package com.teleo.insurances.service.impl;

import com.teleo.insurances.domain.Rapport;
import com.teleo.insurances.repository.RapportRepository;
import com.teleo.insurances.service.RapportService;
import com.teleo.insurances.service.dto.RapportDTO;
import com.teleo.insurances.service.mapper.RapportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.insurances.domain.Rapport}.
 */
@Service
@Transactional
public class RapportServiceImpl implements RapportService {

    private static final Logger log = LoggerFactory.getLogger(RapportServiceImpl.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    public RapportServiceImpl(RapportRepository rapportRepository, RapportMapper rapportMapper) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
    }

    @Override
    public RapportDTO save(RapportDTO rapportDTO) {
        log.debug("Request to save Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public RapportDTO update(RapportDTO rapportDTO) {
        log.debug("Request to update Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public Optional<RapportDTO> partialUpdate(RapportDTO rapportDTO) {
        log.debug("Request to partially update Rapport : {}", rapportDTO);

        return rapportRepository
            .findById(rapportDTO.getId())
            .map(existingRapport -> {
                rapportMapper.partialUpdate(existingRapport, rapportDTO);

                return existingRapport;
            })
            .map(rapportRepository::save)
            .map(rapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rapports");
        return rapportRepository.findAll(pageable).map(rapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RapportDTO> findOne(Long id) {
        log.debug("Request to get Rapport : {}", id);
        return rapportRepository.findById(id).map(rapportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rapport : {}", id);
        rapportRepository.deleteById(id);
    }
}
