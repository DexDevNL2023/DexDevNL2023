package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.FournisseurAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.Fournisseur;
import com.teleo.claims.repository.FournisseurRepository;
import com.teleo.claims.service.dto.FournisseurDTO;
import com.teleo.claims.service.mapper.FournisseurMapper;
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
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FournisseurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICES_FOURNIS = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES_FOURNIS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT_CONTRAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT_CONTRAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN_CONTRAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN_CONTRAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fournisseurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurMapper fournisseurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    private Fournisseur insertedFournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .nom(DEFAULT_NOM)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .addresse(DEFAULT_ADDRESSE)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .servicesFournis(DEFAULT_SERVICES_FOURNIS)
            .dateDebutContrat(DEFAULT_DATE_DEBUT_CONTRAT)
            .dateFinContrat(DEFAULT_DATE_FIN_CONTRAT);
        return fournisseur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .servicesFournis(UPDATED_SERVICES_FOURNIS)
            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
            .dateFinContrat(UPDATED_DATE_FIN_CONTRAT);
        return fournisseur;
    }

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFournisseur != null) {
            fournisseurRepository.delete(insertedFournisseur);
            insertedFournisseur = null;
        }
    }

    @Test
    @Transactional
    void createFournisseur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);
        var returnedFournisseurDTO = om.readValue(
            restFournisseurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FournisseurDTO.class
        );

        // Validate the Fournisseur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFournisseur = fournisseurMapper.toEntity(returnedFournisseurDTO);
        assertFournisseurUpdatableFieldsEquals(returnedFournisseur, getPersistedFournisseur(returnedFournisseur));

        insertedFournisseur = returnedFournisseur;
    }

    @Test
    @Transactional
    void createFournisseurWithExistingId() throws Exception {
        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setNom(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setAdresse(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setTelephone(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setEmail(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setAddresse(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setVille(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setPays(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServicesFournisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setServicesFournis(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutContratIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fournisseur.setDateDebutContrat(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFournisseurs() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].servicesFournis").value(hasItem(DEFAULT_SERVICES_FOURNIS)))
            .andExpect(jsonPath("$.[*].dateDebutContrat").value(hasItem(DEFAULT_DATE_DEBUT_CONTRAT.toString())))
            .andExpect(jsonPath("$.[*].dateFinContrat").value(hasItem(DEFAULT_DATE_FIN_CONTRAT.toString())));
    }

    @Test
    @Transactional
    void getFournisseur() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL_ID, fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.addresse").value(DEFAULT_ADDRESSE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.servicesFournis").value(DEFAULT_SERVICES_FOURNIS))
            .andExpect(jsonPath("$.dateDebutContrat").value(DEFAULT_DATE_DEBUT_CONTRAT.toString()))
            .andExpect(jsonPath("$.dateFinContrat").value(DEFAULT_DATE_FIN_CONTRAT.toString()));
    }

    @Test
    @Transactional
    void getFournisseursByIdFiltering() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        Long id = fournisseur.getId();

        defaultFournisseurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFournisseurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFournisseurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom equals to
        defaultFournisseurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom in
        defaultFournisseurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom is not null
        defaultFournisseurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom contains
        defaultFournisseurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom does not contain
        defaultFournisseurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse equals to
        defaultFournisseurFiltering("adresse.equals=" + DEFAULT_ADRESSE, "adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse in
        defaultFournisseurFiltering("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE, "adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse is not null
        defaultFournisseurFiltering("adresse.specified=true", "adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse contains
        defaultFournisseurFiltering("adresse.contains=" + DEFAULT_ADRESSE, "adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where adresse does not contain
        defaultFournisseurFiltering("adresse.doesNotContain=" + UPDATED_ADRESSE, "adresse.doesNotContain=" + DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone equals to
        defaultFournisseurFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFournisseursByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone in
        defaultFournisseurFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFournisseursByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone is not null
        defaultFournisseurFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone contains
        defaultFournisseurFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFournisseursByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone does not contain
        defaultFournisseurFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllFournisseursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email equals to
        defaultFournisseurFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFournisseursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email in
        defaultFournisseurFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFournisseursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email is not null
        defaultFournisseurFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email contains
        defaultFournisseurFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFournisseursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email does not contain
        defaultFournisseurFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllFournisseursByAddresseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where addresse equals to
        defaultFournisseurFiltering("addresse.equals=" + DEFAULT_ADDRESSE, "addresse.equals=" + UPDATED_ADDRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAddresseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where addresse in
        defaultFournisseurFiltering("addresse.in=" + DEFAULT_ADDRESSE + "," + UPDATED_ADDRESSE, "addresse.in=" + UPDATED_ADDRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAddresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where addresse is not null
        defaultFournisseurFiltering("addresse.specified=true", "addresse.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByAddresseContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where addresse contains
        defaultFournisseurFiltering("addresse.contains=" + DEFAULT_ADDRESSE, "addresse.contains=" + UPDATED_ADDRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByAddresseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where addresse does not contain
        defaultFournisseurFiltering("addresse.doesNotContain=" + UPDATED_ADDRESSE, "addresse.doesNotContain=" + DEFAULT_ADDRESSE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville equals to
        defaultFournisseurFiltering("ville.equals=" + DEFAULT_VILLE, "ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville in
        defaultFournisseurFiltering("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE, "ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville is not null
        defaultFournisseurFiltering("ville.specified=true", "ville.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville contains
        defaultFournisseurFiltering("ville.contains=" + DEFAULT_VILLE, "ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville does not contain
        defaultFournisseurFiltering("ville.doesNotContain=" + UPDATED_VILLE, "ville.doesNotContain=" + DEFAULT_VILLE);
    }

    @Test
    @Transactional
    void getAllFournisseursByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays equals to
        defaultFournisseurFiltering("pays.equals=" + DEFAULT_PAYS, "pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllFournisseursByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays in
        defaultFournisseurFiltering("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS, "pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllFournisseursByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays is not null
        defaultFournisseurFiltering("pays.specified=true", "pays.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByPaysContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays contains
        defaultFournisseurFiltering("pays.contains=" + DEFAULT_PAYS, "pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllFournisseursByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays does not contain
        defaultFournisseurFiltering("pays.doesNotContain=" + UPDATED_PAYS, "pays.doesNotContain=" + DEFAULT_PAYS);
    }

    @Test
    @Transactional
    void getAllFournisseursByServicesFournisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where servicesFournis equals to
        defaultFournisseurFiltering(
            "servicesFournis.equals=" + DEFAULT_SERVICES_FOURNIS,
            "servicesFournis.equals=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByServicesFournisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where servicesFournis in
        defaultFournisseurFiltering(
            "servicesFournis.in=" + DEFAULT_SERVICES_FOURNIS + "," + UPDATED_SERVICES_FOURNIS,
            "servicesFournis.in=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByServicesFournisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where servicesFournis is not null
        defaultFournisseurFiltering("servicesFournis.specified=true", "servicesFournis.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByServicesFournisContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where servicesFournis contains
        defaultFournisseurFiltering(
            "servicesFournis.contains=" + DEFAULT_SERVICES_FOURNIS,
            "servicesFournis.contains=" + UPDATED_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByServicesFournisNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where servicesFournis does not contain
        defaultFournisseurFiltering(
            "servicesFournis.doesNotContain=" + UPDATED_SERVICES_FOURNIS,
            "servicesFournis.doesNotContain=" + DEFAULT_SERVICES_FOURNIS
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByDateDebutContratIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateDebutContrat equals to
        defaultFournisseurFiltering(
            "dateDebutContrat.equals=" + DEFAULT_DATE_DEBUT_CONTRAT,
            "dateDebutContrat.equals=" + UPDATED_DATE_DEBUT_CONTRAT
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByDateDebutContratIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateDebutContrat in
        defaultFournisseurFiltering(
            "dateDebutContrat.in=" + DEFAULT_DATE_DEBUT_CONTRAT + "," + UPDATED_DATE_DEBUT_CONTRAT,
            "dateDebutContrat.in=" + UPDATED_DATE_DEBUT_CONTRAT
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByDateDebutContratIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateDebutContrat is not null
        defaultFournisseurFiltering("dateDebutContrat.specified=true", "dateDebutContrat.specified=false");
    }

    @Test
    @Transactional
    void getAllFournisseursByDateFinContratIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateFinContrat equals to
        defaultFournisseurFiltering(
            "dateFinContrat.equals=" + DEFAULT_DATE_FIN_CONTRAT,
            "dateFinContrat.equals=" + UPDATED_DATE_FIN_CONTRAT
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByDateFinContratIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateFinContrat in
        defaultFournisseurFiltering(
            "dateFinContrat.in=" + DEFAULT_DATE_FIN_CONTRAT + "," + UPDATED_DATE_FIN_CONTRAT,
            "dateFinContrat.in=" + UPDATED_DATE_FIN_CONTRAT
        );
    }

    @Test
    @Transactional
    void getAllFournisseursByDateFinContratIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where dateFinContrat is not null
        defaultFournisseurFiltering("dateFinContrat.specified=true", "dateFinContrat.specified=false");
    }

    private void defaultFournisseurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFournisseurShouldBeFound(shouldBeFound);
        defaultFournisseurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFournisseurShouldBeFound(String filter) throws Exception {
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].servicesFournis").value(hasItem(DEFAULT_SERVICES_FOURNIS)))
            .andExpect(jsonPath("$.[*].dateDebutContrat").value(hasItem(DEFAULT_DATE_DEBUT_CONTRAT.toString())))
            .andExpect(jsonPath("$.[*].dateFinContrat").value(hasItem(DEFAULT_DATE_FIN_CONTRAT.toString())));

        // Check, that the count call also returns 1
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFournisseurShouldNotBeFound(String filter) throws Exception {
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFournisseur() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
        updatedFournisseur
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .servicesFournis(UPDATED_SERVICES_FOURNIS)
            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
            .dateFinContrat(UPDATED_DATE_FIN_CONTRAT);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(updatedFournisseur);

        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fournisseurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFournisseurToMatchAllProperties(updatedFournisseur);
    }

    @Test
    @Transactional
    void putNonExistingFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .servicesFournis(UPDATED_SERVICES_FOURNIS)
            .dateFinContrat(UPDATED_DATE_FIN_CONTRAT);

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFournisseurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFournisseur, fournisseur),
            getPersistedFournisseur(fournisseur)
        );
    }

    @Test
    @Transactional
    void fullUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .servicesFournis(UPDATED_SERVICES_FOURNIS)
            .dateDebutContrat(UPDATED_DATE_DEBUT_CONTRAT)
            .dateFinContrat(UPDATED_DATE_FIN_CONTRAT);

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFournisseurUpdatableFieldsEquals(partialUpdatedFournisseur, getPersistedFournisseur(partialUpdatedFournisseur));
    }

    @Test
    @Transactional
    void patchNonExistingFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFournisseur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fournisseur.setId(longCount.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fournisseurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFournisseur() throws Exception {
        // Initialize the database
        insertedFournisseur = fournisseurRepository.saveAndFlush(fournisseur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fournisseur
        restFournisseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, fournisseur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fournisseurRepository.count();
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

    protected Fournisseur getPersistedFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.findById(fournisseur.getId()).orElseThrow();
    }

    protected void assertPersistedFournisseurToMatchAllProperties(Fournisseur expectedFournisseur) {
        assertFournisseurAllPropertiesEquals(expectedFournisseur, getPersistedFournisseur(expectedFournisseur));
    }

    protected void assertPersistedFournisseurToMatchUpdatableProperties(Fournisseur expectedFournisseur) {
        assertFournisseurAllUpdatablePropertiesEquals(expectedFournisseur, getPersistedFournisseur(expectedFournisseur));
    }
}
