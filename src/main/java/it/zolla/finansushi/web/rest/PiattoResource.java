package it.zolla.finansushi.web.rest;

import it.zolla.finansushi.repository.PiattoRepository;
import it.zolla.finansushi.service.PiattoService;
import it.zolla.finansushi.service.dto.PiattoDTO;
import it.zolla.finansushi.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.zolla.finansushi.domain.Piatto}.
 */
@RestController
@RequestMapping("/api")
public class PiattoResource {

    private final Logger log = LoggerFactory.getLogger(PiattoResource.class);

    private static final String ENTITY_NAME = "piatto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PiattoService piattoService;

    private final PiattoRepository piattoRepository;

    public PiattoResource(PiattoService piattoService, PiattoRepository piattoRepository) {
        this.piattoService = piattoService;
        this.piattoRepository = piattoRepository;
    }

    /**
     * {@code POST  /piattos} : Create a new piatto.
     *
     * @param piattoDTO the piattoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new piattoDTO, or with status {@code 400 (Bad Request)} if the piatto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/piattos")
    public ResponseEntity<PiattoDTO> createPiatto(@RequestBody PiattoDTO piattoDTO) throws URISyntaxException {
        log.debug("REST request to save Piatto : {}", piattoDTO);
        if (piattoDTO.getId() != null) {
            throw new BadRequestAlertException("A new piatto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PiattoDTO result = piattoService.save(piattoDTO);
        return ResponseEntity
            .created(new URI("/api/piattos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /piattos/:id} : Updates an existing piatto.
     *
     * @param id the id of the piattoDTO to save.
     * @param piattoDTO the piattoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piattoDTO,
     * or with status {@code 400 (Bad Request)} if the piattoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the piattoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/piattos/{id}")
    public ResponseEntity<PiattoDTO> updatePiatto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PiattoDTO piattoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Piatto : {}, {}", id, piattoDTO);
        if (piattoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piattoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!piattoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PiattoDTO result = piattoService.update(piattoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, piattoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /piattos/:id} : Partial updates given fields of an existing piatto, field will ignore if it is null
     *
     * @param id the id of the piattoDTO to save.
     * @param piattoDTO the piattoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piattoDTO,
     * or with status {@code 400 (Bad Request)} if the piattoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the piattoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the piattoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/piattos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PiattoDTO> partialUpdatePiatto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PiattoDTO piattoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Piatto partially : {}, {}", id, piattoDTO);
        if (piattoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piattoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!piattoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PiattoDTO> result = piattoService.partialUpdate(piattoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, piattoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /piattos} : get all the piattos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of piattos in body.
     */
    @GetMapping("/piattos")
    public ResponseEntity<List<PiattoDTO>> getAllPiattos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Piattos");
        Page<PiattoDTO> page = piattoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /piattos/:id} : get the "id" piatto.
     *
     * @param id the id of the piattoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the piattoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/piattos/{id}")
    public ResponseEntity<PiattoDTO> getPiatto(@PathVariable Long id) {
        log.debug("REST request to get Piatto : {}", id);
        Optional<PiattoDTO> piattoDTO = piattoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(piattoDTO);
    }

    /**
     * {@code DELETE  /piattos/:id} : delete the "id" piatto.
     *
     * @param id the id of the piattoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/piattos/{id}")
    public ResponseEntity<Void> deletePiatto(@PathVariable Long id) {
        log.debug("REST request to delete Piatto : {}", id);
        piattoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
