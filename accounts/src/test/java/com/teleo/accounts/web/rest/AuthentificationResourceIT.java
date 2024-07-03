package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.AuthentificationAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.Authentification;
import com.teleo.accounts.repository.AuthentificationRepository;
import com.teleo.accounts.service.dto.AuthentificationDTO;
import com.teleo.accounts.service.mapper.AuthentificationMapper;
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
 * Integration tests for the {@link AuthentificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthentificationResourceIT {

    private static final Instant DEFAULT_DATE_CONNEXION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CONNEXION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IP_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/authentifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthentificationRepository authentificationRepository;

    @Autowired
    private AuthentificationMapper authentificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthentificationMockMvc;

    private Authentification authentification;

    private Authentification insertedAuthentification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentification createEntity(EntityManager em) {
        Authentification authentification = new Authentification().dateConnexion(DEFAULT_DATE_CONNEXION).ipAdresse(DEFAULT_IP_ADRESSE);
        return authentification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentification createUpdatedEntity(EntityManager em) {
        Authentification authentification = new Authentification().dateConnexion(UPDATED_DATE_CONNEXION).ipAdresse(UPDATED_IP_ADRESSE);
        return authentification;
    }

    @BeforeEach
    public void initTest() {
        authentification = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAuthentification != null) {
            authentificationRepository.delete(insertedAuthentification);
            insertedAuthentification = null;
        }
    }

    @Test
    @Transactional
    void createAuthentification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);
        var returnedAuthentificationDTO = om.readValue(
            restAuthentificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuthentificationDTO.class
        );

        // Validate the Authentification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuthentification = authentificationMapper.toEntity(returnedAuthentificationDTO);
        assertAuthentificationUpdatableFieldsEquals(returnedAuthentification, getPersistedAuthentification(returnedAuthentification));

        insertedAuthentification = returnedAuthentification;
    }

    @Test
    @Transactional
    void createAuthentificationWithExistingId() throws Exception {
        // Create the Authentification with an existing ID
        authentification.setId(1L);
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthentificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateConnexionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        authentification.setDateConnexion(null);

        // Create the Authentification, which fails.
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        restAuthentificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIpAdresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        authentification.setIpAdresse(null);

        // Create the Authentification, which fails.
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        restAuthentificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthentifications() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        // Get all the authentificationList
        restAuthentificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authentification.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateConnexion").value(hasItem(DEFAULT_DATE_CONNEXION.toString())))
            .andExpect(jsonPath("$.[*].ipAdresse").value(hasItem(DEFAULT_IP_ADRESSE)));
    }

    @Test
    @Transactional
    void getAuthentification() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        // Get the authentification
        restAuthentificationMockMvc
            .perform(get(ENTITY_API_URL_ID, authentification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authentification.getId().intValue()))
            .andExpect(jsonPath("$.dateConnexion").value(DEFAULT_DATE_CONNEXION.toString()))
            .andExpect(jsonPath("$.ipAdresse").value(DEFAULT_IP_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingAuthentification() throws Exception {
        // Get the authentification
        restAuthentificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuthentification() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentification
        Authentification updatedAuthentification = authentificationRepository.findById(authentification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuthentification are not directly saved in db
        em.detach(updatedAuthentification);
        updatedAuthentification.dateConnexion(UPDATED_DATE_CONNEXION).ipAdresse(UPDATED_IP_ADRESSE);
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(updatedAuthentification);

        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authentificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authentificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuthentificationToMatchAllProperties(updatedAuthentification);
    }

    @Test
    @Transactional
    void putNonExistingAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authentificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authentificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authentificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthentificationWithPatch() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentification using partial update
        Authentification partialUpdatedAuthentification = new Authentification();
        partialUpdatedAuthentification.setId(authentification.getId());

        partialUpdatedAuthentification.ipAdresse(UPDATED_IP_ADRESSE);

        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthentification))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthentificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAuthentification, authentification),
            getPersistedAuthentification(authentification)
        );
    }

    @Test
    @Transactional
    void fullUpdateAuthentificationWithPatch() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentification using partial update
        Authentification partialUpdatedAuthentification = new Authentification();
        partialUpdatedAuthentification.setId(authentification.getId());

        partialUpdatedAuthentification.dateConnexion(UPDATED_DATE_CONNEXION).ipAdresse(UPDATED_IP_ADRESSE);

        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthentification))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthentificationUpdatableFieldsEquals(
            partialUpdatedAuthentification,
            getPersistedAuthentification(partialUpdatedAuthentification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authentificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authentificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authentificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthentification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentification.setId(longCount.incrementAndGet());

        // Create the Authentification
        AuthentificationDTO authentificationDTO = authentificationMapper.toDto(authentification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(authentificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthentification() throws Exception {
        // Initialize the database
        insertedAuthentification = authentificationRepository.saveAndFlush(authentification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the authentification
        restAuthentificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, authentification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return authentificationRepository.count();
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

    protected Authentification getPersistedAuthentification(Authentification authentification) {
        return authentificationRepository.findById(authentification.getId()).orElseThrow();
    }

    protected void assertPersistedAuthentificationToMatchAllProperties(Authentification expectedAuthentification) {
        assertAuthentificationAllPropertiesEquals(expectedAuthentification, getPersistedAuthentification(expectedAuthentification));
    }

    protected void assertPersistedAuthentificationToMatchUpdatableProperties(Authentification expectedAuthentification) {
        assertAuthentificationAllUpdatablePropertiesEquals(
            expectedAuthentification,
            getPersistedAuthentification(expectedAuthentification)
        );
    }
}
