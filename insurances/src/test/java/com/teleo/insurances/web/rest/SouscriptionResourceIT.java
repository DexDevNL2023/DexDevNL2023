package com.teleo.insurances.web.rest;

import static com.teleo.insurances.domain.SouscriptionAsserts.*;
import static com.teleo.insurances.web.rest.TestUtil.createUpdateProxyForBean;
import static com.teleo.insurances.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.insurances.IntegrationTest;
import com.teleo.insurances.domain.Souscription;
import com.teleo.insurances.domain.enumeration.PaymentFrequency;
import com.teleo.insurances.domain.enumeration.SubscriptionStatus;
import com.teleo.insurances.repository.SouscriptionRepository;
import com.teleo.insurances.service.dto.SouscriptionDTO;
import com.teleo.insurances.service.mapper.SouscriptionMapper;
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
 * Integration tests for the {@link SouscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SouscriptionResourceIT {

    private static final String DEFAULT_NUMERO_SOUSCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SOUSCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_SOUSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SOUSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SubscriptionStatus DEFAULT_STATUS = SubscriptionStatus.ACTIVE;
    private static final SubscriptionStatus UPDATED_STATUS = SubscriptionStatus.ON_RISK;

    private static final BigDecimal DEFAULT_CUMUL_SINISTRES = new BigDecimal(1);
    private static final BigDecimal UPDATED_CUMUL_SINISTRES = new BigDecimal(2);

    private static final PaymentFrequency DEFAULT_FREQUENCE_PAIEMENT = PaymentFrequency.MENSUEL;
    private static final PaymentFrequency UPDATED_FREQUENCE_PAIEMENT = PaymentFrequency.TRIMESTRIEL;

    private static final Long DEFAULT_UTILISATEUR_ID = 1L;
    private static final Long UPDATED_UTILISATEUR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/souscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SouscriptionRepository souscriptionRepository;

    @Autowired
    private SouscriptionMapper souscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSouscriptionMockMvc;

    private Souscription souscription;

    private Souscription insertedSouscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Souscription createEntity(EntityManager em) {
        Souscription souscription = new Souscription()
            .numeroSouscription(DEFAULT_NUMERO_SOUSCRIPTION)
            .dateSouscription(DEFAULT_DATE_SOUSCRIPTION)
            .status(DEFAULT_STATUS)
            .cumulSinistres(DEFAULT_CUMUL_SINISTRES)
            .frequencePaiement(DEFAULT_FREQUENCE_PAIEMENT)
            .utilisateurId(DEFAULT_UTILISATEUR_ID);
        return souscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Souscription createUpdatedEntity(EntityManager em) {
        Souscription souscription = new Souscription()
            .numeroSouscription(UPDATED_NUMERO_SOUSCRIPTION)
            .dateSouscription(UPDATED_DATE_SOUSCRIPTION)
            .status(UPDATED_STATUS)
            .cumulSinistres(UPDATED_CUMUL_SINISTRES)
            .frequencePaiement(UPDATED_FREQUENCE_PAIEMENT)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
        return souscription;
    }

    @BeforeEach
    public void initTest() {
        souscription = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSouscription != null) {
            souscriptionRepository.delete(insertedSouscription);
            insertedSouscription = null;
        }
    }

    @Test
    @Transactional
    void createSouscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);
        var returnedSouscriptionDTO = om.readValue(
            restSouscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SouscriptionDTO.class
        );

        // Validate the Souscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSouscription = souscriptionMapper.toEntity(returnedSouscriptionDTO);
        assertSouscriptionUpdatableFieldsEquals(returnedSouscription, getPersistedSouscription(returnedSouscription));

        insertedSouscription = returnedSouscription;
    }

    @Test
    @Transactional
    void createSouscriptionWithExistingId() throws Exception {
        // Create the Souscription with an existing ID
        souscription.setId(1L);
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroSouscriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setNumeroSouscription(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSouscriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setDateSouscription(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setStatus(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCumulSinistresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setCumulSinistres(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencePaiementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setFrequencePaiement(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUtilisateurIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        souscription.setUtilisateurId(null);

        // Create the Souscription, which fails.
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        restSouscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSouscriptions() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        // Get all the souscriptionList
        restSouscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(souscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroSouscription").value(hasItem(DEFAULT_NUMERO_SOUSCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSouscription").value(hasItem(DEFAULT_DATE_SOUSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cumulSinistres").value(hasItem(sameNumber(DEFAULT_CUMUL_SINISTRES))))
            .andExpect(jsonPath("$.[*].frequencePaiement").value(hasItem(DEFAULT_FREQUENCE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].utilisateurId").value(hasItem(DEFAULT_UTILISATEUR_ID.intValue())));
    }

    @Test
    @Transactional
    void getSouscription() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        // Get the souscription
        restSouscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, souscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(souscription.getId().intValue()))
            .andExpect(jsonPath("$.numeroSouscription").value(DEFAULT_NUMERO_SOUSCRIPTION))
            .andExpect(jsonPath("$.dateSouscription").value(DEFAULT_DATE_SOUSCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.cumulSinistres").value(sameNumber(DEFAULT_CUMUL_SINISTRES)))
            .andExpect(jsonPath("$.frequencePaiement").value(DEFAULT_FREQUENCE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.utilisateurId").value(DEFAULT_UTILISATEUR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSouscription() throws Exception {
        // Get the souscription
        restSouscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSouscription() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the souscription
        Souscription updatedSouscription = souscriptionRepository.findById(souscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSouscription are not directly saved in db
        em.detach(updatedSouscription);
        updatedSouscription
            .numeroSouscription(UPDATED_NUMERO_SOUSCRIPTION)
            .dateSouscription(UPDATED_DATE_SOUSCRIPTION)
            .status(UPDATED_STATUS)
            .cumulSinistres(UPDATED_CUMUL_SINISTRES)
            .frequencePaiement(UPDATED_FREQUENCE_PAIEMENT)
            .utilisateurId(UPDATED_UTILISATEUR_ID);
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(updatedSouscription);

        restSouscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, souscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(souscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSouscriptionToMatchAllProperties(updatedSouscription);
    }

    @Test
    @Transactional
    void putNonExistingSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, souscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(souscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(souscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSouscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the souscription using partial update
        Souscription partialUpdatedSouscription = new Souscription();
        partialUpdatedSouscription.setId(souscription.getId());

        partialUpdatedSouscription.utilisateurId(UPDATED_UTILISATEUR_ID);

        restSouscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSouscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSouscription))
            )
            .andExpect(status().isOk());

        // Validate the Souscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSouscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSouscription, souscription),
            getPersistedSouscription(souscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateSouscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the souscription using partial update
        Souscription partialUpdatedSouscription = new Souscription();
        partialUpdatedSouscription.setId(souscription.getId());

        partialUpdatedSouscription
            .numeroSouscription(UPDATED_NUMERO_SOUSCRIPTION)
            .dateSouscription(UPDATED_DATE_SOUSCRIPTION)
            .status(UPDATED_STATUS)
            .cumulSinistres(UPDATED_CUMUL_SINISTRES)
            .frequencePaiement(UPDATED_FREQUENCE_PAIEMENT)
            .utilisateurId(UPDATED_UTILISATEUR_ID);

        restSouscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSouscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSouscription))
            )
            .andExpect(status().isOk());

        // Validate the Souscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSouscriptionUpdatableFieldsEquals(partialUpdatedSouscription, getPersistedSouscription(partialUpdatedSouscription));
    }

    @Test
    @Transactional
    void patchNonExistingSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, souscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(souscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(souscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSouscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        souscription.setId(longCount.incrementAndGet());

        // Create the Souscription
        SouscriptionDTO souscriptionDTO = souscriptionMapper.toDto(souscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(souscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Souscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSouscription() throws Exception {
        // Initialize the database
        insertedSouscription = souscriptionRepository.saveAndFlush(souscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the souscription
        restSouscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, souscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return souscriptionRepository.count();
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

    protected Souscription getPersistedSouscription(Souscription souscription) {
        return souscriptionRepository.findById(souscription.getId()).orElseThrow();
    }

    protected void assertPersistedSouscriptionToMatchAllProperties(Souscription expectedSouscription) {
        assertSouscriptionAllPropertiesEquals(expectedSouscription, getPersistedSouscription(expectedSouscription));
    }

    protected void assertPersistedSouscriptionToMatchUpdatableProperties(Souscription expectedSouscription) {
        assertSouscriptionAllUpdatablePropertiesEquals(expectedSouscription, getPersistedSouscription(expectedSouscription));
    }
}
