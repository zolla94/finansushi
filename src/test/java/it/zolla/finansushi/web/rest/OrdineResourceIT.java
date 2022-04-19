package it.zolla.finansushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.zolla.finansushi.IntegrationTest;
import it.zolla.finansushi.domain.Ordine;
import it.zolla.finansushi.repository.OrdineRepository;
import it.zolla.finansushi.service.dto.OrdineDTO;
import it.zolla.finansushi.service.mapper.OrdineMapper;
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
 * Integration tests for the {@link OrdineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdineResourceIT {

    private static final String ENTITY_API_URL = "/api/ordines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private OrdineMapper ordineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdineMockMvc;

    private Ordine ordine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordine createEntity(EntityManager em) {
        Ordine ordine = new Ordine();
        return ordine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordine createUpdatedEntity(EntityManager em) {
        Ordine ordine = new Ordine();
        return ordine;
    }

    @BeforeEach
    public void initTest() {
        ordine = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdine() throws Exception {
        int databaseSizeBeforeCreate = ordineRepository.findAll().size();
        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);
        restOrdineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordineDTO)))
            .andExpect(status().isCreated());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeCreate + 1);
        Ordine testOrdine = ordineList.get(ordineList.size() - 1);
    }

    @Test
    @Transactional
    void createOrdineWithExistingId() throws Exception {
        // Create the Ordine with an existing ID
        ordine.setId(1L);
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        int databaseSizeBeforeCreate = ordineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdines() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        // Get all the ordineList
        restOrdineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordine.getId().intValue())));
    }

    @Test
    @Transactional
    void getOrdine() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        // Get the ordine
        restOrdineMockMvc
            .perform(get(ENTITY_API_URL_ID, ordine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordine.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdine() throws Exception {
        // Get the ordine
        restOrdineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdine() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();

        // Update the ordine
        Ordine updatedOrdine = ordineRepository.findById(ordine.getId()).get();
        // Disconnect from session so that the updates on updatedOrdine are not directly saved in db
        em.detach(updatedOrdine);
        OrdineDTO ordineDTO = ordineMapper.toDto(updatedOrdine);

        restOrdineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
        Ordine testOrdine = ordineList.get(ordineList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdineWithPatch() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();

        // Update the ordine using partial update
        Ordine partialUpdatedOrdine = new Ordine();
        partialUpdatedOrdine.setId(ordine.getId());

        restOrdineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdine))
            )
            .andExpect(status().isOk());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
        Ordine testOrdine = ordineList.get(ordineList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateOrdineWithPatch() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();

        // Update the ordine using partial update
        Ordine partialUpdatedOrdine = new Ordine();
        partialUpdatedOrdine.setId(ordine.getId());

        restOrdineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdine))
            )
            .andExpect(status().isOk());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
        Ordine testOrdine = ordineList.get(ordineList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdine() throws Exception {
        int databaseSizeBeforeUpdate = ordineRepository.findAll().size();
        ordine.setId(count.incrementAndGet());

        // Create the Ordine
        OrdineDTO ordineDTO = ordineMapper.toDto(ordine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordine in the database
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdine() throws Exception {
        // Initialize the database
        ordineRepository.saveAndFlush(ordine);

        int databaseSizeBeforeDelete = ordineRepository.findAll().size();

        // Delete the ordine
        restOrdineMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordine> ordineList = ordineRepository.findAll();
        assertThat(ordineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
