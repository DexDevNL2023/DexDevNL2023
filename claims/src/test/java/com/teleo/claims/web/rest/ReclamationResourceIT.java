package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.ReclamationAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.Reclamation;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.domain.enumeration.ReclamationStatus;
import com.teleo.claims.repository.ReclamationRepository;
import com.teleo.claims.service.dto.ReclamationDTO;
import com.teleo.claims.service.mapper.ReclamationMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReclamationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReclamationResourceIT {

    private static final String DEFAULT_NUMERO_RECLAMATION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_RECLAMATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_RECLAMATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RECLAMATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ReclamationStatus DEFAULT_STATUS = ReclamationStatus.EN_COURS;
    private static final ReclamationStatus UPDATED_STATUS = ReclamationStatus.RESOLUE;

    private static final String ENTITY_API_URL = "/api/reclamations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private ReclamationMapper reclamationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReclamationMockMvc;

    private Reclamation reclamation;

    private Reclamation insertedReclamation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .numeroReclamation(DEFAULT_NUMERO_RECLAMATION)
            .dateReclamation(DEFAULT_DATE_RECLAMATION)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return reclamation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createUpdatedEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .numeroReclamation(UPDATED_NUMERO_RECLAMATION)
            .dateReclamation(UPDATED_DATE_RECLAMATION)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        return reclamation;
    }

    @BeforeEach
    public void initTest() {
        reclamation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedReclamation != null) {
            reclamationRepository.delete(insertedReclamation);
            insertedReclamation = null;
        }
    }

    @Test
    @Transactional
    void createReclamation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);
        var returnedReclamationDTO = om.readValue(
            restReclamationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReclamationDTO.class
        );

        // Validate the Reclamation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReclamation = reclamationMapper.toEntity(returnedReclamationDTO);
        assertReclamationUpdatableFieldsEquals(returnedReclamation, getPersistedReclamation(returnedReclamation));

        insertedReclamation = returnedReclamation;
    }

    @Test
    @Transactional
    void createReclamationWithExistingId() throws Exception {
        // Create the Reclamation with an existing ID
        reclamation.setId(1L);
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroReclamationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setNumeroReclamation(null);

        // Create the Reclamation, which fails.
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateReclamationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setDateReclamation(null);

        // Create the Reclamation, which fails.
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setDescription(null);

        // Create the Reclamation, which fails.
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setStatus(null);

        // Create the Reclamation, which fails.
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReclamations() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroReclamation").value(hasItem(DEFAULT_NUMERO_RECLAMATION)))
            .andExpect(jsonPath("$.[*].dateReclamation").value(hasItem(DEFAULT_DATE_RECLAMATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL_ID, reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reclamation.getId().intValue()))
            .andExpect(jsonPath("$.numeroReclamation").value(DEFAULT_NUMERO_RECLAMATION))
            .andExpect(jsonPath("$.dateReclamation").value(DEFAULT_DATE_RECLAMATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getReclamationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        Long id = reclamation.getId();

        defaultReclamationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReclamationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReclamationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroReclamationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numeroReclamation equals to
        defaultReclamationFiltering(
            "numeroReclamation.equals=" + DEFAULT_NUMERO_RECLAMATION,
            "numeroReclamation.equals=" + UPDATED_NUMERO_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroReclamationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numeroReclamation in
        defaultReclamationFiltering(
            "numeroReclamation.in=" + DEFAULT_NUMERO_RECLAMATION + "," + UPDATED_NUMERO_RECLAMATION,
            "numeroReclamation.in=" + UPDATED_NUMERO_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroReclamationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numeroReclamation is not null
        defaultReclamationFiltering("numeroReclamation.specified=true", "numeroReclamation.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroReclamationContainsSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numeroReclamation contains
        defaultReclamationFiltering(
            "numeroReclamation.contains=" + DEFAULT_NUMERO_RECLAMATION,
            "numeroReclamation.contains=" + UPDATED_NUMERO_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByNumeroReclamationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where numeroReclamation does not contain
        defaultReclamationFiltering(
            "numeroReclamation.doesNotContain=" + UPDATED_NUMERO_RECLAMATION,
            "numeroReclamation.doesNotContain=" + DEFAULT_NUMERO_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByDateReclamationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where dateReclamation equals to
        defaultReclamationFiltering(
            "dateReclamation.equals=" + DEFAULT_DATE_RECLAMATION,
            "dateReclamation.equals=" + UPDATED_DATE_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByDateReclamationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where dateReclamation in
        defaultReclamationFiltering(
            "dateReclamation.in=" + DEFAULT_DATE_RECLAMATION + "," + UPDATED_DATE_RECLAMATION,
            "dateReclamation.in=" + UPDATED_DATE_RECLAMATION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByDateReclamationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where dateReclamation is not null
        defaultReclamationFiltering("dateReclamation.specified=true", "dateReclamation.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where description equals to
        defaultReclamationFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReclamationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where description in
        defaultReclamationFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where description is not null
        defaultReclamationFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where description contains
        defaultReclamationFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReclamationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where description does not contain
        defaultReclamationFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllReclamationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where status equals to
        defaultReclamationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReclamationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where status in
        defaultReclamationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReclamationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList where status is not null
        defaultReclamationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReclamationsBySinistreIsEqualToSomething() throws Exception {
        Sinistre sinistre;
        if (TestUtil.findAll(em, Sinistre.class).isEmpty()) {
            reclamationRepository.saveAndFlush(reclamation);
            sinistre = SinistreResourceIT.createEntity(em);
        } else {
            sinistre = TestUtil.findAll(em, Sinistre.class).get(0);
        }
        em.persist(sinistre);
        em.flush();
        reclamation.setSinistre(sinistre);
        reclamationRepository.saveAndFlush(reclamation);
        Long sinistreId = sinistre.getId();
        // Get all the reclamationList where sinistre equals to sinistreId
        defaultReclamationShouldBeFound("sinistreId.equals=" + sinistreId);

        // Get all the reclamationList where sinistre equals to (sinistreId + 1)
        defaultReclamationShouldNotBeFound("sinistreId.equals=" + (sinistreId + 1));
    }

    private void defaultReclamationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReclamationShouldBeFound(shouldBeFound);
        defaultReclamationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReclamationShouldBeFound(String filter) throws Exception {
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroReclamation").value(hasItem(DEFAULT_NUMERO_RECLAMATION)))
            .andExpect(jsonPath("$.[*].dateReclamation").value(hasItem(DEFAULT_DATE_RECLAMATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReclamationShouldNotBeFound(String filter) throws Exception {
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReclamation() throws Exception {
        // Get the reclamation
        restReclamationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation
        Reclamation updatedReclamation = reclamationRepository.findById(reclamation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReclamation are not directly saved in db
        em.detach(updatedReclamation);
        updatedReclamation
            .numeroReclamation(UPDATED_NUMERO_RECLAMATION)
            .dateReclamation(UPDATED_DATE_RECLAMATION)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(updatedReclamation);

        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reclamationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReclamationToMatchAllProperties(updatedReclamation);
    }

    @Test
    @Transactional
    void putNonExistingReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .numeroReclamation(UPDATED_NUMERO_RECLAMATION)
            .dateReclamation(UPDATED_DATE_RECLAMATION)
            .status(UPDATED_STATUS);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReclamationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReclamation, reclamation),
            getPersistedReclamation(reclamation)
        );
    }

    @Test
    @Transactional
    void fullUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .numeroReclamation(UPDATED_NUMERO_RECLAMATION)
            .dateReclamation(UPDATED_DATE_RECLAMATION)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReclamationUpdatableFieldsEquals(partialUpdatedReclamation, getPersistedReclamation(partialUpdatedReclamation));
    }

    @Test
    @Transactional
    void patchNonExistingReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reclamationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reclamationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // Create the Reclamation
        ReclamationDTO reclamationDTO = reclamationMapper.toDto(reclamation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reclamationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reclamation
        restReclamationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reclamation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reclamationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Reclamation getPersistedReclamation(Reclamation reclamation) {
        return reclamationRepository.findById(reclamation.getId()).orElseThrow();
    }

    protected void assertPersistedReclamationToMatchAllProperties(Reclamation expectedReclamation) {
        assertReclamationAllPropertiesEquals(expectedReclamation, getPersistedReclamation(expectedReclamation));
    }

    protected void assertPersistedReclamationToMatchUpdatableProperties(Reclamation expectedReclamation) {
        assertReclamationAllUpdatablePropertiesEquals(expectedReclamation, getPersistedReclamation(expectedReclamation));
    }
}
