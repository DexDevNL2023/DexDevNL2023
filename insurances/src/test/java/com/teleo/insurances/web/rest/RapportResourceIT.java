package com.teleo.insurances.web.rest;

import static com.teleo.insurances.domain.RapportAsserts.*;
import static com.teleo.insurances.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.insurances.IntegrationTest;
import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.Rapport;
import com.teleo.insurances.domain.enumeration.RapportType;
import com.teleo.insurances.repository.RapportRepository;
import com.teleo.insurances.service.dto.RapportDTO;
import com.teleo.insurances.service.mapper.RapportMapper;
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
 * Integration tests for the {@link RapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final RapportType DEFAULT_TYPE = RapportType.PERFORMANCE;
    private static final RapportType UPDATED_TYPE = RapportType.PAIEMENT;

    private static final Instant DEFAULT_DATE_GENERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GENERATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private RapportMapper rapportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportMockMvc;

    private Rapport rapport;

    private Rapport insertedRapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .dateGeneration(DEFAULT_DATE_GENERATION)
            .url(DEFAULT_URL);
        return rapport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createUpdatedEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .url(UPDATED_URL);
        return rapport;
    }

    @BeforeEach
    public void initTest() {
        rapport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRapport != null) {
            rapportRepository.delete(insertedRapport);
            insertedRapport = null;
        }
    }

    @Test
    @Transactional
    void createRapport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);
        var returnedRapportDTO = om.readValue(
            restRapportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RapportDTO.class
        );

        // Validate the Rapport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRapport = rapportMapper.toEntity(returnedRapportDTO);
        assertRapportUpdatableFieldsEquals(returnedRapport, getPersistedRapport(returnedRapport));

        insertedRapport = returnedRapport;
    }

    @Test
    @Transactional
    void createRapportWithExistingId() throws Exception {
        // Create the Rapport with an existing ID
        rapport.setId(1L);
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setTitre(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setType(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateGenerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setDateGeneration(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setUrl(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRapports() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get the rapport
        restRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, rapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapport.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateGeneration").value(DEFAULT_DATE_GENERATION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getRapportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        Long id = rapport.getId();

        defaultRapportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRapportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRapportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRapportsByTitreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where titre equals to
        defaultRapportFiltering("titre.equals=" + DEFAULT_TITRE, "titre.equals=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllRapportsByTitreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where titre in
        defaultRapportFiltering("titre.in=" + DEFAULT_TITRE + "," + UPDATED_TITRE, "titre.in=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllRapportsByTitreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where titre is not null
        defaultRapportFiltering("titre.specified=true", "titre.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByTitreContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where titre contains
        defaultRapportFiltering("titre.contains=" + DEFAULT_TITRE, "titre.contains=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllRapportsByTitreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where titre does not contain
        defaultRapportFiltering("titre.doesNotContain=" + UPDATED_TITRE, "titre.doesNotContain=" + DEFAULT_TITRE);
    }

    @Test
    @Transactional
    void getAllRapportsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where description equals to
        defaultRapportFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRapportsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where description in
        defaultRapportFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllRapportsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where description is not null
        defaultRapportFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where description contains
        defaultRapportFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRapportsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where description does not contain
        defaultRapportFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type equals to
        defaultRapportFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type in
        defaultRapportFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type is not null
        defaultRapportFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration equals to
        defaultRapportFiltering("dateGeneration.equals=" + DEFAULT_DATE_GENERATION, "dateGeneration.equals=" + UPDATED_DATE_GENERATION);
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration in
        defaultRapportFiltering(
            "dateGeneration.in=" + DEFAULT_DATE_GENERATION + "," + UPDATED_DATE_GENERATION,
            "dateGeneration.in=" + UPDATED_DATE_GENERATION
        );
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration is not null
        defaultRapportFiltering("dateGeneration.specified=true", "dateGeneration.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where url equals to
        defaultRapportFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllRapportsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where url in
        defaultRapportFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllRapportsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where url is not null
        defaultRapportFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where url contains
        defaultRapportFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllRapportsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where url does not contain
        defaultRapportFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllRapportsByAssuranceIsEqualToSomething() throws Exception {
        Assurance assurance;
        if (TestUtil.findAll(em, Assurance.class).isEmpty()) {
            rapportRepository.saveAndFlush(rapport);
            assurance = AssuranceResourceIT.createEntity(em);
        } else {
            assurance = TestUtil.findAll(em, Assurance.class).get(0);
        }
        em.persist(assurance);
        em.flush();
        rapport.setAssurance(assurance);
        rapportRepository.saveAndFlush(rapport);
        Long assuranceId = assurance.getId();
        // Get all the rapportList where assurance equals to assuranceId
        defaultRapportShouldBeFound("assuranceId.equals=" + assuranceId);

        // Get all the rapportList where assurance equals to (assuranceId + 1)
        defaultRapportShouldNotBeFound("assuranceId.equals=" + (assuranceId + 1));
    }

    private void defaultRapportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRapportShouldBeFound(shouldBeFound);
        defaultRapportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRapportShouldBeFound(String filter) throws Exception {
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRapportShouldNotBeFound(String filter) throws Exception {
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRapport() throws Exception {
        // Get the rapport
        restRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport
        Rapport updatedRapport = rapportRepository.findById(rapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRapport are not directly saved in db
        em.detach(updatedRapport);
        updatedRapport
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .url(UPDATED_URL);
        RapportDTO rapportDTO = rapportMapper.toDto(updatedRapport);

        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRapportToMatchAllProperties(updatedRapport);
    }

    @Test
    @Transactional
    void putNonExistingRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport.titre(UPDATED_TITRE).type(UPDATED_TYPE).url(UPDATED_URL);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRapport, rapport), getPersistedRapport(rapport));
    }

    @Test
    @Transactional
    void fullUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .url(UPDATED_URL);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportUpdatableFieldsEquals(partialUpdatedRapport, getPersistedRapport(partialUpdatedRapport));
    }

    @Test
    @Transactional
    void patchNonExistingRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rapport
        restRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rapportRepository.count();
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

    protected Rapport getPersistedRapport(Rapport rapport) {
        return rapportRepository.findById(rapport.getId()).orElseThrow();
    }

    protected void assertPersistedRapportToMatchAllProperties(Rapport expectedRapport) {
        assertRapportAllPropertiesEquals(expectedRapport, getPersistedRapport(expectedRapport));
    }

    protected void assertPersistedRapportToMatchUpdatableProperties(Rapport expectedRapport) {
        assertRapportAllUpdatablePropertiesEquals(expectedRapport, getPersistedRapport(expectedRapport));
    }
}
