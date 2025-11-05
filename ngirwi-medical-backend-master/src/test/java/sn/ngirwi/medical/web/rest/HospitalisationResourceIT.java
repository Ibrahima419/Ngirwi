package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.HospitalisationRepository;

/**
 * Integration tests for the {@link HospitalisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HospitalisationResourceIT {

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DOCTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCTOR_NAME = "BBBBBBBBBB";

    private static final HospitalisationStatus DEFAULT_STATUS = HospitalisationStatus.STARTED;
    private static final HospitalisationStatus UPDATED_STATUS = HospitalisationStatus.ONGOING;

    private static final String ENTITY_API_URL = "/api/hospitalisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HospitalisationRepository hospitalisationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalisationMockMvc;

    private Hospitalisation hospitalisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospitalisation createEntity(EntityManager em) {
        Hospitalisation hospitalisation = new Hospitalisation()
            .entryDate(DEFAULT_ENTRY_DATE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .doctorName(DEFAULT_DOCTOR_NAME)
            .status(DEFAULT_STATUS);
        return hospitalisation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospitalisation createUpdatedEntity(EntityManager em) {
        Hospitalisation hospitalisation = new Hospitalisation()
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .doctorName(UPDATED_DOCTOR_NAME)
            .status(UPDATED_STATUS);
        return hospitalisation;
    }

    @BeforeEach
    public void initTest() {
        hospitalisation = createEntity(em);
    }

    @Test
    @Transactional
    void createHospitalisation() throws Exception {
        int databaseSizeBeforeCreate = hospitalisationRepository.findAll().size();
        // Create the Hospitalisation
        restHospitalisationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isCreated());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeCreate + 1);
        Hospitalisation testHospitalisation = hospitalisationList.get(hospitalisationList.size() - 1);
        assertThat(testHospitalisation.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testHospitalisation.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testHospitalisation.getDoctorName()).isEqualTo(DEFAULT_DOCTOR_NAME);
        assertThat(testHospitalisation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createHospitalisationWithExistingId() throws Exception {
        // Create the Hospitalisation with an existing ID
        hospitalisation.setId(1L);

        int databaseSizeBeforeCreate = hospitalisationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalisationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHospitalisations() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        // Get all the hospitalisationList
        restHospitalisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospitalisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].doctorName").value(hasItem(DEFAULT_DOCTOR_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        // Get the hospitalisation
        restHospitalisationMockMvc
            .perform(get(ENTITY_API_URL_ID, hospitalisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospitalisation.getId().intValue()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.doctorName").value(DEFAULT_DOCTOR_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHospitalisation() throws Exception {
        // Get the hospitalisation
        restHospitalisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();

        // Update the hospitalisation
        Hospitalisation updatedHospitalisation = hospitalisationRepository.findById(hospitalisation.getId()).get();
        // Disconnect from session so that the updates on updatedHospitalisation are not directly saved in db
        em.detach(updatedHospitalisation);
        updatedHospitalisation
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .doctorName(UPDATED_DOCTOR_NAME)
            .status(UPDATED_STATUS);

        restHospitalisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHospitalisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHospitalisation))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
        Hospitalisation testHospitalisation = hospitalisationList.get(hospitalisationList.size() - 1);
        assertThat(testHospitalisation.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testHospitalisation.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testHospitalisation.getDoctorName()).isEqualTo(UPDATED_DOCTOR_NAME);
        assertThat(testHospitalisation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHospitalisationWithPatch() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();

        // Update the hospitalisation using partial update
        Hospitalisation partialUpdatedHospitalisation = new Hospitalisation();
        partialUpdatedHospitalisation.setId(hospitalisation.getId());

        partialUpdatedHospitalisation.entryDate(UPDATED_ENTRY_DATE).releaseDate(UPDATED_RELEASE_DATE).doctorName(UPDATED_DOCTOR_NAME);

        restHospitalisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospitalisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospitalisation))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
        Hospitalisation testHospitalisation = hospitalisationList.get(hospitalisationList.size() - 1);
        assertThat(testHospitalisation.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testHospitalisation.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testHospitalisation.getDoctorName()).isEqualTo(UPDATED_DOCTOR_NAME);
        assertThat(testHospitalisation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateHospitalisationWithPatch() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();

        // Update the hospitalisation using partial update
        Hospitalisation partialUpdatedHospitalisation = new Hospitalisation();
        partialUpdatedHospitalisation.setId(hospitalisation.getId());

        partialUpdatedHospitalisation
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .doctorName(UPDATED_DOCTOR_NAME)
            .status(UPDATED_STATUS);

        restHospitalisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospitalisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospitalisation))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
        Hospitalisation testHospitalisation = hospitalisationList.get(hospitalisationList.size() - 1);
        assertThat(testHospitalisation.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testHospitalisation.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testHospitalisation.getDoctorName()).isEqualTo(UPDATED_DOCTOR_NAME);
        assertThat(testHospitalisation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hospitalisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHospitalisation() throws Exception {
        int databaseSizeBeforeUpdate = hospitalisationRepository.findAll().size();
        hospitalisation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalisationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalisation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospitalisation in the database
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHospitalisation() throws Exception {
        // Initialize the database
        hospitalisationRepository.saveAndFlush(hospitalisation);

        int databaseSizeBeforeDelete = hospitalisationRepository.findAll().size();

        // Delete the hospitalisation
        restHospitalisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, hospitalisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospitalisation> hospitalisationList = hospitalisationRepository.findAll();
        assertThat(hospitalisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
