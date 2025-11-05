package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;

public class HospitalisationDTO implements Serializable {

    private Long id;

    private Instant entryDate;

    private Instant releaseDate;

    private String doctorName;

    private HospitalisationStatus status;

    private String admissionReason;

    private Long dossierMedicalId;

    private String entryDiagnosis;

    private String finalDiagnosis;

    private String service;

    private Long patientId;

    private Set<Long> surveillanceSheetIds;

    private java.math.BigDecimal dailyRate;

    private java.math.BigDecimal comfortFees;

    private java.math.BigDecimal feeOverrun;

    private java.math.BigDecimal insuranceCoveragePercent;

    private java.math.BigDecimal totalAmount; // read-only pour l'UI

    public HospitalisationDTO() {}

    // =========================
    // Getters & Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public HospitalisationStatus getStatus() {
        return status;
    }

    public void setStatus(HospitalisationStatus status) {
        this.status = status;
    }

    public String getAdmissionReason() {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason) {
        this.admissionReason = admissionReason;
    }

    public Long getDossierMedicalId() {
        return dossierMedicalId;
    }

    public void setDossierMedicalId(Long dossierMedicalId) {
        this.dossierMedicalId = dossierMedicalId;
    }

    public String getEntryDiagnosis() {
        return entryDiagnosis;
    }

    public void setEntryDiagnosis(String entryDiagnosis) {
        this.entryDiagnosis = entryDiagnosis;
    }

    public String getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(String finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Set<Long> getSurveillanceSheetIds() {
        return surveillanceSheetIds;
    }

    public void setSurveillanceSheetIds(Set<Long> surveillanceSheetIds) {
        this.surveillanceSheetIds = surveillanceSheetIds;
    }

    public java.math.BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(java.math.BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public java.math.BigDecimal getComfortFees() {
        return comfortFees;
    }

    public void setComfortFees(java.math.BigDecimal comfortFees) {
        this.comfortFees = comfortFees;
    }

    public java.math.BigDecimal getFeeOverrun() {
        return feeOverrun;
    }

    public void setFeeOverrun(java.math.BigDecimal feeOverrun) {
        this.feeOverrun = feeOverrun;
    }

    public java.math.BigDecimal getInsuranceCoveragePercent() {
        return insuranceCoveragePercent;
    }

    public void setInsuranceCoveragePercent(java.math.BigDecimal insuranceCoveragePercent) {
        this.insuranceCoveragePercent = insuranceCoveragePercent;
    }

    public java.math.BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return (
            "HospitalisationDTO{" +
            "id=" +
            id +
            ", entryDate=" +
            entryDate +
            ", releaseDate=" +
            releaseDate +
            ", doctorName='" +
            doctorName +
            '\'' +
            ", status=" +
            status +
            ", admissionReason='" +
            admissionReason +
            '\'' +
            ", dossierMedicalId=" +
            dossierMedicalId +
            ", entryDiagnosis='" +
            entryDiagnosis +
            '\'' +
            ", finalDiagnosis='" +
            finalDiagnosis +
            '\'' +
            ", service='" +
            service +
            '\'' +
            ", patientId=" +
            patientId +
            ", surveillanceSheetIds=" +
            surveillanceSheetIds +
            ", dailyRate=" +
            dailyRate +
            ", comfortFees=" +
            comfortFees +
            ", feeOverrun=" +
            feeOverrun +
            ", insuranceCoveragePercent=" +
            insuranceCoveragePercent +
            ", totalAmount=" +
            totalAmount +
            '}'
        );
    }
}
