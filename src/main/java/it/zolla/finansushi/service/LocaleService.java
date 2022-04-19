package it.zolla.finansushi.service;

import it.zolla.finansushi.domain.Locale;
import it.zolla.finansushi.repository.LocaleRepository;
import it.zolla.finansushi.service.dto.LocaleDTO;
import it.zolla.finansushi.service.mapper.LocaleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Locale}.
 */
@Service
@Transactional
public class LocaleService {

    private final Logger log = LoggerFactory.getLogger(LocaleService.class);

    private final LocaleRepository localeRepository;

    private final LocaleMapper localeMapper;

    public LocaleService(LocaleRepository localeRepository, LocaleMapper localeMapper) {
        this.localeRepository = localeRepository;
        this.localeMapper = localeMapper;
    }

    /**
     * Save a locale.
     *
     * @param localeDTO the entity to save.
     * @return the persisted entity.
     */
    public LocaleDTO save(LocaleDTO localeDTO) {
        log.debug("Request to save Locale : {}", localeDTO);
        Locale locale = localeMapper.toEntity(localeDTO);
        locale = localeRepository.save(locale);
        return localeMapper.toDto(locale);
    }

    /**
     * Update a locale.
     *
     * @param localeDTO the entity to save.
     * @return the persisted entity.
     */
    public LocaleDTO update(LocaleDTO localeDTO) {
        log.debug("Request to save Locale : {}", localeDTO);
        Locale locale = localeMapper.toEntity(localeDTO);
        locale = localeRepository.save(locale);
        return localeMapper.toDto(locale);
    }

    /**
     * Partially update a locale.
     *
     * @param localeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocaleDTO> partialUpdate(LocaleDTO localeDTO) {
        log.debug("Request to partially update Locale : {}", localeDTO);

        return localeRepository
            .findById(localeDTO.getId())
            .map(existingLocale -> {
                localeMapper.partialUpdate(existingLocale, localeDTO);

                return existingLocale;
            })
            .map(localeRepository::save)
            .map(localeMapper::toDto);
    }

    /**
     * Get all the locales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locales");
        return localeRepository.findAll(pageable).map(localeMapper::toDto);
    }

    /**
     * Get one locale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocaleDTO> findOne(Long id) {
        log.debug("Request to get Locale : {}", id);
        return localeRepository.findById(id).map(localeMapper::toDto);
    }

    /**
     * Delete the locale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Locale : {}", id);
        localeRepository.deleteById(id);
    }
}
