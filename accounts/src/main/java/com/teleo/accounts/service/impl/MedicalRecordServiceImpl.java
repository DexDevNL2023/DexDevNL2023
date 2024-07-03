package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.criteria.MedicalRecordCriteria;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.service.MedicalRecordService;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import com.teleo.accounts.service.mapper.MedicalRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.teleo.accounts.domain.MedicalRecord}.
 */
@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, MedicalRecordMapper medicalRecordMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMapper = medicalRecordMapper;
    }

    @Override
    public Mono<MedicalRecordDTO> save(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to save MedicalRecord : {}", medicalRecordDTO);
        return medicalRecordRepository.save(medicalRecordMapper.toEntity(medicalRecordDTO)).map(medicalRecordMapper::toDto);
    }

    @Override
    public Mono<MedicalRecordDTO> update(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to update MedicalRecord : {}", medicalRecordDTO);
        return medicalRecordRepository.save(medicalRecordMapper.toEntity(medicalRecordDTO)).map(medicalRecordMapper::toDto);
    }

    @Override
    public Mono<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to partially update MedicalRecord : {}", medicalRecordDTO);

        return medicalRecordRepository
            .findById(medicalRecordDTO.getId())
            .map(existingMedicalRecord -> {
                medicalRecordMapper.partialUpdate(existingMedicalRecord, medicalRecordDTO);

                return existingMedicalRecord;
            })
            .flatMap(medicalRecordRepository::save)
            .map(medicalRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MedicalRecordDTO> findByCriteria(MedicalRecordCriteria criteria, Pageable pageable) {
        log.debug("Request to get all MedicalRecords by Criteria");
        return medicalRecordRepository.findByCriteria(criteria, pageable).map(medicalRecordMapper::toDto);
    }

    /**
     * Find the count of medicalRecords by criteria.
     * @param criteria filtering criteria
     * @return the count of medicalRecords
     */
    public Mono<Long> countByCriteria(MedicalRecordCriteria criteria) {
        log.debug("Request to get the count of all MedicalRecords by Criteria");
        return medicalRecordRepository.countByCriteria(criteria);
    }

    public Mono<Long> countAll() {
        return medicalRecordRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MedicalRecordDTO> findOne(Long id) {
        log.debug("Request to get MedicalRecord : {}", id);
        return medicalRecordRepository.findById(id).map(medicalRecordMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete MedicalRecord : {}", id);
        return medicalRecordRepository.deleteById(id);
    }
}
