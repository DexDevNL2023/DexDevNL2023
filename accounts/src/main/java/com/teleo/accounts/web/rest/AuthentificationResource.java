package com.teleo.accounts.web.rest;

import com.teleo.accounts.repository.AuthentificationRepository;
import com.teleo.accounts.service.AuthentificationService;
import com.teleo.accounts.service.dto.AuthentificationDTO;
import com.teleo.accounts.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.teleo.accounts.domain.Authentification}.
 */
@RestController
@RequestMapping("/api/authentifications")
public class AuthentificationResource {

    private static final Logger log = LoggerFactory.getLogger(AuthentificationResource.class);

    private static final String ENTITY_NAME = "accountsAuthentification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthentificationService authentificationService;

    private final AuthentificationRepository authentificationRepository;

    public AuthentificationResource(
        AuthentificationService authentificationService,
        AuthentificationRepository authentificationRepository
    ) {
        this.authentificationService = authentificationService;
        this.authentificationRepository = authentificationRepository;
    }

    /**
     * {@code POST  /authentifications} : Create a new authentification.
     *
     * @param authentificationDTO the authentificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authentificationDTO, or with status {@code 400 (Bad Request)} if the authentification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AuthentificationDTO> createAuthentification(@Valid @RequestBody AuthentificationDTO authentificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Authentification : {}", authentificationDTO);
        if (authentificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new authentification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        authentificationDTO = authentificationService.save(authentificationDTO);
        return ResponseEntity.created(new URI("/api/authentifications/" + authentificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authentificationDTO.getId().toString()))
            .body(authentificationDTO);
    }

    /**
     * {@code PUT  /authentifications/:id} : Updates an existing authentification.
     *
     * @param id the id of the authentificationDTO to save.
     * @param authentificationDTO the authentificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authentificationDTO,
     * or with status {@code 400 (Bad Request)} if the authentificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authentificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthentificationDTO> updateAuthentification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuthentificationDTO authentificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Authentification : {}, {}", id, authentificationDTO);
        if (authentificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authentificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        authentificationDTO = authentificationService.update(authentificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authentificationDTO.getId().toString()))
            .body(authentificationDTO);
    }

    /**
     * {@code PATCH  /authentifications/:id} : Partial updates given fields of an existing authentification, field will ignore if it is null
     *
     * @param id the id of the authentificationDTO to save.
     * @param authentificationDTO the authentificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authentificationDTO,
     * or with status {@code 400 (Bad Request)} if the authentificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authentificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authentificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthentificationDTO> partialUpdateAuthentification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuthentificationDTO authentificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authentification partially : {}, {}", id, authentificationDTO);
        if (authentificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authentificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthentificationDTO> result = authentificationService.partialUpdate(authentificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authentificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authentifications} : get all the authentifications.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authentifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AuthentificationDTO>> getAllAuthentifications(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("utilisateur-is-null".equals(filter)) {
            log.debug("REST request to get all Authentifications where utilisateur is null");
            return new ResponseEntity<>(authentificationService.findAllWhereUtilisateurIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Authentifications");
        Page<AuthentificationDTO> page = authentificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authentifications/:id} : get the "id" authentification.
     *
     * @param id the id of the authentificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authentificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthentificationDTO> getAuthentification(@PathVariable("id") Long id) {
        log.debug("REST request to get Authentification : {}", id);
        Optional<AuthentificationDTO> authentificationDTO = authentificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authentificationDTO);
    }

    /**
     * {@code DELETE  /authentifications/:id} : delete the "id" authentification.
     *
     * @param id the id of the authentificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthentification(@PathVariable("id") Long id) {
        log.debug("REST request to delete Authentification : {}", id);
        authentificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
