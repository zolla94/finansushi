package it.zolla.finansushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.zolla.finansushi.IntegrationTest;
import it.zolla.finansushi.domain.Utente;
import it.zolla.finansushi.repository.UtenteRepository;
import it.zolla.finansushi.service.dto.UtenteDTO;
import it.zolla.finansushi.service.mapper.UtenteMapper;
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
 * Integration tests for the {@link UtenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtenteResourceIT {

    private static final String ENTITY_API_URL = "/api/utentes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteMapper utenteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtenteMockMvc;

    private Utente utente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utente createEntity(EntityManager em) {
        Utente utente = new Utente();
        return utente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utente createUpdatedEntity(EntityManager em) {
        Utente utente = new Utente();
        return utente;
    }

    @BeforeEach
    public void initTest() {
        utente = createEntity(em);
    }

    @Test
    @Transactional
    void createUtente() throws Exception {
        int databaseSizeBeforeCreate = utenteRepository.findAll().size();
        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);
        restUtenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utenteDTO)))
            .andExpect(status().isCreated());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeCreate + 1);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
    }

    @Test
    @Transactional
    void createUtenteWithExistingId() throws Exception {
        // Create the Utente with an existing ID
        utente.setId(1L);
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        int databaseSizeBeforeCreate = utenteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUtentes() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        // Get all the utenteList
        restUtenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utente.getId().intValue())));
    }

    @Test
    @Transactional
    void getUtente() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        // Get the utente
        restUtenteMockMvc
            .perform(get(ENTITY_API_URL_ID, utente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utente.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUtente() throws Exception {
        // Get the utente
        restUtenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtente() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();

        // Update the utente
        Utente updatedUtente = utenteRepository.findById(utente.getId()).get();
        // Disconnect from session so that the updates on updatedUtente are not directly saved in db
        em.detach(updatedUtente);
        UtenteDTO utenteDTO = utenteMapper.toDto(updatedUtente);

        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utenteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtenteWithPatch() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();

        // Update the utente using partial update
        Utente partialUpdatedUtente = new Utente();
        partialUpdatedUtente.setId(utente.getId());

        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtente))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateUtenteWithPatch() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();

        // Update the utente using partial update
        Utente partialUpdatedUtente = new Utente();
        partialUpdatedUtente.setId(utente.getId());

        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtente))
            )
            .andExpect(status().isOk());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
        Utente testUtente = utenteList.get(utenteList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utenteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtente() throws Exception {
        int databaseSizeBeforeUpdate = utenteRepository.findAll().size();
        utente.setId(count.incrementAndGet());

        // Create the Utente
        UtenteDTO utenteDTO = utenteMapper.toDto(utente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtenteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utenteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utente in the database
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtente() throws Exception {
        // Initialize the database
        utenteRepository.saveAndFlush(utente);

        int databaseSizeBeforeDelete = utenteRepository.findAll().size();

        // Delete the utente
        restUtenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, utente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utente> utenteList = utenteRepository.findAll();
        assertThat(utenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
