package com.teleo.claims.web.rest;

import com.teleo.claims.repository.SinistreRepository;
import com.teleo.claims.service.SinistreService;
import com.teleo.claims.service.dto.SinistreDTO;
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
 * REST controller for managing {@link com.teleo.claims.domain.Sinistre}.
 */
@RestController
@RequestMapping("/api/sinistres")
public class SinistreResource {

    private static final Logger log = LoggerFactory.getLogger(SinistreResource.class);

    private static final String ENTITY_NAME = "claimsSinistre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SinistreService sinistreService;

    private final SinistreRepository sinistreRepository;

    public SinistreResource(SinistreService sinistreService, SinistreRepository sinistreRepository) {
        this.sinistreService = sinistreService;
        this.sinistreRepository = sinistreRepository;
    }

    /**
     * {@code POST  /sinistres} : Create a new sinistre.
     *
     * @param sinistreDTO the sinistreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sinistreDTO, or with status {@code 400 (Bad Request)} if the sinistre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SinistreDTO> createSinistre(@Valid @RequestBody SinistreDTO sinistreDTO) throws URISyntaxException {
        log.debug("REST request to save Sinistre : {}", sinistreDTO);
        if (sinistreDTO.getId() != null) {
            throw new BadRequestAlertException("A new sinistre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sinistreDTO = sinistreService.save(sinistreDTO);
        return ResponseEntity.created(new URI("/api/sinistres/" + sinistreDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sinistreDTO.getId().toString()))
            .body(sinistreDTO);
    }

    /**
     * {@code PUT  /sinistres/:id} : Updates an existing sinistre.
     *
     * @param id the id of the sinistreDTO to save.
     * @param sinistreDTO the sinistreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sinistreDTO,
     * or with status {@code 400 (Bad Request)} if the sinistreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sinistreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SinistreDTO> updateSinistre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SinistreDTO sinistreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sinistre : {}, {}", id, sinistreDTO);
        if (sinistreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sinistreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sinistreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sinistreDTO = sinistreService.update(sinistreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sinistreDTO.getId().toString()))
            .body(sinistreDTO);
    }

    /**
     * {@code PATCH  /sinistres/:id} : Partial updates given fields of an existing sinistre, field will ignore if it is null
     *
     * @param id the id of the sinistreDTO to save.
     * @param sinistreDTO the sinistreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sinistreDTO,
     * or with status {@code 400 (Bad Request)} if the sinistreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sinistreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sinistreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SinistreDTO> partialUpdateSinistre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SinistreDTO sinistreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sinistre partially : {}, {}", id, sinistreDTO);
        if (sinistreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sinistreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sinistreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SinistreDTO> result = sinistreService.partialUpdate(sinistreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sinistreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sinistres} : get all the sinistres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sinistres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SinistreDTO>> getAllSinistres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Sinistres");
        Page<SinistreDTO> page = sinistreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sinistres/:id} : get the "id" sinistre.
     *
     * @param id the id of the sinistreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sinistreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SinistreDTO> getSinistre(@PathVariable("id") Long id) {
        log.debug("REST request to get Sinistre : {}", id);
        Optional<SinistreDTO> sinistreDTO = sinistreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sinistreDTO);
    }

    /**
     * {@code DELETE  /sinistres/:id} : delete the "id" sinistre.
     *
     * @param id the id of the sinistreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSinistre(@PathVariable("id") Long id) {
        log.debug("REST request to delete Sinistre : {}", id);
        sinistreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
