package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// include collections for medications and acts to match frontend

public class SurveillanceSheetDTO implements Serializable {

    private Long id;

    private LocalDate sheetDate;

    private BigDecimal temperature;

    private Integer systolicBP;

    private Integer diastolicBP;

    private Integer pulseRate;

    private Integer respirationRate;

    private Integer spo2;

    private String nursingNotes;

    private String medicalObservations;

    private String actsPerformed;

    private String administeredMedication;

    private Long hospitalisationId;

    // User-entered line items
    private List<MedicationEntryDTO> medications;
    private List<ActEntryDTO> acts;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSheetDate() {
        return sheetDate;
    }

    public void setSheetDate(LocalDate sheetDate) {
        this.sheetDate = sheetDate;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
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

    public Long getHospitalisationId() {
        return hospitalisationId;
    }

    public void setHospitalisationId(Long hospitalisationId) {
        this.hospitalisationId = hospitalisationId;
    }

    public List<MedicationEntryDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationEntryDTO> medications) {
        this.medications = medications;
    }

    public List<ActEntryDTO> getActs() {
        return acts;
    }

    public void setActs(List<ActEntryDTO> acts) {
        this.acts = acts;
    }

    // removed miniConsultationId & prescriptionIds

    @Override
    public String toString() {
        return (
            "SurveillanceSheetDTO{" +
            "id=" +
            id +
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
            ", hospitalisationId=" +
            hospitalisationId +
            "" +
            '}'
        );
    }
}
