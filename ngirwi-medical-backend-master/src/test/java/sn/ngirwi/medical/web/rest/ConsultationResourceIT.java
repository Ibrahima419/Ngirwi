package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import sn.ngirwi.medical.IntegrationTest;
import sn.ngirwi.medical.domain.Consultation;
import sn.ngirwi.medical.repository.ConsultationRepository;
import sn.ngirwi.medical.service.ConsultationService;
import sn.ngirwi.medical.service.dto.ConsultationDTO;
import sn.ngirwi.medical.service.mapper.ConsultationMapper;

/**
 * Integration tests for the {@link ConsultationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConsultationResourceIT {

    private static final Instant DEFAULT_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    private static final String DEFAULT_TENSION = "AAAAAAAAAA";
    private static final String UPDATED_TENSION = "BBBBBBBBBB";

    private static final String DEFAULT_GLYCEMIE = "AAAAAAAAAA";
    private static final String UPDATED_GLYCEMIE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_HYPOTHESIS = "AAAAAAAAAA";
    private static final String UPDATED_HYPOTHESIS = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMS = "AAAAAAAAAA";
    private static final String UPDATED_EXAMS = "BBBBBBBBBB";

    private static final String DEFAULT_TREATMENT = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_OG = "AAAAAAAAAA";
    private static final String UPDATED_OG = "BBBBBBBBBB";

    private static final String DEFAULT_VD = "AAAAAAAAAA";
    private static final String UPDATED_VD = "BBBBBBBBBB";

    private static final String DEFAULT_ESEPTUM = "AAAAAAAAAA";
    private static final String UPDATED_ESEPTUM = "BBBBBBBBBB";

    private static final String DEFAULT_AO = "AAAAAAAAAA";
    private static final String UPDATED_AO = "BBBBBBBBBB";

    private static final String DEFAULT_V_G_DIASTOLE = "AAAAAAAAAA";
    private static final String UPDATED_V_G_DIASTOLE = "BBBBBBBBBB";

    private static final String DEFAULT_OUVERTURE_AO = "AAAAAAAAAA";
    private static final String UPDATED_OUVERTURE_AO = "BBBBBBBBBB";

    private static final String DEFAULT_V_G_SYSTOLE = "AAAAAAAAAA";
    private static final String UPDATED_V_G_SYSTOLE = "BBBBBBBBBB";

    private static final String DEFAULT_VP = "AAAAAAAAAA";
    private static final String UPDATED_VP = "BBBBBBBBBB";

    private static final String DEFAULT_O_G_AO = "AAAAAAAAAA";
    private static final String UPDATED_O_G_AO = "BBBBBBBBBB";

    private static final String DEFAULT_F_R_TEICHOLTZ = "AAAAAAAAAA";
    private static final String UPDATED_F_R_TEICHOLTZ = "BBBBBBBBBB";

    private static final String DEFAULT_E_VP = "AAAAAAAAAA";
    private static final String UPDATED_E_VP = "BBBBBBBBBB";

    private static final String DEFAULT_SEPTUM_VG = "AAAAAAAAAA";
    private static final String UPDATED_SEPTUM_VG = "BBBBBBBBBB";

    private static final String DEFAULT_F_E_TEICHOLZ = "AAAAAAAAAA";
    private static final String UPDATED_F_E_TEICHOLZ = "BBBBBBBBBB";

    private static final String DEFAULT_TAPSE = "AAAAAAAAAA";
    private static final String UPDATED_TAPSE = "BBBBBBBBBB";

    private static final String DEFAULT_PAROI_POST = "AAAAAAAAAA";
    private static final String UPDATED_PAROI_POST = "BBBBBBBBBB";

    private static final String DEFAULT_SURFACE_OG = "AAAAAAAAAA";
    private static final String UPDATED_SURFACE_OG = "BBBBBBBBBB";

    private static final String DEFAULT_SURFACE_OD = "AAAAAAAAAA";
    private static final String UPDATED_SURFACE_OD = "BBBBBBBBBB";

    private static final String DEFAULT_MESURE_VD = "AAAAAAAAAA";
    private static final String UPDATED_MESURE_VD = "BBBBBBBBBB";

    private static final String DEFAULT_FE = "AAAAAAAAAA";
    private static final String UPDATED_FE = "BBBBBBBBBB";

    private static final String DEFAULT_FE_A_2_C = "AAAAAAAAAA";
    private static final String UPDATED_FE_A_2_C = "BBBBBBBBBB";

    private static final String DEFAULT_FE_BIPLAN = "AAAAAAAAAA";
    private static final String UPDATED_FE_BIPLAN = "BBBBBBBBBB";

    private static final String DEFAULT_E = "AAAAAAAAAA";
    private static final String UPDATED_E = "BBBBBBBBBB";

    private static final String DEFAULT_A = "AAAAAAAAAA";
    private static final String UPDATED_A = "BBBBBBBBBB";

    private static final String DEFAULT_E_A = "AAAAAAAAAA";
    private static final String UPDATED_E_A = "BBBBBBBBBB";

    private static final String DEFAULT_TD = "AAAAAAAAAA";
    private static final String UPDATED_TD = "BBBBBBBBBB";

    private static final String DEFAULT_TRIV = "AAAAAAAAAA";
    private static final String UPDATED_TRIV = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE_AM_IM = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_AM_IM = "BBBBBBBBBB";

    private static final String DEFAULT_SURFACE_REGURGITEE = "AAAAAAAAAA";
    private static final String UPDATED_SURFACE_REGURGITEE = "BBBBBBBBBB";

    private static final String DEFAULT_PBA = "AAAAAAAAAA";
    private static final String UPDATED_PBA = "BBBBBBBBBB";

    private static final String DEFAULT_QR = "AAAAAAAAAA";
    private static final String UPDATED_QR = "BBBBBBBBBB";

    private static final String DEFAULT_VR = "AAAAAAAAAA";
    private static final String UPDATED_VR = "BBBBBBBBBB";

    private static final String DEFAULT_SOR = "AAAAAAAAAA";
    private static final String UPDATED_SOR = "BBBBBBBBBB";

    private static final String DEFAULT_FR = "AAAAAAAAAA";
    private static final String UPDATED_FR = "BBBBBBBBBB";

    private static final String DEFAULT_VMAX_AP = "AAAAAAAAAA";
    private static final String UPDATED_VMAX_AP = "BBBBBBBBBB";

    private static final String DEFAULT_ITV = "AAAAAAAAAA";
    private static final String UPDATED_ITV = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_MAX = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_MAX = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_MOY = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_MOY = "BBBBBBBBBB";

    private static final String DEFAULT_DC = "AAAAAAAAAA";
    private static final String UPDATED_DC = "BBBBBBBBBB";

    private static final String DEFAULT_I_AOEXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_I_AOEXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_VENA_CONTRACTA = "AAAAAAAAAA";
    private static final String UPDATED_VENA_CONTRACTA = "BBBBBBBBBB";

    private static final String DEFAULT_PHT = "AAAAAAAAAA";
    private static final String UPDATED_PHT = "BBBBBBBBBB";

    private static final String DEFAULT_I_T_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_I_T_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_MAX_B = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_MAX_B = "BBBBBBBBBB";

    private static final String DEFAULT_PAPS = "AAAAAAAAAA";
    private static final String UPDATED_PAPS = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_VMAX = "AAAAAAAAAA";
    private static final String UPDATED_VMAX = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_MAX_C = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_MAX_C = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_MOY_B = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_MOY_B = "BBBBBBBBBB";

    private static final String DEFAULT_S = "AAAAAAAAAA";
    private static final String UPDATED_S = "BBBBBBBBBB";

    private static final String DEFAULT_D = "AAAAAAAAAA";
    private static final String UPDATED_D = "BBBBBBBBBB";

    private static final String DEFAULT_S_D = "AAAAAAAAAA";
    private static final String UPDATED_S_D = "BBBBBBBBBB";

    private static final String DEFAULT_A_A = "AAAAAAAAAA";
    private static final String UPDATED_A_A = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE_AP = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_AP = "BBBBBBBBBB";

    private static final String DEFAULT_E_AA = "AAAAAAAAAA";
    private static final String UPDATED_E_AA = "BBBBBBBBBB";

    private static final String DEFAULT_A_AA = "AAAAAAAAAA";
    private static final String UPDATED_A_AA = "BBBBBBBBBB";

    private static final String DEFAULT_E_A_AA = "AAAAAAAAAA";
    private static final String UPDATED_E_A_AA = "BBBBBBBBBB";

    private static final String DEFAULT_E_EA = "AAAAAAAAAA";
    private static final String UPDATED_E_EA = "BBBBBBBBBB";

    private static final String DEFAULT_Z = "AAAAAAAAAA";
    private static final String UPDATED_Z = "BBBBBBBBBB";

    private static final String DEFAULT_EXAM_GENERAL = "AAAAAAAAAA";
    private static final String UPDATED_EXAM_GENERAL = "BBBBBBBBBB";

    private static final Double DEFAULT_FREQUENCE_RESPIRATOIRE = 1D;
    private static final Double UPDATED_FREQUENCE_RESPIRATOIRE = 2D;

    private static final Double DEFAULT_FREQUENCE_CARDIAQUE = 1D;
    private static final Double UPDATED_FREQUENCE_CARDIAQUE = 2D;

    private static final String DEFAULT_COMMENTAIRE_LIBRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_LIBRE = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTATS_PARACLINIQUE = "AAAAAAAAAA";
    private static final String UPDATED_RESULTATS_PARACLINIQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultationRepository consultationRepository;

    @Mock
    private ConsultationRepository consultationRepositoryMock;

    @Autowired
    private ConsultationMapper consultationMapper;

    @Mock
    private ConsultationService consultationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultationMockMvc;

    private Consultation consultation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultation createEntity(EntityManager em) {
        Consultation consultation = new Consultation()
            .dateTime(DEFAULT_DATE_TIME)
            .temperature(DEFAULT_TEMPERATURE)
            .weight(DEFAULT_WEIGHT)
            .tension(DEFAULT_TENSION)
            .glycemie(DEFAULT_GLYCEMIE)
            .comment(DEFAULT_COMMENT)
            .hypothesis(DEFAULT_HYPOTHESIS)
            .exams(DEFAULT_EXAMS)
            .treatment(DEFAULT_TREATMENT)
            .author(DEFAULT_AUTHOR)
            .og(DEFAULT_OG)
            .vd(DEFAULT_VD)
            .eseptum(DEFAULT_ESEPTUM)
            .ao(DEFAULT_AO)
            .vGDiastole(DEFAULT_V_G_DIASTOLE)
            .ouvertureAo(DEFAULT_OUVERTURE_AO)
            .vGSystole(DEFAULT_V_G_SYSTOLE)
            .vp(DEFAULT_VP)
            .oGAo(DEFAULT_O_G_AO)
            .fRTeicholtz(DEFAULT_F_R_TEICHOLTZ)
            .eVp(DEFAULT_E_VP)
            .septumVg(DEFAULT_SEPTUM_VG)
            .fETeicholz(DEFAULT_F_E_TEICHOLZ)
            .tapse(DEFAULT_TAPSE)
            .paroiPost(DEFAULT_PAROI_POST)
            .surfaceOg(DEFAULT_SURFACE_OG)
            .surfaceOd(DEFAULT_SURFACE_OD)
            .mesureVd(DEFAULT_MESURE_VD)
            .fe(DEFAULT_FE)
            .feA2C(DEFAULT_FE_A_2_C)
            .feBiplan(DEFAULT_FE_BIPLAN)
            .e(DEFAULT_E)
            .a(DEFAULT_A)
            .eA(DEFAULT_E_A)
            .td(DEFAULT_TD)
            .triv(DEFAULT_TRIV)
            .dureeAmIm(DEFAULT_DUREE_AM_IM)
            .surfaceRegurgitee(DEFAULT_SURFACE_REGURGITEE)
            .pba(DEFAULT_PBA)
            .qr(DEFAULT_QR)
            .vr(DEFAULT_VR)
            .sor(DEFAULT_SOR)
            .fr(DEFAULT_FR)
            .vmaxAp(DEFAULT_VMAX_AP)
            .itv(DEFAULT_ITV)
            .gradMax(DEFAULT_GRAD_MAX)
            .gradMoy(DEFAULT_GRAD_MOY)
            .dc(DEFAULT_DC)
            .iAoextension(DEFAULT_I_AOEXTENSION)
            .venaContracta(DEFAULT_VENA_CONTRACTA)
            .pht(DEFAULT_PHT)
            .iTExtension(DEFAULT_I_T_EXTENSION)
            .gradMaxB(DEFAULT_GRAD_MAX_B)
            .paps(DEFAULT_PAPS)
            .ip(DEFAULT_IP)
            .vmax(DEFAULT_VMAX)
            .gradMaxC(DEFAULT_GRAD_MAX_C)
            .gradMoyB(DEFAULT_GRAD_MOY_B)
            .s(DEFAULT_S)
            .d(DEFAULT_D)
            .sD(DEFAULT_S_D)
            .aA(DEFAULT_A_A)
            .dureeAp(DEFAULT_DUREE_AP)
            .eAA(DEFAULT_E_AA)
            .aAA(DEFAULT_A_AA)
            .eAAa(DEFAULT_E_A_AA)
            .eEa(DEFAULT_E_EA)
            .z(DEFAULT_Z)
            .examGeneral(DEFAULT_EXAM_GENERAL)
            .frequenceRespiratoire(DEFAULT_FREQUENCE_RESPIRATOIRE)
            .frequenceCardiaque(DEFAULT_FREQUENCE_CARDIAQUE)
            .commentaireLibre(DEFAULT_COMMENTAIRE_LIBRE)
            .resultatsParaclinique(DEFAULT_RESULTATS_PARACLINIQUE);
        return consultation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultation createUpdatedEntity(EntityManager em) {
        Consultation consultation = new Consultation()
            .dateTime(UPDATED_DATE_TIME)
            .temperature(UPDATED_TEMPERATURE)
            .weight(UPDATED_WEIGHT)
            .tension(UPDATED_TENSION)
            .glycemie(UPDATED_GLYCEMIE)
            .comment(UPDATED_COMMENT)
            .hypothesis(UPDATED_HYPOTHESIS)
            .exams(UPDATED_EXAMS)
            .treatment(UPDATED_TREATMENT)
            .author(UPDATED_AUTHOR)
            .og(UPDATED_OG)
            .vd(UPDATED_VD)
            .eseptum(UPDATED_ESEPTUM)
            .ao(UPDATED_AO)
            .vGDiastole(UPDATED_V_G_DIASTOLE)
            .ouvertureAo(UPDATED_OUVERTURE_AO)
            .vGSystole(UPDATED_V_G_SYSTOLE)
            .vp(UPDATED_VP)
            .oGAo(UPDATED_O_G_AO)
            .fRTeicholtz(UPDATED_F_R_TEICHOLTZ)
            .eVp(UPDATED_E_VP)
            .septumVg(UPDATED_SEPTUM_VG)
            .fETeicholz(UPDATED_F_E_TEICHOLZ)
            .tapse(UPDATED_TAPSE)
            .paroiPost(UPDATED_PAROI_POST)
            .surfaceOg(UPDATED_SURFACE_OG)
            .surfaceOd(UPDATED_SURFACE_OD)
            .mesureVd(UPDATED_MESURE_VD)
            .fe(UPDATED_FE)
            .feA2C(UPDATED_FE_A_2_C)
            .feBiplan(UPDATED_FE_BIPLAN)
            .e(UPDATED_E)
            .a(UPDATED_A)
            .eA(UPDATED_E_A)
            .td(UPDATED_TD)
            .triv(UPDATED_TRIV)
            .dureeAmIm(UPDATED_DUREE_AM_IM)
            .surfaceRegurgitee(UPDATED_SURFACE_REGURGITEE)
            .pba(UPDATED_PBA)
            .qr(UPDATED_QR)
            .vr(UPDATED_VR)
            .sor(UPDATED_SOR)
            .fr(UPDATED_FR)
            .vmaxAp(UPDATED_VMAX_AP)
            .itv(UPDATED_ITV)
            .gradMax(UPDATED_GRAD_MAX)
            .gradMoy(UPDATED_GRAD_MOY)
            .dc(UPDATED_DC)
            .iAoextension(UPDATED_I_AOEXTENSION)
            .venaContracta(UPDATED_VENA_CONTRACTA)
            .pht(UPDATED_PHT)
            .iTExtension(UPDATED_I_T_EXTENSION)
            .gradMaxB(UPDATED_GRAD_MAX_B)
            .paps(UPDATED_PAPS)
            .ip(UPDATED_IP)
            .vmax(UPDATED_VMAX)
            .gradMaxC(UPDATED_GRAD_MAX_C)
            .gradMoyB(UPDATED_GRAD_MOY_B)
            .s(UPDATED_S)
            .d(UPDATED_D)
            .sD(UPDATED_S_D)
            .aA(UPDATED_A_A)
            .dureeAp(UPDATED_DUREE_AP)
            .eAA(UPDATED_E_AA)
            .aAA(UPDATED_A_AA)
            .eAAa(UPDATED_E_A_AA)
            .eEa(UPDATED_E_EA)
            .z(UPDATED_Z)
            .examGeneral(UPDATED_EXAM_GENERAL)
            .frequenceRespiratoire(UPDATED_FREQUENCE_RESPIRATOIRE)
            .frequenceCardiaque(UPDATED_FREQUENCE_CARDIAQUE)
            .commentaireLibre(UPDATED_COMMENTAIRE_LIBRE)
            .resultatsParaclinique(UPDATED_RESULTATS_PARACLINIQUE);
        return consultation;
    }

    @BeforeEach
    public void initTest() {
        consultation = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultation() throws Exception {
        int databaseSizeBeforeCreate = consultationRepository.findAll().size();
        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);
        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeCreate + 1);
        Consultation testConsultation = consultationList.get(consultationList.size() - 1);
        assertThat(testConsultation.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testConsultation.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testConsultation.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testConsultation.getTension()).isEqualTo(DEFAULT_TENSION);
        assertThat(testConsultation.getGlycemie()).isEqualTo(DEFAULT_GLYCEMIE);
        assertThat(testConsultation.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testConsultation.getHypothesis()).isEqualTo(DEFAULT_HYPOTHESIS);
        assertThat(testConsultation.getExams()).isEqualTo(DEFAULT_EXAMS);
        assertThat(testConsultation.getTreatment()).isEqualTo(DEFAULT_TREATMENT);
        assertThat(testConsultation.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testConsultation.getOg()).isEqualTo(DEFAULT_OG);
        assertThat(testConsultation.getVd()).isEqualTo(DEFAULT_VD);
        assertThat(testConsultation.getEseptum()).isEqualTo(DEFAULT_ESEPTUM);
        assertThat(testConsultation.getAo()).isEqualTo(DEFAULT_AO);
        assertThat(testConsultation.getvGDiastole()).isEqualTo(DEFAULT_V_G_DIASTOLE);
        assertThat(testConsultation.getOuvertureAo()).isEqualTo(DEFAULT_OUVERTURE_AO);
        assertThat(testConsultation.getvGSystole()).isEqualTo(DEFAULT_V_G_SYSTOLE);
        assertThat(testConsultation.getVp()).isEqualTo(DEFAULT_VP);
        assertThat(testConsultation.getoGAo()).isEqualTo(DEFAULT_O_G_AO);
        assertThat(testConsultation.getfRTeicholtz()).isEqualTo(DEFAULT_F_R_TEICHOLTZ);
        assertThat(testConsultation.geteVp()).isEqualTo(DEFAULT_E_VP);
        assertThat(testConsultation.getSeptumVg()).isEqualTo(DEFAULT_SEPTUM_VG);
        assertThat(testConsultation.getfETeicholz()).isEqualTo(DEFAULT_F_E_TEICHOLZ);
        assertThat(testConsultation.getTapse()).isEqualTo(DEFAULT_TAPSE);
        assertThat(testConsultation.getParoiPost()).isEqualTo(DEFAULT_PAROI_POST);
        assertThat(testConsultation.getSurfaceOg()).isEqualTo(DEFAULT_SURFACE_OG);
        assertThat(testConsultation.getSurfaceOd()).isEqualTo(DEFAULT_SURFACE_OD);
        assertThat(testConsultation.getMesureVd()).isEqualTo(DEFAULT_MESURE_VD);
        assertThat(testConsultation.getFe()).isEqualTo(DEFAULT_FE);
        assertThat(testConsultation.getFeA2C()).isEqualTo(DEFAULT_FE_A_2_C);
        assertThat(testConsultation.getFeBiplan()).isEqualTo(DEFAULT_FE_BIPLAN);
        assertThat(testConsultation.getE()).isEqualTo(DEFAULT_E);
        assertThat(testConsultation.getA()).isEqualTo(DEFAULT_A);
        assertThat(testConsultation.geteA()).isEqualTo(DEFAULT_E_A);
        assertThat(testConsultation.getTd()).isEqualTo(DEFAULT_TD);
        assertThat(testConsultation.getTriv()).isEqualTo(DEFAULT_TRIV);
        assertThat(testConsultation.getDureeAmIm()).isEqualTo(DEFAULT_DUREE_AM_IM);
        assertThat(testConsultation.getSurfaceRegurgitee()).isEqualTo(DEFAULT_SURFACE_REGURGITEE);
        assertThat(testConsultation.getPba()).isEqualTo(DEFAULT_PBA);
        assertThat(testConsultation.getQr()).isEqualTo(DEFAULT_QR);
        assertThat(testConsultation.getVr()).isEqualTo(DEFAULT_VR);
        assertThat(testConsultation.getSor()).isEqualTo(DEFAULT_SOR);
        assertThat(testConsultation.getFr()).isEqualTo(DEFAULT_FR);
        assertThat(testConsultation.getVmaxAp()).isEqualTo(DEFAULT_VMAX_AP);
        assertThat(testConsultation.getItv()).isEqualTo(DEFAULT_ITV);
        assertThat(testConsultation.getGradMax()).isEqualTo(DEFAULT_GRAD_MAX);
        assertThat(testConsultation.getGradMoy()).isEqualTo(DEFAULT_GRAD_MOY);
        assertThat(testConsultation.getDc()).isEqualTo(DEFAULT_DC);
        assertThat(testConsultation.getiAoextension()).isEqualTo(DEFAULT_I_AOEXTENSION);
        assertThat(testConsultation.getVenaContracta()).isEqualTo(DEFAULT_VENA_CONTRACTA);
        assertThat(testConsultation.getPht()).isEqualTo(DEFAULT_PHT);
        assertThat(testConsultation.getiTExtension()).isEqualTo(DEFAULT_I_T_EXTENSION);
        assertThat(testConsultation.getGradMaxB()).isEqualTo(DEFAULT_GRAD_MAX_B);
        assertThat(testConsultation.getPaps()).isEqualTo(DEFAULT_PAPS);
        assertThat(testConsultation.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testConsultation.getVmax()).isEqualTo(DEFAULT_VMAX);
        assertThat(testConsultation.getGradMaxC()).isEqualTo(DEFAULT_GRAD_MAX_C);
        assertThat(testConsultation.getGradMoyB()).isEqualTo(DEFAULT_GRAD_MOY_B);
        assertThat(testConsultation.getS()).isEqualTo(DEFAULT_S);
        assertThat(testConsultation.getD()).isEqualTo(DEFAULT_D);
        assertThat(testConsultation.getsD()).isEqualTo(DEFAULT_S_D);
        assertThat(testConsultation.getaA()).isEqualTo(DEFAULT_A_A);
        assertThat(testConsultation.getDureeAp()).isEqualTo(DEFAULT_DUREE_AP);
        assertThat(testConsultation.geteAA()).isEqualTo(DEFAULT_E_AA);
        assertThat(testConsultation.getaAA()).isEqualTo(DEFAULT_A_AA);
        assertThat(testConsultation.geteAAa()).isEqualTo(DEFAULT_E_A_AA);
        assertThat(testConsultation.geteEa()).isEqualTo(DEFAULT_E_EA);
        assertThat(testConsultation.getZ()).isEqualTo(DEFAULT_Z);
        assertThat(testConsultation.getExamGeneral()).isEqualTo(DEFAULT_EXAM_GENERAL);
        assertThat(testConsultation.getFrequenceRespiratoire()).isEqualTo(DEFAULT_FREQUENCE_RESPIRATOIRE);
        assertThat(testConsultation.getFrequenceCardiaque()).isEqualTo(DEFAULT_FREQUENCE_CARDIAQUE);
        assertThat(testConsultation.getCommentaireLibre()).isEqualTo(DEFAULT_COMMENTAIRE_LIBRE);
        assertThat(testConsultation.getResultatsParaclinique()).isEqualTo(DEFAULT_RESULTATS_PARACLINIQUE);
    }

    @Test
    @Transactional
    void createConsultationWithExistingId() throws Exception {
        // Create the Consultation with an existing ID
        consultation.setId(1L);
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        int databaseSizeBeforeCreate = consultationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTemperatureIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setTemperature(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setWeight(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setTension(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHypothesisIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setHypothesis(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExamsIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setExams(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTreatmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultationRepository.findAll().size();
        // set the field null
        consultation.setTreatment(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsultations() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        // Get all the consultationList
        restConsultationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultation.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].tension").value(hasItem(DEFAULT_TENSION)))
            .andExpect(jsonPath("$.[*].glycemie").value(hasItem(DEFAULT_GLYCEMIE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].hypothesis").value(hasItem(DEFAULT_HYPOTHESIS)))
            .andExpect(jsonPath("$.[*].exams").value(hasItem(DEFAULT_EXAMS)))
            .andExpect(jsonPath("$.[*].treatment").value(hasItem(DEFAULT_TREATMENT)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].og").value(hasItem(DEFAULT_OG)))
            .andExpect(jsonPath("$.[*].vd").value(hasItem(DEFAULT_VD)))
            .andExpect(jsonPath("$.[*].eseptum").value(hasItem(DEFAULT_ESEPTUM)))
            .andExpect(jsonPath("$.[*].ao").value(hasItem(DEFAULT_AO)))
            .andExpect(jsonPath("$.[*].vGDiastole").value(hasItem(DEFAULT_V_G_DIASTOLE)))
            .andExpect(jsonPath("$.[*].ouvertureAo").value(hasItem(DEFAULT_OUVERTURE_AO)))
            .andExpect(jsonPath("$.[*].vGSystole").value(hasItem(DEFAULT_V_G_SYSTOLE)))
            .andExpect(jsonPath("$.[*].vp").value(hasItem(DEFAULT_VP)))
            .andExpect(jsonPath("$.[*].oGAo").value(hasItem(DEFAULT_O_G_AO)))
            .andExpect(jsonPath("$.[*].fRTeicholtz").value(hasItem(DEFAULT_F_R_TEICHOLTZ)))
            .andExpect(jsonPath("$.[*].eVp").value(hasItem(DEFAULT_E_VP)))
            .andExpect(jsonPath("$.[*].septumVg").value(hasItem(DEFAULT_SEPTUM_VG)))
            .andExpect(jsonPath("$.[*].fETeicholz").value(hasItem(DEFAULT_F_E_TEICHOLZ)))
            .andExpect(jsonPath("$.[*].tapse").value(hasItem(DEFAULT_TAPSE)))
            .andExpect(jsonPath("$.[*].paroiPost").value(hasItem(DEFAULT_PAROI_POST)))
            .andExpect(jsonPath("$.[*].surfaceOg").value(hasItem(DEFAULT_SURFACE_OG)))
            .andExpect(jsonPath("$.[*].surfaceOd").value(hasItem(DEFAULT_SURFACE_OD)))
            .andExpect(jsonPath("$.[*].mesureVd").value(hasItem(DEFAULT_MESURE_VD)))
            .andExpect(jsonPath("$.[*].fe").value(hasItem(DEFAULT_FE)))
            .andExpect(jsonPath("$.[*].feA2C").value(hasItem(DEFAULT_FE_A_2_C)))
            .andExpect(jsonPath("$.[*].feBiplan").value(hasItem(DEFAULT_FE_BIPLAN)))
            .andExpect(jsonPath("$.[*].e").value(hasItem(DEFAULT_E)))
            .andExpect(jsonPath("$.[*].a").value(hasItem(DEFAULT_A)))
            .andExpect(jsonPath("$.[*].eA").value(hasItem(DEFAULT_E_A)))
            .andExpect(jsonPath("$.[*].td").value(hasItem(DEFAULT_TD)))
            .andExpect(jsonPath("$.[*].triv").value(hasItem(DEFAULT_TRIV)))
            .andExpect(jsonPath("$.[*].dureeAmIm").value(hasItem(DEFAULT_DUREE_AM_IM)))
            .andExpect(jsonPath("$.[*].surfaceRegurgitee").value(hasItem(DEFAULT_SURFACE_REGURGITEE)))
            .andExpect(jsonPath("$.[*].pba").value(hasItem(DEFAULT_PBA)))
            .andExpect(jsonPath("$.[*].qr").value(hasItem(DEFAULT_QR)))
            .andExpect(jsonPath("$.[*].vr").value(hasItem(DEFAULT_VR)))
            .andExpect(jsonPath("$.[*].sor").value(hasItem(DEFAULT_SOR)))
            .andExpect(jsonPath("$.[*].fr").value(hasItem(DEFAULT_FR)))
            .andExpect(jsonPath("$.[*].vmaxAp").value(hasItem(DEFAULT_VMAX_AP)))
            .andExpect(jsonPath("$.[*].itv").value(hasItem(DEFAULT_ITV)))
            .andExpect(jsonPath("$.[*].gradMax").value(hasItem(DEFAULT_GRAD_MAX)))
            .andExpect(jsonPath("$.[*].gradMoy").value(hasItem(DEFAULT_GRAD_MOY)))
            .andExpect(jsonPath("$.[*].dc").value(hasItem(DEFAULT_DC)))
            .andExpect(jsonPath("$.[*].iAoextension").value(hasItem(DEFAULT_I_AOEXTENSION)))
            .andExpect(jsonPath("$.[*].venaContracta").value(hasItem(DEFAULT_VENA_CONTRACTA)))
            .andExpect(jsonPath("$.[*].pht").value(hasItem(DEFAULT_PHT)))
            .andExpect(jsonPath("$.[*].iTExtension").value(hasItem(DEFAULT_I_T_EXTENSION)))
            .andExpect(jsonPath("$.[*].gradMaxB").value(hasItem(DEFAULT_GRAD_MAX_B)))
            .andExpect(jsonPath("$.[*].paps").value(hasItem(DEFAULT_PAPS)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].vmax").value(hasItem(DEFAULT_VMAX)))
            .andExpect(jsonPath("$.[*].gradMaxC").value(hasItem(DEFAULT_GRAD_MAX_C)))
            .andExpect(jsonPath("$.[*].gradMoyB").value(hasItem(DEFAULT_GRAD_MOY_B)))
            .andExpect(jsonPath("$.[*].s").value(hasItem(DEFAULT_S)))
            .andExpect(jsonPath("$.[*].d").value(hasItem(DEFAULT_D)))
            .andExpect(jsonPath("$.[*].sD").value(hasItem(DEFAULT_S_D)))
            .andExpect(jsonPath("$.[*].aA").value(hasItem(DEFAULT_A_A)))
            .andExpect(jsonPath("$.[*].dureeAp").value(hasItem(DEFAULT_DUREE_AP)))
            .andExpect(jsonPath("$.[*].eAA").value(hasItem(DEFAULT_E_AA)))
            .andExpect(jsonPath("$.[*].aAA").value(hasItem(DEFAULT_A_AA)))
            .andExpect(jsonPath("$.[*].eAAa").value(hasItem(DEFAULT_E_A_AA)))
            .andExpect(jsonPath("$.[*].eEa").value(hasItem(DEFAULT_E_EA)))
            .andExpect(jsonPath("$.[*].z").value(hasItem(DEFAULT_Z)))
            .andExpect(jsonPath("$.[*].examGeneral").value(hasItem(DEFAULT_EXAM_GENERAL.toString())))
            .andExpect(jsonPath("$.[*].frequenceRespiratoire").value(hasItem(DEFAULT_FREQUENCE_RESPIRATOIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].frequenceCardiaque").value(hasItem(DEFAULT_FREQUENCE_CARDIAQUE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaireLibre").value(hasItem(DEFAULT_COMMENTAIRE_LIBRE.toString())))
            .andExpect(jsonPath("$.[*].resultatsParaclinique").value(hasItem(DEFAULT_RESULTATS_PARACLINIQUE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(consultationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(consultationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(consultationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(consultationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        // Get the consultation
        restConsultationMockMvc
            .perform(get(ENTITY_API_URL_ID, consultation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultation.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.tension").value(DEFAULT_TENSION))
            .andExpect(jsonPath("$.glycemie").value(DEFAULT_GLYCEMIE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.hypothesis").value(DEFAULT_HYPOTHESIS))
            .andExpect(jsonPath("$.exams").value(DEFAULT_EXAMS))
            .andExpect(jsonPath("$.treatment").value(DEFAULT_TREATMENT))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.og").value(DEFAULT_OG))
            .andExpect(jsonPath("$.vd").value(DEFAULT_VD))
            .andExpect(jsonPath("$.eseptum").value(DEFAULT_ESEPTUM))
            .andExpect(jsonPath("$.ao").value(DEFAULT_AO))
            .andExpect(jsonPath("$.vGDiastole").value(DEFAULT_V_G_DIASTOLE))
            .andExpect(jsonPath("$.ouvertureAo").value(DEFAULT_OUVERTURE_AO))
            .andExpect(jsonPath("$.vGSystole").value(DEFAULT_V_G_SYSTOLE))
            .andExpect(jsonPath("$.vp").value(DEFAULT_VP))
            .andExpect(jsonPath("$.oGAo").value(DEFAULT_O_G_AO))
            .andExpect(jsonPath("$.fRTeicholtz").value(DEFAULT_F_R_TEICHOLTZ))
            .andExpect(jsonPath("$.eVp").value(DEFAULT_E_VP))
            .andExpect(jsonPath("$.septumVg").value(DEFAULT_SEPTUM_VG))
            .andExpect(jsonPath("$.fETeicholz").value(DEFAULT_F_E_TEICHOLZ))
            .andExpect(jsonPath("$.tapse").value(DEFAULT_TAPSE))
            .andExpect(jsonPath("$.paroiPost").value(DEFAULT_PAROI_POST))
            .andExpect(jsonPath("$.surfaceOg").value(DEFAULT_SURFACE_OG))
            .andExpect(jsonPath("$.surfaceOd").value(DEFAULT_SURFACE_OD))
            .andExpect(jsonPath("$.mesureVd").value(DEFAULT_MESURE_VD))
            .andExpect(jsonPath("$.fe").value(DEFAULT_FE))
            .andExpect(jsonPath("$.feA2C").value(DEFAULT_FE_A_2_C))
            .andExpect(jsonPath("$.feBiplan").value(DEFAULT_FE_BIPLAN))
            .andExpect(jsonPath("$.e").value(DEFAULT_E))
            .andExpect(jsonPath("$.a").value(DEFAULT_A))
            .andExpect(jsonPath("$.eA").value(DEFAULT_E_A))
            .andExpect(jsonPath("$.td").value(DEFAULT_TD))
            .andExpect(jsonPath("$.triv").value(DEFAULT_TRIV))
            .andExpect(jsonPath("$.dureeAmIm").value(DEFAULT_DUREE_AM_IM))
            .andExpect(jsonPath("$.surfaceRegurgitee").value(DEFAULT_SURFACE_REGURGITEE))
            .andExpect(jsonPath("$.pba").value(DEFAULT_PBA))
            .andExpect(jsonPath("$.qr").value(DEFAULT_QR))
            .andExpect(jsonPath("$.vr").value(DEFAULT_VR))
            .andExpect(jsonPath("$.sor").value(DEFAULT_SOR))
            .andExpect(jsonPath("$.fr").value(DEFAULT_FR))
            .andExpect(jsonPath("$.vmaxAp").value(DEFAULT_VMAX_AP))
            .andExpect(jsonPath("$.itv").value(DEFAULT_ITV))
            .andExpect(jsonPath("$.gradMax").value(DEFAULT_GRAD_MAX))
            .andExpect(jsonPath("$.gradMoy").value(DEFAULT_GRAD_MOY))
            .andExpect(jsonPath("$.dc").value(DEFAULT_DC))
            .andExpect(jsonPath("$.iAoextension").value(DEFAULT_I_AOEXTENSION))
            .andExpect(jsonPath("$.venaContracta").value(DEFAULT_VENA_CONTRACTA))
            .andExpect(jsonPath("$.pht").value(DEFAULT_PHT))
            .andExpect(jsonPath("$.iTExtension").value(DEFAULT_I_T_EXTENSION))
            .andExpect(jsonPath("$.gradMaxB").value(DEFAULT_GRAD_MAX_B))
            .andExpect(jsonPath("$.paps").value(DEFAULT_PAPS))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.vmax").value(DEFAULT_VMAX))
            .andExpect(jsonPath("$.gradMaxC").value(DEFAULT_GRAD_MAX_C))
            .andExpect(jsonPath("$.gradMoyB").value(DEFAULT_GRAD_MOY_B))
            .andExpect(jsonPath("$.s").value(DEFAULT_S))
            .andExpect(jsonPath("$.d").value(DEFAULT_D))
            .andExpect(jsonPath("$.sD").value(DEFAULT_S_D))
            .andExpect(jsonPath("$.aA").value(DEFAULT_A_A))
            .andExpect(jsonPath("$.dureeAp").value(DEFAULT_DUREE_AP))
            .andExpect(jsonPath("$.eAA").value(DEFAULT_E_AA))
            .andExpect(jsonPath("$.aAA").value(DEFAULT_A_AA))
            .andExpect(jsonPath("$.eAAa").value(DEFAULT_E_A_AA))
            .andExpect(jsonPath("$.eEa").value(DEFAULT_E_EA))
            .andExpect(jsonPath("$.z").value(DEFAULT_Z))
            .andExpect(jsonPath("$.examGeneral").value(DEFAULT_EXAM_GENERAL.toString()))
            .andExpect(jsonPath("$.frequenceRespiratoire").value(DEFAULT_FREQUENCE_RESPIRATOIRE.doubleValue()))
            .andExpect(jsonPath("$.frequenceCardiaque").value(DEFAULT_FREQUENCE_CARDIAQUE.doubleValue()))
            .andExpect(jsonPath("$.commentaireLibre").value(DEFAULT_COMMENTAIRE_LIBRE.toString()))
            .andExpect(jsonPath("$.resultatsParaclinique").value(DEFAULT_RESULTATS_PARACLINIQUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConsultation() throws Exception {
        // Get the consultation
        restConsultationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();

        // Update the consultation
        Consultation updatedConsultation = consultationRepository.findById(consultation.getId()).get();
        // Disconnect from session so that the updates on updatedConsultation are not directly saved in db
        em.detach(updatedConsultation);
        updatedConsultation
            .dateTime(UPDATED_DATE_TIME)
            .temperature(UPDATED_TEMPERATURE)
            .weight(UPDATED_WEIGHT)
            .tension(UPDATED_TENSION)
            .glycemie(UPDATED_GLYCEMIE)
            .comment(UPDATED_COMMENT)
            .hypothesis(UPDATED_HYPOTHESIS)
            .exams(UPDATED_EXAMS)
            .treatment(UPDATED_TREATMENT)
            .author(UPDATED_AUTHOR)
            .og(UPDATED_OG)
            .vd(UPDATED_VD)
            .eseptum(UPDATED_ESEPTUM)
            .ao(UPDATED_AO)
            .vGDiastole(UPDATED_V_G_DIASTOLE)
            .ouvertureAo(UPDATED_OUVERTURE_AO)
            .vGSystole(UPDATED_V_G_SYSTOLE)
            .vp(UPDATED_VP)
            .oGAo(UPDATED_O_G_AO)
            .fRTeicholtz(UPDATED_F_R_TEICHOLTZ)
            .eVp(UPDATED_E_VP)
            .septumVg(UPDATED_SEPTUM_VG)
            .fETeicholz(UPDATED_F_E_TEICHOLZ)
            .tapse(UPDATED_TAPSE)
            .paroiPost(UPDATED_PAROI_POST)
            .surfaceOg(UPDATED_SURFACE_OG)
            .surfaceOd(UPDATED_SURFACE_OD)
            .mesureVd(UPDATED_MESURE_VD)
            .fe(UPDATED_FE)
            .feA2C(UPDATED_FE_A_2_C)
            .feBiplan(UPDATED_FE_BIPLAN)
            .e(UPDATED_E)
            .a(UPDATED_A)
            .eA(UPDATED_E_A)
            .td(UPDATED_TD)
            .triv(UPDATED_TRIV)
            .dureeAmIm(UPDATED_DUREE_AM_IM)
            .surfaceRegurgitee(UPDATED_SURFACE_REGURGITEE)
            .pba(UPDATED_PBA)
            .qr(UPDATED_QR)
            .vr(UPDATED_VR)
            .sor(UPDATED_SOR)
            .fr(UPDATED_FR)
            .vmaxAp(UPDATED_VMAX_AP)
            .itv(UPDATED_ITV)
            .gradMax(UPDATED_GRAD_MAX)
            .gradMoy(UPDATED_GRAD_MOY)
            .dc(UPDATED_DC)
            .iAoextension(UPDATED_I_AOEXTENSION)
            .venaContracta(UPDATED_VENA_CONTRACTA)
            .pht(UPDATED_PHT)
            .iTExtension(UPDATED_I_T_EXTENSION)
            .gradMaxB(UPDATED_GRAD_MAX_B)
            .paps(UPDATED_PAPS)
            .ip(UPDATED_IP)
            .vmax(UPDATED_VMAX)
            .gradMaxC(UPDATED_GRAD_MAX_C)
            .gradMoyB(UPDATED_GRAD_MOY_B)
            .s(UPDATED_S)
            .d(UPDATED_D)
            .sD(UPDATED_S_D)
            .aA(UPDATED_A_A)
            .dureeAp(UPDATED_DUREE_AP)
            .eAA(UPDATED_E_AA)
            .aAA(UPDATED_A_AA)
            .eAAa(UPDATED_E_A_AA)
            .eEa(UPDATED_E_EA)
            .z(UPDATED_Z)
            .examGeneral(UPDATED_EXAM_GENERAL)
            .frequenceRespiratoire(UPDATED_FREQUENCE_RESPIRATOIRE)
            .frequenceCardiaque(UPDATED_FREQUENCE_CARDIAQUE)
            .commentaireLibre(UPDATED_COMMENTAIRE_LIBRE)
            .resultatsParaclinique(UPDATED_RESULTATS_PARACLINIQUE);
        ConsultationDTO consultationDTO = consultationMapper.toDto(updatedConsultation);

        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
        Consultation testConsultation = consultationList.get(consultationList.size() - 1);
        assertThat(testConsultation.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testConsultation.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testConsultation.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testConsultation.getTension()).isEqualTo(UPDATED_TENSION);
        assertThat(testConsultation.getGlycemie()).isEqualTo(UPDATED_GLYCEMIE);
        assertThat(testConsultation.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testConsultation.getHypothesis()).isEqualTo(UPDATED_HYPOTHESIS);
        assertThat(testConsultation.getExams()).isEqualTo(UPDATED_EXAMS);
        assertThat(testConsultation.getTreatment()).isEqualTo(UPDATED_TREATMENT);
        assertThat(testConsultation.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testConsultation.getOg()).isEqualTo(UPDATED_OG);
        assertThat(testConsultation.getVd()).isEqualTo(UPDATED_VD);
        assertThat(testConsultation.getEseptum()).isEqualTo(UPDATED_ESEPTUM);
        assertThat(testConsultation.getAo()).isEqualTo(UPDATED_AO);
        assertThat(testConsultation.getvGDiastole()).isEqualTo(UPDATED_V_G_DIASTOLE);
        assertThat(testConsultation.getOuvertureAo()).isEqualTo(UPDATED_OUVERTURE_AO);
        assertThat(testConsultation.getvGSystole()).isEqualTo(UPDATED_V_G_SYSTOLE);
        assertThat(testConsultation.getVp()).isEqualTo(UPDATED_VP);
        assertThat(testConsultation.getoGAo()).isEqualTo(UPDATED_O_G_AO);
        assertThat(testConsultation.getfRTeicholtz()).isEqualTo(UPDATED_F_R_TEICHOLTZ);
        assertThat(testConsultation.geteVp()).isEqualTo(UPDATED_E_VP);
        assertThat(testConsultation.getSeptumVg()).isEqualTo(UPDATED_SEPTUM_VG);
        assertThat(testConsultation.getfETeicholz()).isEqualTo(UPDATED_F_E_TEICHOLZ);
        assertThat(testConsultation.getTapse()).isEqualTo(UPDATED_TAPSE);
        assertThat(testConsultation.getParoiPost()).isEqualTo(UPDATED_PAROI_POST);
        assertThat(testConsultation.getSurfaceOg()).isEqualTo(UPDATED_SURFACE_OG);
        assertThat(testConsultation.getSurfaceOd()).isEqualTo(UPDATED_SURFACE_OD);
        assertThat(testConsultation.getMesureVd()).isEqualTo(UPDATED_MESURE_VD);
        assertThat(testConsultation.getFe()).isEqualTo(UPDATED_FE);
        assertThat(testConsultation.getFeA2C()).isEqualTo(UPDATED_FE_A_2_C);
        assertThat(testConsultation.getFeBiplan()).isEqualTo(UPDATED_FE_BIPLAN);
        assertThat(testConsultation.getE()).isEqualTo(UPDATED_E);
        assertThat(testConsultation.getA()).isEqualTo(UPDATED_A);
        assertThat(testConsultation.geteA()).isEqualTo(UPDATED_E_A);
        assertThat(testConsultation.getTd()).isEqualTo(UPDATED_TD);
        assertThat(testConsultation.getTriv()).isEqualTo(UPDATED_TRIV);
        assertThat(testConsultation.getDureeAmIm()).isEqualTo(UPDATED_DUREE_AM_IM);
        assertThat(testConsultation.getSurfaceRegurgitee()).isEqualTo(UPDATED_SURFACE_REGURGITEE);
        assertThat(testConsultation.getPba()).isEqualTo(UPDATED_PBA);
        assertThat(testConsultation.getQr()).isEqualTo(UPDATED_QR);
        assertThat(testConsultation.getVr()).isEqualTo(UPDATED_VR);
        assertThat(testConsultation.getSor()).isEqualTo(UPDATED_SOR);
        assertThat(testConsultation.getFr()).isEqualTo(UPDATED_FR);
        assertThat(testConsultation.getVmaxAp()).isEqualTo(UPDATED_VMAX_AP);
        assertThat(testConsultation.getItv()).isEqualTo(UPDATED_ITV);
        assertThat(testConsultation.getGradMax()).isEqualTo(UPDATED_GRAD_MAX);
        assertThat(testConsultation.getGradMoy()).isEqualTo(UPDATED_GRAD_MOY);
        assertThat(testConsultation.getDc()).isEqualTo(UPDATED_DC);
        assertThat(testConsultation.getiAoextension()).isEqualTo(UPDATED_I_AOEXTENSION);
        assertThat(testConsultation.getVenaContracta()).isEqualTo(UPDATED_VENA_CONTRACTA);
        assertThat(testConsultation.getPht()).isEqualTo(UPDATED_PHT);
        assertThat(testConsultation.getiTExtension()).isEqualTo(UPDATED_I_T_EXTENSION);
        assertThat(testConsultation.getGradMaxB()).isEqualTo(UPDATED_GRAD_MAX_B);
        assertThat(testConsultation.getPaps()).isEqualTo(UPDATED_PAPS);
        assertThat(testConsultation.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testConsultation.getVmax()).isEqualTo(UPDATED_VMAX);
        assertThat(testConsultation.getGradMaxC()).isEqualTo(UPDATED_GRAD_MAX_C);
        assertThat(testConsultation.getGradMoyB()).isEqualTo(UPDATED_GRAD_MOY_B);
        assertThat(testConsultation.getS()).isEqualTo(UPDATED_S);
        assertThat(testConsultation.getD()).isEqualTo(UPDATED_D);
        assertThat(testConsultation.getsD()).isEqualTo(UPDATED_S_D);
        assertThat(testConsultation.getaA()).isEqualTo(UPDATED_A_A);
        assertThat(testConsultation.getDureeAp()).isEqualTo(UPDATED_DUREE_AP);
        assertThat(testConsultation.geteAA()).isEqualTo(UPDATED_E_AA);
        assertThat(testConsultation.getaAA()).isEqualTo(UPDATED_A_AA);
        assertThat(testConsultation.geteAAa()).isEqualTo(UPDATED_E_A_AA);
        assertThat(testConsultation.geteEa()).isEqualTo(UPDATED_E_EA);
        assertThat(testConsultation.getZ()).isEqualTo(UPDATED_Z);
        assertThat(testConsultation.getExamGeneral()).isEqualTo(UPDATED_EXAM_GENERAL);
        assertThat(testConsultation.getFrequenceRespiratoire()).isEqualTo(UPDATED_FREQUENCE_RESPIRATOIRE);
        assertThat(testConsultation.getFrequenceCardiaque()).isEqualTo(UPDATED_FREQUENCE_CARDIAQUE);
        assertThat(testConsultation.getCommentaireLibre()).isEqualTo(UPDATED_COMMENTAIRE_LIBRE);
        assertThat(testConsultation.getResultatsParaclinique()).isEqualTo(UPDATED_RESULTATS_PARACLINIQUE);
    }

    @Test
    @Transactional
    void putNonExistingConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultationWithPatch() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();

        // Update the consultation using partial update
        Consultation partialUpdatedConsultation = new Consultation();
        partialUpdatedConsultation.setId(consultation.getId());

        partialUpdatedConsultation
            .temperature(UPDATED_TEMPERATURE)
            .tension(UPDATED_TENSION)
            .glycemie(UPDATED_GLYCEMIE)
            .exams(UPDATED_EXAMS)
            .treatment(UPDATED_TREATMENT)
            .author(UPDATED_AUTHOR)
            .og(UPDATED_OG)
            .vd(UPDATED_VD)
            .eseptum(UPDATED_ESEPTUM)
            .ao(UPDATED_AO)
            .ouvertureAo(UPDATED_OUVERTURE_AO)
            .vGSystole(UPDATED_V_G_SYSTOLE)
            .vp(UPDATED_VP)
            .oGAo(UPDATED_O_G_AO)
            .fRTeicholtz(UPDATED_F_R_TEICHOLTZ)
            .eVp(UPDATED_E_VP)
            .fETeicholz(UPDATED_F_E_TEICHOLZ)
            .surfaceOg(UPDATED_SURFACE_OG)
            .surfaceOd(UPDATED_SURFACE_OD)
            .e(UPDATED_E)
            .a(UPDATED_A)
            .triv(UPDATED_TRIV)
            .pba(UPDATED_PBA)
            .vmaxAp(UPDATED_VMAX_AP)
            .itv(UPDATED_ITV)
            .gradMax(UPDATED_GRAD_MAX)
            .gradMoy(UPDATED_GRAD_MOY)
            .iAoextension(UPDATED_I_AOEXTENSION)
            .venaContracta(UPDATED_VENA_CONTRACTA)
            .ip(UPDATED_IP)
            .gradMoyB(UPDATED_GRAD_MOY_B)
            .s(UPDATED_S)
            .d(UPDATED_D)
            .z(UPDATED_Z)
            .frequenceRespiratoire(UPDATED_FREQUENCE_RESPIRATOIRE)
            .commentaireLibre(UPDATED_COMMENTAIRE_LIBRE)
            .resultatsParaclinique(UPDATED_RESULTATS_PARACLINIQUE);

        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultation))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
        Consultation testConsultation = consultationList.get(consultationList.size() - 1);
        assertThat(testConsultation.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testConsultation.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testConsultation.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testConsultation.getTension()).isEqualTo(UPDATED_TENSION);
        assertThat(testConsultation.getGlycemie()).isEqualTo(UPDATED_GLYCEMIE);
        assertThat(testConsultation.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testConsultation.getHypothesis()).isEqualTo(DEFAULT_HYPOTHESIS);
        assertThat(testConsultation.getExams()).isEqualTo(UPDATED_EXAMS);
        assertThat(testConsultation.getTreatment()).isEqualTo(UPDATED_TREATMENT);
        assertThat(testConsultation.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testConsultation.getOg()).isEqualTo(UPDATED_OG);
        assertThat(testConsultation.getVd()).isEqualTo(UPDATED_VD);
        assertThat(testConsultation.getEseptum()).isEqualTo(UPDATED_ESEPTUM);
        assertThat(testConsultation.getAo()).isEqualTo(UPDATED_AO);
        assertThat(testConsultation.getvGDiastole()).isEqualTo(DEFAULT_V_G_DIASTOLE);
        assertThat(testConsultation.getOuvertureAo()).isEqualTo(UPDATED_OUVERTURE_AO);
        assertThat(testConsultation.getvGSystole()).isEqualTo(UPDATED_V_G_SYSTOLE);
        assertThat(testConsultation.getVp()).isEqualTo(UPDATED_VP);
        assertThat(testConsultation.getoGAo()).isEqualTo(UPDATED_O_G_AO);
        assertThat(testConsultation.getfRTeicholtz()).isEqualTo(UPDATED_F_R_TEICHOLTZ);
        assertThat(testConsultation.geteVp()).isEqualTo(UPDATED_E_VP);
        assertThat(testConsultation.getSeptumVg()).isEqualTo(DEFAULT_SEPTUM_VG);
        assertThat(testConsultation.getfETeicholz()).isEqualTo(UPDATED_F_E_TEICHOLZ);
        assertThat(testConsultation.getTapse()).isEqualTo(DEFAULT_TAPSE);
        assertThat(testConsultation.getParoiPost()).isEqualTo(DEFAULT_PAROI_POST);
        assertThat(testConsultation.getSurfaceOg()).isEqualTo(UPDATED_SURFACE_OG);
        assertThat(testConsultation.getSurfaceOd()).isEqualTo(UPDATED_SURFACE_OD);
        assertThat(testConsultation.getMesureVd()).isEqualTo(DEFAULT_MESURE_VD);
        assertThat(testConsultation.getFe()).isEqualTo(DEFAULT_FE);
        assertThat(testConsultation.getFeA2C()).isEqualTo(DEFAULT_FE_A_2_C);
        assertThat(testConsultation.getFeBiplan()).isEqualTo(DEFAULT_FE_BIPLAN);
        assertThat(testConsultation.getE()).isEqualTo(UPDATED_E);
        assertThat(testConsultation.getA()).isEqualTo(UPDATED_A);
        assertThat(testConsultation.geteA()).isEqualTo(DEFAULT_E_A);
        assertThat(testConsultation.getTd()).isEqualTo(DEFAULT_TD);
        assertThat(testConsultation.getTriv()).isEqualTo(UPDATED_TRIV);
        assertThat(testConsultation.getDureeAmIm()).isEqualTo(DEFAULT_DUREE_AM_IM);
        assertThat(testConsultation.getSurfaceRegurgitee()).isEqualTo(DEFAULT_SURFACE_REGURGITEE);
        assertThat(testConsultation.getPba()).isEqualTo(UPDATED_PBA);
        assertThat(testConsultation.getQr()).isEqualTo(DEFAULT_QR);
        assertThat(testConsultation.getVr()).isEqualTo(DEFAULT_VR);
        assertThat(testConsultation.getSor()).isEqualTo(DEFAULT_SOR);
        assertThat(testConsultation.getFr()).isEqualTo(DEFAULT_FR);
        assertThat(testConsultation.getVmaxAp()).isEqualTo(UPDATED_VMAX_AP);
        assertThat(testConsultation.getItv()).isEqualTo(UPDATED_ITV);
        assertThat(testConsultation.getGradMax()).isEqualTo(UPDATED_GRAD_MAX);
        assertThat(testConsultation.getGradMoy()).isEqualTo(UPDATED_GRAD_MOY);
        assertThat(testConsultation.getDc()).isEqualTo(DEFAULT_DC);
        assertThat(testConsultation.getiAoextension()).isEqualTo(UPDATED_I_AOEXTENSION);
        assertThat(testConsultation.getVenaContracta()).isEqualTo(UPDATED_VENA_CONTRACTA);
        assertThat(testConsultation.getPht()).isEqualTo(DEFAULT_PHT);
        assertThat(testConsultation.getiTExtension()).isEqualTo(DEFAULT_I_T_EXTENSION);
        assertThat(testConsultation.getGradMaxB()).isEqualTo(DEFAULT_GRAD_MAX_B);
        assertThat(testConsultation.getPaps()).isEqualTo(DEFAULT_PAPS);
        assertThat(testConsultation.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testConsultation.getVmax()).isEqualTo(DEFAULT_VMAX);
        assertThat(testConsultation.getGradMaxC()).isEqualTo(DEFAULT_GRAD_MAX_C);
        assertThat(testConsultation.getGradMoyB()).isEqualTo(UPDATED_GRAD_MOY_B);
        assertThat(testConsultation.getS()).isEqualTo(UPDATED_S);
        assertThat(testConsultation.getD()).isEqualTo(UPDATED_D);
        assertThat(testConsultation.getsD()).isEqualTo(DEFAULT_S_D);
        assertThat(testConsultation.getaA()).isEqualTo(DEFAULT_A_A);
        assertThat(testConsultation.getDureeAp()).isEqualTo(DEFAULT_DUREE_AP);
        assertThat(testConsultation.geteAA()).isEqualTo(DEFAULT_E_AA);
        assertThat(testConsultation.getaAA()).isEqualTo(DEFAULT_A_AA);
        assertThat(testConsultation.geteAAa()).isEqualTo(DEFAULT_E_A_AA);
        assertThat(testConsultation.geteEa()).isEqualTo(DEFAULT_E_EA);
        assertThat(testConsultation.getZ()).isEqualTo(UPDATED_Z);
        assertThat(testConsultation.getExamGeneral()).isEqualTo(DEFAULT_EXAM_GENERAL);
        assertThat(testConsultation.getFrequenceRespiratoire()).isEqualTo(UPDATED_FREQUENCE_RESPIRATOIRE);
        assertThat(testConsultation.getFrequenceCardiaque()).isEqualTo(DEFAULT_FREQUENCE_CARDIAQUE);
        assertThat(testConsultation.getCommentaireLibre()).isEqualTo(UPDATED_COMMENTAIRE_LIBRE);
        assertThat(testConsultation.getResultatsParaclinique()).isEqualTo(UPDATED_RESULTATS_PARACLINIQUE);
    }

    @Test
    @Transactional
    void fullUpdateConsultationWithPatch() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();

        // Update the consultation using partial update
        Consultation partialUpdatedConsultation = new Consultation();
        partialUpdatedConsultation.setId(consultation.getId());

        partialUpdatedConsultation
            .dateTime(UPDATED_DATE_TIME)
            .temperature(UPDATED_TEMPERATURE)
            .weight(UPDATED_WEIGHT)
            .tension(UPDATED_TENSION)
            .glycemie(UPDATED_GLYCEMIE)
            .comment(UPDATED_COMMENT)
            .hypothesis(UPDATED_HYPOTHESIS)
            .exams(UPDATED_EXAMS)
            .treatment(UPDATED_TREATMENT)
            .author(UPDATED_AUTHOR)
            .og(UPDATED_OG)
            .vd(UPDATED_VD)
            .eseptum(UPDATED_ESEPTUM)
            .ao(UPDATED_AO)
            .vGDiastole(UPDATED_V_G_DIASTOLE)
            .ouvertureAo(UPDATED_OUVERTURE_AO)
            .vGSystole(UPDATED_V_G_SYSTOLE)
            .vp(UPDATED_VP)
            .oGAo(UPDATED_O_G_AO)
            .fRTeicholtz(UPDATED_F_R_TEICHOLTZ)
            .eVp(UPDATED_E_VP)
            .septumVg(UPDATED_SEPTUM_VG)
            .fETeicholz(UPDATED_F_E_TEICHOLZ)
            .tapse(UPDATED_TAPSE)
            .paroiPost(UPDATED_PAROI_POST)
            .surfaceOg(UPDATED_SURFACE_OG)
            .surfaceOd(UPDATED_SURFACE_OD)
            .mesureVd(UPDATED_MESURE_VD)
            .fe(UPDATED_FE)
            .feA2C(UPDATED_FE_A_2_C)
            .feBiplan(UPDATED_FE_BIPLAN)
            .e(UPDATED_E)
            .a(UPDATED_A)
            .eA(UPDATED_E_A)
            .td(UPDATED_TD)
            .triv(UPDATED_TRIV)
            .dureeAmIm(UPDATED_DUREE_AM_IM)
            .surfaceRegurgitee(UPDATED_SURFACE_REGURGITEE)
            .pba(UPDATED_PBA)
            .qr(UPDATED_QR)
            .vr(UPDATED_VR)
            .sor(UPDATED_SOR)
            .fr(UPDATED_FR)
            .vmaxAp(UPDATED_VMAX_AP)
            .itv(UPDATED_ITV)
            .gradMax(UPDATED_GRAD_MAX)
            .gradMoy(UPDATED_GRAD_MOY)
            .dc(UPDATED_DC)
            .iAoextension(UPDATED_I_AOEXTENSION)
            .venaContracta(UPDATED_VENA_CONTRACTA)
            .pht(UPDATED_PHT)
            .iTExtension(UPDATED_I_T_EXTENSION)
            .gradMaxB(UPDATED_GRAD_MAX_B)
            .paps(UPDATED_PAPS)
            .ip(UPDATED_IP)
            .vmax(UPDATED_VMAX)
            .gradMaxC(UPDATED_GRAD_MAX_C)
            .gradMoyB(UPDATED_GRAD_MOY_B)
            .s(UPDATED_S)
            .d(UPDATED_D)
            .sD(UPDATED_S_D)
            .aA(UPDATED_A_A)
            .dureeAp(UPDATED_DUREE_AP)
            .eAA(UPDATED_E_AA)
            .aAA(UPDATED_A_AA)
            .eAAa(UPDATED_E_A_AA)
            .eEa(UPDATED_E_EA)
            .z(UPDATED_Z)
            .examGeneral(UPDATED_EXAM_GENERAL)
            .frequenceRespiratoire(UPDATED_FREQUENCE_RESPIRATOIRE)
            .frequenceCardiaque(UPDATED_FREQUENCE_CARDIAQUE)
            .commentaireLibre(UPDATED_COMMENTAIRE_LIBRE)
            .resultatsParaclinique(UPDATED_RESULTATS_PARACLINIQUE);

        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultation))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
        Consultation testConsultation = consultationList.get(consultationList.size() - 1);
        assertThat(testConsultation.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testConsultation.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testConsultation.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testConsultation.getTension()).isEqualTo(UPDATED_TENSION);
        assertThat(testConsultation.getGlycemie()).isEqualTo(UPDATED_GLYCEMIE);
        assertThat(testConsultation.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testConsultation.getHypothesis()).isEqualTo(UPDATED_HYPOTHESIS);
        assertThat(testConsultation.getExams()).isEqualTo(UPDATED_EXAMS);
        assertThat(testConsultation.getTreatment()).isEqualTo(UPDATED_TREATMENT);
        assertThat(testConsultation.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testConsultation.getOg()).isEqualTo(UPDATED_OG);
        assertThat(testConsultation.getVd()).isEqualTo(UPDATED_VD);
        assertThat(testConsultation.getEseptum()).isEqualTo(UPDATED_ESEPTUM);
        assertThat(testConsultation.getAo()).isEqualTo(UPDATED_AO);
        assertThat(testConsultation.getvGDiastole()).isEqualTo(UPDATED_V_G_DIASTOLE);
        assertThat(testConsultation.getOuvertureAo()).isEqualTo(UPDATED_OUVERTURE_AO);
        assertThat(testConsultation.getvGSystole()).isEqualTo(UPDATED_V_G_SYSTOLE);
        assertThat(testConsultation.getVp()).isEqualTo(UPDATED_VP);
        assertThat(testConsultation.getoGAo()).isEqualTo(UPDATED_O_G_AO);
        assertThat(testConsultation.getfRTeicholtz()).isEqualTo(UPDATED_F_R_TEICHOLTZ);
        assertThat(testConsultation.geteVp()).isEqualTo(UPDATED_E_VP);
        assertThat(testConsultation.getSeptumVg()).isEqualTo(UPDATED_SEPTUM_VG);
        assertThat(testConsultation.getfETeicholz()).isEqualTo(UPDATED_F_E_TEICHOLZ);
        assertThat(testConsultation.getTapse()).isEqualTo(UPDATED_TAPSE);
        assertThat(testConsultation.getParoiPost()).isEqualTo(UPDATED_PAROI_POST);
        assertThat(testConsultation.getSurfaceOg()).isEqualTo(UPDATED_SURFACE_OG);
        assertThat(testConsultation.getSurfaceOd()).isEqualTo(UPDATED_SURFACE_OD);
        assertThat(testConsultation.getMesureVd()).isEqualTo(UPDATED_MESURE_VD);
        assertThat(testConsultation.getFe()).isEqualTo(UPDATED_FE);
        assertThat(testConsultation.getFeA2C()).isEqualTo(UPDATED_FE_A_2_C);
        assertThat(testConsultation.getFeBiplan()).isEqualTo(UPDATED_FE_BIPLAN);
        assertThat(testConsultation.getE()).isEqualTo(UPDATED_E);
        assertThat(testConsultation.getA()).isEqualTo(UPDATED_A);
        assertThat(testConsultation.geteA()).isEqualTo(UPDATED_E_A);
        assertThat(testConsultation.getTd()).isEqualTo(UPDATED_TD);
        assertThat(testConsultation.getTriv()).isEqualTo(UPDATED_TRIV);
        assertThat(testConsultation.getDureeAmIm()).isEqualTo(UPDATED_DUREE_AM_IM);
        assertThat(testConsultation.getSurfaceRegurgitee()).isEqualTo(UPDATED_SURFACE_REGURGITEE);
        assertThat(testConsultation.getPba()).isEqualTo(UPDATED_PBA);
        assertThat(testConsultation.getQr()).isEqualTo(UPDATED_QR);
        assertThat(testConsultation.getVr()).isEqualTo(UPDATED_VR);
        assertThat(testConsultation.getSor()).isEqualTo(UPDATED_SOR);
        assertThat(testConsultation.getFr()).isEqualTo(UPDATED_FR);
        assertThat(testConsultation.getVmaxAp()).isEqualTo(UPDATED_VMAX_AP);
        assertThat(testConsultation.getItv()).isEqualTo(UPDATED_ITV);
        assertThat(testConsultation.getGradMax()).isEqualTo(UPDATED_GRAD_MAX);
        assertThat(testConsultation.getGradMoy()).isEqualTo(UPDATED_GRAD_MOY);
        assertThat(testConsultation.getDc()).isEqualTo(UPDATED_DC);
        assertThat(testConsultation.getiAoextension()).isEqualTo(UPDATED_I_AOEXTENSION);
        assertThat(testConsultation.getVenaContracta()).isEqualTo(UPDATED_VENA_CONTRACTA);
        assertThat(testConsultation.getPht()).isEqualTo(UPDATED_PHT);
        assertThat(testConsultation.getiTExtension()).isEqualTo(UPDATED_I_T_EXTENSION);
        assertThat(testConsultation.getGradMaxB()).isEqualTo(UPDATED_GRAD_MAX_B);
        assertThat(testConsultation.getPaps()).isEqualTo(UPDATED_PAPS);
        assertThat(testConsultation.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testConsultation.getVmax()).isEqualTo(UPDATED_VMAX);
        assertThat(testConsultation.getGradMaxC()).isEqualTo(UPDATED_GRAD_MAX_C);
        assertThat(testConsultation.getGradMoyB()).isEqualTo(UPDATED_GRAD_MOY_B);
        assertThat(testConsultation.getS()).isEqualTo(UPDATED_S);
        assertThat(testConsultation.getD()).isEqualTo(UPDATED_D);
        assertThat(testConsultation.getsD()).isEqualTo(UPDATED_S_D);
        assertThat(testConsultation.getaA()).isEqualTo(UPDATED_A_A);
        assertThat(testConsultation.getDureeAp()).isEqualTo(UPDATED_DUREE_AP);
        assertThat(testConsultation.geteAA()).isEqualTo(UPDATED_E_AA);
        assertThat(testConsultation.getaAA()).isEqualTo(UPDATED_A_AA);
        assertThat(testConsultation.geteAAa()).isEqualTo(UPDATED_E_A_AA);
        assertThat(testConsultation.geteEa()).isEqualTo(UPDATED_E_EA);
        assertThat(testConsultation.getZ()).isEqualTo(UPDATED_Z);
        assertThat(testConsultation.getExamGeneral()).isEqualTo(UPDATED_EXAM_GENERAL);
        assertThat(testConsultation.getFrequenceRespiratoire()).isEqualTo(UPDATED_FREQUENCE_RESPIRATOIRE);
        assertThat(testConsultation.getFrequenceCardiaque()).isEqualTo(UPDATED_FREQUENCE_CARDIAQUE);
        assertThat(testConsultation.getCommentaireLibre()).isEqualTo(UPDATED_COMMENTAIRE_LIBRE);
        assertThat(testConsultation.getResultatsParaclinique()).isEqualTo(UPDATED_RESULTATS_PARACLINIQUE);
    }

    @Test
    @Transactional
    void patchNonExistingConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultation() throws Exception {
        int databaseSizeBeforeUpdate = consultationRepository.findAll().size();
        consultation.setId(count.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultation in the database
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultation() throws Exception {
        // Initialize the database
        consultationRepository.saveAndFlush(consultation);

        int databaseSizeBeforeDelete = consultationRepository.findAll().size();

        // Delete the consultation
        restConsultationMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultation> consultationList = consultationRepository.findAll();
        assertThat(consultationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
