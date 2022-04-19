package it.zolla.finansushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.zolla.finansushi.IntegrationTest;
import it.zolla.finansushi.domain.Piatto;
import it.zolla.finansushi.repository.PiattoRepository;
import it.zolla.finansushi.service.dto.PiattoDTO;
import it.zolla.finansushi.service.mapper.PiattoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PiattoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PiattoResourceIT {

    private static final String DEFAULT_CODICE = "AAAAAAAAAA";
    private static final String UPDATED_CODICE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SPICY = false;
    private static final Boolean UPDATED_SPICY = true;

    private static final Boolean DEFAULT_VEGAN = false;
    private static final Boolean UPDATED_VEGAN = true;

    private static final Boolean DEFAULT_LIMITE_ORDINE = false;
    private static final Boolean UPDATED_LIMITE_ORDINE = true;

    private static final String ENTITY_API_URL = "/api/piattos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PiattoRepository piattoRepository;

    @Autowired
    private PiattoMapper piattoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPiattoMockMvc;

    private Piatto piatto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piatto createEntity(EntityManager em) {
        Piatto piatto = new Piatto()
            .codice(DEFAULT_CODICE)
            .descrizione(DEFAULT_DESCRIZIONE)
            .url(DEFAULT_URL)
            .spicy(DEFAULT_SPICY)
            .vegan(DEFAULT_VEGAN)
            .limiteOrdine(DEFAULT_LIMITE_ORDINE);
        return piatto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piatto createUpdatedEntity(EntityManager em) {
        Piatto piatto = new Piatto()
            .codice(UPDATED_CODICE)
            .descrizione(UPDATED_DESCRIZIONE)
            .url(UPDATED_URL)
            .spicy(UPDATED_SPICY)
            .vegan(UPDATED_VEGAN)
            .limiteOrdine(UPDATED_LIMITE_ORDINE);
        return piatto;
    }

    @BeforeEach
    public void initTest() {
        piatto = createEntity(em);
    }

    @Test
    @Transactional
    void createPiatto() throws Exception {
        int databaseSizeBeforeCreate = piattoRepository.findAll().size();
        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);
        restPiattoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piattoDTO)))
            .andExpect(status().isCreated());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeCreate + 1);
        Piatto testPiatto = piattoList.get(piattoList.size() - 1);
        assertThat(testPiatto.getCodice()).isEqualTo(DEFAULT_CODICE);
        assertThat(testPiatto.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testPiatto.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPiatto.getSpicy()).isEqualTo(DEFAULT_SPICY);
        assertThat(testPiatto.getVegan()).isEqualTo(DEFAULT_VEGAN);
        assertThat(testPiatto.getLimiteOrdine()).isEqualTo(DEFAULT_LIMITE_ORDINE);
    }

    @Test
    @Transactional
    void createPiattoWithExistingId() throws Exception {
        // Create the Piatto with an existing ID
        piatto.setId(1L);
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        int databaseSizeBeforeCreate = piattoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPiattoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piattoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPiattos() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        // Get all the piattoList
        restPiattoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piatto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codice").value(hasItem(DEFAULT_CODICE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].spicy").value(hasItem(DEFAULT_SPICY.booleanValue())))
            .andExpect(jsonPath("$.[*].vegan").value(hasItem(DEFAULT_VEGAN.booleanValue())))
            .andExpect(jsonPath("$.[*].limiteOrdine").value(hasItem(DEFAULT_LIMITE_ORDINE.booleanValue())));
    }

    @Test
    @Transactional
    void getPiatto() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        // Get the piatto
        restPiattoMockMvc
            .perform(get(ENTITY_API_URL_ID, piatto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(piatto.getId().intValue()))
            .andExpect(jsonPath("$.codice").value(DEFAULT_CODICE))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.spicy").value(DEFAULT_SPICY.booleanValue()))
            .andExpect(jsonPath("$.vegan").value(DEFAULT_VEGAN.booleanValue()))
            .andExpect(jsonPath("$.limiteOrdine").value(DEFAULT_LIMITE_ORDINE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPiatto() throws Exception {
        // Get the piatto
        restPiattoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPiatto() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();

        // Update the piatto
        Piatto updatedPiatto = piattoRepository.findById(piatto.getId()).get();
        // Disconnect from session so that the updates on updatedPiatto are not directly saved in db
        em.detach(updatedPiatto);
        updatedPiatto
            .codice(UPDATED_CODICE)
            .descrizione(UPDATED_DESCRIZIONE)
            .url(UPDATED_URL)
            .spicy(UPDATED_SPICY)
            .vegan(UPDATED_VEGAN)
            .limiteOrdine(UPDATED_LIMITE_ORDINE);
        PiattoDTO piattoDTO = piattoMapper.toDto(updatedPiatto);

        restPiattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, piattoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
        Piatto testPiatto = piattoList.get(piattoList.size() - 1);
        assertThat(testPiatto.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testPiatto.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testPiatto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPiatto.getSpicy()).isEqualTo(UPDATED_SPICY);
        assertThat(testPiatto.getVegan()).isEqualTo(UPDATED_VEGAN);
        assertThat(testPiatto.getLimiteOrdine()).isEqualTo(UPDATED_LIMITE_ORDINE);
    }

    @Test
    @Transactional
    void putNonExistingPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, piattoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(piattoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePiattoWithPatch() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();

        // Update the piatto using partial update
        Piatto partialUpdatedPiatto = new Piatto();
        partialUpdatedPiatto.setId(piatto.getId());

        partialUpdatedPiatto
            .codice(UPDATED_CODICE)
            .descrizione(UPDATED_DESCRIZIONE)
            .url(UPDATED_URL)
            .spicy(UPDATED_SPICY)
            .vegan(UPDATED_VEGAN)
            .limiteOrdine(UPDATED_LIMITE_ORDINE);

        restPiattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiatto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiatto))
            )
            .andExpect(status().isOk());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
        Piatto testPiatto = piattoList.get(piattoList.size() - 1);
        assertThat(testPiatto.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testPiatto.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testPiatto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPiatto.getSpicy()).isEqualTo(UPDATED_SPICY);
        assertThat(testPiatto.getVegan()).isEqualTo(UPDATED_VEGAN);
        assertThat(testPiatto.getLimiteOrdine()).isEqualTo(UPDATED_LIMITE_ORDINE);
    }

    @Test
    @Transactional
    void fullUpdatePiattoWithPatch() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();

        // Update the piatto using partial update
        Piatto partialUpdatedPiatto = new Piatto();
        partialUpdatedPiatto.setId(piatto.getId());

        partialUpdatedPiatto
            .codice(UPDATED_CODICE)
            .descrizione(UPDATED_DESCRIZIONE)
            .url(UPDATED_URL)
            .spicy(UPDATED_SPICY)
            .vegan(UPDATED_VEGAN)
            .limiteOrdine(UPDATED_LIMITE_ORDINE);

        restPiattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiatto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPiatto))
            )
            .andExpect(status().isOk());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
        Piatto testPiatto = piattoList.get(piattoList.size() - 1);
        assertThat(testPiatto.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testPiatto.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testPiatto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPiatto.getSpicy()).isEqualTo(UPDATED_SPICY);
        assertThat(testPiatto.getVegan()).isEqualTo(UPDATED_VEGAN);
        assertThat(testPiatto.getLimiteOrdine()).isEqualTo(UPDATED_LIMITE_ORDINE);
    }

    @Test
    @Transactional
    void patchNonExistingPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, piattoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPiatto() throws Exception {
        int databaseSizeBeforeUpdate = piattoRepository.findAll().size();
        piatto.setId(count.incrementAndGet());

        // Create the Piatto
        PiattoDTO piattoDTO = piattoMapper.toDto(piatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiattoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(piattoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piatto in the database
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePiatto() throws Exception {
        // Initialize the database
        piattoRepository.saveAndFlush(piatto);

        int databaseSizeBeforeDelete = piattoRepository.findAll().size();

        // Delete the piatto
        restPiattoMockMvc
            .perform(delete(ENTITY_API_URL_ID, piatto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Piatto> piattoList = piattoRepository.findAll();
        assertThat(piattoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
