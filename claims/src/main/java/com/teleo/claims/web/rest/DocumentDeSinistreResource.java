package com.teleo.claims.web.rest;

import com.teleo.claims.repository.DocumentDeSinistreRepository;
import com.teleo.claims.service.DocumentDeSinistreService;
import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import com.teleo.claims.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.teleo.claims.domain.DocumentDeSinistre}.
 */
@RestController
@RequestMapping("/api/document-de-sinistres")
public class DocumentDeSinistreResource {

    private static final Logger log = LoggerFactory.getLogger(DocumentDeSinistreResource.class);

    private static final String ENTITY_NAME = "claimsDocumentDeSinistre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentDeSinistreService documentDeSinistreService;

    private final DocumentDeSinistreRepository documentDeSinistreRepository;

    public DocumentDeSinistreResource(
        DocumentDeSinistreService documentDeSinistreService,
        DocumentDeSinistreRepository documentDeSinistreRepository
    ) {
        this.documentDeSinistreService = documentDeSinistreService;
        this.documentDeSinistreRepository = documentDeSinistreRepository;
    }

    /**
     * {@code POST  /document-de-sinistres} : Create a new documentDeSinistre.
     *
     * @param documentDeSinistreDTO the documentDeSinistreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentDeSinistreDTO, or with status {@code 400 (Bad Request)} if the documentDeSinistre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentDeSinistreDTO> createDocumentDeSinistre(@Valid @RequestBody DocumentDeSinistreDTO documentDeSinistreDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentDeSinistre : {}", documentDeSinistreDTO);
        if (documentDeSinistreDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentDeSinistre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentDeSinistreDTO = documentDeSinistreService.save(documentDeSinistreDTO);
        return ResponseEntity.created(new URI("/api/document-de-sinistres/" + documentDeSinistreDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentDeSinistreDTO.getId().toString()))
            .body(documentDeSinistreDTO);
    }

    /**
     * {@code PUT  /document-de-sinistres/:id} : Updates an existing documentDeSinistre.
     *
     * @param id the id of the documentDeSinistreDTO to save.
     * @param documentDeSinistreDTO the documentDeSinistreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDeSinistreDTO,
     * or with status {@code 400 (Bad Request)} if the documentDeSinistreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentDeSinistreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDeSinistreDTO> updateDocumentDeSinistre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentDeSinistreDTO documentDeSinistreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentDeSinistre : {}, {}", id, documentDeSinistreDTO);
        if (documentDeSinistreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDeSinistreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentDeSinistreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentDeSinistreDTO = documentDeSinistreService.update(documentDeSinistreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDeSinistreDTO.getId().toString()))
            .body(documentDeSinistreDTO);
    }

    /**
     * {@code PATCH  /document-de-sinistres/:id} : Partial updates given fields of an existing documentDeSinistre, field will ignore if it is null
     *
     * @param id the id of the documentDeSinistreDTO to save.
     * @param documentDeSinistreDTO the documentDeSinistreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDeSinistreDTO,
     * or with status {@code 400 (Bad Request)} if the documentDeSinistreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentDeSinistreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentDeSinistreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentDeSinistreDTO> partialUpdateDocumentDeSinistre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentDeSinistreDTO documentDeSinistreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentDeSinistre partially : {}, {}", id, documentDeSinistreDTO);
        if (documentDeSinistreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDeSinistreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentDeSinistreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentDeSinistreDTO> result = documentDeSinistreService.partialUpdate(documentDeSinistreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDeSinistreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-de-sinistres} : get all the documentDeSinistres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentDeSinistres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentDeSinistreDTO>> getAllDocumentDeSinistres(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DocumentDeSinistres");
        Page<DocumentDeSinistreDTO> page = documentDeSinistreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-de-sinistres/:id} : get the "id" documentDeSinistre.
     *
     * @param id the id of the documentDeSinistreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDeSinistreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDeSinistreDTO> getDocumentDeSinistre(@PathVariable("id") Long id) {
        log.debug("REST request to get DocumentDeSinistre : {}", id);
        Optional<DocumentDeSinistreDTO> documentDeSinistreDTO = documentDeSinistreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentDeSinistreDTO);
    }

    /**
     * {@code DELETE  /document-de-sinistres/:id} : delete the "id" documentDeSinistre.
     *
     * @param id the id of the documentDeSinistreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentDeSinistre(@PathVariable("id") Long id) {
        log.debug("REST request to delete DocumentDeSinistre : {}", id);
        documentDeSinistreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
