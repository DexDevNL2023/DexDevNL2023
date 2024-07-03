package com.teleo.accounts.service.impl;

import com.teleo.accounts.domain.criteria.MedicalDocumentCriteria;
import com.teleo.accounts.repository.MedicalDocumentRepository;
import com.teleo.accounts.service.MedicalDocumentService;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.service.mapper.MedicalDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<MedicalDocumentDTO> save(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to save MedicalDocument : {}", medicalDocumentDTO);
        return medicalDocumentRepository.save(medicalDocumentMapper.toEntity(medicalDocumentDTO)).map(medicalDocumentMapper::toDto);
    }

    @Override
    public Mono<MedicalDocumentDTO> update(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to update MedicalDocument : {}", medicalDocumentDTO);
        return medicalDocumentRepository.save(medicalDocumentMapper.toEntity(medicalDocumentDTO)).map(medicalDocumentMapper::toDto);
    }

    @Override
    public Mono<MedicalDocumentDTO> partialUpdate(MedicalDocumentDTO medicalDocumentDTO) {
        log.debug("Request to partially update MedicalDocument : {}", medicalDocumentDTO);

        return medicalDocumentRepository
            .findById(medicalDocumentDTO.getId())
            .map(existingMedicalDocument -> {
                medicalDocumentMapper.partialUpdate(existingMedicalDocument, medicalDocumentDTO);

                return existingMedicalDocument;
            })
            .flatMap(medicalDocumentRepository::save)
            .map(medicalDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MedicalDocumentDTO> findByCriteria(MedicalDocumentCriteria criteria, Pageable pageable) {
        log.debug("Request to get all MedicalDocuments by Criteria");
        return medicalDocumentRepository.findByCriteria(criteria, pageable).map(medicalDocumentMapper::toDto);
    }

    /**
     * Find the count of medicalDocuments by criteria.
     * @param criteria filtering criteria
     * @return the count of medicalDocuments
     */
    public Mono<Long> countByCriteria(MedicalDocumentCriteria criteria) {
        log.debug("Request to get the count of all MedicalDocuments by Criteria");
        return medicalDocumentRepository.countByCriteria(criteria);
    }

    public Mono<Long> countAll() {
        return medicalDocumentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MedicalDocumentDTO> findOne(Long id) {
        log.debug("Request to get MedicalDocument : {}", id);
        return medicalDocumentRepository.findById(id).map(medicalDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete MedicalDocument : {}", id);
        return medicalDocumentRepository.deleteById(id);
    }
}
