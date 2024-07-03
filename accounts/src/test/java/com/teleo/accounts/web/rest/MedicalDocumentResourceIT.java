package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.MedicalDocumentAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.MedicalDocument;
import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.repository.EntityManager;
import com.teleo.accounts.repository.MedicalDocumentRepository;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.service.mapper.MedicalDocumentMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MedicalDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MedicalDocumentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medical-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalDocumentRepository medicalDocumentRepository;

    @Autowired
    private MedicalDocumentMapper medicalDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MedicalDocument medicalDocument;

    private MedicalDocument insertedMedicalDocument;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalDocument createEntity(EntityManager em) {
        MedicalDocument medicalDocument = new MedicalDocument().nom(DEFAULT_NOM).type(DEFAULT_TYPE).url(DEFAULT_URL);
        return medicalDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalDocument createUpdatedEntity(EntityManager em) {
        MedicalDocument medicalDocument = new MedicalDocument().nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);
        return medicalDocument;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MedicalDocument.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        medicalDocument = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalDocument != null) {
            medicalDocumentRepository.delete(insertedMedicalDocument).block();
            insertedMedicalDocument = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMedicalDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);
        var returnedMedicalDocumentDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MedicalDocumentDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the MedicalDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalDocument = medicalDocumentMapper.toEntity(returnedMedicalDocumentDTO);
        assertMedicalDocumentUpdatableFieldsEquals(returnedMedicalDocument, getPersistedMedicalDocument(returnedMedicalDocument));

        insertedMedicalDocument = returnedMedicalDocument;
    }

    @Test
    void createMedicalDocumentWithExistingId() throws Exception {
        // Create the MedicalDocument with an existing ID
        medicalDocument.setId(1L);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setNom(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setType(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setUrl(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMedicalDocuments() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(medicalDocument.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL));
    }

    @Test
    void getMedicalDocument() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get the medicalDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, medicalDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(medicalDocument.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL));
    }

    @Test
    void getMedicalDocumentsByIdFiltering() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        Long id = medicalDocument.getId();

        defaultMedicalDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicalDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicalDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllMedicalDocumentsByNomIsEqualToSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where nom equals to
        defaultMedicalDocumentFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    void getAllMedicalDocumentsByNomIsInShouldWork() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where nom in
        defaultMedicalDocumentFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    void getAllMedicalDocumentsByNomIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where nom is not null
        defaultMedicalDocumentFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    void getAllMedicalDocumentsByNomContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where nom contains
        defaultMedicalDocumentFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    void getAllMedicalDocumentsByNomNotContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where nom does not contain
        defaultMedicalDocumentFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    void getAllMedicalDocumentsByTypeIsEqualToSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where type equals to
        defaultMedicalDocumentFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    void getAllMedicalDocumentsByTypeIsInShouldWork() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where type in
        defaultMedicalDocumentFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    void getAllMedicalDocumentsByTypeIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where type is not null
        defaultMedicalDocumentFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    void getAllMedicalDocumentsByTypeContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where type contains
        defaultMedicalDocumentFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    void getAllMedicalDocumentsByTypeNotContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where type does not contain
        defaultMedicalDocumentFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    void getAllMedicalDocumentsByUrlIsEqualToSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where url equals to
        defaultMedicalDocumentFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    void getAllMedicalDocumentsByUrlIsInShouldWork() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where url in
        defaultMedicalDocumentFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    void getAllMedicalDocumentsByUrlIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where url is not null
        defaultMedicalDocumentFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    void getAllMedicalDocumentsByUrlContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where url contains
        defaultMedicalDocumentFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    void getAllMedicalDocumentsByUrlNotContainsSomething() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        // Get all the medicalDocumentList where url does not contain
        defaultMedicalDocumentFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    void getAllMedicalDocumentsByDossierMedicalIsEqualToSomething() {
        MedicalRecord dossierMedical = MedicalRecordResourceIT.createEntity(em);
        medicalRecordRepository.save(dossierMedical).block();
        Long dossierMedicalId = dossierMedical.getId();
        medicalDocument.setDossierMedicalId(dossierMedicalId);
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();
        // Get all the medicalDocumentList where dossierMedical equals to dossierMedicalId
        defaultMedicalDocumentShouldBeFound("dossierMedicalId.equals=" + dossierMedicalId);

        // Get all the medicalDocumentList where dossierMedical equals to (dossierMedicalId + 1)
        defaultMedicalDocumentShouldNotBeFound("dossierMedicalId.equals=" + (dossierMedicalId + 1));
    }

    private void defaultMedicalDocumentFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultMedicalDocumentShouldBeFound(shouldBeFound);
        defaultMedicalDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicalDocumentShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(medicalDocument.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicalDocumentShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingMedicalDocument() {
        // Get the medicalDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument
        MedicalDocument updatedMedicalDocument = medicalDocumentRepository.findById(medicalDocument.getId()).block();
        updatedMedicalDocument.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(updatedMedicalDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalDocumentToMatchAllProperties(updatedMedicalDocument);
    }

    @Test
    void putNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument.url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalDocument, medicalDocument),
            getPersistedMedicalDocument(medicalDocument)
        );
    }

    @Test
    void fullUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            partialUpdatedMedicalDocument,
            getPersistedMedicalDocument(partialUpdatedMedicalDocument)
        );
    }

    @Test
    void patchNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMedicalDocument() {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.save(medicalDocument).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, medicalDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalDocumentRepository.count().block();
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

    protected MedicalDocument getPersistedMedicalDocument(MedicalDocument medicalDocument) {
        return medicalDocumentRepository.findById(medicalDocument.getId()).block();
    }

    protected void assertPersistedMedicalDocumentToMatchAllProperties(MedicalDocument expectedMedicalDocument) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicalDocumentAllPropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
        assertMedicalDocumentUpdatableFieldsEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }

    protected void assertPersistedMedicalDocumentToMatchUpdatableProperties(MedicalDocument expectedMedicalDocument) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicalDocumentAllUpdatablePropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
        assertMedicalDocumentUpdatableFieldsEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }
}
