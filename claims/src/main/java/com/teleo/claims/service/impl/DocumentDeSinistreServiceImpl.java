package com.teleo.claims.service.impl;

import com.teleo.claims.domain.DocumentDeSinistre;
import com.teleo.claims.repository.DocumentDeSinistreRepository;
import com.teleo.claims.service.DocumentDeSinistreService;
import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import com.teleo.claims.service.mapper.DocumentDeSinistreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.teleo.claims.domain.DocumentDeSinistre}.
 */
@Service
@Transactional
public class DocumentDeSinistreServiceImpl implements DocumentDeSinistreService {

    private static final Logger log = LoggerFactory.getLogger(DocumentDeSinistreServiceImpl.class);

    private final DocumentDeSinistreRepository documentDeSinistreRepository;

    private final DocumentDeSinistreMapper documentDeSinistreMapper;

    public DocumentDeSinistreServiceImpl(
        DocumentDeSinistreRepository documentDeSinistreRepository,
        DocumentDeSinistreMapper documentDeSinistreMapper
    ) {
        this.documentDeSinistreRepository = documentDeSinistreRepository;
        this.documentDeSinistreMapper = documentDeSinistreMapper;
    }

    @Override
    public DocumentDeSinistreDTO save(DocumentDeSinistreDTO documentDeSinistreDTO) {
        log.debug("Request to save DocumentDeSinistre : {}", documentDeSinistreDTO);
        DocumentDeSinistre documentDeSinistre = documentDeSinistreMapper.toEntity(documentDeSinistreDTO);
        documentDeSinistre = documentDeSinistreRepository.save(documentDeSinistre);
        return documentDeSinistreMapper.toDto(documentDeSinistre);
    }

    @Override
    public DocumentDeSinistreDTO update(DocumentDeSinistreDTO documentDeSinistreDTO) {
        log.debug("Request to update DocumentDeSinistre : {}", documentDeSinistreDTO);
        DocumentDeSinistre documentDeSinistre = documentDeSinistreMapper.toEntity(documentDeSinistreDTO);
        documentDeSinistre = documentDeSinistreRepository.save(documentDeSinistre);
        return documentDeSinistreMapper.toDto(documentDeSinistre);
    }

    @Override
    public Optional<DocumentDeSinistreDTO> partialUpdate(DocumentDeSinistreDTO documentDeSinistreDTO) {
        log.debug("Request to partially update DocumentDeSinistre : {}", documentDeSinistreDTO);

        return documentDeSinistreRepository
            .findById(documentDeSinistreDTO.getId())
            .map(existingDocumentDeSinistre -> {
                documentDeSinistreMapper.partialUpdate(existingDocumentDeSinistre, documentDeSinistreDTO);

                return existingDocumentDeSinistre;
            })
            .map(documentDeSinistreRepository::save)
            .map(documentDeSinistreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDeSinistreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentDeSinistres");
        return documentDeSinistreRepository.findAll(pageable).map(documentDeSinistreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDeSinistreDTO> findOne(Long id) {
        log.debug("Request to get DocumentDeSinistre : {}", id);
        return documentDeSinistreRepository.findById(id).map(documentDeSinistreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentDeSinistre : {}", id);
        documentDeSinistreRepository.deleteById(id);
    }
}
