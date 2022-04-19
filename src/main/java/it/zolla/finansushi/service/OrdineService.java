package it.zolla.finansushi.service;

import it.zolla.finansushi.domain.Ordine;
import it.zolla.finansushi.repository.OrdineRepository;
import it.zolla.finansushi.service.dto.OrdineDTO;
import it.zolla.finansushi.service.mapper.OrdineMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ordine}.
 */
@Service
@Transactional
public class OrdineService {

    private final Logger log = LoggerFactory.getLogger(OrdineService.class);

    private final OrdineRepository ordineRepository;

    private final OrdineMapper ordineMapper;

    public OrdineService(OrdineRepository ordineRepository, OrdineMapper ordineMapper) {
        this.ordineRepository = ordineRepository;
        this.ordineMapper = ordineMapper;
    }

    /**
     * Save a ordine.
     *
     * @param ordineDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdineDTO save(OrdineDTO ordineDTO) {
        log.debug("Request to save Ordine : {}", ordineDTO);
        Ordine ordine = ordineMapper.toEntity(ordineDTO);
        ordine = ordineRepository.save(ordine);
        return ordineMapper.toDto(ordine);
    }

    /**
     * Update a ordine.
     *
     * @param ordineDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdineDTO update(OrdineDTO ordineDTO) {
        log.debug("Request to save Ordine : {}", ordineDTO);
        Ordine ordine = ordineMapper.toEntity(ordineDTO);
        ordine = ordineRepository.save(ordine);
        return ordineMapper.toDto(ordine);
    }

    /**
     * Partially update a ordine.
     *
     * @param ordineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrdineDTO> partialUpdate(OrdineDTO ordineDTO) {
        log.debug("Request to partially update Ordine : {}", ordineDTO);

        return ordineRepository
            .findById(ordineDTO.getId())
            .map(existingOrdine -> {
                ordineMapper.partialUpdate(existingOrdine, ordineDTO);

                return existingOrdine;
            })
            .map(ordineRepository::save)
            .map(ordineMapper::toDto);
    }

    /**
     * Get all the ordines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ordines");
        return ordineRepository.findAll(pageable).map(ordineMapper::toDto);
    }

    /**
     *  Get all the ordines where Piatto is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdineDTO> findAllWherePiattoIsNull() {
        log.debug("Request to get all ordines where Piatto is null");
        return StreamSupport
            .stream(ordineRepository.findAll().spliterator(), false)
            .filter(ordine -> ordine.getPiatto() == null)
            .map(ordineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ordine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrdineDTO> findOne(Long id) {
        log.debug("Request to get Ordine : {}", id);
        return ordineRepository.findById(id).map(ordineMapper::toDto);
    }

    /**
     * Delete the ordine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordine : {}", id);
        ordineRepository.deleteById(id);
    }
}
