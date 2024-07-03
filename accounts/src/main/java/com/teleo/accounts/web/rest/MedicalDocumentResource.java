package com.teleo.accounts.web.rest;

import com.teleo.accounts.domain.criteria.MedicalDocumentCriteria;
import com.teleo.accounts.repository.MedicalDocumentRepository;
import com.teleo.accounts.service.MedicalDocumentService;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.teleo.accounts.domain.MedicalDocument}.
 */
@RestController
@RequestMapping("/api/medical-documents")
public class MedicalDocumentResource {

    private static final Logger log = LoggerFactory.getLogger(MedicalDocumentResource.class);

    private static final String ENTITY_NAME = "medicalDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalDocumentService medicalDocumentService;

    private final MedicalDocumentRepository medicalDocumentRepository;

    public MedicalDocumentResource(MedicalDocumentService medicalDocumentService, MedicalDocumentRepository medicalDocumentRepository) {
        this.medicalDocumentService = medicalDocumentService;
        this.medicalDocumentRepository = medicalDocumentRepository;
    }

    /**
     * {@code POST  /medical-documents} : Create a new medicalDocument.
     *
     * @param medicalDocumentDTO the medicalDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalDocumentDTO, or with status {@code 400 (Bad Request)} if the medicalDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MedicalDocumentDTO>> createMedicalDocument(@Valid @RequestBody MedicalDocumentDTO medicalDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save MedicalDocument : {}", medicalDocumentDTO);
        if (medicalDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return medicalDocumentService
            .save(medicalDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/medical-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /medical-documents/:id} : Updates an existing medicalDocument.
     *
     * @param id the id of the medicalDocumentDTO to save.
     * @param medicalDocumentDTO the medicalDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the medicalDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MedicalDocumentDTO>> updateMedicalDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicalDocumentDTO medicalDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicalDocument : {}, {}", id, medicalDocumentDTO);
        if (medicalDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicalDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return medicalDocumentService
                    .update(medicalDocumentDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /medical-documents/:id} : Partial updates given fields of an existing medicalDocument, field will ignore if it is null
     *
     * @param id the id of the medicalDocumentDTO to save.
     * @param medicalDocumentDTO the medicalDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the medicalDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MedicalDocumentDTO>> partialUpdateMedicalDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicalDocumentDTO medicalDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicalDocument partially : {}, {}", id, medicalDocumentDTO);
        if (medicalDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicalDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MedicalDocumentDTO> result = medicalDocumentService.partialUpdate(medicalDocumentDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /medical-documents} : get all the medicalDocuments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalDocuments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MedicalDocumentDTO>>> getAllMedicalDocuments(
        MedicalDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get MedicalDocuments by criteria: {}", criteria);
        return medicalDocumentService
            .countByCriteria(criteria)
            .zipWith(medicalDocumentService.findByCriteria(criteria, pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /medical-documents/count} : count all the medicalDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countMedicalDocuments(MedicalDocumentCriteria criteria) {
        log.debug("REST request to count MedicalDocuments by criteria: {}", criteria);
        return medicalDocumentService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /medical-documents/:id} : get the "id" medicalDocument.
     *
     * @param id the id of the medicalDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MedicalDocumentDTO>> getMedicalDocument(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicalDocument : {}", id);
        Mono<MedicalDocumentDTO> medicalDocumentDTO = medicalDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalDocumentDTO);
    }

    /**
     * {@code DELETE  /medical-documents/:id} : delete the "id" medicalDocument.
     *
     * @param id the id of the medicalDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMedicalDocument(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicalDocument : {}", id);
        return medicalDocumentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
