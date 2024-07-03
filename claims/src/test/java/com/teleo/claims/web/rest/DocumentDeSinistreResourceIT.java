package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.DocumentDeSinistreAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.DocumentDeSinistre;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.repository.DocumentDeSinistreRepository;
import com.teleo.claims.service.dto.DocumentDeSinistreDTO;
import com.teleo.claims.service.mapper.DocumentDeSinistreMapper;
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
 * Integration tests for the {@link DocumentDeSinistreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentDeSinistreResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TELECHARGEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TELECHARGEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-de-sinistres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentDeSinistreRepository documentDeSinistreRepository;

    @Autowired
    private DocumentDeSinistreMapper documentDeSinistreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentDeSinistreMockMvc;

    private DocumentDeSinistre documentDeSinistre;

    private DocumentDeSinistre insertedDocumentDeSinistre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentDeSinistre createEntity(EntityManager em) {
        DocumentDeSinistre documentDeSinistre = new DocumentDeSinistre()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .dateTelechargement(DEFAULT_DATE_TELECHARGEMENT);
        return documentDeSinistre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentDeSinistre createUpdatedEntity(EntityManager em) {
        DocumentDeSinistre documentDeSinistre = new DocumentDeSinistre()
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .dateTelechargement(UPDATED_DATE_TELECHARGEMENT);
        return documentDeSinistre;
    }

    @BeforeEach
    public void initTest() {
        documentDeSinistre = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDocumentDeSinistre != null) {
            documentDeSinistreRepository.delete(insertedDocumentDeSinistre);
            insertedDocumentDeSinistre = null;
        }
    }

    @Test
    @Transactional
    void createDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);
        var returnedDocumentDeSinistreDTO = om.readValue(
            restDocumentDeSinistreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentDeSinistreDTO.class
        );

        // Validate the DocumentDeSinistre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentDeSinistre = documentDeSinistreMapper.toEntity(returnedDocumentDeSinistreDTO);
        assertDocumentDeSinistreUpdatableFieldsEquals(
            returnedDocumentDeSinistre,
            getPersistedDocumentDeSinistre(returnedDocumentDeSinistre)
        );

        insertedDocumentDeSinistre = returnedDocumentDeSinistre;
    }

    @Test
    @Transactional
    void createDocumentDeSinistreWithExistingId() throws Exception {
        // Create the DocumentDeSinistre with an existing ID
        documentDeSinistre.setId(1L);
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentDeSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentDeSinistre.setNom(null);

        // Create the DocumentDeSinistre, which fails.
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        restDocumentDeSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentDeSinistre.setType(null);

        // Create the DocumentDeSinistre, which fails.
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        restDocumentDeSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentDeSinistre.setUrl(null);

        // Create the DocumentDeSinistre, which fails.
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        restDocumentDeSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTelechargementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentDeSinistre.setDateTelechargement(null);

        // Create the DocumentDeSinistre, which fails.
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        restDocumentDeSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistres() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentDeSinistre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].dateTelechargement").value(hasItem(DEFAULT_DATE_TELECHARGEMENT.toString())));
    }

    @Test
    @Transactional
    void getDocumentDeSinistre() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get the documentDeSinistre
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL_ID, documentDeSinistre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentDeSinistre.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.dateTelechargement").value(DEFAULT_DATE_TELECHARGEMENT.toString()));
    }

    @Test
    @Transactional
    void getDocumentDeSinistresByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        Long id = documentDeSinistre.getId();

        defaultDocumentDeSinistreFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentDeSinistreFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentDeSinistreFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where nom equals to
        defaultDocumentDeSinistreFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where nom in
        defaultDocumentDeSinistreFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where nom is not null
        defaultDocumentDeSinistreFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where nom contains
        defaultDocumentDeSinistreFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where nom does not contain
        defaultDocumentDeSinistreFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where type equals to
        defaultDocumentDeSinistreFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where type in
        defaultDocumentDeSinistreFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where type is not null
        defaultDocumentDeSinistreFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where type contains
        defaultDocumentDeSinistreFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where type does not contain
        defaultDocumentDeSinistreFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where url equals to
        defaultDocumentDeSinistreFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where url in
        defaultDocumentDeSinistreFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where url is not null
        defaultDocumentDeSinistreFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where url contains
        defaultDocumentDeSinistreFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where url does not contain
        defaultDocumentDeSinistreFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByDateTelechargementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where dateTelechargement equals to
        defaultDocumentDeSinistreFiltering(
            "dateTelechargement.equals=" + DEFAULT_DATE_TELECHARGEMENT,
            "dateTelechargement.equals=" + UPDATED_DATE_TELECHARGEMENT
        );
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByDateTelechargementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where dateTelechargement in
        defaultDocumentDeSinistreFiltering(
            "dateTelechargement.in=" + DEFAULT_DATE_TELECHARGEMENT + "," + UPDATED_DATE_TELECHARGEMENT,
            "dateTelechargement.in=" + UPDATED_DATE_TELECHARGEMENT
        );
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresByDateTelechargementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        // Get all the documentDeSinistreList where dateTelechargement is not null
        defaultDocumentDeSinistreFiltering("dateTelechargement.specified=true", "dateTelechargement.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentDeSinistresBySinistreIsEqualToSomething() throws Exception {
        Sinistre sinistre;
        if (TestUtil.findAll(em, Sinistre.class).isEmpty()) {
            documentDeSinistreRepository.saveAndFlush(documentDeSinistre);
            sinistre = SinistreResourceIT.createEntity(em);
        } else {
            sinistre = TestUtil.findAll(em, Sinistre.class).get(0);
        }
        em.persist(sinistre);
        em.flush();
        documentDeSinistre.setSinistre(sinistre);
        documentDeSinistreRepository.saveAndFlush(documentDeSinistre);
        Long sinistreId = sinistre.getId();
        // Get all the documentDeSinistreList where sinistre equals to sinistreId
        defaultDocumentDeSinistreShouldBeFound("sinistreId.equals=" + sinistreId);

        // Get all the documentDeSinistreList where sinistre equals to (sinistreId + 1)
        defaultDocumentDeSinistreShouldNotBeFound("sinistreId.equals=" + (sinistreId + 1));
    }

    private void defaultDocumentDeSinistreFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentDeSinistreShouldBeFound(shouldBeFound);
        defaultDocumentDeSinistreShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentDeSinistreShouldBeFound(String filter) throws Exception {
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentDeSinistre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].dateTelechargement").value(hasItem(DEFAULT_DATE_TELECHARGEMENT.toString())));

        // Check, that the count call also returns 1
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentDeSinistreShouldNotBeFound(String filter) throws Exception {
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentDeSinistreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentDeSinistre() throws Exception {
        // Get the documentDeSinistre
        restDocumentDeSinistreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentDeSinistre() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentDeSinistre
        DocumentDeSinistre updatedDocumentDeSinistre = documentDeSinistreRepository.findById(documentDeSinistre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentDeSinistre are not directly saved in db
        em.detach(updatedDocumentDeSinistre);
        updatedDocumentDeSinistre.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL).dateTelechargement(UPDATED_DATE_TELECHARGEMENT);
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(updatedDocumentDeSinistre);

        restDocumentDeSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDeSinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDeSinistreDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentDeSinistreToMatchAllProperties(updatedDocumentDeSinistre);
    }

    @Test
    @Transactional
    void putNonExistingDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDeSinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDeSinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDeSinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentDeSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentDeSinistre using partial update
        DocumentDeSinistre partialUpdatedDocumentDeSinistre = new DocumentDeSinistre();
        partialUpdatedDocumentDeSinistre.setId(documentDeSinistre.getId());

        partialUpdatedDocumentDeSinistre.nom(UPDATED_NOM).type(UPDATED_TYPE);

        restDocumentDeSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentDeSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentDeSinistre))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDeSinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentDeSinistreUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentDeSinistre, documentDeSinistre),
            getPersistedDocumentDeSinistre(documentDeSinistre)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentDeSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentDeSinistre using partial update
        DocumentDeSinistre partialUpdatedDocumentDeSinistre = new DocumentDeSinistre();
        partialUpdatedDocumentDeSinistre.setId(documentDeSinistre.getId());

        partialUpdatedDocumentDeSinistre
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .dateTelechargement(UPDATED_DATE_TELECHARGEMENT);

        restDocumentDeSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentDeSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentDeSinistre))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDeSinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentDeSinistreUpdatableFieldsEquals(
            partialUpdatedDocumentDeSinistre,
            getPersistedDocumentDeSinistre(partialUpdatedDocumentDeSinistre)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDeSinistreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDeSinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDeSinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentDeSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentDeSinistre.setId(longCount.incrementAndGet());

        // Create the DocumentDeSinistre
        DocumentDeSinistreDTO documentDeSinistreDTO = documentDeSinistreMapper.toDto(documentDeSinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDeSinistreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentDeSinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentDeSinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentDeSinistre() throws Exception {
        // Initialize the database
        insertedDocumentDeSinistre = documentDeSinistreRepository.saveAndFlush(documentDeSinistre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentDeSinistre
        restDocumentDeSinistreMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentDeSinistre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentDeSinistreRepository.count();
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

    protected DocumentDeSinistre getPersistedDocumentDeSinistre(DocumentDeSinistre documentDeSinistre) {
        return documentDeSinistreRepository.findById(documentDeSinistre.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentDeSinistreToMatchAllProperties(DocumentDeSinistre expectedDocumentDeSinistre) {
        assertDocumentDeSinistreAllPropertiesEquals(expectedDocumentDeSinistre, getPersistedDocumentDeSinistre(expectedDocumentDeSinistre));
    }

    protected void assertPersistedDocumentDeSinistreToMatchUpdatableProperties(DocumentDeSinistre expectedDocumentDeSinistre) {
        assertDocumentDeSinistreAllUpdatablePropertiesEquals(
            expectedDocumentDeSinistre,
            getPersistedDocumentDeSinistre(expectedDocumentDeSinistre)
        );
    }
}
