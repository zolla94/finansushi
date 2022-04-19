package it.zolla.finansushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.zolla.finansushi.IntegrationTest;
import it.zolla.finansushi.domain.StoricoOrdini;
import it.zolla.finansushi.repository.StoricoOrdiniRepository;
import it.zolla.finansushi.service.dto.StoricoOrdiniDTO;
import it.zolla.finansushi.service.mapper.StoricoOrdiniMapper;
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
 * Integration tests for the {@link StoricoOrdiniResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoricoOrdiniResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/storico-ordinis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StoricoOrdiniRepository storicoOrdiniRepository;

    @Autowired
    private StoricoOrdiniMapper storicoOrdiniMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoricoOrdiniMockMvc;

    private StoricoOrdini storicoOrdini;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoricoOrdini createEntity(EntityManager em) {
        StoricoOrdini storicoOrdini = new StoricoOrdini().note(DEFAULT_NOTE);
        return storicoOrdini;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoricoOrdini createUpdatedEntity(EntityManager em) {
        StoricoOrdini storicoOrdini = new StoricoOrdini().note(UPDATED_NOTE);
        return storicoOrdini;
    }

    @BeforeEach
    public void initTest() {
        storicoOrdini = createEntity(em);
    }

    @Test
    @Transactional
    void createStoricoOrdini() throws Exception {
        int databaseSizeBeforeCreate = storicoOrdiniRepository.findAll().size();
        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);
        restStoricoOrdiniMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeCreate + 1);
        StoricoOrdini testStoricoOrdini = storicoOrdiniList.get(storicoOrdiniList.size() - 1);
        assertThat(testStoricoOrdini.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createStoricoOrdiniWithExistingId() throws Exception {
        // Create the StoricoOrdini with an existing ID
        storicoOrdini.setId(1L);
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        int databaseSizeBeforeCreate = storicoOrdiniRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoricoOrdiniMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStoricoOrdinis() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        // Get all the storicoOrdiniList
        restStoricoOrdiniMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storicoOrdini.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getStoricoOrdini() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        // Get the storicoOrdini
        restStoricoOrdiniMockMvc
            .perform(get(ENTITY_API_URL_ID, storicoOrdini.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storicoOrdini.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingStoricoOrdini() throws Exception {
        // Get the storicoOrdini
        restStoricoOrdiniMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStoricoOrdini() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();

        // Update the storicoOrdini
        StoricoOrdini updatedStoricoOrdini = storicoOrdiniRepository.findById(storicoOrdini.getId()).get();
        // Disconnect from session so that the updates on updatedStoricoOrdini are not directly saved in db
        em.detach(updatedStoricoOrdini);
        updatedStoricoOrdini.note(UPDATED_NOTE);
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(updatedStoricoOrdini);

        restStoricoOrdiniMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storicoOrdiniDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isOk());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
        StoricoOrdini testStoricoOrdini = storicoOrdiniList.get(storicoOrdiniList.size() - 1);
        assertThat(testStoricoOrdini.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storicoOrdiniDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoricoOrdiniWithPatch() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();

        // Update the storicoOrdini using partial update
        StoricoOrdini partialUpdatedStoricoOrdini = new StoricoOrdini();
        partialUpdatedStoricoOrdini.setId(storicoOrdini.getId());

        partialUpdatedStoricoOrdini.note(UPDATED_NOTE);

        restStoricoOrdiniMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoricoOrdini.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoricoOrdini))
            )
            .andExpect(status().isOk());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
        StoricoOrdini testStoricoOrdini = storicoOrdiniList.get(storicoOrdiniList.size() - 1);
        assertThat(testStoricoOrdini.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateStoricoOrdiniWithPatch() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();

        // Update the storicoOrdini using partial update
        StoricoOrdini partialUpdatedStoricoOrdini = new StoricoOrdini();
        partialUpdatedStoricoOrdini.setId(storicoOrdini.getId());

        partialUpdatedStoricoOrdini.note(UPDATED_NOTE);

        restStoricoOrdiniMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoricoOrdini.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoricoOrdini))
            )
            .andExpect(status().isOk());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
        StoricoOrdini testStoricoOrdini = storicoOrdiniList.get(storicoOrdiniList.size() - 1);
        assertThat(testStoricoOrdini.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storicoOrdiniDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStoricoOrdini() throws Exception {
        int databaseSizeBeforeUpdate = storicoOrdiniRepository.findAll().size();
        storicoOrdini.setId(count.incrementAndGet());

        // Create the StoricoOrdini
        StoricoOrdiniDTO storicoOrdiniDTO = storicoOrdiniMapper.toDto(storicoOrdini);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoricoOrdiniMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storicoOrdiniDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoricoOrdini in the database
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStoricoOrdini() throws Exception {
        // Initialize the database
        storicoOrdiniRepository.saveAndFlush(storicoOrdini);

        int databaseSizeBeforeDelete = storicoOrdiniRepository.findAll().size();

        // Delete the storicoOrdini
        restStoricoOrdiniMockMvc
            .perform(delete(ENTITY_API_URL_ID, storicoOrdini.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StoricoOrdini> storicoOrdiniList = storicoOrdiniRepository.findAll();
        assertThat(storicoOrdiniList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
