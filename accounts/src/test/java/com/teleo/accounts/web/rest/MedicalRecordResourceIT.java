package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.MedicalRecordAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.MedicalRecord;
import com.teleo.accounts.repository.MedicalRecordRepository;
import com.teleo.accounts.service.dto.MedicalRecordDTO;
import com.teleo.accounts.service.mapper.MedicalRecordMapper;
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
 * Integration tests for the {@link MedicalRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restMedicalRecordMockMvc;

    private MedicalRecord medicalRecord;

    private MedicalRecord insertedMedicalRecord;

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

    @BeforeEach
    public void initTest() {
        medicalRecord = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalRecord != null) {
            medicalRecordRepository.delete(insertedMedicalRecord);
            insertedMedicalRecord = null;
        }
    }

    @Test
    @Transactional
    void createMedicalRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);
        var returnedMedicalRecordDTO = om.readValue(
            restMedicalRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalRecordDTO.class
        );

        // Validate the MedicalRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalRecord = medicalRecordMapper.toEntity(returnedMedicalRecordDTO);
        assertMedicalRecordUpdatableFieldsEquals(returnedMedicalRecord, getPersistedMedicalRecord(returnedMedicalRecord));

        insertedMedicalRecord = returnedMedicalRecord;
    }

    @Test
    @Transactional
    void createMedicalRecordWithExistingId() throws Exception {
        // Create the MedicalRecord with an existing ID
        medicalRecord.setId(1L);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumMedicalRecordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setNumMedicalRecord(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDetailsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDetails(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDateUpdated(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalRecords() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].numMedicalRecord").value(hasItem(DEFAULT_NUM_MEDICAL_RECORD)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get the medicalRecord
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalRecord.getId().intValue()))
            .andExpect(jsonPath("$.numMedicalRecord").value(DEFAULT_NUM_MEDICAL_RECORD))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicalRecord() throws Exception {
        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalRecord are not directly saved in db
        em.detach(updatedMedicalRecord);
        updatedMedicalRecord.numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD).details(UPDATED_DETAILS).dateUpdated(UPDATED_DATE_UPDATED);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(updatedMedicalRecord);

        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalRecordToMatchAllProperties(updatedMedicalRecord);
    }

    @Test
    @Transactional
    void putNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord.details(UPDATED_DETAILS).dateUpdated(UPDATED_DATE_UPDATED);

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalRecord, medicalRecord),
            getPersistedMedicalRecord(medicalRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord.numMedicalRecord(UPDATED_NUM_MEDICAL_RECORD).details(UPDATED_DETAILS).dateUpdated(UPDATED_DATE_UPDATED);

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(partialUpdatedMedicalRecord, getPersistedMedicalRecord(partialUpdatedMedicalRecord));
    }

    @Test
    @Transactional
    void patchNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalRecord
        restMedicalRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalRecordRepository.count();
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
        return medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalRecordToMatchAllProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllPropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }

    protected void assertPersistedMedicalRecordToMatchUpdatableProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllUpdatablePropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }
}
