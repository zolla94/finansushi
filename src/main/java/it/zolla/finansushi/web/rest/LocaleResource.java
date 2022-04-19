package it.zolla.finansushi.web.rest;

import it.zolla.finansushi.repository.LocaleRepository;
import it.zolla.finansushi.service.LocaleService;
import it.zolla.finansushi.service.dto.LocaleDTO;
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
 * REST controller for managing {@link it.zolla.finansushi.domain.Locale}.
 */
@RestController
@RequestMapping("/api")
public class LocaleResource {

    private final Logger log = LoggerFactory.getLogger(LocaleResource.class);

    private static final String ENTITY_NAME = "locale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocaleService localeService;

    private final LocaleRepository localeRepository;

    public LocaleResource(LocaleService localeService, LocaleRepository localeRepository) {
        this.localeService = localeService;
        this.localeRepository = localeRepository;
    }

    /**
     * {@code POST  /locales} : Create a new locale.
     *
     * @param localeDTO the localeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localeDTO, or with status {@code 400 (Bad Request)} if the locale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locales")
    public ResponseEntity<LocaleDTO> createLocale(@RequestBody LocaleDTO localeDTO) throws URISyntaxException {
        log.debug("REST request to save Locale : {}", localeDTO);
        if (localeDTO.getId() != null) {
            throw new BadRequestAlertException("A new locale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocaleDTO result = localeService.save(localeDTO);
        return ResponseEntity
            .created(new URI("/api/locales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locales/:id} : Updates an existing locale.
     *
     * @param id the id of the localeDTO to save.
     * @param localeDTO the localeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localeDTO,
     * or with status {@code 400 (Bad Request)} if the localeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locales/{id}")
    public ResponseEntity<LocaleDTO> updateLocale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocaleDTO localeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Locale : {}, {}", id, localeDTO);
        if (localeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocaleDTO result = localeService.update(localeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /locales/:id} : Partial updates given fields of an existing locale, field will ignore if it is null
     *
     * @param id the id of the localeDTO to save.
     * @param localeDTO the localeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localeDTO,
     * or with status {@code 400 (Bad Request)} if the localeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocaleDTO> partialUpdateLocale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocaleDTO localeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Locale partially : {}, {}", id, localeDTO);
        if (localeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocaleDTO> result = localeService.partialUpdate(localeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /locales} : get all the locales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locales in body.
     */
    @GetMapping("/locales")
    public ResponseEntity<List<LocaleDTO>> getAllLocales(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Locales");
        Page<LocaleDTO> page = localeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locales/:id} : get the "id" locale.
     *
     * @param id the id of the localeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locales/{id}")
    public ResponseEntity<LocaleDTO> getLocale(@PathVariable Long id) {
        log.debug("REST request to get Locale : {}", id);
        Optional<LocaleDTO> localeDTO = localeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localeDTO);
    }

    /**
     * {@code DELETE  /locales/:id} : delete the "id" locale.
     *
     * @param id the id of the localeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locales/{id}")
    public ResponseEntity<Void> deleteLocale(@PathVariable Long id) {
        log.debug("REST request to delete Locale : {}", id);
        localeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
