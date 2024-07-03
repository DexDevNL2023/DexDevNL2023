package com.teleo.insurances.web.rest;

import static com.teleo.insurances.domain.PolicesAssurancesAsserts.*;
import static com.teleo.insurances.web.rest.TestUtil.createUpdateProxyForBean;
import static com.teleo.insurances.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.insurances.IntegrationTest;
import com.teleo.insurances.domain.Assurance;
import com.teleo.insurances.domain.PolicesAssurances;
import com.teleo.insurances.repository.PolicesAssurancesRepository;
import com.teleo.insurances.service.dto.PolicesAssurancesDTO;
import com.teleo.insurances.service.mapper.PolicesAssurancesMapper;
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
 * Integration tests for the {@link PolicesAssurancesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PolicesAssurancesResourceIT {

    private static final String DEFAULT_NUMERO_POLICE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_POLICE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_EMISSION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EMISSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EXPIRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT_ASSURE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_ASSURE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_ASSURE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PLAFOND_ASSURE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLAFOND_ASSURE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PLAFOND_ASSURE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/polices-assurances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PolicesAssurancesRepository policesAssurancesRepository;

    @Autowired
    private PolicesAssurancesMapper policesAssurancesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicesAssurancesMockMvc;

    private PolicesAssurances policesAssurances;

    private PolicesAssurances insertedPolicesAssurances;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicesAssurances createEntity(EntityManager em) {
        PolicesAssurances policesAssurances = new PolicesAssurances()
            .numeroPolice(DEFAULT_NUMERO_POLICE)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION)
            .description(DEFAULT_DESCRIPTION)
            .montantAssure(DEFAULT_MONTANT_ASSURE)
            .plafondAssure(DEFAULT_PLAFOND_ASSURE);
        return policesAssurances;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicesAssurances createUpdatedEntity(EntityManager em) {
        PolicesAssurances policesAssurances = new PolicesAssurances()
            .numeroPolice(UPDATED_NUMERO_POLICE)
            .dateEmission(UPDATED_DATE_EMISSION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .description(UPDATED_DESCRIPTION)
            .montantAssure(UPDATED_MONTANT_ASSURE)
            .plafondAssure(UPDATED_PLAFOND_ASSURE);
        return policesAssurances;
    }

    @BeforeEach
    public void initTest() {
        policesAssurances = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPolicesAssurances != null) {
            policesAssurancesRepository.delete(insertedPolicesAssurances);
            insertedPolicesAssurances = null;
        }
    }

    @Test
    @Transactional
    void createPolicesAssurances() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);
        var returnedPolicesAssurancesDTO = om.readValue(
            restPolicesAssurancesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PolicesAssurancesDTO.class
        );

        // Validate the PolicesAssurances in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPolicesAssurances = policesAssurancesMapper.toEntity(returnedPolicesAssurancesDTO);
        assertPolicesAssurancesUpdatableFieldsEquals(returnedPolicesAssurances, getPersistedPolicesAssurances(returnedPolicesAssurances));

        insertedPolicesAssurances = returnedPolicesAssurances;
    }

    @Test
    @Transactional
    void createPolicesAssurancesWithExistingId() throws Exception {
        // Create the PolicesAssurances with an existing ID
        policesAssurances.setId(1L);
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroPoliceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policesAssurances.setNumeroPolice(null);

        // Create the PolicesAssurances, which fails.
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEmissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policesAssurances.setDateEmission(null);

        // Create the PolicesAssurances, which fails.
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateExpirationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policesAssurances.setDateExpiration(null);

        // Create the PolicesAssurances, which fails.
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantAssureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policesAssurances.setMontantAssure(null);

        // Create the PolicesAssurances, which fails.
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlafondAssureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policesAssurances.setPlafondAssure(null);

        // Create the PolicesAssurances, which fails.
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        restPolicesAssurancesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPolicesAssurances() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policesAssurances.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroPolice").value(hasItem(DEFAULT_NUMERO_POLICE)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].montantAssure").value(hasItem(sameNumber(DEFAULT_MONTANT_ASSURE))))
            .andExpect(jsonPath("$.[*].plafondAssure").value(hasItem(sameNumber(DEFAULT_PLAFOND_ASSURE))));
    }

    @Test
    @Transactional
    void getPolicesAssurances() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get the policesAssurances
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL_ID, policesAssurances.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policesAssurances.getId().intValue()))
            .andExpect(jsonPath("$.numeroPolice").value(DEFAULT_NUMERO_POLICE))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.montantAssure").value(sameNumber(DEFAULT_MONTANT_ASSURE)))
            .andExpect(jsonPath("$.plafondAssure").value(sameNumber(DEFAULT_PLAFOND_ASSURE)));
    }

    @Test
    @Transactional
    void getPolicesAssurancesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        Long id = policesAssurances.getId();

        defaultPolicesAssurancesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPolicesAssurancesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPolicesAssurancesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByNumeroPoliceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where numeroPolice equals to
        defaultPolicesAssurancesFiltering("numeroPolice.equals=" + DEFAULT_NUMERO_POLICE, "numeroPolice.equals=" + UPDATED_NUMERO_POLICE);
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByNumeroPoliceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where numeroPolice in
        defaultPolicesAssurancesFiltering(
            "numeroPolice.in=" + DEFAULT_NUMERO_POLICE + "," + UPDATED_NUMERO_POLICE,
            "numeroPolice.in=" + UPDATED_NUMERO_POLICE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByNumeroPoliceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where numeroPolice is not null
        defaultPolicesAssurancesFiltering("numeroPolice.specified=true", "numeroPolice.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByNumeroPoliceContainsSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where numeroPolice contains
        defaultPolicesAssurancesFiltering(
            "numeroPolice.contains=" + DEFAULT_NUMERO_POLICE,
            "numeroPolice.contains=" + UPDATED_NUMERO_POLICE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByNumeroPoliceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where numeroPolice does not contain
        defaultPolicesAssurancesFiltering(
            "numeroPolice.doesNotContain=" + UPDATED_NUMERO_POLICE,
            "numeroPolice.doesNotContain=" + DEFAULT_NUMERO_POLICE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateEmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateEmission equals to
        defaultPolicesAssurancesFiltering("dateEmission.equals=" + DEFAULT_DATE_EMISSION, "dateEmission.equals=" + UPDATED_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateEmissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateEmission in
        defaultPolicesAssurancesFiltering(
            "dateEmission.in=" + DEFAULT_DATE_EMISSION + "," + UPDATED_DATE_EMISSION,
            "dateEmission.in=" + UPDATED_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateEmissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateEmission is not null
        defaultPolicesAssurancesFiltering("dateEmission.specified=true", "dateEmission.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateExpirationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateExpiration equals to
        defaultPolicesAssurancesFiltering(
            "dateExpiration.equals=" + DEFAULT_DATE_EXPIRATION,
            "dateExpiration.equals=" + UPDATED_DATE_EXPIRATION
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateExpirationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateExpiration in
        defaultPolicesAssurancesFiltering(
            "dateExpiration.in=" + DEFAULT_DATE_EXPIRATION + "," + UPDATED_DATE_EXPIRATION,
            "dateExpiration.in=" + UPDATED_DATE_EXPIRATION
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDateExpirationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where dateExpiration is not null
        defaultPolicesAssurancesFiltering("dateExpiration.specified=true", "dateExpiration.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where description equals to
        defaultPolicesAssurancesFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where description in
        defaultPolicesAssurancesFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where description is not null
        defaultPolicesAssurancesFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where description contains
        defaultPolicesAssurancesFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where description does not contain
        defaultPolicesAssurancesFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure equals to
        defaultPolicesAssurancesFiltering(
            "montantAssure.equals=" + DEFAULT_MONTANT_ASSURE,
            "montantAssure.equals=" + UPDATED_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure in
        defaultPolicesAssurancesFiltering(
            "montantAssure.in=" + DEFAULT_MONTANT_ASSURE + "," + UPDATED_MONTANT_ASSURE,
            "montantAssure.in=" + UPDATED_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure is not null
        defaultPolicesAssurancesFiltering("montantAssure.specified=true", "montantAssure.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure is greater than or equal to
        defaultPolicesAssurancesFiltering(
            "montantAssure.greaterThanOrEqual=" + DEFAULT_MONTANT_ASSURE,
            "montantAssure.greaterThanOrEqual=" + UPDATED_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure is less than or equal to
        defaultPolicesAssurancesFiltering(
            "montantAssure.lessThanOrEqual=" + DEFAULT_MONTANT_ASSURE,
            "montantAssure.lessThanOrEqual=" + SMALLER_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure is less than
        defaultPolicesAssurancesFiltering(
            "montantAssure.lessThan=" + UPDATED_MONTANT_ASSURE,
            "montantAssure.lessThan=" + DEFAULT_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByMontantAssureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where montantAssure is greater than
        defaultPolicesAssurancesFiltering(
            "montantAssure.greaterThan=" + SMALLER_MONTANT_ASSURE,
            "montantAssure.greaterThan=" + DEFAULT_MONTANT_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure equals to
        defaultPolicesAssurancesFiltering(
            "plafondAssure.equals=" + DEFAULT_PLAFOND_ASSURE,
            "plafondAssure.equals=" + UPDATED_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure in
        defaultPolicesAssurancesFiltering(
            "plafondAssure.in=" + DEFAULT_PLAFOND_ASSURE + "," + UPDATED_PLAFOND_ASSURE,
            "plafondAssure.in=" + UPDATED_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure is not null
        defaultPolicesAssurancesFiltering("plafondAssure.specified=true", "plafondAssure.specified=false");
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure is greater than or equal to
        defaultPolicesAssurancesFiltering(
            "plafondAssure.greaterThanOrEqual=" + DEFAULT_PLAFOND_ASSURE,
            "plafondAssure.greaterThanOrEqual=" + UPDATED_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure is less than or equal to
        defaultPolicesAssurancesFiltering(
            "plafondAssure.lessThanOrEqual=" + DEFAULT_PLAFOND_ASSURE,
            "plafondAssure.lessThanOrEqual=" + SMALLER_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure is less than
        defaultPolicesAssurancesFiltering(
            "plafondAssure.lessThan=" + UPDATED_PLAFOND_ASSURE,
            "plafondAssure.lessThan=" + DEFAULT_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByPlafondAssureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        // Get all the policesAssurancesList where plafondAssure is greater than
        defaultPolicesAssurancesFiltering(
            "plafondAssure.greaterThan=" + SMALLER_PLAFOND_ASSURE,
            "plafondAssure.greaterThan=" + DEFAULT_PLAFOND_ASSURE
        );
    }

    @Test
    @Transactional
    void getAllPolicesAssurancesByAssuranceIsEqualToSomething() throws Exception {
        Assurance assurance;
        if (TestUtil.findAll(em, Assurance.class).isEmpty()) {
            policesAssurancesRepository.saveAndFlush(policesAssurances);
            assurance = AssuranceResourceIT.createEntity(em);
        } else {
            assurance = TestUtil.findAll(em, Assurance.class).get(0);
        }
        em.persist(assurance);
        em.flush();
        policesAssurances.setAssurance(assurance);
        policesAssurancesRepository.saveAndFlush(policesAssurances);
        Long assuranceId = assurance.getId();
        // Get all the policesAssurancesList where assurance equals to assuranceId
        defaultPolicesAssurancesShouldBeFound("assuranceId.equals=" + assuranceId);

        // Get all the policesAssurancesList where assurance equals to (assuranceId + 1)
        defaultPolicesAssurancesShouldNotBeFound("assuranceId.equals=" + (assuranceId + 1));
    }

    private void defaultPolicesAssurancesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPolicesAssurancesShouldBeFound(shouldBeFound);
        defaultPolicesAssurancesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPolicesAssurancesShouldBeFound(String filter) throws Exception {
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policesAssurances.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroPolice").value(hasItem(DEFAULT_NUMERO_POLICE)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].montantAssure").value(hasItem(sameNumber(DEFAULT_MONTANT_ASSURE))))
            .andExpect(jsonPath("$.[*].plafondAssure").value(hasItem(sameNumber(DEFAULT_PLAFOND_ASSURE))));

        // Check, that the count call also returns 1
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPolicesAssurancesShouldNotBeFound(String filter) throws Exception {
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPolicesAssurancesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPolicesAssurances() throws Exception {
        // Get the policesAssurances
        restPolicesAssurancesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPolicesAssurances() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policesAssurances
        PolicesAssurances updatedPolicesAssurances = policesAssurancesRepository.findById(policesAssurances.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPolicesAssurances are not directly saved in db
        em.detach(updatedPolicesAssurances);
        updatedPolicesAssurances
            .numeroPolice(UPDATED_NUMERO_POLICE)
            .dateEmission(UPDATED_DATE_EMISSION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .description(UPDATED_DESCRIPTION)
            .montantAssure(UPDATED_MONTANT_ASSURE)
            .plafondAssure(UPDATED_PLAFOND_ASSURE);
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(updatedPolicesAssurances);

        restPolicesAssurancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policesAssurancesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policesAssurancesDTO))
            )
            .andExpect(status().isOk());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPolicesAssurancesToMatchAllProperties(updatedPolicesAssurances);
    }

    @Test
    @Transactional
    void putNonExistingPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policesAssurancesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policesAssurancesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policesAssurancesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePolicesAssurancesWithPatch() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policesAssurances using partial update
        PolicesAssurances partialUpdatedPolicesAssurances = new PolicesAssurances();
        partialUpdatedPolicesAssurances.setId(policesAssurances.getId());

        partialUpdatedPolicesAssurances.montantAssure(UPDATED_MONTANT_ASSURE);

        restPolicesAssurancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicesAssurances.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicesAssurances))
            )
            .andExpect(status().isOk());

        // Validate the PolicesAssurances in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicesAssurancesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPolicesAssurances, policesAssurances),
            getPersistedPolicesAssurances(policesAssurances)
        );
    }

    @Test
    @Transactional
    void fullUpdatePolicesAssurancesWithPatch() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policesAssurances using partial update
        PolicesAssurances partialUpdatedPolicesAssurances = new PolicesAssurances();
        partialUpdatedPolicesAssurances.setId(policesAssurances.getId());

        partialUpdatedPolicesAssurances
            .numeroPolice(UPDATED_NUMERO_POLICE)
            .dateEmission(UPDATED_DATE_EMISSION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .description(UPDATED_DESCRIPTION)
            .montantAssure(UPDATED_MONTANT_ASSURE)
            .plafondAssure(UPDATED_PLAFOND_ASSURE);

        restPolicesAssurancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicesAssurances.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicesAssurances))
            )
            .andExpect(status().isOk());

        // Validate the PolicesAssurances in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicesAssurancesUpdatableFieldsEquals(
            partialUpdatedPolicesAssurances,
            getPersistedPolicesAssurances(partialUpdatedPolicesAssurances)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, policesAssurancesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policesAssurancesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policesAssurancesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolicesAssurances() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policesAssurances.setId(longCount.incrementAndGet());

        // Create the PolicesAssurances
        PolicesAssurancesDTO policesAssurancesDTO = policesAssurancesMapper.toDto(policesAssurances);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicesAssurancesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(policesAssurancesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicesAssurances in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePolicesAssurances() throws Exception {
        // Initialize the database
        insertedPolicesAssurances = policesAssurancesRepository.saveAndFlush(policesAssurances);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the policesAssurances
        restPolicesAssurancesMockMvc
            .perform(delete(ENTITY_API_URL_ID, policesAssurances.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return policesAssurancesRepository.count();
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

    protected PolicesAssurances getPersistedPolicesAssurances(PolicesAssurances policesAssurances) {
        return policesAssurancesRepository.findById(policesAssurances.getId()).orElseThrow();
    }

    protected void assertPersistedPolicesAssurancesToMatchAllProperties(PolicesAssurances expectedPolicesAssurances) {
        assertPolicesAssurancesAllPropertiesEquals(expectedPolicesAssurances, getPersistedPolicesAssurances(expectedPolicesAssurances));
    }

    protected void assertPersistedPolicesAssurancesToMatchUpdatableProperties(PolicesAssurances expectedPolicesAssurances) {
        assertPolicesAssurancesAllUpdatablePropertiesEquals(
            expectedPolicesAssurances,
            getPersistedPolicesAssurances(expectedPolicesAssurances)
        );
    }
}
