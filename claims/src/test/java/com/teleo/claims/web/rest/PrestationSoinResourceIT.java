package com.teleo.claims.web.rest;

import static com.teleo.claims.domain.PrestationSoinAsserts.*;
import static com.teleo.claims.web.rest.TestUtil.createUpdateProxyForBean;
import static com.teleo.claims.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleo.claims.IntegrationTest;
import com.teleo.claims.domain.PrestationSoin;
import com.teleo.claims.domain.enumeration.PrestationType;
import com.teleo.claims.repository.PrestationSoinRepository;
import com.teleo.claims.service.PrestationSoinService;
import com.teleo.claims.service.dto.PrestationSoinDTO;
import com.teleo.claims.service.mapper.PrestationSoinMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PrestationSoinResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrestationSoinResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final PrestationType DEFAULT_TYPE = PrestationType.CONSULTATION;
    private static final PrestationType UPDATED_TYPE = PrestationType.HOSPITALISATION;

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/prestation-soins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrestationSoinRepository prestationSoinRepository;

    @Mock
    private PrestationSoinRepository prestationSoinRepositoryMock;

    @Autowired
    private PrestationSoinMapper prestationSoinMapper;

    @Mock
    private PrestationSoinService prestationSoinServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestationSoinMockMvc;

    private PrestationSoin prestationSoin;

    private PrestationSoin insertedPrestationSoin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrestationSoin createEntity(EntityManager em) {
        PrestationSoin prestationSoin = new PrestationSoin()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .montant(DEFAULT_MONTANT);
        return prestationSoin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrestationSoin createUpdatedEntity(EntityManager em) {
        PrestationSoin prestationSoin = new PrestationSoin()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .montant(UPDATED_MONTANT);
        return prestationSoin;
    }

    @BeforeEach
    public void initTest() {
        prestationSoin = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPrestationSoin != null) {
            prestationSoinRepository.delete(insertedPrestationSoin);
            insertedPrestationSoin = null;
        }
    }

    @Test
    @Transactional
    void createPrestationSoin() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);
        var returnedPrestationSoinDTO = om.readValue(
            restPrestationSoinMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrestationSoinDTO.class
        );

        // Validate the PrestationSoin in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrestationSoin = prestationSoinMapper.toEntity(returnedPrestationSoinDTO);
        assertPrestationSoinUpdatableFieldsEquals(returnedPrestationSoin, getPersistedPrestationSoin(returnedPrestationSoin));

        insertedPrestationSoin = returnedPrestationSoin;
    }

    @Test
    @Transactional
    void createPrestationSoinWithExistingId() throws Exception {
        // Create the PrestationSoin with an existing ID
        prestationSoin.setId(1L);
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestationSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prestationSoin.setNom(null);

        // Create the PrestationSoin, which fails.
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        restPrestationSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prestationSoin.setType(null);

        // Create the PrestationSoin, which fails.
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        restPrestationSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prestationSoin.setMontant(null);

        // Create the PrestationSoin, which fails.
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        restPrestationSoinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrestationSoins() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        // Get all the prestationSoinList
        restPrestationSoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestationSoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationSoinsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prestationSoinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestationSoinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prestationSoinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationSoinsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prestationSoinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestationSoinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prestationSoinRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrestationSoin() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        // Get the prestationSoin
        restPrestationSoinMockMvc
            .perform(get(ENTITY_API_URL_ID, prestationSoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestationSoin.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.montant").value(sameNumber(DEFAULT_MONTANT)));
    }

    @Test
    @Transactional
    void getNonExistingPrestationSoin() throws Exception {
        // Get the prestationSoin
        restPrestationSoinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestationSoin() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestationSoin
        PrestationSoin updatedPrestationSoin = prestationSoinRepository.findById(prestationSoin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrestationSoin are not directly saved in db
        em.detach(updatedPrestationSoin);
        updatedPrestationSoin.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).montant(UPDATED_MONTANT);
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(updatedPrestationSoin);

        restPrestationSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationSoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationSoinDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrestationSoinToMatchAllProperties(updatedPrestationSoin);
    }

    @Test
    @Transactional
    void putNonExistingPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationSoinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestationSoinWithPatch() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestationSoin using partial update
        PrestationSoin partialUpdatedPrestationSoin = new PrestationSoin();
        partialUpdatedPrestationSoin.setId(prestationSoin.getId());

        partialUpdatedPrestationSoin.montant(UPDATED_MONTANT);

        restPrestationSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestationSoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestationSoin))
            )
            .andExpect(status().isOk());

        // Validate the PrestationSoin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationSoinUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrestationSoin, prestationSoin),
            getPersistedPrestationSoin(prestationSoin)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrestationSoinWithPatch() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestationSoin using partial update
        PrestationSoin partialUpdatedPrestationSoin = new PrestationSoin();
        partialUpdatedPrestationSoin.setId(prestationSoin.getId());

        partialUpdatedPrestationSoin.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).montant(UPDATED_MONTANT);

        restPrestationSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestationSoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestationSoin))
            )
            .andExpect(status().isOk());

        // Validate the PrestationSoin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationSoinUpdatableFieldsEquals(partialUpdatedPrestationSoin, getPersistedPrestationSoin(partialUpdatedPrestationSoin));
    }

    @Test
    @Transactional
    void patchNonExistingPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestationSoinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationSoinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestationSoin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestationSoin.setId(longCount.incrementAndGet());

        // Create the PrestationSoin
        PrestationSoinDTO prestationSoinDTO = prestationSoinMapper.toDto(prestationSoin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationSoinMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prestationSoinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrestationSoin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestationSoin() throws Exception {
        // Initialize the database
        insertedPrestationSoin = prestationSoinRepository.saveAndFlush(prestationSoin);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prestationSoin
        restPrestationSoinMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestationSoin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prestationSoinRepository.count();
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

    protected PrestationSoin getPersistedPrestationSoin(PrestationSoin prestationSoin) {
        return prestationSoinRepository.findById(prestationSoin.getId()).orElseThrow();
    }

    protected void assertPersistedPrestationSoinToMatchAllProperties(PrestationSoin expectedPrestationSoin) {
        assertPrestationSoinAllPropertiesEquals(expectedPrestationSoin, getPersistedPrestationSoin(expectedPrestationSoin));
    }

    protected void assertPersistedPrestationSoinToMatchUpdatableProperties(PrestationSoin expectedPrestationSoin) {
        assertPrestationSoinAllUpdatablePropertiesEquals(expectedPrestationSoin, getPersistedPrestationSoin(expectedPrestationSoin));
    }
}
