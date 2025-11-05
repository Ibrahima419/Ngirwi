package sn.ngirwi.medical.service.model;

import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;

public class HospitalisationForm {

    private String entryDate;
    private String releaseDate;
    private String doctorName;
    private HospitalisationStatus status;
    private String ta;
    private String temperature;
    private String pulseRate;
    private String respiratoryFrequency;
    private String recolorationTime;
    private String glasgow;
    private String gravityClass;
    private String horaryDiuresis;
    private String spo2;
    private String treatment;
    private String healthEvolution;
    private String sheetDate;

    private Patient patient;

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
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

    public String getTa() {
        return ta;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(String pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getRespiratoryFrequency() {
        return respiratoryFrequency;
    }

    public void setRespiratoryFrequency(String respiratoryFrequency) {
        this.respiratoryFrequency = respiratoryFrequency;
    }

    public String getRecolorationTime() {
        return recolorationTime;
    }

    public void setRecolorationTime(String recolorationTime) {
        this.recolorationTime = recolorationTime;
    }

    public String getGlasgow() {
        return glasgow;
    }

    public void setGlasgow(String glasgow) {
        this.glasgow = glasgow;
    }

    public String getGravityClass() {
        return gravityClass;
    }

    public void setGravityClass(String gravityClass) {
        this.gravityClass = gravityClass;
    }

    public String getHoraryDiuresis() {
        return horaryDiuresis;
    }

    public void setHoraryDiuresis(String horaryDiuresis) {
        this.horaryDiuresis = horaryDiuresis;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getHealthEvolution() {
        return healthEvolution;
    }

    public void setHealthEvolution(String healthEvolution) {
        this.healthEvolution = healthEvolution;
    }

    public String getSheetDate() {
        return sheetDate;
    }

    public void setSheetDate(String sheetDate) {
        this.sheetDate = sheetDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return (
            "HospitalisationForm{" +
            "entryDate='" +
            entryDate +
            '\'' +
            ", releaseDate='" +
            releaseDate +
            '\'' +
            ", doctorName='" +
            doctorName +
            '\'' +
            ", status=" +
            status +
            ", ta='" +
            ta +
            '\'' +
            ", temperature='" +
            temperature +
            '\'' +
            ", pulseRate='" +
            pulseRate +
            '\'' +
            ", respiratoryFrequency='" +
            respiratoryFrequency +
            '\'' +
            ", recolorationTime='" +
            recolorationTime +
            '\'' +
            ", glasgow='" +
            glasgow +
            '\'' +
            ", gravityClass='" +
            gravityClass +
            '\'' +
            ", horaryDiuresis='" +
            horaryDiuresis +
            '\'' +
            ", spo2='" +
            spo2 +
            '\'' +
            ", treatment='" +
            treatment +
            '\'' +
            ", healthEvolution='" +
            healthEvolution +
            '\'' +
            ", sheetDate='" +
            sheetDate +
            '\'' +
            ", patient=" +
            patient +
            '}'
        );
    }
}
