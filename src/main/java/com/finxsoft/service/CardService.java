package com.finxsoft.service;

import com.finxsoft.domain.Card;
import com.finxsoft.repository.CardRepository;
import com.finxsoft.service.dto.CardDTO;
import com.finxsoft.service.mapper.CardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.finxsoft.domain.Card}.
 */
@Service
@Transactional
public class CardService {

    private final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save.
     * @return the persisted entity.
     */
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    /**
     * Update a card.
     *
     * @param cardDTO the entity to save.
     * @return the persisted entity.
     */
    public CardDTO update(CardDTO cardDTO) {
        log.debug("Request to update Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    /**
     * Partially update a card.
     *
     * @param cardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CardDTO> partialUpdate(CardDTO cardDTO) {
        log.debug("Request to partially update Card : {}", cardDTO);

        return cardRepository
            .findById(cardDTO.getId())
            .map(existingCard -> {
                cardMapper.partialUpdate(existingCard, cardDTO);

                return existingCard;
            })
            .map(cardRepository::save)
            .map(cardMapper::toDto);
    }

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        return cardRepository.findAll(pageable).map(cardMapper::toDto);
    }

    /**
     * Get one card by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    /**
     * Delete the card by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
