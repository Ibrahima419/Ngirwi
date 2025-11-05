package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.Consultation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultationDTO implements Serializable {

    private Long id;

    private Instant dateTime;

    @NotNull
    private Double temperature;

    @NotNull
    private Double weight;

    @NotNull
    private String tension;

    private String glycemie;

    private String comment;

    @NotNull
    private String hypothesis;

    @NotNull
    private String exams;

    @NotNull
    private String treatment;

    private String author;

    private String og;

    private String vd;

    private String eseptum;

    private String ao;

    private String vGDiastole;

    private String ouvertureAo;

    private String vGSystole;

    private String vp;

    private String oGAo;

    private String fRTeicholtz;

    private String eVp;

    private String septumVg;

    private String fETeicholz;

    private String tapse;

    private String paroiPost;

    private String surfaceOg;

    private String surfaceOd;

    private String mesureVd;

    private String fe;

    private String feA2C;

    private String feBiplan;

    private String e;

    private String a;

    private String eA;

    private String td;

    private String triv;

    private String dureeAmIm;

    private String surfaceRegurgitee;

    private String pba;

    private String qr;

    private String vr;

    private String sor;

    private String fr;

    private String vmaxAp;

    private String itv;

    private String gradMax;

    private String gradMoy;

    private String dc;

    private String iAoextension;

    private String venaContracta;

    private String pht;

    private String iTExtension;

    private String gradMaxB;

    private String paps;

    private String ip;

    private String vmax;

    private String gradMaxC;

    private String gradMoyB;

    private String s;

    private String d;

    private String sD;

    private String aA;

    private String dureeAp;

    private String eAA;

    private String aAA;

    private String eAAa;

    private String eEa;

    private String z;

    @Lob
    private String examGeneral;

    private Double frequenceRespiratoire;

    private Double frequenceCardiaque;

    @Lob
    private String commentaireLibre;

    @Lob
    private String resultatsParaclinique;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getGlycemie() {
        return glycemie;
    }

    public void setGlycemie(String glycemie) {
        this.glycemie = glycemie;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    public String getExams() {
        return exams;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOg() {
        return og;
    }

    public void setOg(String og) {
        this.og = og;
    }

    public String getVd() {
        return vd;
    }

    public void setVd(String vd) {
        this.vd = vd;
    }

    public String getEseptum() {
        return eseptum;
    }

    public void setEseptum(String eseptum) {
        this.eseptum = eseptum;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getvGDiastole() {
        return vGDiastole;
    }

    public void setvGDiastole(String vGDiastole) {
        this.vGDiastole = vGDiastole;
    }

    public String getOuvertureAo() {
        return ouvertureAo;
    }

    public void setOuvertureAo(String ouvertureAo) {
        this.ouvertureAo = ouvertureAo;
    }

    public String getvGSystole() {
        return vGSystole;
    }

    public void setvGSystole(String vGSystole) {
        this.vGSystole = vGSystole;
    }

    public String getVp() {
        return vp;
    }

    public void setVp(String vp) {
        this.vp = vp;
    }

    public String getoGAo() {
        return oGAo;
    }

    public void setoGAo(String oGAo) {
        this.oGAo = oGAo;
    }

    public String getfRTeicholtz() {
        return fRTeicholtz;
    }

    public void setfRTeicholtz(String fRTeicholtz) {
        this.fRTeicholtz = fRTeicholtz;
    }

    public String geteVp() {
        return eVp;
    }

    public void seteVp(String eVp) {
        this.eVp = eVp;
    }

    public String getSeptumVg() {
        return septumVg;
    }

    public void setSeptumVg(String septumVg) {
        this.septumVg = septumVg;
    }

    public String getfETeicholz() {
        return fETeicholz;
    }

    public void setfETeicholz(String fETeicholz) {
        this.fETeicholz = fETeicholz;
    }

    public String getTapse() {
        return tapse;
    }

    public void setTapse(String tapse) {
        this.tapse = tapse;
    }

    public String getParoiPost() {
        return paroiPost;
    }

    public void setParoiPost(String paroiPost) {
        this.paroiPost = paroiPost;
    }

    public String getSurfaceOg() {
        return surfaceOg;
    }

    public void setSurfaceOg(String surfaceOg) {
        this.surfaceOg = surfaceOg;
    }

    public String getSurfaceOd() {
        return surfaceOd;
    }

    public void setSurfaceOd(String surfaceOd) {
        this.surfaceOd = surfaceOd;
    }

    public String getMesureVd() {
        return mesureVd;
    }

    public void setMesureVd(String mesureVd) {
        this.mesureVd = mesureVd;
    }

    public String getFe() {
        return fe;
    }

    public void setFe(String fe) {
        this.fe = fe;
    }

    public String getFeA2C() {
        return feA2C;
    }

    public void setFeA2C(String feA2C) {
        this.feA2C = feA2C;
    }

    public String getFeBiplan() {
        return feBiplan;
    }

    public void setFeBiplan(String feBiplan) {
        this.feBiplan = feBiplan;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String geteA() {
        return eA;
    }

    public void seteA(String eA) {
        this.eA = eA;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public String getTriv() {
        return triv;
    }

    public void setTriv(String triv) {
        this.triv = triv;
    }

    public String getDureeAmIm() {
        return dureeAmIm;
    }

    public void setDureeAmIm(String dureeAmIm) {
        this.dureeAmIm = dureeAmIm;
    }

    public String getSurfaceRegurgitee() {
        return surfaceRegurgitee;
    }

    public void setSurfaceRegurgitee(String surfaceRegurgitee) {
        this.surfaceRegurgitee = surfaceRegurgitee;
    }

    public String getPba() {
        return pba;
    }

    public void setPba(String pba) {
        this.pba = pba;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getVr() {
        return vr;
    }

    public void setVr(String vr) {
        this.vr = vr;
    }

    public String getSor() {
        return sor;
    }

    public void setSor(String sor) {
        this.sor = sor;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getVmaxAp() {
        return vmaxAp;
    }

    public void setVmaxAp(String vmaxAp) {
        this.vmaxAp = vmaxAp;
    }

    public String getItv() {
        return itv;
    }

    public void setItv(String itv) {
        this.itv = itv;
    }

    public String getGradMax() {
        return gradMax;
    }

    public void setGradMax(String gradMax) {
        this.gradMax = gradMax;
    }

    public String getGradMoy() {
        return gradMoy;
    }

    public void setGradMoy(String gradMoy) {
        this.gradMoy = gradMoy;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getiAoextension() {
        return iAoextension;
    }

    public void setiAoextension(String iAoextension) {
        this.iAoextension = iAoextension;
    }

    public String getVenaContracta() {
        return venaContracta;
    }

    public void setVenaContracta(String venaContracta) {
        this.venaContracta = venaContracta;
    }

    public String getPht() {
        return pht;
    }

    public void setPht(String pht) {
        this.pht = pht;
    }

    public String getiTExtension() {
        return iTExtension;
    }

    public void setiTExtension(String iTExtension) {
        this.iTExtension = iTExtension;
    }

    public String getGradMaxB() {
        return gradMaxB;
    }

    public void setGradMaxB(String gradMaxB) {
        this.gradMaxB = gradMaxB;
    }

    public String getPaps() {
        return paps;
    }

    public void setPaps(String paps) {
        this.paps = paps;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVmax() {
        return vmax;
    }

    public void setVmax(String vmax) {
        this.vmax = vmax;
    }

    public String getGradMaxC() {
        return gradMaxC;
    }

    public void setGradMaxC(String gradMaxC) {
        this.gradMaxC = gradMaxC;
    }

    public String getGradMoyB() {
        return gradMoyB;
    }

    public void setGradMoyB(String gradMoyB) {
        this.gradMoyB = gradMoyB;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getsD() {
        return sD;
    }

    public void setsD(String sD) {
        this.sD = sD;
    }

    public String getaA() {
        return aA;
    }

    public void setaA(String aA) {
        this.aA = aA;
    }

    public String getDureeAp() {
        return dureeAp;
    }

    public void setDureeAp(String dureeAp) {
        this.dureeAp = dureeAp;
    }

    public String geteAA() {
        return eAA;
    }

    public void seteAA(String eAA) {
        this.eAA = eAA;
    }

    public String getaAA() {
        return aAA;
    }

    public void setaAA(String aAA) {
        this.aAA = aAA;
    }

    public String geteAAa() {
        return eAAa;
    }

    public void seteAAa(String eAAa) {
        this.eAAa = eAAa;
    }

    public String geteEa() {
        return eEa;
    }

    public void seteEa(String eEa) {
        this.eEa = eEa;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getExamGeneral() {
        return examGeneral;
    }

    public void setExamGeneral(String examGeneral) {
        this.examGeneral = examGeneral;
    }

    public Double getFrequenceRespiratoire() {
        return frequenceRespiratoire;
    }

    public void setFrequenceRespiratoire(Double frequenceRespiratoire) {
        this.frequenceRespiratoire = frequenceRespiratoire;
    }

    public Double getFrequenceCardiaque() {
        return frequenceCardiaque;
    }

    public void setFrequenceCardiaque(Double frequenceCardiaque) {
        this.frequenceCardiaque = frequenceCardiaque;
    }

    public String getCommentaireLibre() {
        return commentaireLibre;
    }

    public void setCommentaireLibre(String commentaireLibre) {
        this.commentaireLibre = commentaireLibre;
    }

    public String getResultatsParaclinique() {
        return resultatsParaclinique;
    }

    public void setResultatsParaclinique(String resultatsParaclinique) {
        this.resultatsParaclinique = resultatsParaclinique;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultationDTO)) {
            return false;
        }

        ConsultationDTO consultationDTO = (ConsultationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultationDTO{" +
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
            ", patient=" + getPatient() +
            "}";
    }
}
