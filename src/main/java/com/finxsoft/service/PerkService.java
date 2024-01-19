package com.finxsoft.service;

import com.finxsoft.domain.Perk;
import com.finxsoft.repository.PerkRepository;
import com.finxsoft.service.dto.PerkDTO;
import com.finxsoft.service.mapper.PerkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.finxsoft.domain.Perk}.
 */
@Service
@Transactional
public class PerkService {

    private final Logger log = LoggerFactory.getLogger(PerkService.class);

    private final PerkRepository perkRepository;

    private final PerkMapper perkMapper;

    public PerkService(PerkRepository perkRepository, PerkMapper perkMapper) {
        this.perkRepository = perkRepository;
        this.perkMapper = perkMapper;
    }

    /**
     * Save a perk.
     *
     * @param perkDTO the entity to save.
     * @return the persisted entity.
     */
    public PerkDTO save(PerkDTO perkDTO) {
        log.debug("Request to save Perk : {}", perkDTO);
        Perk perk = perkMapper.toEntity(perkDTO);
        perk = perkRepository.save(perk);
        return perkMapper.toDto(perk);
    }

    /**
     * Update a perk.
     *
     * @param perkDTO the entity to save.
     * @return the persisted entity.
     */
    public PerkDTO update(PerkDTO perkDTO) {
        log.debug("Request to update Perk : {}", perkDTO);
        Perk perk = perkMapper.toEntity(perkDTO);
        perk = perkRepository.save(perk);
        return perkMapper.toDto(perk);
    }

    /**
     * Partially update a perk.
     *
     * @param perkDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PerkDTO> partialUpdate(PerkDTO perkDTO) {
        log.debug("Request to partially update Perk : {}", perkDTO);

        return perkRepository
            .findById(perkDTO.getId())
            .map(existingPerk -> {
                perkMapper.partialUpdate(existingPerk, perkDTO);

                return existingPerk;
            })
            .map(perkRepository::save)
            .map(perkMapper::toDto);
    }

    /**
     * Get all the perks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PerkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Perks");
        return perkRepository.findAll(pageable).map(perkMapper::toDto);
    }

    /**
     * Get one perk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PerkDTO> findOne(Long id) {
        log.debug("Request to get Perk : {}", id);
        return perkRepository.findById(id).map(perkMapper::toDto);
    }

    /**
     * Delete the perk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Perk : {}", id);
        perkRepository.deleteById(id);
    }
}
