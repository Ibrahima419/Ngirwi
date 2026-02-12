package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SurveillanceSheet.
 */
@Entity
@Table(
    name = "surveillance_sheet",
    uniqueConstraints = { @UniqueConstraint(name = "ux_fj_hosp_date", columnNames = { "hospitalisation_id", "sheet_date" }) }
) // Pour une même hospitalisation (hospitalisation_id), il ne peut y avoir qu’une seule fiche journalière (sheet_date) à une date donnée.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurveillanceSheet extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sheet_date", nullable = false)
    private LocalDate sheetDate;

    @DecimalMin(value = "30.0", message = "La température doit être d'au moins 30°C")
    @DecimalMax(value = "45.0", message = "La température ne peut pas dépasser 45°C")
    @Digits(integer = 2, fraction = 1, message = "La température doit avoir au plus 2 chiffres avant la virgule et 1 après")
    @Column(name = "temperature", precision = 3, scale = 1)
    private BigDecimal temperature;

    /** TA systolique (mmHg) */
    @Min(0)
    @Max(300)
    @Column(name = "systolic_bp")
    private Integer systolicBP;

    /** TA diastolique (mmHg) */
    @Min(0)
    @Max(200)
    @Column(name = "diastolic_bp")
    private Integer diastolicBP;

    /** Pouls (bpm) */
    @Min(0)
    @Max(300)
    @Column(name = "pulse_rate")
    private Integer pulseRate;

    /** Fréquence respiratoire (rpm) */
    @Min(0)
    @Max(120)
    @Column(name = "respiration_rate")
    private Integer respirationRate;

    /** Saturation O2 (%) */
    @Min(0)
    @Max(100)
    @Column(name = "spo2")
    private Integer spo2;

    /** Notes soignantes */

    @Column(name = "nursing_notes")
    private String nursingNotes;

    /** Observations médicales */

    @Column(name = "medical_observations")
    private String medicalObservations;

    /** Actes réalisés */

    @Column(name = "acts_performed")
    private String actsPerformed;

    /** Médicaments administrés */
    @Lob
    @Column(name = "administered_medication") // Medecine
    private String administeredMedication;

    /** Médicaments administrés (journalier) — saisis par l'utilisateur */
    @ElementCollection
    @CollectionTable(name = "surveillance_sheet_medications", joinColumns = @JoinColumn(name = "surveillance_sheet_id"))
    private List<MedicationEntry> medications = new ArrayList<>();

    /** Actes/soins réalisés (journalier) — saisis par l'utilisateur */
    @ElementCollection
    @CollectionTable(name = "surveillance_sheet_acts", joinColumns = @JoinColumn(name = "surveillance_sheet_id"))
    private List<ActEntry> acts = new ArrayList<>();

    /** Total journalier (médicaments + actes) calculé côté backend */
    @Transient
    public java.math.BigDecimal getDailyTotal() {
        java.math.BigDecimal meds = medications
            .stream()
            .map(MedicationEntry::getTotal)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        java.math.BigDecimal actz = acts.stream().map(ActEntry::getTotal).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        return meds.add(actz);
    }

    public List<MedicationEntry> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationEntry> medications) {
        this.medications = medications;
    }

    public List<ActEntry> getActs() {
        return acts;
    }

    public void setActs(List<ActEntry> acts) {
        this.acts = acts;
    }

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalisation_id", nullable = false)
    @JsonIgnoreProperties(value = { "patient", "surveillanceSheets" }, allowSetters = true)
    private Hospitalisation hospitalisation;

    @OneToMany(mappedBy = "surveillanceSheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "surveillanceSheet" }, allowSetters = true)
    private java.util.List<MiniConsultation> miniConsultations = new java.util.ArrayList<>();

    // Removed: legacy ManyToMany prescriptions link (out of scope for hospitalisation)

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Override
    public Long getId() {
        return id;
    }

    public SurveillanceSheet id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSheetDate() {
        return sheetDate;
    }

    public SurveillanceSheet sheetDate(LocalDate sheetDate) {
        this.sheetDate = sheetDate;
        return this;
    }

    public void setSheetDate(LocalDate sheetDate) {
        this.sheetDate = sheetDate;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public SurveillanceSheet temperature(BigDecimal temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getSystolicBP() {
        return systolicBP;
    }

    public SurveillanceSheet systolicBP(Integer systolicBP) {
        this.systolicBP = systolicBP;
        return this;
    }

    public void setSystolicBP(Integer systolicBP) {
        this.systolicBP = systolicBP;
    }

    public Integer getDiastolicBP() {
        return diastolicBP;
    }

    public SurveillanceSheet diastolicBP(Integer diastolicBP) {
        this.diastolicBP = diastolicBP;
        return this;
    }

    public void setDiastolicBP(Integer diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public SurveillanceSheet pulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
        return this;
    }

    public void setPulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
    }

    public Integer getRespirationRate() {
        return respirationRate;
    }

    public SurveillanceSheet respirationRate(Integer respirationRate) {
        this.respirationRate = respirationRate;
        return this;
    }

    public void setRespirationRate(Integer respirationRate) {
        this.respirationRate = respirationRate;
    }

    public Integer getSpo2() {
        return spo2;
    }

    public SurveillanceSheet spo2(Integer spo2) {
        this.spo2 = spo2;
        return this;
    }

    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }

    public String getNursingNotes() {
        return nursingNotes;
    }

    public SurveillanceSheet nursingNotes(String nursingNotes) {
        this.nursingNotes = nursingNotes;
        return this;
    }

    public void setNursingNotes(String nursingNotes) {
        this.nursingNotes = nursingNotes;
    }

    public String getMedicalObservations() {
        return medicalObservations;
    }

    public SurveillanceSheet medicalObservations(String medicalObservations) {
        this.medicalObservations = medicalObservations;
        return this;
    }

    public void setMedicalObservations(String medicalObservations) {
        this.medicalObservations = medicalObservations;
    }

    public String getActsPerformed() {
        return actsPerformed;
    }

    public SurveillanceSheet actsPerformed(String actsPerformed) {
        this.actsPerformed = actsPerformed;
        return this;
    }

    public void setActsPerformed(String actsPerformed) {
        this.actsPerformed = actsPerformed;
    }

    public String getAdministeredMedication() {
        return administeredMedication;
    }

    public SurveillanceSheet administeredMedication(String administeredMedication) {
        this.administeredMedication = administeredMedication;
        return this;
    }

    public void setAdministeredMedication(String administeredMedication) {
        this.administeredMedication = administeredMedication;
    }

    public Hospitalisation getHospitalisation() {
        return hospitalisation;
    }

    public SurveillanceSheet hospitalisation(Hospitalisation hospitalisation) {
        this.hospitalisation = hospitalisation;
        return this;
    }

    public void setHospitalisation(Hospitalisation hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    public java.util.List<MiniConsultation> getMiniConsultations() {
        return miniConsultations;
    }

    public void setMiniConsultations(java.util.List<MiniConsultation> miniConsultations) {
        this.miniConsultations = miniConsultations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveillanceSheet)) return false;
        return id != null && id.equals(((SurveillanceSheet) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "SurveillanceSheet{" +
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
            '}'
        );
    }
}
