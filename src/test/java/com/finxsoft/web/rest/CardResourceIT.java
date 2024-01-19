package com.finxsoft.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finxsoft.IntegrationTest;
import com.finxsoft.domain.Card;
import com.finxsoft.repository.CardRepository;
import com.finxsoft.service.dto.CardDTO;
import com.finxsoft.service.mapper.CardMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card().name(DEFAULT_NAME);
        return card;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card().name(UPDATED_NAME);
        return card;
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();
        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        // Create the Card with an existing ID
        card.setId(1L);
        CardDTO cardDTO = cardMapper.toDto(card);

        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get(ENTITY_API_URL_ID, card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard.name(UPDATED_NAME);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.name(UPDATED_NAME);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Delete the card
        restCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, card.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
