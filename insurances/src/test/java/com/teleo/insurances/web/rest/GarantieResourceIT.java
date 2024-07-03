package com.teleo.insurances.web.rest;

import static com.teleo.insurances.domain.GarantieAsserts.*;
import static com.teleo.insurances.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.insurances.IntegrationTest;
import com.teleo.insurances.domain.Garantie;
import com.teleo.insurances.domain.enumeration.GarantieStatus;
import com.teleo.insurances.repository.GarantieRepository;
import com.teleo.insurances.service.dto.GarantieDTO;
import com.teleo.insurances.service.mapper.GarantieMapper;
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
 * Integration tests for the {@link GarantieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GarantieResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GarantieStatus DEFAULT_STATUS = GarantieStatus.ACTIVE;
    private static final GarantieStatus UPDATED_STATUS = GarantieStatus.EXPIREE;

    private static final String ENTITY_API_URL = "/api/garanties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GarantieRepository garantieRepository;

    @Autowired
    private GarantieMapper garantieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGarantieMockMvc;

    private Garantie garantie;

    private Garantie insertedGarantie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garantie createEntity(EntityManager em) {
        Garantie garantie = new Garantie()
            .description(DEFAULT_DESCRIPTION)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .status(DEFAULT_STATUS);
        return garantie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garantie createUpdatedEntity(EntityManager em) {
        Garantie garantie = new Garantie()
            .description(UPDATED_DESCRIPTION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .status(UPDATED_STATUS);
        return garantie;
    }

    @BeforeEach
    public void initTest() {
        garantie = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGarantie != null) {
            garantieRepository.delete(insertedGarantie);
            insertedGarantie = null;
        }
    }

    @Test
    @Transactional
    void createGarantie() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);
        var returnedGarantieDTO = om.readValue(
            restGarantieMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GarantieDTO.class
        );

        // Validate the Garantie in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGarantie = garantieMapper.toEntity(returnedGarantieDTO);
        assertGarantieUpdatableFieldsEquals(returnedGarantie, getPersistedGarantie(returnedGarantie));

        insertedGarantie = returnedGarantie;
    }

    @Test
    @Transactional
    void createGarantieWithExistingId() throws Exception {
        // Create the Garantie with an existing ID
        garantie.setId(1L);
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGarantieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        garantie.setDescription(null);

        // Create the Garantie, which fails.
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        restGarantieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        garantie.setDateDebut(null);

        // Create the Garantie, which fails.
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        restGarantieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        garantie.setDateFin(null);

        // Create the Garantie, which fails.
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        restGarantieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        garantie.setStatus(null);

        // Create the Garantie, which fails.
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        restGarantieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGaranties() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        // Get all the garantieList
        restGarantieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(garantie.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getGarantie() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        // Get the garantie
        restGarantieMockMvc
            .perform(get(ENTITY_API_URL_ID, garantie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(garantie.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGarantie() throws Exception {
        // Get the garantie
        restGarantieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGarantie() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the garantie
        Garantie updatedGarantie = garantieRepository.findById(garantie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGarantie are not directly saved in db
        em.detach(updatedGarantie);
        updatedGarantie.description(UPDATED_DESCRIPTION).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).status(UPDATED_STATUS);
        GarantieDTO garantieDTO = garantieMapper.toDto(updatedGarantie);

        restGarantieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, garantieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(garantieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGarantieToMatchAllProperties(updatedGarantie);
    }

    @Test
    @Transactional
    void putNonExistingGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, garantieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(garantieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(garantieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGarantieWithPatch() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the garantie using partial update
        Garantie partialUpdatedGarantie = new Garantie();
        partialUpdatedGarantie.setId(garantie.getId());

        partialUpdatedGarantie.description(UPDATED_DESCRIPTION);

        restGarantieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGarantie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGarantie))
            )
            .andExpect(status().isOk());

        // Validate the Garantie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGarantieUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGarantie, garantie), getPersistedGarantie(garantie));
    }

    @Test
    @Transactional
    void fullUpdateGarantieWithPatch() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the garantie using partial update
        Garantie partialUpdatedGarantie = new Garantie();
        partialUpdatedGarantie.setId(garantie.getId());

        partialUpdatedGarantie
            .description(UPDATED_DESCRIPTION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .status(UPDATED_STATUS);

        restGarantieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGarantie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGarantie))
            )
            .andExpect(status().isOk());

        // Validate the Garantie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGarantieUpdatableFieldsEquals(partialUpdatedGarantie, getPersistedGarantie(partialUpdatedGarantie));
    }

    @Test
    @Transactional
    void patchNonExistingGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, garantieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(garantieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(garantieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGarantie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        garantie.setId(longCount.incrementAndGet());

        // Create the Garantie
        GarantieDTO garantieDTO = garantieMapper.toDto(garantie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGarantieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(garantieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Garantie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGarantie() throws Exception {
        // Initialize the database
        insertedGarantie = garantieRepository.saveAndFlush(garantie);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the garantie
        restGarantieMockMvc
            .perform(delete(ENTITY_API_URL_ID, garantie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return garantieRepository.count();
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

    protected Garantie getPersistedGarantie(Garantie garantie) {
        return garantieRepository.findById(garantie.getId()).orElseThrow();
    }

    protected void assertPersistedGarantieToMatchAllProperties(Garantie expectedGarantie) {
        assertGarantieAllPropertiesEquals(expectedGarantie, getPersistedGarantie(expectedGarantie));
    }

    protected void assertPersistedGarantieToMatchUpdatableProperties(Garantie expectedGarantie) {
        assertGarantieAllUpdatablePropertiesEquals(expectedGarantie, getPersistedGarantie(expectedGarantie));
    }
}
