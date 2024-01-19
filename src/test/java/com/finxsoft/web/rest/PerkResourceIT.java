package com.finxsoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finxsoft.IntegrationTest;
import com.finxsoft.domain.Perk;
import com.finxsoft.repository.PerkRepository;
import com.finxsoft.service.dto.PerkDTO;
import com.finxsoft.service.mapper.PerkMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PerkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_EXPIRED = false;
    private static final Boolean UPDATED_EXPIRED = true;

    private static final String ENTITY_API_URL = "/api/perks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerkRepository perkRepository;

    @Autowired
    private PerkMapper perkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerkMockMvc;

    private Perk perk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perk createEntity(EntityManager em) {
        Perk perk = new Perk()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .active(DEFAULT_ACTIVE)
            .expired(DEFAULT_EXPIRED);
        return perk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perk createUpdatedEntity(EntityManager em) {
        Perk perk = new Perk()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .active(UPDATED_ACTIVE)
            .expired(UPDATED_EXPIRED);
        return perk;
    }

    @BeforeEach
    public void initTest() {
        perk = createEntity(em);
    }

    @Test
    @Transactional
    void createPerk() throws Exception {
        int databaseSizeBeforeCreate = perkRepository.findAll().size();
        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);
        restPerkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perkDTO)))
            .andExpect(status().isCreated());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeCreate + 1);
        Perk testPerk = perkList.get(perkList.size() - 1);
        assertThat(testPerk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerk.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPerk.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPerk.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPerk.getExpired()).isEqualTo(DEFAULT_EXPIRED);
    }

    @Test
    @Transactional
    void createPerkWithExistingId() throws Exception {
        // Create the Perk with an existing ID
        perk.setId(1L);
        PerkDTO perkDTO = perkMapper.toDto(perk);

        int databaseSizeBeforeCreate = perkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPerks() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        // Get all the perkList
        restPerkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perk.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED.booleanValue())));
    }

    @Test
    @Transactional
    void getPerk() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        // Get the perk
        restPerkMockMvc
            .perform(get(ENTITY_API_URL_ID, perk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perk.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.expired").value(DEFAULT_EXPIRED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPerk() throws Exception {
        // Get the perk
        restPerkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerk() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        int databaseSizeBeforeUpdate = perkRepository.findAll().size();

        // Update the perk
        Perk updatedPerk = perkRepository.findById(perk.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPerk are not directly saved in db
        em.detach(updatedPerk);
        updatedPerk
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .active(UPDATED_ACTIVE)
            .expired(UPDATED_EXPIRED);
        PerkDTO perkDTO = perkMapper.toDto(updatedPerk);

        restPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
        Perk testPerk = perkList.get(perkList.size() - 1);
        assertThat(testPerk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerk.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPerk.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPerk.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPerk.getExpired()).isEqualTo(UPDATED_EXPIRED);
    }

    @Test
    @Transactional
    void putNonExistingPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerkWithPatch() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        int databaseSizeBeforeUpdate = perkRepository.findAll().size();

        // Update the perk using partial update
        Perk partialUpdatedPerk = new Perk();
        partialUpdatedPerk.setId(perk.getId());

        partialUpdatedPerk.expirationDate(UPDATED_EXPIRATION_DATE).active(UPDATED_ACTIVE).expired(UPDATED_EXPIRED);

        restPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerk))
            )
            .andExpect(status().isOk());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
        Perk testPerk = perkList.get(perkList.size() - 1);
        assertThat(testPerk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerk.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPerk.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPerk.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPerk.getExpired()).isEqualTo(UPDATED_EXPIRED);
    }

    @Test
    @Transactional
    void fullUpdatePerkWithPatch() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        int databaseSizeBeforeUpdate = perkRepository.findAll().size();

        // Update the perk using partial update
        Perk partialUpdatedPerk = new Perk();
        partialUpdatedPerk.setId(perk.getId());

        partialUpdatedPerk
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .active(UPDATED_ACTIVE)
            .expired(UPDATED_EXPIRED);

        restPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerk))
            )
            .andExpect(status().isOk());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
        Perk testPerk = perkList.get(perkList.size() - 1);
        assertThat(testPerk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerk.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPerk.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPerk.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPerk.getExpired()).isEqualTo(UPDATED_EXPIRED);
    }

    @Test
    @Transactional
    void patchNonExistingPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerk() throws Exception {
        int databaseSizeBeforeUpdate = perkRepository.findAll().size();
        perk.setId(longCount.incrementAndGet());

        // Create the Perk
        PerkDTO perkDTO = perkMapper.toDto(perk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(perkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perk in the database
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerk() throws Exception {
        // Initialize the database
        perkRepository.saveAndFlush(perk);

        int databaseSizeBeforeDelete = perkRepository.findAll().size();

        // Delete the perk
        restPerkMockMvc
            .perform(delete(ENTITY_API_URL_ID, perk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Perk> perkList = perkRepository.findAll();
        assertThat(perkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
