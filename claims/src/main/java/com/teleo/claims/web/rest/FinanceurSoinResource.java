package com.teleo.claims.web.rest;

import com.teleo.claims.repository.FinanceurSoinRepository;
import com.teleo.claims.service.FinanceurSoinService;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
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
 * REST controller for managing {@link com.teleo.claims.domain.FinanceurSoin}.
 */
@RestController
@RequestMapping("/api/financeur-soins")
public class FinanceurSoinResource {

    private static final Logger log = LoggerFactory.getLogger(FinanceurSoinResource.class);

    private static final String ENTITY_NAME = "claimsFinanceurSoin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinanceurSoinService financeurSoinService;

    private final FinanceurSoinRepository financeurSoinRepository;

    public FinanceurSoinResource(FinanceurSoinService financeurSoinService, FinanceurSoinRepository financeurSoinRepository) {
        this.financeurSoinService = financeurSoinService;
        this.financeurSoinRepository = financeurSoinRepository;
    }

    /**
     * {@code POST  /financeur-soins} : Create a new financeurSoin.
     *
     * @param financeurSoinDTO the financeurSoinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financeurSoinDTO, or with status {@code 400 (Bad Request)} if the financeurSoin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FinanceurSoinDTO> createFinanceurSoin(@Valid @RequestBody FinanceurSoinDTO financeurSoinDTO)
        throws URISyntaxException {
        log.debug("REST request to save FinanceurSoin : {}", financeurSoinDTO);
        if (financeurSoinDTO.getId() != null) {
            throw new BadRequestAlertException("A new financeurSoin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financeurSoinDTO = financeurSoinService.save(financeurSoinDTO);
        return ResponseEntity.created(new URI("/api/financeur-soins/" + financeurSoinDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, financeurSoinDTO.getId().toString()))
            .body(financeurSoinDTO);
    }

    /**
     * {@code PUT  /financeur-soins/:id} : Updates an existing financeurSoin.
     *
     * @param id the id of the financeurSoinDTO to save.
     * @param financeurSoinDTO the financeurSoinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financeurSoinDTO,
     * or with status {@code 400 (Bad Request)} if the financeurSoinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financeurSoinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinanceurSoinDTO> updateFinanceurSoin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinanceurSoinDTO financeurSoinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinanceurSoin : {}, {}", id, financeurSoinDTO);
        if (financeurSoinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financeurSoinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financeurSoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financeurSoinDTO = financeurSoinService.update(financeurSoinDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financeurSoinDTO.getId().toString()))
            .body(financeurSoinDTO);
    }

    /**
     * {@code PATCH  /financeur-soins/:id} : Partial updates given fields of an existing financeurSoin, field will ignore if it is null
     *
     * @param id the id of the financeurSoinDTO to save.
     * @param financeurSoinDTO the financeurSoinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financeurSoinDTO,
     * or with status {@code 400 (Bad Request)} if the financeurSoinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financeurSoinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financeurSoinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinanceurSoinDTO> partialUpdateFinanceurSoin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinanceurSoinDTO financeurSoinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinanceurSoin partially : {}, {}", id, financeurSoinDTO);
        if (financeurSoinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financeurSoinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financeurSoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinanceurSoinDTO> result = financeurSoinService.partialUpdate(financeurSoinDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financeurSoinDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /financeur-soins} : get all the financeurSoins.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financeurSoins in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FinanceurSoinDTO>> getAllFinanceurSoins(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FinanceurSoins");
        Page<FinanceurSoinDTO> page = financeurSoinService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financeur-soins/:id} : get the "id" financeurSoin.
     *
     * @param id the id of the financeurSoinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financeurSoinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinanceurSoinDTO> getFinanceurSoin(@PathVariable("id") Long id) {
        log.debug("REST request to get FinanceurSoin : {}", id);
        Optional<FinanceurSoinDTO> financeurSoinDTO = financeurSoinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financeurSoinDTO);
    }

    /**
     * {@code DELETE  /financeur-soins/:id} : delete the "id" financeurSoin.
     *
     * @param id the id of the financeurSoinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinanceurSoin(@PathVariable("id") Long id) {
        log.debug("REST request to delete FinanceurSoin : {}", id);
        financeurSoinService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
