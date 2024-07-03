package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.service.MedicalRecordService;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import com.teleo.accounts.service.mapper.MedicalRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to save MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    public MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to update MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    public Optional<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO) {
        log.debug("Request to partially update MedicalRecord : {}", medicalRecordDTO);

        return medicalRecordRepository
            .findById(medicalRecordDTO.getId())
            .map(existingMedicalRecord -> {
                medicalRecordMapper.partialUpdate(existingMedicalRecord, medicalRecordDTO);

                return existingMedicalRecord;
            })
            .map(medicalRecordRepository::save)
            .map(medicalRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalRecords");
        return medicalRecordRepository.findAll(pageable).map(medicalRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalRecordDTO> findOne(Long id) {
        log.debug("Request to get MedicalRecord : {}", id);
        return medicalRecordRepository.findById(id).map(medicalRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MedicalRecord : {}", id);
        medicalRecordRepository.deleteById(id);
    }
}
