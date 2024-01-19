package com.finxsoft.web.rest;

import com.finxsoft.repository.PerkRepository;
import com.finxsoft.service.PerkService;
import com.finxsoft.service.dto.PerkDTO;
import com.finxsoft.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.finxsoft.domain.Perk}.
 */
@RestController
@RequestMapping("/api/perks")
public class PerkResource {

    private final Logger log = LoggerFactory.getLogger(PerkResource.class);

    private static final String ENTITY_NAME = "perk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerkService perkService;

    private final PerkRepository perkRepository;

    public PerkResource(PerkService perkService, PerkRepository perkRepository) {
        this.perkService = perkService;
        this.perkRepository = perkRepository;
    }

    /**
     * {@code POST  /perks} : Create a new perk.
     *
     * @param perkDTO the perkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perkDTO, or with status {@code 400 (Bad Request)} if the perk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PerkDTO> createPerk(@RequestBody PerkDTO perkDTO) throws URISyntaxException {
        log.debug("REST request to save Perk : {}", perkDTO);
        if (perkDTO.getId() != null) {
            throw new BadRequestAlertException("A new perk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerkDTO result = perkService.save(perkDTO);
        return ResponseEntity
            .created(new URI("/api/perks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perks/:id} : Updates an existing perk.
     *
     * @param id the id of the perkDTO to save.
     * @param perkDTO the perkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perkDTO,
     * or with status {@code 400 (Bad Request)} if the perkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PerkDTO> updatePerk(@PathVariable(value = "id", required = false) final Long id, @RequestBody PerkDTO perkDTO)
        throws URISyntaxException {
        log.debug("REST request to update Perk : {}, {}", id, perkDTO);
        if (perkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PerkDTO result = perkService.update(perkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /perks/:id} : Partial updates given fields of an existing perk, field will ignore if it is null
     *
     * @param id the id of the perkDTO to save.
     * @param perkDTO the perkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perkDTO,
     * or with status {@code 400 (Bad Request)} if the perkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerkDTO> partialUpdatePerk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PerkDTO perkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Perk partially : {}, {}", id, perkDTO);
        if (perkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerkDTO> result = perkService.partialUpdate(perkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /perks} : get all the perks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PerkDTO>> getAllPerks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Perks");
        Page<PerkDTO> page = perkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perks/:id} : get the "id" perk.
     *
     * @param id the id of the perkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PerkDTO> getPerk(@PathVariable("id") Long id) {
        log.debug("REST request to get Perk : {}", id);
        Optional<PerkDTO> perkDTO = perkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perkDTO);
    }

    /**
     * {@code DELETE  /perks/:id} : delete the "id" perk.
     *
     * @param id the id of the perkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerk(@PathVariable("id") Long id) {
        log.debug("REST request to delete Perk : {}", id);
        perkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
