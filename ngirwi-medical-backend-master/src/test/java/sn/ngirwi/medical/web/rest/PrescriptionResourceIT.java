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
import sn.ngirwi.medical.domain.Prescription;
import sn.ngirwi.medical.repository.PrescriptionRepository;
import sn.ngirwi.medical.service.dto.PrescriptionDTO;
import sn.ngirwi.medical.service.mapper.PrescriptionMapper;

/**
 * Integration tests for the {@link PrescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrescriptionResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prescriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createEntity(EntityManager em) {
        Prescription prescription = new Prescription().creationDate(DEFAULT_CREATION_DATE).author(DEFAULT_AUTHOR);
        return prescription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createUpdatedEntity(EntityManager em) {
        Prescription prescription = new Prescription().creationDate(UPDATED_CREATION_DATE).author(UPDATED_AUTHOR);
        return prescription;
    }

    @BeforeEach
    public void initTest() {
        prescription = createEntity(em);
    }

    @Test
    @Transactional
    void createPrescription() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();
        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);
        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPrescription.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    void createPrescriptionWithExistingId() throws Exception {
        // Create the Prescription with an existing ID
        prescription.setId(1L);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrescriptions() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)));
    }

    @Test
    @Transactional
    void getPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get the prescription
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prescription.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR));
    }

    @Test
    @Transactional
    void getNonExistingPrescription() throws Exception {
        // Get the prescription
        restPrescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).get();
        // Disconnect from session so that the updates on updatedPrescription are not directly saved in db
        em.detach(updatedPrescription);
        updatedPrescription.creationDate(UPDATED_CREATION_DATE).author(UPDATED_AUTHOR);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(updatedPrescription);

        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPrescription.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void putNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPrescription.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    void fullUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        partialUpdatedPrescription.creationDate(UPDATED_CREATION_DATE).author(UPDATED_AUTHOR);

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPrescription.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void patchNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeDelete = prescriptionRepository.findAll().size();

        // Delete the prescription
        restPrescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prescription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
