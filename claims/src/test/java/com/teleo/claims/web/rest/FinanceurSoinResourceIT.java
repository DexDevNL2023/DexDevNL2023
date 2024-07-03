package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.FinanceurSoinAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.FinanceurSoin;
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.domain.enumeration.FinanceurType;
import com.teleo.claims.repository.FinanceurSoinRepository;
import com.teleo.claims.service.dto.FinanceurSoinDTO;
import com.teleo.claims.service.mapper.FinanceurSoinMapper;
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
 * Integration tests for the {@link FinanceurSoinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinanceurSoinResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final FinanceurType DEFAULT_TYPE = FinanceurType.ASSUREUR;
    private static final FinanceurType UPDATED_TYPE = FinanceurType.MUTUELLE;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICES_FOURNIS = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES_FOURNIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/financeur-soins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinanceurSoinRepository financeurSoinRepository;

    @Autowired
    private FinanceurSoinMapper financeurSoinMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinanceurSoinMockMvc;

    private FinanceurSoin financeurSoin;

    private FinanceurSoin insertedFinanceurSoin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinanceurSoin createEntity(EntityManager em) {
        FinanceurSoin financeurSoin = new FinanceurSoin()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .servicesFournis(DEFAULT_SERVICES_FOURNIS);
        return financeurSoin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinanceurSoin createUpdatedEntity(EntityManager em) {
        FinanceurSoin financeurSoin = new FinanceurSoin()
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .servicesFournis(UPDATED_SERVICES_FOURNIS);
        return financeurSoin;
    }

    @BeforeEach
    public void initTest() {
        financeurSoin = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFinanceurSoin != null) {
            financeurSoinRepository.delete(insertedFinanceurSoin);
            insertedFinanceurSoin = null;
        }
    }

    @Test
    @Transactional
    void createFinanceurSoin() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);
        var returnedFinanceurSoinDTO = om.readValue(
            restFinanceurSoinMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FinanceurSoinDTO.class
        );

        // Validate the FinanceurSoin in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFinanceurSoin = financeurSoinMapper.toEntity(returnedFinanceurSoinDTO);
        assertFinanceurSoinUpdatableFieldsEquals(returnedFinanceurSoin, getPersistedFinanceurSoin(returnedFinanceurSoin));

        insertedFinanceurSoin = returnedFinanceurSoin;
    }

    @Test
    @Transactional
    void createFinanceurSoinWithExistingId() throws Exception {
        // Create the FinanceurSoin with an existing ID
        financeurSoin.setId(1L);
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setNom(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setType(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setAdresse(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setTelephone(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setEmail(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServicesFournisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financeurSoin.setServicesFournis(null);

        // Create the FinanceurSoin, which fails.
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        restFinanceurSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinanceurSoins() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financeurSoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].servicesFournis").value(hasItem(DEFAULT_SERVICES_FOURNIS)));
    }

    @Test
    @Transactional
    void getFinanceurSoin() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get the financeurSoin
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL_ID, financeurSoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financeurSoin.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.servicesFournis").value(DEFAULT_SERVICES_FOURNIS));
    }

    @Test
    @Transactional
    void getFinanceurSoinsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        Long id = financeurSoin.getId();

        defaultFinanceurSoinFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFinanceurSoinFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFinanceurSoinFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where nom equals to
        defaultFinanceurSoinFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where nom in
        defaultFinanceurSoinFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where nom is not null
        defaultFinanceurSoinFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where nom contains
        defaultFinanceurSoinFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where nom does not contain
        defaultFinanceurSoinFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where type equals to
        defaultFinanceurSoinFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where type in
        defaultFinanceurSoinFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where type is not null
        defaultFinanceurSoinFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where adresse equals to
        defaultFinanceurSoinFiltering("adresse.equals=" + DEFAULT_ADRESSE, "adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where adresse in
        defaultFinanceurSoinFiltering("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE, "adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where adresse is not null
        defaultFinanceurSoinFiltering("adresse.specified=true", "adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where adresse contains
        defaultFinanceurSoinFiltering("adresse.contains=" + DEFAULT_ADRESSE, "adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where adresse does not contain
        defaultFinanceurSoinFiltering("adresse.doesNotContain=" + UPDATED_ADRESSE, "adresse.doesNotContain=" + DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where telephone equals to
        defaultFinanceurSoinFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where telephone in
        defaultFinanceurSoinFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where telephone is not null
        defaultFinanceurSoinFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where telephone contains
        defaultFinanceurSoinFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where telephone does not contain
        defaultFinanceurSoinFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where email equals to
        defaultFinanceurSoinFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where email in
        defaultFinanceurSoinFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where email is not null
        defaultFinanceurSoinFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where email contains
        defaultFinanceurSoinFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where email does not contain
        defaultFinanceurSoinFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByServicesFournisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where servicesFournis equals to
        defaultFinanceurSoinFiltering(
            "servicesFournis.equals=" + DEFAULT_SERVICES_FOURNIS,
            "servicesFournis.equals=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByServicesFournisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where servicesFournis in
        defaultFinanceurSoinFiltering(
            "servicesFournis.in=" + DEFAULT_SERVICES_FOURNIS + "," + UPDATED_SERVICES_FOURNIS,
            "servicesFournis.in=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByServicesFournisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where servicesFournis is not null
        defaultFinanceurSoinFiltering("servicesFournis.specified=true", "servicesFournis.specified=false");
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByServicesFournisContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where servicesFournis contains
        defaultFinanceurSoinFiltering(
            "servicesFournis.contains=" + DEFAULT_SERVICES_FOURNIS,
            "servicesFournis.contains=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByServicesFournisNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        // Get all the financeurSoinList where servicesFournis does not contain
        defaultFinanceurSoinFiltering(
            "servicesFournis.doesNotContain=" + UPDATED_SERVICES_FOURNIS,
            "servicesFournis.doesNotContain=" + DEFAULT_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFinanceurSoinsByPrestationSoinIsEqualToSomething() throws Exception {
        PrestationSoin prestationSoin;
        if (TestUtil.findAll(em, PrestationSoin.class).isEmpty()) {
            financeurSoinRepository.saveAndFlush(financeurSoin);
            prestationSoin = PrestationSoinResourceIT.createEntity(em);
        } else {
            prestationSoin = TestUtil.findAll(em, PrestationSoin.class).get(0);
        }
        em.persist(prestationSoin);
        em.flush();
        financeurSoin.addPrestationSoin(prestationSoin);
        financeurSoinRepository.saveAndFlush(financeurSoin);
        Long prestationSoinId = prestationSoin.getId();
        // Get all the financeurSoinList where prestationSoin equals to prestationSoinId
        defaultFinanceurSoinShouldBeFound("prestationSoinId.equals=" + prestationSoinId);

        // Get all the financeurSoinList where prestationSoin equals to (prestationSoinId + 1)
        defaultFinanceurSoinShouldNotBeFound("prestationSoinId.equals=" + (prestationSoinId + 1));
    }

    private void defaultFinanceurSoinFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFinanceurSoinShouldBeFound(shouldBeFound);
        defaultFinanceurSoinShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinanceurSoinShouldBeFound(String filter) throws Exception {
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financeurSoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].servicesFournis").value(hasItem(DEFAULT_SERVICES_FOURNIS)));

        // Check, that the count call also returns 1
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinanceurSoinShouldNotBeFound(String filter) throws Exception {
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinanceurSoinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFinanceurSoin() throws Exception {
        // Get the financeurSoin
        restFinanceurSoinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinanceurSoin() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeurSoin
        FinanceurSoin updatedFinanceurSoin = financeurSoinRepository.findById(financeurSoin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFinanceurSoin are not directly saved in db
        em.detach(updatedFinanceurSoin);
        updatedFinanceurSoin
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .servicesFournis(UPDATED_SERVICES_FOURNIS);
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(updatedFinanceurSoin);

        restFinanceurSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financeurSoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financeurSoinDTO))
            )
            .andExpect(status().isOk());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinanceurSoinToMatchAllProperties(updatedFinanceurSoin);
    }

    @Test
    @Transactional
    void putNonExistingFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financeurSoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financeurSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financeurSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinanceurSoinWithPatch() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeurSoin using partial update
        FinanceurSoin partialUpdatedFinanceurSoin = new FinanceurSoin();
        partialUpdatedFinanceurSoin.setId(financeurSoin.getId());

        partialUpdatedFinanceurSoin.nom(UPDATED_NOM).adresse(UPDATED_ADRESSE);

        restFinanceurSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinanceurSoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinanceurSoin))
            )
            .andExpect(status().isOk());

        // Validate the FinanceurSoin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinanceurSoinUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinanceurSoin, financeurSoin),
            getPersistedFinanceurSoin(financeurSoin)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinanceurSoinWithPatch() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financeurSoin using partial update
        FinanceurSoin partialUpdatedFinanceurSoin = new FinanceurSoin();
        partialUpdatedFinanceurSoin.setId(financeurSoin.getId());

        partialUpdatedFinanceurSoin
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .servicesFournis(UPDATED_SERVICES_FOURNIS);

        restFinanceurSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinanceurSoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinanceurSoin))
            )
            .andExpect(status().isOk());

        // Validate the FinanceurSoin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinanceurSoinUpdatableFieldsEquals(partialUpdatedFinanceurSoin, getPersistedFinanceurSoin(partialUpdatedFinanceurSoin));
    }

    @Test
    @Transactional
    void patchNonExistingFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financeurSoinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financeurSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financeurSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinanceurSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financeurSoin.setId(longCount.incrementAndGet());

        // Create the FinanceurSoin
        FinanceurSoinDTO financeurSoinDTO = financeurSoinMapper.toDto(financeurSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinanceurSoinMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(financeurSoinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinanceurSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinanceurSoin() throws Exception {
        // Initialize the database
        insertedFinanceurSoin = financeurSoinRepository.saveAndFlush(financeurSoin);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financeurSoin
        restFinanceurSoinMockMvc
            .perform(delete(ENTITY_API_URL_ID, financeurSoin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financeurSoinRepository.count();
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

    protected FinanceurSoin getPersistedFinanceurSoin(FinanceurSoin financeurSoin) {
        return financeurSoinRepository.findById(financeurSoin.getId()).orElseThrow();
    }

    protected void assertPersistedFinanceurSoinToMatchAllProperties(FinanceurSoin expectedFinanceurSoin) {
        assertFinanceurSoinAllPropertiesEquals(expectedFinanceurSoin, getPersistedFinanceurSoin(expectedFinanceurSoin));
    }

    protected void assertPersistedFinanceurSoinToMatchUpdatableProperties(FinanceurSoin expectedFinanceurSoin) {
        assertFinanceurSoinAllUpdatablePropertiesEquals(expectedFinanceurSoin, getPersistedFinanceurSoin(expectedFinanceurSoin));
    }
}
