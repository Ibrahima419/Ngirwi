package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for summarizing a Hospitalisation with its latest SurveillanceSheet data.
 */
public class HospitalisationResumeDTO implements Serializable {

    private Long hospitalisationId;

    private Instant entryDate;

    private Instant releaseDate;

    private String doctorName;

    private String service;

    private String admissionReason;

    private String entryDiagnosis;

    private String finalDiagnosis;

    private String status;

    private Integer numberOfDays;

    private BigDecimal dailyRate;

    private BigDecimal forfaitSejour;

    private BigDecimal comfortFees;

    private BigDecimal feeOverrun;

    private BigDecimal medsTotal;

    private BigDecimal actsTotal;

    private BigDecimal subtotal;

    private BigDecimal insuranceCoveragePercent;

    private BigDecimal totalAmount;

    // ==============================
    // Last Surveillance Sheet fields
    // ==============================

    private Long surveillanceSheetId;

    private LocalDate sheetDate;

    private Double temperature;

    private Integer systolicBP;

    private Integer diastolicBP;

    private Integer pulseRate;

    private Integer respirationRate;

    private Integer spo2;

    private String nursingNotes;

    private String medicalObservations;

    private String actsPerformed;

    private String administeredMedication;

    // ==============================
    // Getters and Setters
    // ==============================

    public Long getHospitalisationId() {
        return hospitalisationId;
    }

    public void setHospitalisationId(Long hospitalisationId) {
        this.hospitalisationId = hospitalisationId;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAdmissionReason() {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason) {
        this.admissionReason = admissionReason;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSurveillanceSheetId() {
        return surveillanceSheetId;
    }

    public void setSurveillanceSheetId(Long surveillanceSheetId) {
        this.surveillanceSheetId = surveillanceSheetId;
    }

    public LocalDate getSheetDate() {
        return sheetDate;
    }

    public void setSheetDate(LocalDate sheetDate) {
        this.sheetDate = sheetDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getSystolicBP() {
        return systolicBP;
    }

    public void setSystolicBP(Integer systolicBP) {
        this.systolicBP = systolicBP;
    }

    public Integer getDiastolicBP() {
        return diastolicBP;
    }

    public void setDiastolicBP(Integer diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
    }

    public Integer getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(Integer respirationRate) {
        this.respirationRate = respirationRate;
    }

    public Integer getSpo2() {
        return spo2;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public String getNursingNotes() {
        return nursingNotes;
    }

    public void setNursingNotes(String nursingNotes) {
        this.nursingNotes = nursingNotes;
    }

    public String getMedicalObservations() {
        return medicalObservations;
    }

    public void setMedicalObservations(String medicalObservations) {
        this.medicalObservations = medicalObservations;
    }

    public String getActsPerformed() {
        return actsPerformed;
    }

    public void setActsPerformed(String actsPerformed) {
        this.actsPerformed = actsPerformed;
    }

    public String getAdministeredMedication() {
        return administeredMedication;
    }

    public void setAdministeredMedication(String administeredMedication) {
        this.administeredMedication = administeredMedication;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getForfaitSejour() {
        return forfaitSejour;
    }

    public void setForfaitSejour(BigDecimal forfaitSejour) {
        this.forfaitSejour = forfaitSejour;
    }

    public BigDecimal getComfortFees() {
        return comfortFees;
    }

    public void setComfortFees(BigDecimal comfortFees) {
        this.comfortFees = comfortFees;
    }

    public BigDecimal getFeeOverrun() {
        return feeOverrun;
    }

    public void setFeeOverrun(BigDecimal feeOverrun) {
        this.feeOverrun = feeOverrun;
    }

    public BigDecimal getMedsTotal() {
        return medsTotal;
    }

    public void setMedsTotal(BigDecimal medsTotal) {
        this.medsTotal = medsTotal;
    }

    public BigDecimal getActsTotal() {
        return actsTotal;
    }

    public void setActsTotal(BigDecimal actsTotal) {
        this.actsTotal = actsTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getInsuranceCoveragePercent() {
        return insuranceCoveragePercent;
    }

    public void setInsuranceCoveragePercent(BigDecimal insuranceCoveragePercent) {
        this.insuranceCoveragePercent = insuranceCoveragePercent;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // ==============================
    // equals & hashCode
    // ==============================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HospitalisationResumeDTO)) return false;
        HospitalisationResumeDTO that = (HospitalisationResumeDTO) o;
        return Objects.equals(hospitalisationId, that.hospitalisationId) && Objects.equals(surveillanceSheetId, that.surveillanceSheetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hospitalisationId, surveillanceSheetId);
    }

    @Override
    public String toString() {
        return (
            "HospitalisationResumeDTO{" +
            "hospitalisationId=" +
            hospitalisationId +
            ", entryDate=" +
            entryDate +
            ", releaseDate=" +
            releaseDate +
            ", doctorName='" +
            doctorName +
            '\'' +
            ", service='" +
            service +
            '\'' +
            ", admissionReason='" +
            admissionReason +
            '\'' +
            ", entryDiagnosis='" +
            entryDiagnosis +
            '\'' +
            ", finalDiagnosis='" +
            finalDiagnosis +
            '\'' +
            ", status='" +
            status +
            '\'' +
            ", numberOfDays=" +
            numberOfDays +
            ", dailyRate=" +
            dailyRate +
            ", forfaitSejour=" +
            forfaitSejour +
            ", comfortFees=" +
            comfortFees +
            ", feeOverrun=" +
            feeOverrun +
            ", medsTotal=" +
            medsTotal +
            ", actsTotal=" +
            actsTotal +
            ", subtotal=" +
            subtotal +
            ", insuranceCoveragePercent=" +
            insuranceCoveragePercent +
            ", totalAmount=" +
            totalAmount +
            ", surveillanceSheetId=" +
            surveillanceSheetId +
            ", sheetDate=" +
            sheetDate +
            ", temperature=" +
            temperature +
            ", systolicBP=" +
            systolicBP +
            ", diastolicBP=" +
            diastolicBP +
            ", pulseRate=" +
            pulseRate +
            ", respirationRate=" +
            respirationRate +
            ", spo2=" +
            spo2 +
            ", nursingNotes='" +
            nursingNotes +
            '\'' +
            ", medicalObservations='" +
            medicalObservations +
            '\'' +
            ", actsPerformed='" +
            actsPerformed +
            '\'' +
            ", administeredMedication='" +
            administeredMedication +
            '\'' +
            '}'
        );
    }
}
