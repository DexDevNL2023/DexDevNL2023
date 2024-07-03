package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.SinistreAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static com.teleo.claims.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.Sinistre;
import com.teleo.claims.domain.enumeration.ClaimStatus;
import com.teleo.claims.repository.SinistreRepository;
import com.teleo.claims.service.dto.SinistreDTO;
import com.teleo.claims.service.mapper.SinistreMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link SinistreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SinistreResourceIT {

    private static final String DEFAULT_NUMERO_SINISTRE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SINISTRE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DECLARATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DECLARATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TRAITEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TRAITEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ClaimStatus DEFAULT_STATUS = ClaimStatus.EN_ATTENTE;
    private static final ClaimStatus UPDATED_STATUS = ClaimStatus.APPROUVE;

    private static final String DEFAULT_RAISON = "AAAAAAAAAA";
    private static final String UPDATED_RAISON = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT_RECLAME = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_RECLAME = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MONTANT_APPROUVE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_APPROUVE = new BigDecimal(2);

    private static final Long DEFAULT_SOUSCRIPTION_ID = 1L;
    private static final Long UPDATED_SOUSCRIPTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/sinistres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SinistreRepository sinistreRepository;

    @Autowired
    private SinistreMapper sinistreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSinistreMockMvc;

    private Sinistre sinistre;

    private Sinistre insertedSinistre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sinistre createEntity(EntityManager em) {
        Sinistre sinistre = new Sinistre()
            .numeroSinistre(DEFAULT_NUMERO_SINISTRE)
            .dateDeclaration(DEFAULT_DATE_DECLARATION)
            .dateTraitement(DEFAULT_DATE_TRAITEMENT)
            .status(DEFAULT_STATUS)
            .raison(DEFAULT_RAISON)
            .montantReclame(DEFAULT_MONTANT_RECLAME)
            .montantApprouve(DEFAULT_MONTANT_APPROUVE)
            .souscriptionId(DEFAULT_SOUSCRIPTION_ID);
        return sinistre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sinistre createUpdatedEntity(EntityManager em) {
        Sinistre sinistre = new Sinistre()
            .numeroSinistre(UPDATED_NUMERO_SINISTRE)
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .dateTraitement(UPDATED_DATE_TRAITEMENT)
            .status(UPDATED_STATUS)
            .raison(UPDATED_RAISON)
            .montantReclame(UPDATED_MONTANT_RECLAME)
            .montantApprouve(UPDATED_MONTANT_APPROUVE)
            .souscriptionId(UPDATED_SOUSCRIPTION_ID);
        return sinistre;
    }

    @BeforeEach
    public void initTest() {
        sinistre = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSinistre != null) {
            sinistreRepository.delete(insertedSinistre);
            insertedSinistre = null;
        }
    }

    @Test
    @Transactional
    void createSinistre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);
        var returnedSinistreDTO = om.readValue(
            restSinistreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SinistreDTO.class
        );

        // Validate the Sinistre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSinistre = sinistreMapper.toEntity(returnedSinistreDTO);
        assertSinistreUpdatableFieldsEquals(returnedSinistre, getPersistedSinistre(returnedSinistre));

        insertedSinistre = returnedSinistre;
    }

    @Test
    @Transactional
    void createSinistreWithExistingId() throws Exception {
        // Create the Sinistre with an existing ID
        sinistre.setId(1L);
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroSinistreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setNumeroSinistre(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDeclarationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setDateDeclaration(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setStatus(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRaisonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setRaison(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantReclameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setMontantReclame(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSouscriptionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sinistre.setSouscriptionId(null);

        // Create the Sinistre, which fails.
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSinistres() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        // Get all the sinistreList
        restSinistreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sinistre.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroSinistre").value(hasItem(DEFAULT_NUMERO_SINISTRE)))
            .andExpect(jsonPath("$.[*].dateDeclaration").value(hasItem(DEFAULT_DATE_DECLARATION.toString())))
            .andExpect(jsonPath("$.[*].dateTraitement").value(hasItem(DEFAULT_DATE_TRAITEMENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)))
            .andExpect(jsonPath("$.[*].montantReclame").value(hasItem(sameNumber(DEFAULT_MONTANT_RECLAME))))
            .andExpect(jsonPath("$.[*].montantApprouve").value(hasItem(sameNumber(DEFAULT_MONTANT_APPROUVE))))
            .andExpect(jsonPath("$.[*].souscriptionId").value(hasItem(DEFAULT_SOUSCRIPTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        // Get the sinistre
        restSinistreMockMvc
            .perform(get(ENTITY_API_URL_ID, sinistre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sinistre.getId().intValue()))
            .andExpect(jsonPath("$.numeroSinistre").value(DEFAULT_NUMERO_SINISTRE))
            .andExpect(jsonPath("$.dateDeclaration").value(DEFAULT_DATE_DECLARATION.toString()))
            .andExpect(jsonPath("$.dateTraitement").value(DEFAULT_DATE_TRAITEMENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON))
            .andExpect(jsonPath("$.montantReclame").value(sameNumber(DEFAULT_MONTANT_RECLAME)))
            .andExpect(jsonPath("$.montantApprouve").value(sameNumber(DEFAULT_MONTANT_APPROUVE)))
            .andExpect(jsonPath("$.souscriptionId").value(DEFAULT_SOUSCRIPTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSinistre() throws Exception {
        // Get the sinistre
        restSinistreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre
        Sinistre updatedSinistre = sinistreRepository.findById(sinistre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSinistre are not directly saved in db
        em.detach(updatedSinistre);
        updatedSinistre
            .numeroSinistre(UPDATED_NUMERO_SINISTRE)
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .dateTraitement(UPDATED_DATE_TRAITEMENT)
            .status(UPDATED_STATUS)
            .raison(UPDATED_RAISON)
            .montantReclame(UPDATED_MONTANT_RECLAME)
            .montantApprouve(UPDATED_MONTANT_APPROUVE)
            .souscriptionId(UPDATED_SOUSCRIPTION_ID);
        SinistreDTO sinistreDTO = sinistreMapper.toDto(updatedSinistre);

        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSinistreToMatchAllProperties(updatedSinistre);
    }

    @Test
    @Transactional
    void putNonExistingSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre using partial update
        Sinistre partialUpdatedSinistre = new Sinistre();
        partialUpdatedSinistre.setId(sinistre.getId());

        partialUpdatedSinistre
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .dateTraitement(UPDATED_DATE_TRAITEMENT)
            .status(UPDATED_STATUS)
            .raison(UPDATED_RAISON)
            .montantReclame(UPDATED_MONTANT_RECLAME);

        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinistre))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinistreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSinistre, sinistre), getPersistedSinistre(sinistre));
    }

    @Test
    @Transactional
    void fullUpdateSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre using partial update
        Sinistre partialUpdatedSinistre = new Sinistre();
        partialUpdatedSinistre.setId(sinistre.getId());

        partialUpdatedSinistre
            .numeroSinistre(UPDATED_NUMERO_SINISTRE)
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .dateTraitement(UPDATED_DATE_TRAITEMENT)
            .status(UPDATED_STATUS)
            .raison(UPDATED_RAISON)
            .montantReclame(UPDATED_MONTANT_RECLAME)
            .montantApprouve(UPDATED_MONTANT_APPROUVE)
            .souscriptionId(UPDATED_SOUSCRIPTION_ID);

        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinistre))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinistreUpdatableFieldsEquals(partialUpdatedSinistre, getPersistedSinistre(partialUpdatedSinistre));
    }

    @Test
    @Transactional
    void patchNonExistingSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sinistre
        restSinistreMockMvc
            .perform(delete(ENTITY_API_URL_ID, sinistre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sinistreRepository.count();
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

    protected Sinistre getPersistedSinistre(Sinistre sinistre) {
        return sinistreRepository.findById(sinistre.getId()).orElseThrow();
    }

    protected void assertPersistedSinistreToMatchAllProperties(Sinistre expectedSinistre) {
        assertSinistreAllPropertiesEquals(expectedSinistre, getPersistedSinistre(expectedSinistre));
    }

    protected void assertPersistedSinistreToMatchUpdatableProperties(Sinistre expectedSinistre) {
        assertSinistreAllUpdatablePropertiesEquals(expectedSinistre, getPersistedSinistre(expectedSinistre));
    }
}
