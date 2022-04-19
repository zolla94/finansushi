package it.zolla.finansushi.web.rest;

import it.zolla.finansushi.repository.StoricoOrdiniRepository;
import it.zolla.finansushi.service.StoricoOrdiniService;
import it.zolla.finansushi.service.dto.StoricoOrdiniDTO;
import it.zolla.finansushi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.zolla.finansushi.domain.StoricoOrdini}.
 */
@RestController
@RequestMapping("/api")
public class StoricoOrdiniResource {

    private final Logger log = LoggerFactory.getLogger(StoricoOrdiniResource.class);

    private static final String ENTITY_NAME = "storicoOrdini";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoricoOrdiniService storicoOrdiniService;

    private final StoricoOrdiniRepository storicoOrdiniRepository;

    public StoricoOrdiniResource(StoricoOrdiniService storicoOrdiniService, StoricoOrdiniRepository storicoOrdiniRepository) {
        this.storicoOrdiniService = storicoOrdiniService;
        this.storicoOrdiniRepository = storicoOrdiniRepository;
    }

    /**
     * {@code POST  /storico-ordinis} : Create a new storicoOrdini.
     *
     * @param storicoOrdiniDTO the storicoOrdiniDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storicoOrdiniDTO, or with status {@code 400 (Bad Request)} if the storicoOrdini has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/storico-ordinis")
    public ResponseEntity<StoricoOrdiniDTO> createStoricoOrdini(@RequestBody StoricoOrdiniDTO storicoOrdiniDTO) throws URISyntaxException {
        log.debug("REST request to save StoricoOrdini : {}", storicoOrdiniDTO);
        if (storicoOrdiniDTO.getId() != null) {
            throw new BadRequestAlertException("A new storicoOrdini cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoricoOrdiniDTO result = storicoOrdiniService.save(storicoOrdiniDTO);
        return ResponseEntity
            .created(new URI("/api/storico-ordinis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /storico-ordinis/:id} : Updates an existing storicoOrdini.
     *
     * @param id the id of the storicoOrdiniDTO to save.
     * @param storicoOrdiniDTO the storicoOrdiniDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storicoOrdiniDTO,
     * or with status {@code 400 (Bad Request)} if the storicoOrdiniDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storicoOrdiniDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/storico-ordinis/{id}")
    public ResponseEntity<StoricoOrdiniDTO> updateStoricoOrdini(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoricoOrdiniDTO storicoOrdiniDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StoricoOrdini : {}, {}", id, storicoOrdiniDTO);
        if (storicoOrdiniDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storicoOrdiniDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storicoOrdiniRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StoricoOrdiniDTO result = storicoOrdiniService.update(storicoOrdiniDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storicoOrdiniDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /storico-ordinis/:id} : Partial updates given fields of an existing storicoOrdini, field will ignore if it is null
     *
     * @param id the id of the storicoOrdiniDTO to save.
     * @param storicoOrdiniDTO the storicoOrdiniDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storicoOrdiniDTO,
     * or with status {@code 400 (Bad Request)} if the storicoOrdiniDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storicoOrdiniDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storicoOrdiniDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/storico-ordinis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StoricoOrdiniDTO> partialUpdateStoricoOrdini(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StoricoOrdiniDTO storicoOrdiniDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StoricoOrdini partially : {}, {}", id, storicoOrdiniDTO);
        if (storicoOrdiniDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storicoOrdiniDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storicoOrdiniRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StoricoOrdiniDTO> result = storicoOrdiniService.partialUpdate(storicoOrdiniDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storicoOrdiniDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storico-ordinis} : get all the storicoOrdinis.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storicoOrdinis in body.
     */
    @GetMapping("/storico-ordinis")
    public ResponseEntity<List<StoricoOrdiniDTO>> getAllStoricoOrdinis(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("piatto-is-null".equals(filter)) {
            log.debug("REST request to get all StoricoOrdinis where piatto is null");
            return new ResponseEntity<>(storicoOrdiniService.findAllWherePiattoIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of StoricoOrdinis");
        Page<StoricoOrdiniDTO> page = storicoOrdiniService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storico-ordinis/:id} : get the "id" storicoOrdini.
     *
     * @param id the id of the storicoOrdiniDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storicoOrdiniDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/storico-ordinis/{id}")
    public ResponseEntity<StoricoOrdiniDTO> getStoricoOrdini(@PathVariable Long id) {
        log.debug("REST request to get StoricoOrdini : {}", id);
        Optional<StoricoOrdiniDTO> storicoOrdiniDTO = storicoOrdiniService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storicoOrdiniDTO);
    }

    /**
     * {@code DELETE  /storico-ordinis/:id} : delete the "id" storicoOrdini.
     *
     * @param id the id of the storicoOrdiniDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/storico-ordinis/{id}")
    public ResponseEntity<Void> deleteStoricoOrdini(@PathVariable Long id) {
        log.debug("REST request to delete StoricoOrdini : {}", id);
        storicoOrdiniService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
