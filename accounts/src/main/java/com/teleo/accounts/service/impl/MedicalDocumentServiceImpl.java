package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.MedicalDocument;
import com.teleo.accounts.repository.MedicalDocumentRepository;
import com.teleo.accounts.service.MedicalDocumentService;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.service.mapper.MedicalDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.accounts.domain.MedicalDocument}.
 */
@Service
@Transactional
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private static final Logger log = LoggerFactory.getLogger(MedicalDocumentServiceImpl.class);

    private final MedicalDocumentRepository medicalDocumentRepository;

    private final MedicalDocumentMapper medicalDocumentMapper;

    public MedicalDocumentServiceImpl(MedicalDocumentRepository medicalDocumentRepository, MedicalDocumentMapper medicalDocumentMapper) {
        this.medicalDocumentRepository = medicalDocumentRepository;
        this.medicalDocumentMapper = medicalDocumentMapper;
    }

    @Override
    public MedicalDocumentDTO save(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to save MedicalDocument : {}", medicalDocumentDTO);
        MedicalDocument medicalDocument = medicalDocumentMapper.toEntity(medicalDocumentDTO);
        medicalDocument = medicalDocumentRepository.save(medicalDocument);
        return medicalDocumentMapper.toDto(medicalDocument);
    }

    @Override
    public MedicalDocumentDTO update(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to update MedicalDocument : {}", medicalDocumentDTO);
        MedicalDocument medicalDocument = medicalDocumentMapper.toEntity(medicalDocumentDTO);
        medicalDocument = medicalDocumentRepository.save(medicalDocument);
        return medicalDocumentMapper.toDto(medicalDocument);
    }

    @Override
    public Optional<MedicalDocumentDTO> partialUpdate(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to partially update MedicalDocument : {}", medicalDocumentDTO);

        return medicalDocumentRepository
            .findById(medicalDocumentDTO.getId())
            .map(existingMedicalDocument -> {
                medicalDocumentMapper.partialUpdate(existingMedicalDocument, medicalDocumentDTO);

                return existingMedicalDocument;
            })
            .map(medicalDocumentRepository::save)
            .map(medicalDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalDocuments");
        return medicalDocumentRepository.findAll(pageable).map(medicalDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalDocumentDTO> findOne(Long id) {
        log.debug("Request to get MedicalDocument : {}", id);
        return medicalDocumentRepository.findById(id).map(medicalDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MedicalDocument : {}", id);
        medicalDocumentRepository.deleteById(id);
    }
}
