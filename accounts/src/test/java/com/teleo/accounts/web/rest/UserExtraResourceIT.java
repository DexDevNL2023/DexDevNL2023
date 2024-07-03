package com.teleo.accounts.web.rest;

import static com.teleo.accounts.domain.UserExtraAsserts.*;
import static com.teleo.accounts.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.accounts.IntegrationTest;
import com.teleo.accounts.domain.User;
import com.teleo.accounts.domain.UserExtra;
import com.teleo.accounts.domain.enumeration.Gender;
import com.teleo.accounts.domain.enumeration.UserExtraType;
import com.teleo.accounts.repository.EntityManager;
import com.teleo.accounts.repository.UserExtraRepository;
import com.teleo.accounts.repository.UserRepository;
import com.teleo.accounts.repository.UserRepository;
import com.teleo.accounts.service.UserExtraService;
import com.teleo.accounts.service.dto.UserExtraDTO;
import com.teleo.accounts.service.mapper.UserExtraMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link UserExtraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserExtraResourceIT {

    private static final String DEFAULT_NUM_NIU = "AAAAAAAAAA";
    private static final String UPDATED_NUM_NIU = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "nqQ@a8)A3X.j";
    private static final String UPDATED_EMAIL = "ssRP@SuOr!/.W!";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NUM_CNI = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CNI = "BBBBBBBBBB";

    private static final Gender DEFAULT_SEXE = Gender.MALE;
    private static final Gender UPDATED_SEXE = Gender.FEMALE;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final UserExtraType DEFAULT_TYPE = UserExtraType.CLIENT;
    private static final UserExtraType UPDATED_TYPE = UserExtraType.AGENT;

    private static final byte[] DEFAULT_SIGNATURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SIGNATURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SIGNATURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SIGNATURE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/user-extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserExtraRepository userExtraRepositoryMock;

    @Autowired
    private UserExtraMapper userExtraMapper;

    @Mock
    private UserExtraService userExtraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserExtra userExtra;

    private UserExtra insertedUserExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .numNiu(DEFAULT_NUM_NIU)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .numCni(DEFAULT_NUM_CNI)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .addresse(DEFAULT_ADDRESSE)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .type(DEFAULT_TYPE)
            .signature(DEFAULT_SIGNATURE)
            .signatureContentType(DEFAULT_SIGNATURE_CONTENT_TYPE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        userExtra.setInternalUser(user);
        return userExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createUpdatedEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .numNiu(UPDATED_NUM_NIU)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .numCni(UPDATED_NUM_CNI)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .type(UPDATED_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        userExtra.setInternalUser(user);
        return userExtra;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserExtra.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        userExtra = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserExtra != null) {
            userExtraRepository.delete(insertedUserExtra).block();
            insertedUserExtra = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createUserExtra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);
        var returnedUserExtraDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserExtraDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserExtra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserExtra = userExtraMapper.toEntity(returnedUserExtraDTO);
        assertUserExtraUpdatableFieldsEquals(returnedUserExtra, getPersistedUserExtra(returnedUserExtra));

        assertUserExtraMapsIdRelationshipPersistedValue(userExtra, returnedUserExtra);

        insertedUserExtra = returnedUserExtra;
    }

    @Test
    void createUserExtraWithExistingId() throws Exception {
        // Create the UserExtra with an existing ID
        userExtra.setId(1L);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void updateUserExtraMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);

        // Load the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).block();
        assertThat(updatedUserExtra).isNotNull();

        // Update the User with new association value
        updatedUserExtra.setUser(user);
        UserExtraDTO updatedUserExtraDTO = userExtraMapper.toDto(updatedUserExtra);
        assertThat(updatedUserExtraDTO).isNotNull();

        // Update the entity
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUserExtraDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedUserExtraDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testUserExtra.getId()).isEqualTo(testUserExtra.getUser().getId());
         */
    }

    @Test
    void checkNumNiuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setNumNiu(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setNom(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setPrenom(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setEmail(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMotDePasseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setMotDePasse(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDateNaissanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setDateNaissance(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNumCniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setNumCni(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSexeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setSexe(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTelephoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setTelephone(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAddresseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setAddresse(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkVilleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setVille(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setPays(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setType(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserExtras() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userExtra.getId().intValue()))
            .jsonPath("$.[*].numNiu")
            .value(hasItem(DEFAULT_NUM_NIU))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].prenom")
            .value(hasItem(DEFAULT_PRENOM))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].motDePasse")
            .value(hasItem(DEFAULT_MOT_DE_PASSE))
            .jsonPath("$.[*].dateNaissance")
            .value(hasItem(DEFAULT_DATE_NAISSANCE.toString()))
            .jsonPath("$.[*].numCni")
            .value(hasItem(DEFAULT_NUM_CNI))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].telephone")
            .value(hasItem(DEFAULT_TELEPHONE))
            .jsonPath("$.[*].addresse")
            .value(hasItem(DEFAULT_ADDRESSE))
            .jsonPath("$.[*].ville")
            .value(hasItem(DEFAULT_VILLE))
            .jsonPath("$.[*].pays")
            .value(hasItem(DEFAULT_PAYS))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].signatureContentType")
            .value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .jsonPath("$.[*].signature")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_SIGNATURE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserExtrasWithEagerRelationshipsIsEnabled() {
        when(userExtraServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userExtraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserExtrasWithEagerRelationshipsIsNotEnabled() {
        when(userExtraServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userExtraRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserExtra() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get the userExtra
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userExtra.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userExtra.getId().intValue()))
            .jsonPath("$.numNiu")
            .value(is(DEFAULT_NUM_NIU))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.prenom")
            .value(is(DEFAULT_PRENOM))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.motDePasse")
            .value(is(DEFAULT_MOT_DE_PASSE))
            .jsonPath("$.dateNaissance")
            .value(is(DEFAULT_DATE_NAISSANCE.toString()))
            .jsonPath("$.numCni")
            .value(is(DEFAULT_NUM_CNI))
            .jsonPath("$.sexe")
            .value(is(DEFAULT_SEXE.toString()))
            .jsonPath("$.telephone")
            .value(is(DEFAULT_TELEPHONE))
            .jsonPath("$.addresse")
            .value(is(DEFAULT_ADDRESSE))
            .jsonPath("$.ville")
            .value(is(DEFAULT_VILLE))
            .jsonPath("$.pays")
            .value(is(DEFAULT_PAYS))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.signatureContentType")
            .value(is(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .jsonPath("$.signature")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_SIGNATURE)));
    }

    @Test
    void getUserExtrasByIdFiltering() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        Long id = userExtra.getId();

        defaultUserExtraFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserExtraFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserExtraFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllUserExtrasByNumNiuIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numNiu equals to
        defaultUserExtraFiltering("numNiu.equals=" + DEFAULT_NUM_NIU, "numNiu.equals=" + UPDATED_NUM_NIU);
    }

    @Test
    void getAllUserExtrasByNumNiuIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numNiu in
        defaultUserExtraFiltering("numNiu.in=" + DEFAULT_NUM_NIU + "," + UPDATED_NUM_NIU, "numNiu.in=" + UPDATED_NUM_NIU);
    }

    @Test
    void getAllUserExtrasByNumNiuIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numNiu is not null
        defaultUserExtraFiltering("numNiu.specified=true", "numNiu.specified=false");
    }

    @Test
    void getAllUserExtrasByNumNiuContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numNiu contains
        defaultUserExtraFiltering("numNiu.contains=" + DEFAULT_NUM_NIU, "numNiu.contains=" + UPDATED_NUM_NIU);
    }

    @Test
    void getAllUserExtrasByNumNiuNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numNiu does not contain
        defaultUserExtraFiltering("numNiu.doesNotContain=" + UPDATED_NUM_NIU, "numNiu.doesNotContain=" + DEFAULT_NUM_NIU);
    }

    @Test
    void getAllUserExtrasByNomIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where nom equals to
        defaultUserExtraFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    void getAllUserExtrasByNomIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where nom in
        defaultUserExtraFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    void getAllUserExtrasByNomIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where nom is not null
        defaultUserExtraFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    void getAllUserExtrasByNomContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where nom contains
        defaultUserExtraFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    void getAllUserExtrasByNomNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where nom does not contain
        defaultUserExtraFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    void getAllUserExtrasByPrenomIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where prenom equals to
        defaultUserExtraFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    void getAllUserExtrasByPrenomIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where prenom in
        defaultUserExtraFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    void getAllUserExtrasByPrenomIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where prenom is not null
        defaultUserExtraFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    void getAllUserExtrasByPrenomContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where prenom contains
        defaultUserExtraFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    void getAllUserExtrasByPrenomNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where prenom does not contain
        defaultUserExtraFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    void getAllUserExtrasByEmailIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where email equals to
        defaultUserExtraFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    void getAllUserExtrasByEmailIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where email in
        defaultUserExtraFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    void getAllUserExtrasByEmailIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where email is not null
        defaultUserExtraFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    void getAllUserExtrasByEmailContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where email contains
        defaultUserExtraFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    void getAllUserExtrasByEmailNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where email does not contain
        defaultUserExtraFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    void getAllUserExtrasByMotDePasseIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where motDePasse equals to
        defaultUserExtraFiltering("motDePasse.equals=" + DEFAULT_MOT_DE_PASSE, "motDePasse.equals=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    void getAllUserExtrasByMotDePasseIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where motDePasse in
        defaultUserExtraFiltering(
            "motDePasse.in=" + DEFAULT_MOT_DE_PASSE + "," + UPDATED_MOT_DE_PASSE,
            "motDePasse.in=" + UPDATED_MOT_DE_PASSE
        );
    }

    @Test
    void getAllUserExtrasByMotDePasseIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where motDePasse is not null
        defaultUserExtraFiltering("motDePasse.specified=true", "motDePasse.specified=false");
    }

    @Test
    void getAllUserExtrasByMotDePasseContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where motDePasse contains
        defaultUserExtraFiltering("motDePasse.contains=" + DEFAULT_MOT_DE_PASSE, "motDePasse.contains=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    void getAllUserExtrasByMotDePasseNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where motDePasse does not contain
        defaultUserExtraFiltering("motDePasse.doesNotContain=" + UPDATED_MOT_DE_PASSE, "motDePasse.doesNotContain=" + DEFAULT_MOT_DE_PASSE);
    }

    @Test
    void getAllUserExtrasByDateNaissanceIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where dateNaissance equals to
        defaultUserExtraFiltering("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE, "dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    void getAllUserExtrasByDateNaissanceIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where dateNaissance in
        defaultUserExtraFiltering(
            "dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE,
            "dateNaissance.in=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    void getAllUserExtrasByDateNaissanceIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where dateNaissance is not null
        defaultUserExtraFiltering("dateNaissance.specified=true", "dateNaissance.specified=false");
    }

    @Test
    void getAllUserExtrasByNumCniIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numCni equals to
        defaultUserExtraFiltering("numCni.equals=" + DEFAULT_NUM_CNI, "numCni.equals=" + UPDATED_NUM_CNI);
    }

    @Test
    void getAllUserExtrasByNumCniIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numCni in
        defaultUserExtraFiltering("numCni.in=" + DEFAULT_NUM_CNI + "," + UPDATED_NUM_CNI, "numCni.in=" + UPDATED_NUM_CNI);
    }

    @Test
    void getAllUserExtrasByNumCniIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numCni is not null
        defaultUserExtraFiltering("numCni.specified=true", "numCni.specified=false");
    }

    @Test
    void getAllUserExtrasByNumCniContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numCni contains
        defaultUserExtraFiltering("numCni.contains=" + DEFAULT_NUM_CNI, "numCni.contains=" + UPDATED_NUM_CNI);
    }

    @Test
    void getAllUserExtrasByNumCniNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where numCni does not contain
        defaultUserExtraFiltering("numCni.doesNotContain=" + UPDATED_NUM_CNI, "numCni.doesNotContain=" + DEFAULT_NUM_CNI);
    }

    @Test
    void getAllUserExtrasBySexeIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where sexe equals to
        defaultUserExtraFiltering("sexe.equals=" + DEFAULT_SEXE, "sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    void getAllUserExtrasBySexeIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where sexe in
        defaultUserExtraFiltering("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE, "sexe.in=" + UPDATED_SEXE);
    }

    @Test
    void getAllUserExtrasBySexeIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where sexe is not null
        defaultUserExtraFiltering("sexe.specified=true", "sexe.specified=false");
    }

    @Test
    void getAllUserExtrasByTelephoneIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where telephone equals to
        defaultUserExtraFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    void getAllUserExtrasByTelephoneIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where telephone in
        defaultUserExtraFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    void getAllUserExtrasByTelephoneIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where telephone is not null
        defaultUserExtraFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    void getAllUserExtrasByTelephoneContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where telephone contains
        defaultUserExtraFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    void getAllUserExtrasByTelephoneNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where telephone does not contain
        defaultUserExtraFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    void getAllUserExtrasByAddresseIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where addresse equals to
        defaultUserExtraFiltering("addresse.equals=" + DEFAULT_ADDRESSE, "addresse.equals=" + UPDATED_ADDRESSE);
    }

    @Test
    void getAllUserExtrasByAddresseIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where addresse in
        defaultUserExtraFiltering("addresse.in=" + DEFAULT_ADDRESSE + "," + UPDATED_ADDRESSE, "addresse.in=" + UPDATED_ADDRESSE);
    }

    @Test
    void getAllUserExtrasByAddresseIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where addresse is not null
        defaultUserExtraFiltering("addresse.specified=true", "addresse.specified=false");
    }

    @Test
    void getAllUserExtrasByAddresseContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where addresse contains
        defaultUserExtraFiltering("addresse.contains=" + DEFAULT_ADDRESSE, "addresse.contains=" + UPDATED_ADDRESSE);
    }

    @Test
    void getAllUserExtrasByAddresseNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where addresse does not contain
        defaultUserExtraFiltering("addresse.doesNotContain=" + UPDATED_ADDRESSE, "addresse.doesNotContain=" + DEFAULT_ADDRESSE);
    }

    @Test
    void getAllUserExtrasByVilleIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where ville equals to
        defaultUserExtraFiltering("ville.equals=" + DEFAULT_VILLE, "ville.equals=" + UPDATED_VILLE);
    }

    @Test
    void getAllUserExtrasByVilleIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where ville in
        defaultUserExtraFiltering("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE, "ville.in=" + UPDATED_VILLE);
    }

    @Test
    void getAllUserExtrasByVilleIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where ville is not null
        defaultUserExtraFiltering("ville.specified=true", "ville.specified=false");
    }

    @Test
    void getAllUserExtrasByVilleContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where ville contains
        defaultUserExtraFiltering("ville.contains=" + DEFAULT_VILLE, "ville.contains=" + UPDATED_VILLE);
    }

    @Test
    void getAllUserExtrasByVilleNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where ville does not contain
        defaultUserExtraFiltering("ville.doesNotContain=" + UPDATED_VILLE, "ville.doesNotContain=" + DEFAULT_VILLE);
    }

    @Test
    void getAllUserExtrasByPaysIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where pays equals to
        defaultUserExtraFiltering("pays.equals=" + DEFAULT_PAYS, "pays.equals=" + UPDATED_PAYS);
    }

    @Test
    void getAllUserExtrasByPaysIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where pays in
        defaultUserExtraFiltering("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS, "pays.in=" + UPDATED_PAYS);
    }

    @Test
    void getAllUserExtrasByPaysIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where pays is not null
        defaultUserExtraFiltering("pays.specified=true", "pays.specified=false");
    }

    @Test
    void getAllUserExtrasByPaysContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where pays contains
        defaultUserExtraFiltering("pays.contains=" + DEFAULT_PAYS, "pays.contains=" + UPDATED_PAYS);
    }

    @Test
    void getAllUserExtrasByPaysNotContainsSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where pays does not contain
        defaultUserExtraFiltering("pays.doesNotContain=" + UPDATED_PAYS, "pays.doesNotContain=" + DEFAULT_PAYS);
    }

    @Test
    void getAllUserExtrasByTypeIsEqualToSomething() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where type equals to
        defaultUserExtraFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    void getAllUserExtrasByTypeIsInShouldWork() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where type in
        defaultUserExtraFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    void getAllUserExtrasByTypeIsNullOrNotNull() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        // Get all the userExtraList where type is not null
        defaultUserExtraFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    void getAllUserExtrasByInternalUserIsEqualToSomething() {
        // Get already existing entity
        User internalUser = userExtra.getInternalUser();
        // Get all the userExtraList where internalUser equals to internalUserId
        defaultUserExtraShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the userExtraList where internalUser equals to (internalUserId + 1)
        defaultUserExtraShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultUserExtraFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultUserExtraShouldBeFound(shouldBeFound);
        defaultUserExtraShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserExtraShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userExtra.getId().intValue()))
            .jsonPath("$.[*].numNiu")
            .value(hasItem(DEFAULT_NUM_NIU))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].prenom")
            .value(hasItem(DEFAULT_PRENOM))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].motDePasse")
            .value(hasItem(DEFAULT_MOT_DE_PASSE))
            .jsonPath("$.[*].dateNaissance")
            .value(hasItem(DEFAULT_DATE_NAISSANCE.toString()))
            .jsonPath("$.[*].numCni")
            .value(hasItem(DEFAULT_NUM_CNI))
            .jsonPath("$.[*].sexe")
            .value(hasItem(DEFAULT_SEXE.toString()))
            .jsonPath("$.[*].telephone")
            .value(hasItem(DEFAULT_TELEPHONE))
            .jsonPath("$.[*].addresse")
            .value(hasItem(DEFAULT_ADDRESSE))
            .jsonPath("$.[*].ville")
            .value(hasItem(DEFAULT_VILLE))
            .jsonPath("$.[*].pays")
            .value(hasItem(DEFAULT_PAYS))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].signatureContentType")
            .value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .jsonPath("$.[*].signature")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_SIGNATURE)));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserExtraShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingUserExtra() {
        // Get the userExtra
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserExtra() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).block();
        updatedUserExtra
            .numNiu(UPDATED_NUM_NIU)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .numCni(UPDATED_NUM_CNI)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .type(UPDATED_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(updatedUserExtra);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userExtraDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserExtraToMatchAllProperties(updatedUserExtra);
    }

    @Test
    void putNonExistingUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userExtraDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra
            .numNiu(UPDATED_NUM_NIU)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .numCni(UPDATED_NUM_CNI)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .type(UPDATED_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserExtra))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserExtra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserExtraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserExtra, userExtra),
            getPersistedUserExtra(userExtra)
        );
    }

    @Test
    void fullUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra
            .numNiu(UPDATED_NUM_NIU)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .numCni(UPDATED_NUM_CNI)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .addresse(UPDATED_ADDRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .type(UPDATED_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserExtra))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserExtra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserExtraUpdatableFieldsEquals(partialUpdatedUserExtra, getPersistedUserExtra(partialUpdatedUserExtra));
    }

    @Test
    void patchNonExistingUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userExtraDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userExtraDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserExtra() {
        // Initialize the database
        insertedUserExtra = userExtraRepository.save(userExtra).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userExtra
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userExtra.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userExtraRepository.count().block();
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

    protected UserExtra getPersistedUserExtra(UserExtra userExtra) {
        return userExtraRepository.findById(userExtra.getId()).block();
    }

    protected void assertPersistedUserExtraToMatchAllProperties(UserExtra expectedUserExtra) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserExtraAllPropertiesEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
        assertUserExtraUpdatableFieldsEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
    }

    protected void assertPersistedUserExtraToMatchUpdatableProperties(UserExtra expectedUserExtra) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserExtraAllUpdatablePropertiesEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
        assertUserExtraUpdatableFieldsEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
    }
}
