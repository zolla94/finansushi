package it.zolla.finansushi.web.rest;

import it.zolla.finansushi.repository.UtenteRepository;
import it.zolla.finansushi.service.UtenteService;
import it.zolla.finansushi.service.dto.UtenteDTO;
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
 * REST controller for managing {@link it.zolla.finansushi.domain.Utente}.
 */
@RestController
@RequestMapping("/api")
public class UtenteResource {

    private final Logger log = LoggerFactory.getLogger(UtenteResource.class);

    private static final String ENTITY_NAME = "utente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtenteService utenteService;

    private final UtenteRepository utenteRepository;

    public UtenteResource(UtenteService utenteService, UtenteRepository utenteRepository) {
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
    }

    /**
     * {@code POST  /utentes} : Create a new utente.
     *
     * @param utenteDTO the utenteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utenteDTO, or with status {@code 400 (Bad Request)} if the utente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utentes")
    public ResponseEntity<UtenteDTO> createUtente(@RequestBody UtenteDTO utenteDTO) throws URISyntaxException {
        log.debug("REST request to save Utente : {}", utenteDTO);
        if (utenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new utente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UtenteDTO result = utenteService.save(utenteDTO);
        return ResponseEntity
            .created(new URI("/api/utentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utentes/:id} : Updates an existing utente.
     *
     * @param id the id of the utenteDTO to save.
     * @param utenteDTO the utenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utenteDTO,
     * or with status {@code 400 (Bad Request)} if the utenteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utentes/{id}")
    public ResponseEntity<UtenteDTO> updateUtente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UtenteDTO utenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Utente : {}, {}", id, utenteDTO);
        if (utenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UtenteDTO result = utenteService.update(utenteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utenteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utentes/:id} : Partial updates given fields of an existing utente, field will ignore if it is null
     *
     * @param id the id of the utenteDTO to save.
     * @param utenteDTO the utenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utenteDTO,
     * or with status {@code 400 (Bad Request)} if the utenteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the utenteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the utenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utentes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtenteDTO> partialUpdateUtente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UtenteDTO utenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utente partially : {}, {}", id, utenteDTO);
        if (utenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtenteDTO> result = utenteService.partialUpdate(utenteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utenteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utentes} : get all the utentes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utentes in body.
     */
    @GetMapping("/utentes")
    public ResponseEntity<List<UtenteDTO>> getAllUtentes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Utentes");
        Page<UtenteDTO> page = utenteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utentes/:id} : get the "id" utente.
     *
     * @param id the id of the utenteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utenteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utentes/{id}")
    public ResponseEntity<UtenteDTO> getUtente(@PathVariable Long id) {
        log.debug("REST request to get Utente : {}", id);
        Optional<UtenteDTO> utenteDTO = utenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utenteDTO);
    }

    /**
     * {@code DELETE  /utentes/:id} : delete the "id" utente.
     *
     * @param id the id of the utenteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utentes/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
        log.debug("REST request to delete Utente : {}", id);
        utenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
