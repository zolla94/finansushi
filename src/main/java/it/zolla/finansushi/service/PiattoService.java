package it.zolla.finansushi.service;

import it.zolla.finansushi.domain.Piatto;
import it.zolla.finansushi.repository.PiattoRepository;
import it.zolla.finansushi.service.dto.PiattoDTO;
import it.zolla.finansushi.service.mapper.PiattoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Piatto}.
 */
@Service
@Transactional
public class PiattoService {

    private final Logger log = LoggerFactory.getLogger(PiattoService.class);

    private final PiattoRepository piattoRepository;

    private final PiattoMapper piattoMapper;

    public PiattoService(PiattoRepository piattoRepository, PiattoMapper piattoMapper) {
        this.piattoRepository = piattoRepository;
        this.piattoMapper = piattoMapper;
    }

    /**
     * Save a piatto.
     *
     * @param piattoDTO the entity to save.
     * @return the persisted entity.
     */
    public PiattoDTO save(PiattoDTO piattoDTO) {
        log.debug("Request to save Piatto : {}", piattoDTO);
        Piatto piatto = piattoMapper.toEntity(piattoDTO);
        piatto = piattoRepository.save(piatto);
        return piattoMapper.toDto(piatto);
    }

    /**
     * Update a piatto.
     *
     * @param piattoDTO the entity to save.
     * @return the persisted entity.
     */
    public PiattoDTO update(PiattoDTO piattoDTO) {
        log.debug("Request to save Piatto : {}", piattoDTO);
        Piatto piatto = piattoMapper.toEntity(piattoDTO);
        piatto = piattoRepository.save(piatto);
        return piattoMapper.toDto(piatto);
    }

    /**
     * Partially update a piatto.
     *
     * @param piattoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PiattoDTO> partialUpdate(PiattoDTO piattoDTO) {
        log.debug("Request to partially update Piatto : {}", piattoDTO);

        return piattoRepository
            .findById(piattoDTO.getId())
            .map(existingPiatto -> {
                piattoMapper.partialUpdate(existingPiatto, piattoDTO);

                return existingPiatto;
            })
            .map(piattoRepository::save)
            .map(piattoMapper::toDto);
    }

    /**
     * Get all the piattos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PiattoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Piattos");
        return piattoRepository.findAll(pageable).map(piattoMapper::toDto);
    }

    /**
     * Get one piatto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PiattoDTO> findOne(Long id) {
        log.debug("Request to get Piatto : {}", id);
        return piattoRepository.findById(id).map(piattoMapper::toDto);
    }

    /**
     * Delete the piatto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Piatto : {}", id);
        piattoRepository.deleteById(id);
    }
}
