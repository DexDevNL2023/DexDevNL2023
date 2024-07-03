package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.MedicalDocumentAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.MedicalDocument;
import com.teleo.accounts.repository.MedicalDocumentRepository;
import com.teleo.accounts.service.dto.MedicalDocumentDTO;
import com.teleo.accounts.service.mapper.MedicalDocumentMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link MedicalDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restMedicalDocumentMockMvc;

    private MedicalDocument medicalDocument;

    private MedicalDocument insertedMedicalDocument;

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

    @BeforeEach
    public void initTest() {
        medicalDocument = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalDocument != null) {
            medicalDocumentRepository.delete(insertedMedicalDocument);
            insertedMedicalDocument = null;
        }
    }

    @Test
    @Transactional
    void createMedicalDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);
        var returnedMedicalDocumentDTO = om.readValue(
            restMedicalDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalDocumentDTO.class
        );

        // Validate the MedicalDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalDocument = medicalDocumentMapper.toEntity(returnedMedicalDocumentDTO);
        assertMedicalDocumentUpdatableFieldsEquals(returnedMedicalDocument, getPersistedMedicalDocument(returnedMedicalDocument));

        insertedMedicalDocument = returnedMedicalDocument;
    }

    @Test
    @Transactional
    void createMedicalDocumentWithExistingId() throws Exception {
        // Create the MedicalDocument with an existing ID
        medicalDocument.setId(1L);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setNom(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setType(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setUrl(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalDocuments() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        // Get all the medicalDocumentList
        restMedicalDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        // Get the medicalDocument
        restMedicalDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalDocument.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingMedicalDocument() throws Exception {
        // Get the medicalDocument
        restMedicalDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument
        MedicalDocument updatedMedicalDocument = medicalDocumentRepository.findById(medicalDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalDocument are not directly saved in db
        em.detach(updatedMedicalDocument);
        updatedMedicalDocument.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(updatedMedicalDocument);

        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalDocumentToMatchAllProperties(updatedMedicalDocument);
    }

    @Test
    @Transactional
    void putNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);

        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalDocument, medicalDocument),
            getPersistedMedicalDocument(medicalDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument.nom(UPDATED_NOM).type(UPDATED_TYPE).url(UPDATED_URL);

        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            partialUpdatedMedicalDocument,
            getPersistedMedicalDocument(partialUpdatedMedicalDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalDocument
        restMedicalDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalDocumentRepository.count();
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
        return medicalDocumentRepository.findById(medicalDocument.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalDocumentToMatchAllProperties(MedicalDocument expectedMedicalDocument) {
        assertMedicalDocumentAllPropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }

    protected void assertPersistedMedicalDocumentToMatchUpdatableProperties(MedicalDocument expectedMedicalDocument) {
        assertMedicalDocumentAllUpdatablePropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }
}
