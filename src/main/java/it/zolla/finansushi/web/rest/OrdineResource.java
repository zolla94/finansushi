package it.zolla.finansushi.web.rest;

import it.zolla.finansushi.repository.OrdineRepository;
import it.zolla.finansushi.service.OrdineService;
import it.zolla.finansushi.service.dto.OrdineDTO;
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
 * REST controller for managing {@link it.zolla.finansushi.domain.Ordine}.
 */
@RestController
@RequestMapping("/api")
public class OrdineResource {

    private final Logger log = LoggerFactory.getLogger(OrdineResource.class);

    private static final String ENTITY_NAME = "ordine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdineService ordineService;

    private final OrdineRepository ordineRepository;

    public OrdineResource(OrdineService ordineService, OrdineRepository ordineRepository) {
        this.ordineService = ordineService;
        this.ordineRepository = ordineRepository;
    }

    /**
     * {@code POST  /ordines} : Create a new ordine.
     *
     * @param ordineDTO the ordineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordineDTO, or with status {@code 400 (Bad Request)} if the ordine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordines")
    public ResponseEntity<OrdineDTO> createOrdine(@RequestBody OrdineDTO ordineDTO) throws URISyntaxException {
        log.debug("REST request to save Ordine : {}", ordineDTO);
        if (ordineDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdineDTO result = ordineService.save(ordineDTO);
        return ResponseEntity
            .created(new URI("/api/ordines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordines/:id} : Updates an existing ordine.
     *
     * @param id the id of the ordineDTO to save.
     * @param ordineDTO the ordineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordineDTO,
     * or with status {@code 400 (Bad Request)} if the ordineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordines/{id}")
    public ResponseEntity<OrdineDTO> updateOrdine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdineDTO ordineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ordine : {}, {}", id, ordineDTO);
        if (ordineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdineDTO result = ordineService.update(ordineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordines/:id} : Partial updates given fields of an existing ordine, field will ignore if it is null
     *
     * @param id the id of the ordineDTO to save.
     * @param ordineDTO the ordineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordineDTO,
     * or with status {@code 400 (Bad Request)} if the ordineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ordineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdineDTO> partialUpdateOrdine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdineDTO ordineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ordine partially : {}, {}", id, ordineDTO);
        if (ordineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdineDTO> result = ordineService.partialUpdate(ordineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ordines} : get all the ordines.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordines in body.
     */
    @GetMapping("/ordines")
    public ResponseEntity<List<OrdineDTO>> getAllOrdines(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("piatto-is-null".equals(filter)) {
            log.debug("REST request to get all Ordines where piatto is null");
            return new ResponseEntity<>(ordineService.findAllWherePiattoIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Ordines");
        Page<OrdineDTO> page = ordineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordines/:id} : get the "id" ordine.
     *
     * @param id the id of the ordineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordines/{id}")
    public ResponseEntity<OrdineDTO> getOrdine(@PathVariable Long id) {
        log.debug("REST request to get Ordine : {}", id);
        Optional<OrdineDTO> ordineDTO = ordineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordineDTO);
    }

    /**
     * {@code DELETE  /ordines/:id} : delete the "id" ordine.
     *
     * @param id the id of the ordineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordines/{id}")
    public ResponseEntity<Void> deleteOrdine(@PathVariable Long id) {
        log.debug("REST request to delete Ordine : {}", id);
        ordineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
