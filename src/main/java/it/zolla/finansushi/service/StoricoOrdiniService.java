package it.zolla.finansushi.service;

import it.zolla.finansushi.domain.StoricoOrdini;
import it.zolla.finansushi.repository.StoricoOrdiniRepository;
import it.zolla.finansushi.service.dto.StoricoOrdiniDTO;
import it.zolla.finansushi.service.mapper.StoricoOrdiniMapper;
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
 * Service Implementation for managing {@link StoricoOrdini}.
 */
@Service
@Transactional
public class StoricoOrdiniService {

    private final Logger log = LoggerFactory.getLogger(StoricoOrdiniService.class);

    private final StoricoOrdiniRepository storicoOrdiniRepository;

    private final StoricoOrdiniMapper storicoOrdiniMapper;

    public StoricoOrdiniService(StoricoOrdiniRepository storicoOrdiniRepository, StoricoOrdiniMapper storicoOrdiniMapper) {
        this.storicoOrdiniRepository = storicoOrdiniRepository;
        this.storicoOrdiniMapper = storicoOrdiniMapper;
    }

    /**
     * Save a storicoOrdini.
     *
     * @param storicoOrdiniDTO the entity to save.
     * @return the persisted entity.
     */
    public StoricoOrdiniDTO save(StoricoOrdiniDTO storicoOrdiniDTO) {
        log.debug("Request to save StoricoOrdini : {}", storicoOrdiniDTO);
        StoricoOrdini storicoOrdini = storicoOrdiniMapper.toEntity(storicoOrdiniDTO);
        storicoOrdini = storicoOrdiniRepository.save(storicoOrdini);
        return storicoOrdiniMapper.toDto(storicoOrdini);
    }

    /**
     * Update a storicoOrdini.
     *
     * @param storicoOrdiniDTO the entity to save.
     * @return the persisted entity.
     */
    public StoricoOrdiniDTO update(StoricoOrdiniDTO storicoOrdiniDTO) {
        log.debug("Request to save StoricoOrdini : {}", storicoOrdiniDTO);
        StoricoOrdini storicoOrdini = storicoOrdiniMapper.toEntity(storicoOrdiniDTO);
        storicoOrdini = storicoOrdiniRepository.save(storicoOrdini);
        return storicoOrdiniMapper.toDto(storicoOrdini);
    }

    /**
     * Partially update a storicoOrdini.
     *
     * @param storicoOrdiniDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StoricoOrdiniDTO> partialUpdate(StoricoOrdiniDTO storicoOrdiniDTO) {
        log.debug("Request to partially update StoricoOrdini : {}", storicoOrdiniDTO);

        return storicoOrdiniRepository
            .findById(storicoOrdiniDTO.getId())
            .map(existingStoricoOrdini -> {
                storicoOrdiniMapper.partialUpdate(existingStoricoOrdini, storicoOrdiniDTO);

                return existingStoricoOrdini;
            })
            .map(storicoOrdiniRepository::save)
            .map(storicoOrdiniMapper::toDto);
    }

    /**
     * Get all the storicoOrdinis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StoricoOrdiniDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StoricoOrdinis");
        return storicoOrdiniRepository.findAll(pageable).map(storicoOrdiniMapper::toDto);
    }

    /**
     *  Get all the storicoOrdinis where Piatto is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StoricoOrdiniDTO> findAllWherePiattoIsNull() {
        log.debug("Request to get all storicoOrdinis where Piatto is null");
        return StreamSupport
            .stream(storicoOrdiniRepository.findAll().spliterator(), false)
            .filter(storicoOrdini -> storicoOrdini.getPiatto() == null)
            .map(storicoOrdiniMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one storicoOrdini by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StoricoOrdiniDTO> findOne(Long id) {
        log.debug("Request to get StoricoOrdini : {}", id);
        return storicoOrdiniRepository.findById(id).map(storicoOrdiniMapper::toDto);
    }

    /**
     * Delete the storicoOrdini by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StoricoOrdini : {}", id);
        storicoOrdiniRepository.deleteById(id);
    }
}
