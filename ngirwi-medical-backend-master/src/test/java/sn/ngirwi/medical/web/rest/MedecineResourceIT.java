package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.ngirwi.medical.IntegrationTest;
import sn.ngirwi.medical.domain.Medecine;
import sn.ngirwi.medical.repository.MedecineRepository;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.service.mapper.MedecineMapper;

/**
 * Integration tests for the {@link MedecineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedecineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;

    private static final Double DEFAULT_FREQUENCY = 1D;
    private static final Double UPDATED_FREQUENCY = 2D;

    private static final String ENTITY_API_URL = "/api/medecines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedecineRepository medecineRepository;

    @Autowired
    private MedecineMapper medecineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedecineMockMvc;

    private Medecine medecine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecine createEntity(EntityManager em) {
        Medecine medecine = new Medecine().name(DEFAULT_NAME).duration(DEFAULT_DURATION).frequency(DEFAULT_FREQUENCY);
        return medecine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecine createUpdatedEntity(EntityManager em) {
        Medecine medecine = new Medecine().name(UPDATED_NAME).duration(UPDATED_DURATION).frequency(UPDATED_FREQUENCY);
        return medecine;
    }

    @BeforeEach
    public void initTest() {
        medecine = createEntity(em);
    }

    @Test
    @Transactional
    void createMedecine() throws Exception {
        int databaseSizeBeforeCreate = medecineRepository.findAll().size();
        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);
        restMedecineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecineDTO)))
            .andExpect(status().isCreated());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeCreate + 1);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedecine.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMedecine.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    @Transactional
    void createMedecineWithExistingId() throws Exception {
        // Create the Medecine with an existing ID
        medecine.setId(1L);
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        int databaseSizeBeforeCreate = medecineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedecineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedecines() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        // Get all the medecineList
        restMedecineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.doubleValue())));
    }

    @Test
    @Transactional
    void getMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        // Get the medecine
        restMedecineMockMvc
            .perform(get(ENTITY_API_URL_ID, medecine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medecine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMedecine() throws Exception {
        // Get the medecine
        restMedecineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();

        // Update the medecine
        Medecine updatedMedecine = medecineRepository.findById(medecine.getId()).get();
        // Disconnect from session so that the updates on updatedMedecine are not directly saved in db
        em.detach(updatedMedecine);
        updatedMedecine.name(UPDATED_NAME).duration(UPDATED_DURATION).frequency(UPDATED_FREQUENCY);
        MedecineDTO medecineDTO = medecineMapper.toDto(updatedMedecine);

        restMedecineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medecineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedecine.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMedecine.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void putNonExistingMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medecineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medecineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedecineWithPatch() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();

        // Update the medecine using partial update
        Medecine partialUpdatedMedecine = new Medecine();
        partialUpdatedMedecine.setId(medecine.getId());

        partialUpdatedMedecine.frequency(UPDATED_FREQUENCY);

        restMedecineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedecine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedecine))
            )
            .andExpect(status().isOk());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedecine.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMedecine.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void fullUpdateMedecineWithPatch() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();

        // Update the medecine using partial update
        Medecine partialUpdatedMedecine = new Medecine();
        partialUpdatedMedecine.setId(medecine.getId());

        partialUpdatedMedecine.name(UPDATED_NAME).duration(UPDATED_DURATION).frequency(UPDATED_FREQUENCY);

        restMedecineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedecine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedecine))
            )
            .andExpect(status().isOk());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
        Medecine testMedecine = medecineList.get(medecineList.size() - 1);
        assertThat(testMedecine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedecine.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMedecine.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    void patchNonExistingMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medecineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedecine() throws Exception {
        int databaseSizeBeforeUpdate = medecineRepository.findAll().size();
        medecine.setId(count.incrementAndGet());

        // Create the Medecine
        MedecineDTO medecineDTO = medecineMapper.toDto(medecine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedecineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medecineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medecine in the database
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedecine() throws Exception {
        // Initialize the database
        medecineRepository.saveAndFlush(medecine);

        int databaseSizeBeforeDelete = medecineRepository.findAll().size();

        // Delete the medecine
        restMedecineMockMvc
            .perform(delete(ENTITY_API_URL_ID, medecine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medecine> medecineList = medecineRepository.findAll();
        assertThat(medecineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
