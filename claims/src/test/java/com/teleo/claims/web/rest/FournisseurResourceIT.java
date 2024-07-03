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
