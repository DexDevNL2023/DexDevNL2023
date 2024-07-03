package com.teleo.claims.web.rest;

import com.teleo.claims.repository.PrestationSoinRepository;
import com.teleo.claims.service.PrestationSoinQueryService;
import com.teleo.claims.service.PrestationSoinService;
import com.teleo.claims.service.criteria.PrestationSoinCriteria;
import com.teleo.claims.service.dto.PrestationSoinDTO;
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
 * REST controller for managing {@link com.teleo.claims.domain.PrestationSoin}.
 */
@RestController
@RequestMapping("/api/prestation-soins")
public class PrestationSoinResource {

    private static final Logger log = LoggerFactory.getLogger(PrestationSoinResource.class);

    private static final String ENTITY_NAME = "claimsPrestationSoin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestationSoinService prestationSoinService;

    private final PrestationSoinRepository prestationSoinRepository;

    private final PrestationSoinQueryService prestationSoinQueryService;

    public PrestationSoinResource(
        PrestationSoinService prestationSoinService,
        PrestationSoinRepository prestationSoinRepository,
        PrestationSoinQueryService prestationSoinQueryService
    ) {
        this.prestationSoinService = prestationSoinService;
        this.prestationSoinRepository = prestationSoinRepository;
        this.prestationSoinQueryService = prestationSoinQueryService;
    }

    /**
     * {@code POST  /prestation-soins} : Create a new prestationSoin.
     *
     * @param prestationSoinDTO the prestationSoinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestationSoinDTO, or with status {@code 400 (Bad Request)} if the prestationSoin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrestationSoinDTO> createPrestationSoin(@Valid @RequestBody PrestationSoinDTO prestationSoinDTO)
        throws URISyntaxException {
        log.debug("REST request to save PrestationSoin : {}", prestationSoinDTO);
        if (prestationSoinDTO.getId() != null) {
            throw new BadRequestAlertException("A new prestationSoin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prestationSoinDTO = prestationSoinService.save(prestationSoinDTO);
        return ResponseEntity.created(new URI("/api/prestation-soins/" + prestationSoinDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prestationSoinDTO.getId().toString()))
            .body(prestationSoinDTO);
    }

    /**
     * {@code PUT  /prestation-soins/:id} : Updates an existing prestationSoin.
     *
     * @param id the id of the prestationSoinDTO to save.
     * @param prestationSoinDTO the prestationSoinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestationSoinDTO,
     * or with status {@code 400 (Bad Request)} if the prestationSoinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestationSoinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrestationSoinDTO> updatePrestationSoin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrestationSoinDTO prestationSoinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrestationSoin : {}, {}", id, prestationSoinDTO);
        if (prestationSoinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestationSoinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestationSoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prestationSoinDTO = prestationSoinService.update(prestationSoinDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestationSoinDTO.getId().toString()))
            .body(prestationSoinDTO);
    }

    /**
     * {@code PATCH  /prestation-soins/:id} : Partial updates given fields of an existing prestationSoin, field will ignore if it is null
     *
     * @param id the id of the prestationSoinDTO to save.
     * @param prestationSoinDTO the prestationSoinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestationSoinDTO,
     * or with status {@code 400 (Bad Request)} if the prestationSoinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prestationSoinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestationSoinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrestationSoinDTO> partialUpdatePrestationSoin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrestationSoinDTO prestationSoinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrestationSoin partially : {}, {}", id, prestationSoinDTO);
        if (prestationSoinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestationSoinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestationSoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrestationSoinDTO> result = prestationSoinService.partialUpdate(prestationSoinDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestationSoinDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prestation-soins} : get all the prestationSoins.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestationSoins in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PrestationSoinDTO>> getAllPrestationSoins(
        PrestationSoinCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PrestationSoins by criteria: {}", criteria);

        Page<PrestationSoinDTO> page = prestationSoinQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestation-soins/count} : count all the prestationSoins.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPrestationSoins(PrestationSoinCriteria criteria) {
        log.debug("REST request to count PrestationSoins by criteria: {}", criteria);
        return ResponseEntity.ok().body(prestationSoinQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prestation-soins/:id} : get the "id" prestationSoin.
     *
     * @param id the id of the prestationSoinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestationSoinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestationSoinDTO> getPrestationSoin(@PathVariable("id") Long id) {
        log.debug("REST request to get PrestationSoin : {}", id);
        Optional<PrestationSoinDTO> prestationSoinDTO = prestationSoinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestationSoinDTO);
    }

    /**
     * {@code DELETE  /prestation-soins/:id} : delete the "id" prestationSoin.
     *
     * @param id the id of the prestationSoinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestationSoin(@PathVariable("id") Long id) {
        log.debug("REST request to delete PrestationSoin : {}", id);
        prestationSoinService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
