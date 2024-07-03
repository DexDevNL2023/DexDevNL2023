package com.teleo.accounts.web.rest;

import com.teleo.accounts.domain.criteria.MedicalRecordCriteria;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.service.MedicalRecordService;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
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
 * REST controller for managing {@link com.teleo.accounts.domain.MedicalRecord}.
 */
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordResource {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordResource.class);

    private static final String ENTITY_NAME = "medicalRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalRecordService medicalRecordService;

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordResource(MedicalRecordService medicalRecordService, MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordService = medicalRecordService;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * {@code POST  /medical-records} : Create a new medicalRecord.
     *
     * @param medicalRecordDTO the medicalRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalRecordDTO, or with status {@code 400 (Bad Request)} if the medicalRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MedicalRecordDTO>> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save MedicalRecord : {}", medicalRecordDTO);
        if (medicalRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return medicalRecordService
            .save(medicalRecordDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/medical-records/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /medical-records/:id} : Updates an existing medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to save.
     * @param medicalRecordDTO the medicalRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalRecordDTO,
     * or with status {@code 400 (Bad Request)} if the medicalRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MedicalRecordDTO>> updateMedicalRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicalRecordDTO medicalRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicalRecord : {}, {}", id, medicalRecordDTO);
        if (medicalRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicalRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return medicalRecordService
                    .update(medicalRecordDTO)
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
     * {@code PATCH  /medical-records/:id} : Partial updates given fields of an existing medicalRecord, field will ignore if it is null
     *
     * @param id the id of the medicalRecordDTO to save.
     * @param medicalRecordDTO the medicalRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalRecordDTO,
     * or with status {@code 400 (Bad Request)} if the medicalRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MedicalRecordDTO>> partialUpdateMedicalRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicalRecordDTO medicalRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicalRecord partially : {}, {}", id, medicalRecordDTO);
        if (medicalRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicalRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MedicalRecordDTO> result = medicalRecordService.partialUpdate(medicalRecordDTO);

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
     * {@code GET  /medical-records} : get all the medicalRecords.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalRecords in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MedicalRecordDTO>>> getAllMedicalRecords(
        MedicalRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get MedicalRecords by criteria: {}", criteria);
        return medicalRecordService
            .countByCriteria(criteria)
            .zipWith(medicalRecordService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /medical-records/count} : count all the medicalRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countMedicalRecords(MedicalRecordCriteria criteria) {
        log.debug("REST request to count MedicalRecords by criteria: {}", criteria);
        return medicalRecordService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /medical-records/:id} : get the "id" medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MedicalRecordDTO>> getMedicalRecord(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicalRecord : {}", id);
        Mono<MedicalRecordDTO> medicalRecordDTO = medicalRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalRecordDTO);
    }

    /**
     * {@code DELETE  /medical-records/:id} : delete the "id" medicalRecord.
     *
     * @param id the id of the medicalRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMedicalRecord(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicalRecord : {}", id);
        return medicalRecordService
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
