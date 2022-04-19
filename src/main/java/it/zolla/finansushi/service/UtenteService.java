package it.zolla.finansushi.service;

import it.zolla.finansushi.domain.Utente;
import it.zolla.finansushi.repository.UtenteRepository;
import it.zolla.finansushi.service.dto.UtenteDTO;
import it.zolla.finansushi.service.mapper.UtenteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Utente}.
 */
@Service
@Transactional
public class UtenteService {

    private final Logger log = LoggerFactory.getLogger(UtenteService.class);

    private final UtenteRepository utenteRepository;

    private final UtenteMapper utenteMapper;

    public UtenteService(UtenteRepository utenteRepository, UtenteMapper utenteMapper) {
        this.utenteRepository = utenteRepository;
        this.utenteMapper = utenteMapper;
    }

    /**
     * Save a utente.
     *
     * @param utenteDTO the entity to save.
     * @return the persisted entity.
     */
    public UtenteDTO save(UtenteDTO utenteDTO) {
        log.debug("Request to save Utente : {}", utenteDTO);
        Utente utente = utenteMapper.toEntity(utenteDTO);
        utente = utenteRepository.save(utente);
        return utenteMapper.toDto(utente);
    }

    /**
     * Update a utente.
     *
     * @param utenteDTO the entity to save.
     * @return the persisted entity.
     */
    public UtenteDTO update(UtenteDTO utenteDTO) {
        log.debug("Request to save Utente : {}", utenteDTO);
        Utente utente = utenteMapper.toEntity(utenteDTO);
        // no save call needed as we have no fields that can be updated
        return utenteMapper.toDto(utente);
    }

    /**
     * Partially update a utente.
     *
     * @param utenteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtenteDTO> partialUpdate(UtenteDTO utenteDTO) {
        log.debug("Request to partially update Utente : {}", utenteDTO);

        return utenteRepository
            .findById(utenteDTO.getId())
            .map(existingUtente -> {
                utenteMapper.partialUpdate(existingUtente, utenteDTO);

                return existingUtente;
            })
            // .map(utenteRepository::save)
            .map(utenteMapper::toDto);
    }

    /**
     * Get all the utentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UtenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Utentes");
        return utenteRepository.findAll(pageable).map(utenteMapper::toDto);
    }

    /**
     * Get one utente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtenteDTO> findOne(Long id) {
        log.debug("Request to get Utente : {}", id);
        return utenteRepository.findById(id).map(utenteMapper::toDto);
    }

    /**
     * Delete the utente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Utente : {}", id);
        utenteRepository.deleteById(id);
    }
}
