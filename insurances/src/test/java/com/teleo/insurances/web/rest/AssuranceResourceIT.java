package com.teleo.insurances.web.rest;

import static com.teleo.insurances.domain.AssuranceAsserts.*;
import static com.teleo.insurances.web.rest.TestUtil.createUpdateProxyForBean;
import static com.teleo.insurances.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.insurances.IntegrationTest;
import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.enumeration.InsuranceType;
import com.teleo.insurances.repository.AssuranceRepository;
import com.teleo.insurances.service.AssuranceService;
import com.teleo.insurances.service.dto.AssuranceDTO;
import com.teleo.insurances.service.mapper.AssuranceMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssuranceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssuranceResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final InsuranceType DEFAULT_TYPE = InsuranceType.PERSONNE;
    private static final InsuranceType UPDATED_TYPE = InsuranceType.BIEN;

    private static final BigDecimal DEFAULT_PRIX_ASSURANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_ASSURANCE = new BigDecimal(2);

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/assurances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Mock
    private AssuranceRepository assuranceRepositoryMock;

    @Autowired
    private AssuranceMapper assuranceMapper;

    @Mock
    private AssuranceService assuranceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssuranceMockMvc;

    private Assurance assurance;

    private Assurance insertedAssurance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assurance createEntity(EntityManager em) {
        Assurance assurance = new Assurance()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .conditions(DEFAULT_CONDITIONS)
            .type(DEFAULT_TYPE)
            .prixAssurance(DEFAULT_PRIX_ASSURANCE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return assurance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assurance createUpdatedEntity(EntityManager em) {
        Assurance assurance = new Assurance()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .conditions(UPDATED_CONDITIONS)
            .type(UPDATED_TYPE)
            .prixAssurance(UPDATED_PRIX_ASSURANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return assurance;
    }

    @BeforeEach
    public void initTest() {
        assurance = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAssurance != null) {
            assuranceRepository.delete(insertedAssurance);
            insertedAssurance = null;
        }
    }

    @Test
    @Transactional
    void createAssurance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);
        var returnedAssuranceDTO = om.readValue(
            restAssuranceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssuranceDTO.class
        );

        // Validate the Assurance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssurance = assuranceMapper.toEntity(returnedAssuranceDTO);
        assertAssuranceUpdatableFieldsEquals(returnedAssurance, getPersistedAssurance(returnedAssurance));

        insertedAssurance = returnedAssurance;
    }

    @Test
    @Transactional
    void createAssuranceWithExistingId() throws Exception {
        // Create the Assurance with an existing ID
        assurance.setId(1L);
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assurance.setNom(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assurance.setType(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixAssuranceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assurance.setPrixAssurance(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assurance.setDateDebut(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assurance.setDateFin(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssurances() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        // Get all the assuranceList
        restAssuranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].prixAssurance").value(hasItem(sameNumber(DEFAULT_PRIX_ASSURANCE))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssurancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assuranceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssuranceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assuranceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssurancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assuranceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssuranceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assuranceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssurance() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        // Get the assurance
        restAssuranceMockMvc
            .perform(get(ENTITY_API_URL_ID, assurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assurance.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.prixAssurance").value(sameNumber(DEFAULT_PRIX_ASSURANCE)))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssurance() throws Exception {
        // Get the assurance
        restAssuranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssurance() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assurance
        Assurance updatedAssurance = assuranceRepository.findById(assurance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssurance are not directly saved in db
        em.detach(updatedAssurance);
        updatedAssurance
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .conditions(UPDATED_CONDITIONS)
            .type(UPDATED_TYPE)
            .prixAssurance(UPDATED_PRIX_ASSURANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(updatedAssurance);

        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assuranceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssuranceToMatchAllProperties(updatedAssurance);
    }

    @Test
    @Transactional
    void putNonExistingAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssuranceWithPatch() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assurance using partial update
        Assurance partialUpdatedAssurance = new Assurance();
        partialUpdatedAssurance.setId(assurance.getId());

        partialUpdatedAssurance
            .description(UPDATED_DESCRIPTION)
            .conditions(UPDATED_CONDITIONS)
            .type(UPDATED_TYPE)
            .prixAssurance(UPDATED_PRIX_ASSURANCE)
            .dateDebut(UPDATED_DATE_DEBUT);

        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssurance))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssuranceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssurance, assurance),
            getPersistedAssurance(assurance)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssuranceWithPatch() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assurance using partial update
        Assurance partialUpdatedAssurance = new Assurance();
        partialUpdatedAssurance.setId(assurance.getId());

        partialUpdatedAssurance
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .conditions(UPDATED_CONDITIONS)
            .type(UPDATED_TYPE)
            .prixAssurance(UPDATED_PRIX_ASSURANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssurance))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssuranceUpdatableFieldsEquals(partialUpdatedAssurance, getPersistedAssurance(partialUpdatedAssurance));
    }

    @Test
    @Transactional
    void patchNonExistingAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssurance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assurance.setId(longCount.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assuranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assurance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssurance() throws Exception {
        // Initialize the database
        insertedAssurance = assuranceRepository.saveAndFlush(assurance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the assurance
        restAssuranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, assurance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return assuranceRepository.count();
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

    protected Assurance getPersistedAssurance(Assurance assurance) {
        return assuranceRepository.findById(assurance.getId()).orElseThrow();
    }

    protected void assertPersistedAssuranceToMatchAllProperties(Assurance expectedAssurance) {
        assertAssuranceAllPropertiesEquals(expectedAssurance, getPersistedAssurance(expectedAssurance));
    }

    protected void assertPersistedAssuranceToMatchUpdatableProperties(Assurance expectedAssurance) {
        assertAssuranceAllUpdatablePropertiesEquals(expectedAssurance, getPersistedAssurance(expectedAssurance));
    }
}
