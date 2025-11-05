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
import org.springframework.util.Base64Utils;
import sn.ngirwi.medical.IntegrationTest;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.repository.HospitalRepository;

/**
 * Integration tests for the {@link HospitalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HospitalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/hospitals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalMockMvc;

    private Hospital hospital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(DEFAULT_NAME)
            .adress(DEFAULT_ADRESS)
            .phone(DEFAULT_PHONE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return hospital;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createUpdatedEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .phone(UPDATED_PHONE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return hospital;
    }

    @BeforeEach
    public void initTest() {
        hospital = createEntity(em);
    }

    @Test
    @Transactional
    void createHospital() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();
        // Create the Hospital
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isCreated());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate + 1);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testHospital.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testHospital.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testHospital.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createHospitalWithExistingId() throws Exception {
        // Create the Hospital with an existing ID
        hospital.setId(1L);

        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHospitals() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get the hospital
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL_ID, hospital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospital.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getNonExistingHospital() throws Exception {
        // Get the hospital
        restHospitalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital
        Hospital updatedHospital = hospitalRepository.findById(hospital.getId()).get();
        // Disconnect from session so that the updates on updatedHospital are not directly saved in db
        em.detach(updatedHospital);
        updatedHospital
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .phone(UPDATED_PHONE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHospital.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testHospital.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHospital.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testHospital.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospital.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospital))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospital))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testHospital.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testHospital.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testHospital.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        partialUpdatedHospital
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS)
            .phone(UPDATED_PHONE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testHospital.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHospital.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testHospital.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospital))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospital))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hospital)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeDelete = hospitalRepository.findAll().size();

        // Delete the hospital
        restHospitalMockMvc
            .perform(delete(ENTITY_API_URL_ID, hospital.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
