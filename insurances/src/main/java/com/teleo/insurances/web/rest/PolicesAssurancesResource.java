package com.teleo.insurances.web.rest;

import com.teleo.insurances.repository.PolicesAssurancesRepository;
import com.teleo.insurances.service.PolicesAssurancesService;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
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
 * REST controller for managing {@link com.teleo.insurances.domain.PolicesAssurances}.
 */
@RestController
@RequestMapping("/api/polices-assurances")
public class PolicesAssurancesResource {

    private static final Logger log = LoggerFactory.getLogger(PolicesAssurancesResource.class);

    private static final String ENTITY_NAME = "insurancesPolicesAssurances";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolicesAssurancesService policesAssurancesService;

    private final PolicesAssurancesRepository policesAssurancesRepository;

    public PolicesAssurancesResource(
        PolicesAssurancesService policesAssurancesService,
        PolicesAssurancesRepository policesAssurancesRepository
    ) {
        this.policesAssurancesService = policesAssurancesService;
        this.policesAssurancesRepository = policesAssurancesRepository;
    }

    /**
     * {@code POST  /polices-assurances} : Create a new policesAssurances.
     *
     * @param policesAssurancesDTO the policesAssurancesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new policesAssurancesDTO, or with status {@code 400 (Bad Request)} if the policesAssurances has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PolicesAssurancesDTO> createPolicesAssurances(@Valid @RequestBody PolicesAssurancesDTO policesAssurancesDTO)
        throws URISyntaxException {
        log.debug("REST request to save PolicesAssurances : {}", policesAssurancesDTO);
        if (policesAssurancesDTO.getId() != null) {
            throw new BadRequestAlertException("A new policesAssurances cannot already have an ID", ENTITY_NAME, "idexists");
        }
        policesAssurancesDTO = policesAssurancesService.save(policesAssurancesDTO);
        return ResponseEntity.created(new URI("/api/polices-assurances/" + policesAssurancesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, policesAssurancesDTO.getId().toString()))
            .body(policesAssurancesDTO);
    }

    /**
     * {@code PUT  /polices-assurances/:id} : Updates an existing policesAssurances.
     *
     * @param id the id of the policesAssurancesDTO to save.
     * @param policesAssurancesDTO the policesAssurancesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policesAssurancesDTO,
     * or with status {@code 400 (Bad Request)} if the policesAssurancesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the policesAssurancesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PolicesAssurancesDTO> updatePolicesAssurances(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PolicesAssurancesDTO policesAssurancesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PolicesAssurances : {}, {}", id, policesAssurancesDTO);
        if (policesAssurancesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policesAssurancesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policesAssurancesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        policesAssurancesDTO = policesAssurancesService.update(policesAssurancesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, policesAssurancesDTO.getId().toString()))
            .body(policesAssurancesDTO);
    }

    /**
     * {@code PATCH  /polices-assurances/:id} : Partial updates given fields of an existing policesAssurances, field will ignore if it is null
     *
     * @param id the id of the policesAssurancesDTO to save.
     * @param policesAssurancesDTO the policesAssurancesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policesAssurancesDTO,
     * or with status {@code 400 (Bad Request)} if the policesAssurancesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the policesAssurancesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the policesAssurancesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PolicesAssurancesDTO> partialUpdatePolicesAssurances(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PolicesAssurancesDTO policesAssurancesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PolicesAssurances partially : {}, {}", id, policesAssurancesDTO);
        if (policesAssurancesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policesAssurancesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policesAssurancesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PolicesAssurancesDTO> result = policesAssurancesService.partialUpdate(policesAssurancesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, policesAssurancesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /polices-assurances} : get all the policesAssurances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of policesAssurances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PolicesAssurancesDTO>> getAllPolicesAssurances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PolicesAssurances");
        Page<PolicesAssurancesDTO> page = policesAssurancesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /polices-assurances/:id} : get the "id" policesAssurances.
     *
     * @param id the id of the policesAssurancesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the policesAssurancesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PolicesAssurancesDTO> getPolicesAssurances(@PathVariable("id") Long id) {
        log.debug("REST request to get PolicesAssurances : {}", id);
        Optional<PolicesAssurancesDTO> policesAssurancesDTO = policesAssurancesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(policesAssurancesDTO);
    }

    /**
     * {@code DELETE  /polices-assurances/:id} : delete the "id" policesAssurances.
     *
     * @param id the id of the policesAssurancesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicesAssurances(@PathVariable("id") Long id) {
        log.debug("REST request to delete PolicesAssurances : {}", id);
        policesAssurancesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
