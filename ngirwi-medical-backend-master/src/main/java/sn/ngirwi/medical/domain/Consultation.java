package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Consultation.
 */
@Entity
@Table(name = "consultation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_time")
    private Instant dateTime;

    @NotNull
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Double weight;

    @NotNull
    @Column(name = "tension", nullable = false)
    private String tension;

    @Column(name = "glycemie")
    private String glycemie;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "hypothesis", nullable = false)
    private String hypothesis;

    @NotNull
    @Column(name = "exams", nullable = false)
    private String exams;

    @NotNull
    @Column(name = "treatment", nullable = false)
    private String treatment;

    @Column(name = "author")
    private String author;

    @Column(name = "og")
    private String og;

    @Column(name = "vd")
    private String vd;

    @Column(name = "eseptum")
    private String eseptum;

    @Column(name = "ao")
    private String ao;

    @Column(name = "v_g_diastole")
    private String vGDiastole;

    @Column(name = "ouverture_ao")
    private String ouvertureAo;

    @Column(name = "v_g_systole")
    private String vGSystole;

    @Column(name = "vp")
    private String vp;

    @Column(name = "o_g_ao")
    private String oGAo;

    @Column(name = "f_r_teicholtz")
    private String fRTeicholtz;

    @Column(name = "e_vp")
    private String eVp;

    @Column(name = "septum_vg")
    private String septumVg;

    @Column(name = "f_e_teicholz")
    private String fETeicholz;

    @Column(name = "tapse")
    private String tapse;

    @Column(name = "paroi_post")
    private String paroiPost;

    @Column(name = "surface_og")
    private String surfaceOg;

    @Column(name = "surface_od")
    private String surfaceOd;

    @Column(name = "mesure_vd")
    private String mesureVd;

    @Column(name = "fe")
    private String fe;

    @Column(name = "fe_a_2_c")
    private String feA2C;

    @Column(name = "fe_biplan")
    private String feBiplan;

    @Column(name = "e")
    private String e;

    @Column(name = "a")
    private String a;

    @Column(name = "e_a")
    private String eA;

    @Column(name = "td")
    private String td;

    @Column(name = "triv")
    private String triv;

    @Column(name = "duree_am_im")
    private String dureeAmIm;

    @Column(name = "surface_regurgitee")
    private String surfaceRegurgitee;

    @Column(name = "pba")
    private String pba;

    @Column(name = "qr")
    private String qr;

    @Column(name = "vr")
    private String vr;

    @Column(name = "sor")
    private String sor;

    @Column(name = "fr")
    private String fr;

    @Column(name = "vmax_ap")
    private String vmaxAp;

    @Column(name = "itv")
    private String itv;

    @Column(name = "grad_max")
    private String gradMax;

    @Column(name = "grad_moy")
    private String gradMoy;

    @Column(name = "dc")
    private String dc;

    @Column(name = "i_aoextension")
    private String iAoextension;

    @Column(name = "vena_contracta")
    private String venaContracta;

    @Column(name = "pht")
    private String pht;

    @Column(name = "i_t_extension")
    private String iTExtension;

    @Column(name = "grad_max_b")
    private String gradMaxB;

    @Column(name = "paps")
    private String paps;

    @Column(name = "ip")
    private String ip;

    @Column(name = "vmax")
    private String vmax;

    @Column(name = "grad_max_c")
    private String gradMaxC;

    @Column(name = "grad_moy_b")
    private String gradMoyB;

    @Column(name = "s")
    private String s;

    @Column(name = "d")
    private String d;

    @Column(name = "s_d")
    private String sD;

    @Column(name = "a_a")
    private String aA;

    @Column(name = "duree_ap")
    private String dureeAp;

    @Column(name = "e_aa")
    private String eAA;

    @Column(name = "a_aa")
    private String aAA;

    @Column(name = "e_a_aa")
    private String eAAa;

    @Column(name = "e_ea")
    private String eEa;

    @Column(name = "z")
    private String z;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "exam_general")
    private String examGeneral;

    @Column(name = "frequence_respiratoire")
    private Double frequenceRespiratoire;

    @Column(name = "frequence_cardiaque")
    private Double frequenceCardiaque;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "commentaire_libre")
    private String commentaireLibre;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "resultats_paraclinique")
    private String resultatsParaclinique;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dossierMedical", "consultations" }, allowSetters = true)
    private Patient patient;

    @JsonIgnoreProperties(value = { "consultation", "medecines" }, allowSetters = true)
    @OneToOne(mappedBy = "consultation")
    private Prescription ordonance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consultation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTime() {
        return this.dateTime;
    }

    public Consultation dateTime(Instant dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public Consultation temperature(Double temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWeight() {
        return this.weight;
    }

    public Consultation weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTension() {
        return this.tension;
    }

    public Consultation tension(String tension) {
        this.setTension(tension);
        return this;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getGlycemie() {
        return this.glycemie;
    }

    public Consultation glycemie(String glycemie) {
        this.setGlycemie(glycemie);
        return this;
    }

    public void setGlycemie(String glycemie) {
        this.glycemie = glycemie;
    }

    public String getComment() {
        return this.comment;
    }

    public Consultation comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHypothesis() {
        return this.hypothesis;
    }

    public Consultation hypothesis(String hypothesis) {
        this.setHypothesis(hypothesis);
        return this;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    public String getExams() {
        return this.exams;
    }

    public Consultation exams(String exams) {
        this.setExams(exams);
        return this;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public String getTreatment() {
        return this.treatment;
    }

    public Consultation treatment(String treatment) {
        this.setTreatment(treatment);
        return this;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getAuthor() {
        return this.author;
    }

    public Consultation author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOg() {
        return this.og;
    }

    public Consultation og(String og) {
        this.setOg(og);
        return this;
    }

    public void setOg(String og) {
        this.og = og;
    }

    public String getVd() {
        return this.vd;
    }

    public Consultation vd(String vd) {
        this.setVd(vd);
        return this;
    }

    public void setVd(String vd) {
        this.vd = vd;
    }

    public String getEseptum() {
        return this.eseptum;
    }

    public Consultation eseptum(String eseptum) {
        this.setEseptum(eseptum);
        return this;
    }

    public void setEseptum(String eseptum) {
        this.eseptum = eseptum;
    }

    public String getAo() {
        return this.ao;
    }

    public Consultation ao(String ao) {
        this.setAo(ao);
        return this;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getvGDiastole() {
        return this.vGDiastole;
    }

    public Consultation vGDiastole(String vGDiastole) {
        this.setvGDiastole(vGDiastole);
        return this;
    }

    public void setvGDiastole(String vGDiastole) {
        this.vGDiastole = vGDiastole;
    }

    public String getOuvertureAo() {
        return this.ouvertureAo;
    }

    public Consultation ouvertureAo(String ouvertureAo) {
        this.setOuvertureAo(ouvertureAo);
        return this;
    }

    public void setOuvertureAo(String ouvertureAo) {
        this.ouvertureAo = ouvertureAo;
    }

    public String getvGSystole() {
        return this.vGSystole;
    }

    public Consultation vGSystole(String vGSystole) {
        this.setvGSystole(vGSystole);
        return this;
    }

    public void setvGSystole(String vGSystole) {
        this.vGSystole = vGSystole;
    }

    public String getVp() {
        return this.vp;
    }

    public Consultation vp(String vp) {
        this.setVp(vp);
        return this;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public String getoGAo() {
        return this.oGAo;
    }

    public Consultation oGAo(String oGAo) {
        this.setoGAo(oGAo);
        return this;
    }

    public void setoGAo(String oGAo) {
        this.oGAo = oGAo;
    }

    public String getfRTeicholtz() {
        return this.fRTeicholtz;
    }

    public Consultation fRTeicholtz(String fRTeicholtz) {
        this.setfRTeicholtz(fRTeicholtz);
        return this;
    }

    public void setfRTeicholtz(String fRTeicholtz) {
        this.fRTeicholtz = fRTeicholtz;
    }

    public String geteVp() {
        return this.eVp;
    }

    public Consultation eVp(String eVp) {
        this.seteVp(eVp);
        return this;
    }

    public void seteVp(String eVp) {
        this.eVp = eVp;
    }

    public String getSeptumVg() {
        return this.septumVg;
    }

    public Consultation septumVg(String septumVg) {
        this.setSeptumVg(septumVg);
        return this;
    }

    public void setSeptumVg(String septumVg) {
        this.septumVg = septumVg;
    }

    public String getfETeicholz() {
        return this.fETeicholz;
    }

    public Consultation fETeicholz(String fETeicholz) {
        this.setfETeicholz(fETeicholz);
        return this;
    }

    public void setfETeicholz(String fETeicholz) {
        this.fETeicholz = fETeicholz;
    }

    public String getTapse() {
        return this.tapse;
    }

    public Consultation tapse(String tapse) {
        this.setTapse(tapse);
        return this;
    }

    public void setTapse(String tapse) {
        this.tapse = tapse;
    }

    public String getParoiPost() {
        return this.paroiPost;
    }

    public Consultation paroiPost(String paroiPost) {
        this.setParoiPost(paroiPost);
        return this;
    }

    public void setParoiPost(String paroiPost) {
        this.paroiPost = paroiPost;
    }

    public String getSurfaceOg() {
        return this.surfaceOg;
    }

    public Consultation surfaceOg(String surfaceOg) {
        this.setSurfaceOg(surfaceOg);
        return this;
    }

    public void setSurfaceOg(String surfaceOg) {
        this.surfaceOg = surfaceOg;
    }

    public String getSurfaceOd() {
        return this.surfaceOd;
    }

    public Consultation surfaceOd(String surfaceOd) {
        this.setSurfaceOd(surfaceOd);
        return this;
    }

    public void setSurfaceOd(String surfaceOd) {
        this.surfaceOd = surfaceOd;
    }

    public String getMesureVd() {
        return this.mesureVd;
    }

    public Consultation mesureVd(String mesureVd) {
        this.setMesureVd(mesureVd);
        return this;
    }

    public void setMesureVd(String mesureVd) {
        this.mesureVd = mesureVd;
    }

    public String getFe() {
        return this.fe;
    }

    public Consultation fe(String fe) {
        this.setFe(fe);
        return this;
    }

    public void setFe(String fe) {
        this.fe = fe;
    }

    public String getFeA2C() {
        return this.feA2C;
    }

    public Consultation feA2C(String feA2C) {
        this.setFeA2C(feA2C);
        return this;
    }

    public void setFeA2C(String feA2C) {
        this.feA2C = feA2C;
    }

    public String getFeBiplan() {
        return this.feBiplan;
    }

    public Consultation feBiplan(String feBiplan) {
        this.setFeBiplan(feBiplan);
        return this;
    }

    public void setFeBiplan(String feBiplan) {
        this.feBiplan = feBiplan;
    }

    public String getE() {
        return this.e;
    }

    public Consultation e(String e) {
        this.setE(e);
        return this;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getA() {
        return this.a;
    }

    public Consultation a(String a) {
        this.setA(a);
        return this;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String geteA() {
        return this.eA;
    }

    public Consultation eA(String eA) {
        this.seteA(eA);
        return this;
    }

    public void seteA(String eA) {
        this.eA = eA;
    }

    public String getTd() {
        return this.td;
    }

    public Consultation td(String td) {
        this.setTd(td);
        return this;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public String getTriv() {
        return this.triv;
    }

    public Consultation triv(String triv) {
        this.setTriv(triv);
        return this;
    }

    public void setTriv(String triv) {
        this.triv = triv;
    }

    public String getDureeAmIm() {
        return this.dureeAmIm;
    }

    public Consultation dureeAmIm(String dureeAmIm) {
        this.setDureeAmIm(dureeAmIm);
        return this;
    }

    public void setDureeAmIm(String dureeAmIm) {
        this.dureeAmIm = dureeAmIm;
    }

    public String getSurfaceRegurgitee() {
        return this.surfaceRegurgitee;
    }

    public Consultation surfaceRegurgitee(String surfaceRegurgitee) {
        this.setSurfaceRegurgitee(surfaceRegurgitee);
        return this;
    }

    public void setSurfaceRegurgitee(String surfaceRegurgitee) {
        this.surfaceRegurgitee = surfaceRegurgitee;
    }

    public String getPba() {
        return this.pba;
    }

    public Consultation pba(String pba) {
        this.setPba(pba);
        return this;
    }

    public void setPba(String pba) {
        this.pba = pba;
    }

    public String getQr() {
        return this.qr;
    }

    public Consultation qr(String qr) {
        this.setQr(qr);
        return this;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getVr() {
        return this.vr;
    }

    public Consultation vr(String vr) {
        this.setVr(vr);
        return this;
    }

    public void setVr(String vr) {
        this.vr = vr;
    }

    public String getSor() {
        return this.sor;
    }

    public Consultation sor(String sor) {
        this.setSor(sor);
        return this;
    }

    public void setSor(String sor) {
        this.sor = sor;
    }

    public String getFr() {
        return this.fr;
    }

    public Consultation fr(String fr) {
        this.setFr(fr);
        return this;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getVmaxAp() {
        return this.vmaxAp;
    }

    public Consultation vmaxAp(String vmaxAp) {
        this.setVmaxAp(vmaxAp);
        return this;
    }

    public void setVmaxAp(String vmaxAp) {
        this.vmaxAp = vmaxAp;
    }

    public String getItv() {
        return this.itv;
    }

    public Consultation itv(String itv) {
        this.setItv(itv);
        return this;
    }

    public void setItv(String itv) {
        this.itv = itv;
    }

    public String getGradMax() {
        return this.gradMax;
    }

    public Consultation gradMax(String gradMax) {
        this.setGradMax(gradMax);
        return this;
    }

    public void setGradMax(String gradMax) {
        this.gradMax = gradMax;
    }

    public String getGradMoy() {
        return this.gradMoy;
    }

    public Consultation gradMoy(String gradMoy) {
        this.setGradMoy(gradMoy);
        return this;
    }

    public void setGradMoy(String gradMoy) {
        this.gradMoy = gradMoy;
    }

    public String getDc() {
        return this.dc;
    }

    public Consultation dc(String dc) {
        this.setDc(dc);
        return this;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getiAoextension() {
        return this.iAoextension;
    }

    public Consultation iAoextension(String iAoextension) {
        this.setiAoextension(iAoextension);
        return this;
    }

    public void setiAoextension(String iAoextension) {
        this.iAoextension = iAoextension;
    }

    public String getVenaContracta() {
        return this.venaContracta;
    }

    public Consultation venaContracta(String venaContracta) {
        this.setVenaContracta(venaContracta);
        return this;
    }

    public void setVenaContracta(String venaContracta) {
        this.venaContracta = venaContracta;
    }

    public String getPht() {
        return this.pht;
    }

    public Consultation pht(String pht) {
        this.setPht(pht);
        return this;
    }

    public void setPht(String pht) {
        this.pht = pht;
    }

    public String getiTExtension() {
        return this.iTExtension;
    }

    public Consultation iTExtension(String iTExtension) {
        this.setiTExtension(iTExtension);
        return this;
    }

    public void setiTExtension(String iTExtension) {
        this.iTExtension = iTExtension;
    }

    public String getGradMaxB() {
        return this.gradMaxB;
    }

    public Consultation gradMaxB(String gradMaxB) {
        this.setGradMaxB(gradMaxB);
        return this;
    }

    public void setGradMaxB(String gradMaxB) {
        this.gradMaxB = gradMaxB;
    }

    public String getPaps() {
        return this.paps;
    }

    public Consultation paps(String paps) {
        this.setPaps(paps);
        return this;
    }

    public void setPaps(String paps) {
        this.paps = paps;
    }

    public String getIp() {
        return this.ip;
    }

    public Consultation ip(String ip) {
        this.setIp(ip);
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVmax() {
        return this.vmax;
    }

    public Consultation vmax(String vmax) {
        this.setVmax(vmax);
        return this;
    }

    public void setVmax(String vmax) {
        this.vmax = vmax;
    }

    public String getGradMaxC() {
        return this.gradMaxC;
    }

    public Consultation gradMaxC(String gradMaxC) {
        this.setGradMaxC(gradMaxC);
        return this;
    }

    public void setGradMaxC(String gradMaxC) {
        this.gradMaxC = gradMaxC;
    }

    public String getGradMoyB() {
        return this.gradMoyB;
    }

    public Consultation gradMoyB(String gradMoyB) {
        this.setGradMoyB(gradMoyB);
        return this;
    }

    public void setGradMoyB(String gradMoyB) {
        this.gradMoyB = gradMoyB;
    }

    public String getS() {
        return this.s;
    }

    public Consultation s(String s) {
        this.setS(s);
        return this;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getD() {
        return this.d;
    }

    public Consultation d(String d) {
        this.setD(d);
        return this;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getsD() {
        return this.sD;
    }

    public Consultation sD(String sD) {
        this.setsD(sD);
        return this;
    }

    public void setsD(String sD) {
        this.sD = sD;
    }

    public String getaA() {
        return this.aA;
    }

    public Consultation aA(String aA) {
        this.setaA(aA);
        return this;
    }

    public void setaA(String aA) {
        this.aA = aA;
    }

    public String getDureeAp() {
        return this.dureeAp;
    }

    public Consultation dureeAp(String dureeAp) {
        this.setDureeAp(dureeAp);
        return this;
    }

    public void setDureeAp(String dureeAp) {
        this.dureeAp = dureeAp;
    }

    public String geteAA() {
        return this.eAA;
    }

    public Consultation eAA(String eAA) {
        this.seteAA(eAA);
        return this;
    }

    public void seteAA(String eAA) {
        this.eAA = eAA;
    }

    public String getaAA() {
        return this.aAA;
    }

    public Consultation aAA(String aAA) {
        this.setaAA(aAA);
        return this;
    }

    public void setaAA(String aAA) {
        this.aAA = aAA;
    }

    public String geteAAa() {
        return this.eAAa;
    }

    public Consultation eAAa(String eAAa) {
        this.seteAAa(eAAa);
        return this;
    }

    public void seteAAa(String eAAa) {
        this.eAAa = eAAa;
    }

    public String geteEa() {
        return this.eEa;
    }

    public Consultation eEa(String eEa) {
        this.seteEa(eEa);
        return this;
    }

    public void seteEa(String eEa) {
        this.eEa = eEa;
    }

    public String getZ() {
        return this.z;
    }

    public Consultation z(String z) {
        this.setZ(z);
        return this;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getExamGeneral() {
        return this.examGeneral;
    }

    public Consultation examGeneral(String examGeneral) {
        this.setExamGeneral(examGeneral);
        return this;
    }

    public void setExamGeneral(String examGeneral) {
        this.examGeneral = examGeneral;
    }

    public Double getFrequenceRespiratoire() {
        return this.frequenceRespiratoire;
    }

    public Consultation frequenceRespiratoire(Double frequenceRespiratoire) {
        this.setFrequenceRespiratoire(frequenceRespiratoire);
        return this;
    }

    public void setFrequenceRespiratoire(Double frequenceRespiratoire) {
        this.frequenceRespiratoire = frequenceRespiratoire;
    }

    public Double getFrequenceCardiaque() {
        return this.frequenceCardiaque;
    }

    public Consultation frequenceCardiaque(Double frequenceCardiaque) {
        this.setFrequenceCardiaque(frequenceCardiaque);
        return this;
    }

    public void setFrequenceCardiaque(Double frequenceCardiaque) {
        this.frequenceCardiaque = frequenceCardiaque;
    }

    public String getCommentaireLibre() {
        return this.commentaireLibre;
    }

    public Consultation commentaireLibre(String commentaireLibre) {
        this.setCommentaireLibre(commentaireLibre);
        return this;
    }

    public void setCommentaireLibre(String commentaireLibre) {
        this.commentaireLibre = commentaireLibre;
    }

    public String getResultatsParaclinique() {
        return this.resultatsParaclinique;
    }

    public Consultation resultatsParaclinique(String resultatsParaclinique) {
        this.setResultatsParaclinique(resultatsParaclinique);
        return this;
    }

    public void setResultatsParaclinique(String resultatsParaclinique) {
        this.resultatsParaclinique = resultatsParaclinique;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Consultation patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Prescription getOrdonance() {
        return this.ordonance;
    }

    public void setOrdonance(Prescription prescription) {
        if (this.ordonance != null) {
            this.ordonance.setConsultation(null);
        }
        if (prescription != null) {
            prescription.setConsultation(this);
        }
        this.ordonance = prescription;
    }

    public Consultation ordonance(Prescription prescription) {
        this.setOrdonance(prescription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultation)) {
            return false;
        }
        return id != null && id.equals(((Consultation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultation{" +
            "id=" + getId() +
            ", dateTime='" + getDateTime() + "'" +
            ", temperature=" + getTemperature() +
            ", weight=" + getWeight() +
            ", tension='" + getTension() + "'" +
            ", glycemie='" + getGlycemie() + "'" +
            ", comment='" + getComment() + "'" +
            ", hypothesis='" + getHypothesis() + "'" +
            ", exams='" + getExams() + "'" +
            ", treatment='" + getTreatment() + "'" +
            ", author='" + getAuthor() + "'" +
            ", og='" + getOg() + "'" +
            ", vd='" + getVd() + "'" +
            ", eseptum='" + getEseptum() + "'" +
            ", ao='" + getAo() + "'" +
            ", vGDiastole='" + getvGDiastole() + "'" +
            ", ouvertureAo='" + getOuvertureAo() + "'" +
            ", vGSystole='" + getvGSystole() + "'" +
            ", vp='" + getVp() + "'" +
            ", oGAo='" + getoGAo() + "'" +
            ", fRTeicholtz='" + getfRTeicholtz() + "'" +
            ", eVp='" + geteVp() + "'" +
            ", septumVg='" + getSeptumVg() + "'" +
            ", fETeicholz='" + getfETeicholz() + "'" +
            ", tapse='" + getTapse() + "'" +
            ", paroiPost='" + getParoiPost() + "'" +
            ", surfaceOg='" + getSurfaceOg() + "'" +
            ", surfaceOd='" + getSurfaceOd() + "'" +
            ", mesureVd='" + getMesureVd() + "'" +
            ", fe='" + getFe() + "'" +
            ", feA2C='" + getFeA2C() + "'" +
            ", feBiplan='" + getFeBiplan() + "'" +
            ", e='" + getE() + "'" +
            ", a='" + getA() + "'" +
            ", eA='" + geteA() + "'" +
            ", td='" + getTd() + "'" +
            ", triv='" + getTriv() + "'" +
            ", dureeAmIm='" + getDureeAmIm() + "'" +
            ", surfaceRegurgitee='" + getSurfaceRegurgitee() + "'" +
            ", pba='" + getPba() + "'" +
            ", qr='" + getQr() + "'" +
            ", vr='" + getVr() + "'" +
            ", sor='" + getSor() + "'" +
            ", fr='" + getFr() + "'" +
            ", vmaxAp='" + getVmaxAp() + "'" +
            ", itv='" + getItv() + "'" +
            ", gradMax='" + getGradMax() + "'" +
            ", gradMoy='" + getGradMoy() + "'" +
            ", dc='" + getDc() + "'" +
            ", iAoextension='" + getiAoextension() + "'" +
            ", venaContracta='" + getVenaContracta() + "'" +
            ", pht='" + getPht() + "'" +
            ", iTExtension='" + getiTExtension() + "'" +
            ", gradMaxB='" + getGradMaxB() + "'" +
            ", paps='" + getPaps() + "'" +
            ", ip='" + getIp() + "'" +
            ", vmax='" + getVmax() + "'" +
            ", gradMaxC='" + getGradMaxC() + "'" +
            ", gradMoyB='" + getGradMoyB() + "'" +
            ", s='" + getS() + "'" +
            ", d='" + getD() + "'" +
            ", sD='" + getsD() + "'" +
            ", aA='" + getaA() + "'" +
            ", dureeAp='" + getDureeAp() + "'" +
            ", eAA='" + geteAA() + "'" +
            ", aAA='" + getaAA() + "'" +
            ", eAAa='" + geteAAa() + "'" +
            ", eEa='" + geteEa() + "'" +
            ", z='" + getZ() + "'" +
            ", examGeneral='" + getExamGeneral() + "'" +
            ", frequenceRespiratoire=" + getFrequenceRespiratoire() +
            ", frequenceCardiaque=" + getFrequenceCardiaque() +
            ", commentaireLibre='" + getCommentaireLibre() + "'" +
            ", resultatsParaclinique='" + getResultatsParaclinique() + "'" +
            "}";
    }
}
