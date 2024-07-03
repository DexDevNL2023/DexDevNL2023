package com.teleo.claims.service.impl;

import com.teleo.claims.domain.FinanceurSoin;
import com.teleo.claims.repository.FinanceurSoinRepository;
import com.teleo.claims.service.FinanceurSoinService;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
import com.teleo.claims.service.mapper.FinanceurSoinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.claims.domain.FinanceurSoin}.
 */
@Service
@Transactional
public class FinanceurSoinServiceImpl implements FinanceurSoinService {

    private static final Logger log = LoggerFactory.getLogger(FinanceurSoinServiceImpl.class);

    private final FinanceurSoinRepository financeurSoinRepository;

    private final FinanceurSoinMapper financeurSoinMapper;

    public FinanceurSoinServiceImpl(FinanceurSoinRepository financeurSoinRepository, FinanceurSoinMapper financeurSoinMapper) {
        this.financeurSoinRepository = financeurSoinRepository;
        this.financeurSoinMapper = financeurSoinMapper;
    }

    @Override
    public FinanceurSoinDTO save(FinanceurSoinDTO financeurSoinDTO) {
        log.debug("Request to save FinanceurSoin : {}", financeurSoinDTO);
        FinanceurSoin financeurSoin = financeurSoinMapper.toEntity(financeurSoinDTO);
        financeurSoin = financeurSoinRepository.save(financeurSoin);
        return financeurSoinMapper.toDto(financeurSoin);
    }

    @Override
    public FinanceurSoinDTO update(FinanceurSoinDTO financeurSoinDTO) {
        log.debug("Request to update FinanceurSoin : {}", financeurSoinDTO);
        FinanceurSoin financeurSoin = financeurSoinMapper.toEntity(financeurSoinDTO);
        financeurSoin = financeurSoinRepository.save(financeurSoin);
        return financeurSoinMapper.toDto(financeurSoin);
    }

    @Override
    public Optional<FinanceurSoinDTO> partialUpdate(FinanceurSoinDTO financeurSoinDTO) {
        log.debug("Request to partially update FinanceurSoin : {}", financeurSoinDTO);

        return financeurSoinRepository
            .findById(financeurSoinDTO.getId())
            .map(existingFinanceurSoin -> {
                financeurSoinMapper.partialUpdate(existingFinanceurSoin, financeurSoinDTO);

                return existingFinanceurSoin;
            })
            .map(financeurSoinRepository::save)
            .map(financeurSoinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FinanceurSoinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FinanceurSoins");
        return financeurSoinRepository.findAll(pageable).map(financeurSoinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinanceurSoinDTO> findOne(Long id) {
        log.debug("Request to get FinanceurSoin : {}", id);
        return financeurSoinRepository.findById(id).map(financeurSoinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinanceurSoin : {}", id);
        financeurSoinRepository.deleteById(id);
    }
}
