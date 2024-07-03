package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.MedicalRecordAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.repository.EntityManager;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.repository.UserExtraRepository;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import com.teleo.accounts.service.mapper.MedicalRecordMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MedicalRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MedicalRecordResourceIT {

    private static final String DEFAULT_NUM_MEDICAL_RECORD = "AAAAAAAAAA";
    private static final String UPDATED_NUM_MEDICAL_RECORD = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/medical-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MedicalRecord medicalRecord;

    private MedicalRecord insertedMedicalRecord;

    @Autowired
    private UserExtraRepository userExtraRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createEntity(EntityManager em) {
        MedicalRecord medicalRecord = new MedicalRecord()
            .numMedicalRecord(DEFAULT_NUM_MEDICAL_RECORD)
            .details(DEFAULT_DETAILS)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return medicalRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createUpdatedEntity(EntityManager em) {
        MedicalRecord medicalRecord = new MedicalRecord()
            .numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD)
            .details(UPDATED_DETAILS)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return medicalRecord;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MedicalRecord.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        medicalRecord = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalRecord != null) {
            medicalRecordRepository.delete(insertedMedicalRecord).block();
            insertedMedicalRecord = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMedicalRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);
        var returnedMedicalRecordDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MedicalRecordDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the MedicalRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalRecord = medicalRecordMapper.toEntity(returnedMedicalRecordDTO);
        assertMedicalRecordUpdatableFieldsEquals(returnedMedicalRecord, getPersistedMedicalRecord(returnedMedicalRecord));

        insertedMedicalRecord = returnedMedicalRecord;
    }

    @Test
    void createMedicalRecordWithExistingId() throws Exception {
        // Create the MedicalRecord with an existing ID
        medicalRecord.setId(1L);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNumMedicalRecordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setNumMedicalRecord(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDetailsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDetails(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDateUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDateUpdated(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMedicalRecords() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList
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
            .value(hasItem(medicalRecord.getId().intValue()))
            .jsonPath("$.[*].numMedicalRecord")
            .value(hasItem(DEFAULT_NUM_MEDICAL_RECORD))
            .jsonPath("$.[*].details")
            .value(hasItem(DEFAULT_DETAILS))
            .jsonPath("$.[*].dateUpdated")
            .value(hasItem(DEFAULT_DATE_UPDATED.toString()));
    }

    @Test
    void getMedicalRecord() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get the medicalRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, medicalRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(medicalRecord.getId().intValue()))
            .jsonPath("$.numMedicalRecord")
            .value(is(DEFAULT_NUM_MEDICAL_RECORD))
            .jsonPath("$.details")
            .value(is(DEFAULT_DETAILS))
            .jsonPath("$.dateUpdated")
            .value(is(DEFAULT_DATE_UPDATED.toString()));
    }

    @Test
    void getMedicalRecordsByIdFiltering() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        Long id = medicalRecord.getId();

        defaultMedicalRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicalRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicalRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllMedicalRecordsByNumMedicalRecordIsEqualToSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where numMedicalRecord equals to
        defaultMedicalRecordFiltering(
            "numMedicalRecord.equals=" + DEFAULT_NUM_MEDICAL_RECORD,
            "numMedicalRecord.equals=" + UPDATED_NUM_MEDICAL_RECORD
        );
    }

    @Test
    void getAllMedicalRecordsByNumMedicalRecordIsInShouldWork() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where numMedicalRecord in
        defaultMedicalRecordFiltering(
            "numMedicalRecord.in=" + DEFAULT_NUM_MEDICAL_RECORD + "," + UPDATED_NUM_MEDICAL_RECORD,
            "numMedicalRecord.in=" + UPDATED_NUM_MEDICAL_RECORD
        );
    }

    @Test
    void getAllMedicalRecordsByNumMedicalRecordIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where numMedicalRecord is not null
        defaultMedicalRecordFiltering("numMedicalRecord.specified=true", "numMedicalRecord.specified=false");
    }

    @Test
    void getAllMedicalRecordsByNumMedicalRecordContainsSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where numMedicalRecord contains
        defaultMedicalRecordFiltering(
            "numMedicalRecord.contains=" + DEFAULT_NUM_MEDICAL_RECORD,
            "numMedicalRecord.contains=" + UPDATED_NUM_MEDICAL_RECORD
        );
    }

    @Test
    void getAllMedicalRecordsByNumMedicalRecordNotContainsSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where numMedicalRecord does not contain
        defaultMedicalRecordFiltering(
            "numMedicalRecord.doesNotContain=" + UPDATED_NUM_MEDICAL_RECORD,
            "numMedicalRecord.doesNotContain=" + DEFAULT_NUM_MEDICAL_RECORD
        );
    }

    @Test
    void getAllMedicalRecordsByDetailsIsEqualToSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where details equals to
        defaultMedicalRecordFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    void getAllMedicalRecordsByDetailsIsInShouldWork() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where details in
        defaultMedicalRecordFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    void getAllMedicalRecordsByDetailsIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where details is not null
        defaultMedicalRecordFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    void getAllMedicalRecordsByDetailsContainsSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where details contains
        defaultMedicalRecordFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    void getAllMedicalRecordsByDetailsNotContainsSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where details does not contain
        defaultMedicalRecordFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    void getAllMedicalRecordsByDateUpdatedIsEqualToSomething() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where dateUpdated equals to
        defaultMedicalRecordFiltering("dateUpdated.equals=" + DEFAULT_DATE_UPDATED, "dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    void getAllMedicalRecordsByDateUpdatedIsInShouldWork() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where dateUpdated in
        defaultMedicalRecordFiltering(
            "dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED,
            "dateUpdated.in=" + UPDATED_DATE_UPDATED
        );
    }

    @Test
    void getAllMedicalRecordsByDateUpdatedIsNullOrNotNull() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        // Get all the medicalRecordList where dateUpdated is not null
        defaultMedicalRecordFiltering("dateUpdated.specified=true", "dateUpdated.specified=false");
    }

    @Test
    void getAllMedicalRecordsByUtilisateurIsEqualToSomething() {
        UserExtra utilisateur = UserExtraResourceIT.createEntity(em);
        userExtraRepository.save(utilisateur).block();
        Long utilisateurId = utilisateur.getId();
        medicalRecord.setUtilisateurId(utilisateurId);
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();
        // Get all the medicalRecordList where utilisateur equals to utilisateurId
        defaultMedicalRecordShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the medicalRecordList where utilisateur equals to (utilisateurId + 1)
        defaultMedicalRecordShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultMedicalRecordFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultMedicalRecordShouldBeFound(shouldBeFound);
        defaultMedicalRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicalRecordShouldBeFound(String filter) {
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
            .value(hasItem(medicalRecord.getId().intValue()))
            .jsonPath("$.[*].numMedicalRecord")
            .value(hasItem(DEFAULT_NUM_MEDICAL_RECORD))
            .jsonPath("$.[*].details")
            .value(hasItem(DEFAULT_DETAILS))
            .jsonPath("$.[*].dateUpdated")
            .value(hasItem(DEFAULT_DATE_UPDATED.toString()));

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
    private void defaultMedicalRecordShouldNotBeFound(String filter) {
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
    void getNonExistingMedicalRecord() {
        // Get the medicalRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId()).block();
        updatedMedicalRecord.numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD).details(UPDATED_DETAILS).dateUpdated(UPDATED_DATE_UPDATED);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(updatedMedicalRecord);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicalRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalRecordToMatchAllProperties(updatedMedicalRecord);
    }

    @Test
    void putNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicalRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord.numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalRecord, medicalRecord),
            getPersistedMedicalRecord(medicalRecord)
        );
    }

    @Test
    void fullUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord.numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD).details(UPDATED_DETAILS).dateUpdated(UPDATED_DATE_UPDATED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(partialUpdatedMedicalRecord, getPersistedMedicalRecord(partialUpdatedMedicalRecord));
    }

    @Test
    void patchNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, medicalRecordDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicalRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMedicalRecord() {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.save(medicalRecord).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalRecord
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, medicalRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalRecordRepository.count().block();
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

    protected MedicalRecord getPersistedMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.findById(medicalRecord.getId()).block();
    }

    protected void assertPersistedMedicalRecordToMatchAllProperties(MedicalRecord expectedMedicalRecord) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicalRecordAllPropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
        assertMedicalRecordUpdatableFieldsEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }

    protected void assertPersistedMedicalRecordToMatchUpdatableProperties(MedicalRecord expectedMedicalRecord) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicalRecordAllUpdatablePropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
        assertMedicalRecordUpdatableFieldsEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }
}
