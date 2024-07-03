package com.teleo.insurances.web.rest;

import com.teleo.insurances.repository.GarantieRepository;
import com.teleo.insurances.service.GarantieQueryService;
import com.teleo.insurances.service.GarantieService;
import com.teleo.insurances.service.criteria.GarantieCriteria;
import com.teleo.insurances.service.dto.GarantieDTO;
import com.teleo.insurances.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.teleo.insurances.domain.Garantie}.
 */
@RestController
@RequestMapping("/api/garanties")
public class GarantieResource {

    private static final Logger log = LoggerFactory.getLogger(GarantieResource.class);

    private static final String ENTITY_NAME = "insurancesGarantie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GarantieService garantieService;

    private final GarantieRepository garantieRepository;

    private final GarantieQueryService garantieQueryService;

    public GarantieResource(
        GarantieService garantieService,
        GarantieRepository garantieRepository,
        GarantieQueryService garantieQueryService
    ) {
        this.garantieService = garantieService;
        this.garantieRepository = garantieRepository;
        this.garantieQueryService = garantieQueryService;
    }

    /**
     * {@code POST  /garanties} : Create a new garantie.
     *
     * @param garantieDTO the garantieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new garantieDTO, or with status {@code 400 (Bad Request)} if the garantie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GarantieDTO> createGarantie(@Valid @RequestBody GarantieDTO garantieDTO) throws URISyntaxException {
        log.debug("REST request to save Garantie : {}", garantieDTO);
        if (garantieDTO.getId() != null) {
            throw new BadRequestAlertException("A new garantie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        garantieDTO = garantieService.save(garantieDTO);
        return ResponseEntity.created(new URI("/api/garanties/" + garantieDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, garantieDTO.getId().toString()))
            .body(garantieDTO);
    }

    /**
     * {@code PUT  /garanties/:id} : Updates an existing garantie.
     *
     * @param id the id of the garantieDTO to save.
     * @param garantieDTO the garantieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated garantieDTO,
     * or with status {@code 400 (Bad Request)} if the garantieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the garantieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GarantieDTO> updateGarantie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GarantieDTO garantieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Garantie : {}, {}", id, garantieDTO);
        if (garantieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, garantieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!garantieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        garantieDTO = garantieService.update(garantieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, garantieDTO.getId().toString()))
            .body(garantieDTO);
    }

    /**
     * {@code PATCH  /garanties/:id} : Partial updates given fields of an existing garantie, field will ignore if it is null
     *
     * @param id the id of the garantieDTO to save.
     * @param garantieDTO the garantieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated garantieDTO,
     * or with status {@code 400 (Bad Request)} if the garantieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the garantieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the garantieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GarantieDTO> partialUpdateGarantie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GarantieDTO garantieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Garantie partially : {}, {}", id, garantieDTO);
        if (garantieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, garantieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!garantieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GarantieDTO> result = garantieService.partialUpdate(garantieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, garantieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /garanties} : get all the garanties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of garanties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GarantieDTO>> getAllGaranties(
        GarantieCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Garanties by criteria: {}", criteria);

        Page<GarantieDTO> page = garantieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /garanties/count} : count all the garanties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGaranties(GarantieCriteria criteria) {
        log.debug("REST request to count Garanties by criteria: {}", criteria);
        return ResponseEntity.ok().body(garantieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /garanties/:id} : get the "id" garantie.
     *
     * @param id the id of the garantieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the garantieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GarantieDTO> getGarantie(@PathVariable("id") Long id) {
        log.debug("REST request to get Garantie : {}", id);
        Optional<GarantieDTO> garantieDTO = garantieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(garantieDTO);
    }

    /**
     * {@code DELETE  /garanties/:id} : delete the "id" garantie.
     *
     * @param id the id of the garantieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarantie(@PathVariable("id") Long id) {
        log.debug("REST request to delete Garantie : {}", id);
        garantieService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
