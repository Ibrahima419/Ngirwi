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
import sn.ngirwi.medical.domain.DossierMedical;
import sn.ngirwi.medical.repository.DossierMedicalRepository;
import sn.ngirwi.medical.service.dto.DossierMedicalDTO;
import sn.ngirwi.medical.service.mapper.DossierMedicalMapper;

/**
 * Integration tests for the {@link DossierMedicalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierMedicalResourceIT {

    private static final String DEFAULT_MOTIF_CONSULTATION = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF_CONSULTATION = "BBBBBBBBBB";

    private static final String DEFAULT_HISTOIRE_MALADIE = "AAAAAAAAAA";
    private static final String UPDATED_HISTOIRE_MALADIE = "BBBBBBBBBB";

    private static final String DEFAULT_TERRAIN = "AAAAAAAAAA";
    private static final String UPDATED_TERRAIN = "BBBBBBBBBB";

    private static final String DEFAULT_ANTECEDANTS_PERSONNELS = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDANTS_PERSONNELS = "BBBBBBBBBB";

    private static final String DEFAULT_ANTECEDANTS_CHIRURGICAUX = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDANTS_CHIRURGICAUX = "BBBBBBBBBB";

    private static final String DEFAULT_ANTECEDANTS_FAMILIAUX = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDANTS_FAMILIAUX = "BBBBBBBBBB";

    private static final String DEFAULT_GYNECO_OBSTRETRIQUE = "AAAAAAAAAA";
    private static final String UPDATED_GYNECO_OBSTRETRIQUE = "BBBBBBBBBB";

    private static final String DEFAULT_SYNDROMIQUE = "AAAAAAAAAA";
    private static final String UPDATED_SYNDROMIQUE = "BBBBBBBBBB";

    private static final String DEFAULT_DAD = "AAAAAAAAAA";
    private static final String UPDATED_DAD = "BBBBBBBBBB";

    private static final String DEFAULT_MOM = "AAAAAAAAAA";
    private static final String UPDATED_MOM = "BBBBBBBBBB";

    private static final String DEFAULT_SIBLINGS = "AAAAAAAAAA";
    private static final String UPDATED_SIBLINGS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCENDANTS = "AAAAAAAAAA";
    private static final String UPDATED_DESCENDANTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dossier-medicals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Autowired
    private DossierMedicalMapper dossierMedicalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDossierMedicalMockMvc;

    private DossierMedical dossierMedical;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierMedical createEntity(EntityManager em) {
        DossierMedical dossierMedical = new DossierMedical()
            .motifConsultation(DEFAULT_MOTIF_CONSULTATION)
            .histoireMaladie(DEFAULT_HISTOIRE_MALADIE)
            .terrain(DEFAULT_TERRAIN)
            .antecedantsPersonnels(DEFAULT_ANTECEDANTS_PERSONNELS)
            .antecedantsChirurgicaux(DEFAULT_ANTECEDANTS_CHIRURGICAUX)
            .antecedantsFamiliaux(DEFAULT_ANTECEDANTS_FAMILIAUX)
            .gynecoObstretrique(DEFAULT_GYNECO_OBSTRETRIQUE)
            .syndromique(DEFAULT_SYNDROMIQUE)
            .dad(DEFAULT_DAD)
            .mom(DEFAULT_MOM)
            .siblings(DEFAULT_SIBLINGS)
            .descendants(DEFAULT_DESCENDANTS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .author(DEFAULT_AUTHOR);
        return dossierMedical;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierMedical createUpdatedEntity(EntityManager em) {
        DossierMedical dossierMedical = new DossierMedical()
            .motifConsultation(UPDATED_MOTIF_CONSULTATION)
            .histoireMaladie(UPDATED_HISTOIRE_MALADIE)
            .terrain(UPDATED_TERRAIN)
            .antecedantsPersonnels(UPDATED_ANTECEDANTS_PERSONNELS)
            .antecedantsChirurgicaux(UPDATED_ANTECEDANTS_CHIRURGICAUX)
            .antecedantsFamiliaux(UPDATED_ANTECEDANTS_FAMILIAUX)
            .gynecoObstretrique(UPDATED_GYNECO_OBSTRETRIQUE)
            .syndromique(UPDATED_SYNDROMIQUE)
            .dad(UPDATED_DAD)
            .mom(UPDATED_MOM)
            .siblings(UPDATED_SIBLINGS)
            .descendants(UPDATED_DESCENDANTS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .author(UPDATED_AUTHOR);
        return dossierMedical;
    }

    @BeforeEach
    public void initTest() {
        dossierMedical = createEntity(em);
    }

    @Test
    @Transactional
    void createDossierMedical() throws Exception {
        int databaseSizeBeforeCreate = dossierMedicalRepository.findAll().size();
        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);
        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeCreate + 1);
        DossierMedical testDossierMedical = dossierMedicalList.get(dossierMedicalList.size() - 1);
        assertThat(testDossierMedical.getMotifConsultation()).isEqualTo(DEFAULT_MOTIF_CONSULTATION);
        assertThat(testDossierMedical.getHistoireMaladie()).isEqualTo(DEFAULT_HISTOIRE_MALADIE);
        assertThat(testDossierMedical.getTerrain()).isEqualTo(DEFAULT_TERRAIN);
        assertThat(testDossierMedical.getAntecedantsPersonnels()).isEqualTo(DEFAULT_ANTECEDANTS_PERSONNELS);
        assertThat(testDossierMedical.getAntecedantsChirurgicaux()).isEqualTo(DEFAULT_ANTECEDANTS_CHIRURGICAUX);
        assertThat(testDossierMedical.getAntecedantsFamiliaux()).isEqualTo(DEFAULT_ANTECEDANTS_FAMILIAUX);
        assertThat(testDossierMedical.getGynecoObstretrique()).isEqualTo(DEFAULT_GYNECO_OBSTRETRIQUE);
        assertThat(testDossierMedical.getSyndromique()).isEqualTo(DEFAULT_SYNDROMIQUE);
        assertThat(testDossierMedical.getDad()).isEqualTo(DEFAULT_DAD);
        assertThat(testDossierMedical.getMom()).isEqualTo(DEFAULT_MOM);
        assertThat(testDossierMedical.getSiblings()).isEqualTo(DEFAULT_SIBLINGS);
        assertThat(testDossierMedical.getDescendants()).isEqualTo(DEFAULT_DESCENDANTS);
        assertThat(testDossierMedical.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testDossierMedical.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testDossierMedical.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    void createDossierMedicalWithExistingId() throws Exception {
        // Create the DossierMedical with an existing ID
        dossierMedical.setId(1L);
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        int databaseSizeBeforeCreate = dossierMedicalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMotifConsultationIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setMotifConsultation(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHistoireMaladieIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setHistoireMaladie(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerrainIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setTerrain(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAntecedantsPersonnelsIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setAntecedantsPersonnels(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAntecedantsChirurgicauxIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setAntecedantsChirurgicaux(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAntecedantsFamiliauxIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setAntecedantsFamiliaux(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSyndromiqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierMedicalRepository.findAll().size();
        // set the field null
        dossierMedical.setSyndromique(null);

        // Create the DossierMedical, which fails.
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        restDossierMedicalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDossierMedicals() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        // Get all the dossierMedicalList
        restDossierMedicalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierMedical.getId().intValue())))
            .andExpect(jsonPath("$.[*].motifConsultation").value(hasItem(DEFAULT_MOTIF_CONSULTATION)))
            .andExpect(jsonPath("$.[*].histoireMaladie").value(hasItem(DEFAULT_HISTOIRE_MALADIE)))
            .andExpect(jsonPath("$.[*].terrain").value(hasItem(DEFAULT_TERRAIN)))
            .andExpect(jsonPath("$.[*].antecedantsPersonnels").value(hasItem(DEFAULT_ANTECEDANTS_PERSONNELS)))
            .andExpect(jsonPath("$.[*].antecedantsChirurgicaux").value(hasItem(DEFAULT_ANTECEDANTS_CHIRURGICAUX)))
            .andExpect(jsonPath("$.[*].antecedantsFamiliaux").value(hasItem(DEFAULT_ANTECEDANTS_FAMILIAUX)))
            .andExpect(jsonPath("$.[*].gynecoObstretrique").value(hasItem(DEFAULT_GYNECO_OBSTRETRIQUE)))
            .andExpect(jsonPath("$.[*].syndromique").value(hasItem(DEFAULT_SYNDROMIQUE)))
            .andExpect(jsonPath("$.[*].dad").value(hasItem(DEFAULT_DAD)))
            .andExpect(jsonPath("$.[*].mom").value(hasItem(DEFAULT_MOM)))
            .andExpect(jsonPath("$.[*].siblings").value(hasItem(DEFAULT_SIBLINGS)))
            .andExpect(jsonPath("$.[*].descendants").value(hasItem(DEFAULT_DESCENDANTS)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)));
    }

    @Test
    @Transactional
    void getDossierMedical() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        // Get the dossierMedical
        restDossierMedicalMockMvc
            .perform(get(ENTITY_API_URL_ID, dossierMedical.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossierMedical.getId().intValue()))
            .andExpect(jsonPath("$.motifConsultation").value(DEFAULT_MOTIF_CONSULTATION))
            .andExpect(jsonPath("$.histoireMaladie").value(DEFAULT_HISTOIRE_MALADIE))
            .andExpect(jsonPath("$.terrain").value(DEFAULT_TERRAIN))
            .andExpect(jsonPath("$.antecedantsPersonnels").value(DEFAULT_ANTECEDANTS_PERSONNELS))
            .andExpect(jsonPath("$.antecedantsChirurgicaux").value(DEFAULT_ANTECEDANTS_CHIRURGICAUX))
            .andExpect(jsonPath("$.antecedantsFamiliaux").value(DEFAULT_ANTECEDANTS_FAMILIAUX))
            .andExpect(jsonPath("$.gynecoObstretrique").value(DEFAULT_GYNECO_OBSTRETRIQUE))
            .andExpect(jsonPath("$.syndromique").value(DEFAULT_SYNDROMIQUE))
            .andExpect(jsonPath("$.dad").value(DEFAULT_DAD))
            .andExpect(jsonPath("$.mom").value(DEFAULT_MOM))
            .andExpect(jsonPath("$.siblings").value(DEFAULT_SIBLINGS))
            .andExpect(jsonPath("$.descendants").value(DEFAULT_DESCENDANTS))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR));
    }

    @Test
    @Transactional
    void getNonExistingDossierMedical() throws Exception {
        // Get the dossierMedical
        restDossierMedicalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDossierMedical() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();

        // Update the dossierMedical
        DossierMedical updatedDossierMedical = dossierMedicalRepository.findById(dossierMedical.getId()).get();
        // Disconnect from session so that the updates on updatedDossierMedical are not directly saved in db
        em.detach(updatedDossierMedical);
        updatedDossierMedical
            .motifConsultation(UPDATED_MOTIF_CONSULTATION)
            .histoireMaladie(UPDATED_HISTOIRE_MALADIE)
            .terrain(UPDATED_TERRAIN)
            .antecedantsPersonnels(UPDATED_ANTECEDANTS_PERSONNELS)
            .antecedantsChirurgicaux(UPDATED_ANTECEDANTS_CHIRURGICAUX)
            .antecedantsFamiliaux(UPDATED_ANTECEDANTS_FAMILIAUX)
            .gynecoObstretrique(UPDATED_GYNECO_OBSTRETRIQUE)
            .syndromique(UPDATED_SYNDROMIQUE)
            .dad(UPDATED_DAD)
            .mom(UPDATED_MOM)
            .siblings(UPDATED_SIBLINGS)
            .descendants(UPDATED_DESCENDANTS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .author(UPDATED_AUTHOR);
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(updatedDossierMedical);

        restDossierMedicalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierMedicalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isOk());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
        DossierMedical testDossierMedical = dossierMedicalList.get(dossierMedicalList.size() - 1);
        assertThat(testDossierMedical.getMotifConsultation()).isEqualTo(UPDATED_MOTIF_CONSULTATION);
        assertThat(testDossierMedical.getHistoireMaladie()).isEqualTo(UPDATED_HISTOIRE_MALADIE);
        assertThat(testDossierMedical.getTerrain()).isEqualTo(UPDATED_TERRAIN);
        assertThat(testDossierMedical.getAntecedantsPersonnels()).isEqualTo(UPDATED_ANTECEDANTS_PERSONNELS);
        assertThat(testDossierMedical.getAntecedantsChirurgicaux()).isEqualTo(UPDATED_ANTECEDANTS_CHIRURGICAUX);
        assertThat(testDossierMedical.getAntecedantsFamiliaux()).isEqualTo(UPDATED_ANTECEDANTS_FAMILIAUX);
        assertThat(testDossierMedical.getGynecoObstretrique()).isEqualTo(UPDATED_GYNECO_OBSTRETRIQUE);
        assertThat(testDossierMedical.getSyndromique()).isEqualTo(UPDATED_SYNDROMIQUE);
        assertThat(testDossierMedical.getDad()).isEqualTo(UPDATED_DAD);
        assertThat(testDossierMedical.getMom()).isEqualTo(UPDATED_MOM);
        assertThat(testDossierMedical.getSiblings()).isEqualTo(UPDATED_SIBLINGS);
        assertThat(testDossierMedical.getDescendants()).isEqualTo(UPDATED_DESCENDANTS);
        assertThat(testDossierMedical.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testDossierMedical.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testDossierMedical.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void putNonExistingDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierMedicalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDossierMedicalWithPatch() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();

        // Update the dossierMedical using partial update
        DossierMedical partialUpdatedDossierMedical = new DossierMedical();
        partialUpdatedDossierMedical.setId(dossierMedical.getId());

        partialUpdatedDossierMedical
            .antecedantsChirurgicaux(UPDATED_ANTECEDANTS_CHIRURGICAUX)
            .gynecoObstretrique(UPDATED_GYNECO_OBSTRETRIQUE)
            .dad(UPDATED_DAD)
            .mom(UPDATED_MOM)
            .siblings(UPDATED_SIBLINGS)
            .descendants(UPDATED_DESCENDANTS)
            .author(UPDATED_AUTHOR);

        restDossierMedicalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierMedical.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierMedical))
            )
            .andExpect(status().isOk());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
        DossierMedical testDossierMedical = dossierMedicalList.get(dossierMedicalList.size() - 1);
        assertThat(testDossierMedical.getMotifConsultation()).isEqualTo(DEFAULT_MOTIF_CONSULTATION);
        assertThat(testDossierMedical.getHistoireMaladie()).isEqualTo(DEFAULT_HISTOIRE_MALADIE);
        assertThat(testDossierMedical.getTerrain()).isEqualTo(DEFAULT_TERRAIN);
        assertThat(testDossierMedical.getAntecedantsPersonnels()).isEqualTo(DEFAULT_ANTECEDANTS_PERSONNELS);
        assertThat(testDossierMedical.getAntecedantsChirurgicaux()).isEqualTo(UPDATED_ANTECEDANTS_CHIRURGICAUX);
        assertThat(testDossierMedical.getAntecedantsFamiliaux()).isEqualTo(DEFAULT_ANTECEDANTS_FAMILIAUX);
        assertThat(testDossierMedical.getGynecoObstretrique()).isEqualTo(UPDATED_GYNECO_OBSTRETRIQUE);
        assertThat(testDossierMedical.getSyndromique()).isEqualTo(DEFAULT_SYNDROMIQUE);
        assertThat(testDossierMedical.getDad()).isEqualTo(UPDATED_DAD);
        assertThat(testDossierMedical.getMom()).isEqualTo(UPDATED_MOM);
        assertThat(testDossierMedical.getSiblings()).isEqualTo(UPDATED_SIBLINGS);
        assertThat(testDossierMedical.getDescendants()).isEqualTo(UPDATED_DESCENDANTS);
        assertThat(testDossierMedical.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testDossierMedical.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testDossierMedical.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void fullUpdateDossierMedicalWithPatch() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();

        // Update the dossierMedical using partial update
        DossierMedical partialUpdatedDossierMedical = new DossierMedical();
        partialUpdatedDossierMedical.setId(dossierMedical.getId());

        partialUpdatedDossierMedical
            .motifConsultation(UPDATED_MOTIF_CONSULTATION)
            .histoireMaladie(UPDATED_HISTOIRE_MALADIE)
            .terrain(UPDATED_TERRAIN)
            .antecedantsPersonnels(UPDATED_ANTECEDANTS_PERSONNELS)
            .antecedantsChirurgicaux(UPDATED_ANTECEDANTS_CHIRURGICAUX)
            .antecedantsFamiliaux(UPDATED_ANTECEDANTS_FAMILIAUX)
            .gynecoObstretrique(UPDATED_GYNECO_OBSTRETRIQUE)
            .syndromique(UPDATED_SYNDROMIQUE)
            .dad(UPDATED_DAD)
            .mom(UPDATED_MOM)
            .siblings(UPDATED_SIBLINGS)
            .descendants(UPDATED_DESCENDANTS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .author(UPDATED_AUTHOR);

        restDossierMedicalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierMedical.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierMedical))
            )
            .andExpect(status().isOk());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
        DossierMedical testDossierMedical = dossierMedicalList.get(dossierMedicalList.size() - 1);
        assertThat(testDossierMedical.getMotifConsultation()).isEqualTo(UPDATED_MOTIF_CONSULTATION);
        assertThat(testDossierMedical.getHistoireMaladie()).isEqualTo(UPDATED_HISTOIRE_MALADIE);
        assertThat(testDossierMedical.getTerrain()).isEqualTo(UPDATED_TERRAIN);
        assertThat(testDossierMedical.getAntecedantsPersonnels()).isEqualTo(UPDATED_ANTECEDANTS_PERSONNELS);
        assertThat(testDossierMedical.getAntecedantsChirurgicaux()).isEqualTo(UPDATED_ANTECEDANTS_CHIRURGICAUX);
        assertThat(testDossierMedical.getAntecedantsFamiliaux()).isEqualTo(UPDATED_ANTECEDANTS_FAMILIAUX);
        assertThat(testDossierMedical.getGynecoObstretrique()).isEqualTo(UPDATED_GYNECO_OBSTRETRIQUE);
        assertThat(testDossierMedical.getSyndromique()).isEqualTo(UPDATED_SYNDROMIQUE);
        assertThat(testDossierMedical.getDad()).isEqualTo(UPDATED_DAD);
        assertThat(testDossierMedical.getMom()).isEqualTo(UPDATED_MOM);
        assertThat(testDossierMedical.getSiblings()).isEqualTo(UPDATED_SIBLINGS);
        assertThat(testDossierMedical.getDescendants()).isEqualTo(UPDATED_DESCENDANTS);
        assertThat(testDossierMedical.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testDossierMedical.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testDossierMedical.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void patchNonExistingDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossierMedicalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDossierMedical() throws Exception {
        int databaseSizeBeforeUpdate = dossierMedicalRepository.findAll().size();
        dossierMedical.setId(count.incrementAndGet());

        // Create the DossierMedical
        DossierMedicalDTO dossierMedicalDTO = dossierMedicalMapper.toDto(dossierMedical);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMedicalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierMedicalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierMedical in the database
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDossierMedical() throws Exception {
        // Initialize the database
        dossierMedicalRepository.saveAndFlush(dossierMedical);

        int databaseSizeBeforeDelete = dossierMedicalRepository.findAll().size();

        // Delete the dossierMedical
        restDossierMedicalMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossierMedical.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DossierMedical> dossierMedicalList = dossierMedicalRepository.findAll();
        assertThat(dossierMedicalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
