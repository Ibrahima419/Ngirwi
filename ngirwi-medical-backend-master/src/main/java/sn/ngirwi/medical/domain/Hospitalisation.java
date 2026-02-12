package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;

/**
 * A Hospitalisation.
 */
@Entity
@Table(name = "hospitalisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hospitalisation extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "entry_date")
    private Instant entryDate;

    @Column(name = "release_date")
    private Instant releaseDate;

    @NotBlank
    @Column(name = "doctor_name")
    private String doctorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private HospitalisationStatus status = HospitalisationStatus.STARTED;

    @Size(max = 512)
    @Column(name = "admission_reason", length = 512)
    private String admissionReason;

    /*    @Column(name = "medical_history") // Dossier Medical
    private String medicalHistory;*/

    @Column(name = "entry_diagnosis", length = 512)
    private String entryDiagnosis;

    @Column(name = "final_diagnosis", length = 512)
    private String finalDiagnosis;

    @Column(name = "service", length = 128)
    private String service;

    /** Tarif journalier hospitalier (FCFA) — saisi par l'utilisateur */
    @Column(name = "daily_rate", precision = 21, scale = 2)
    private BigDecimal dailyRate;

    /** Frais de confort (FCFA) — saisi par l'utilisateur */
    @Column(name = "comfort_fees", precision = 21, scale = 2)
    private BigDecimal comfortFees;

    /** Dépassements d'honoraires (FCFA) — saisi par l'utilisateur */
    @Column(name = "fee_overrun", precision = 21, scale = 2)
    private BigDecimal feeOverrun;

    /** Couverture assurance (%) — saisi par l'utilisateur, [0..100] */
    @Column(name = "insurance_coverage_percent", precision = 5, scale = 2)
    private BigDecimal insuranceCoveragePercent;

    /** Montant total facturable (FCFA) — rempli lors de la finalisation */
    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id")
    @JsonIgnoreProperties(value = { "patient", "consultations" }, allowSetters = true)
    private DossierMedical dossierMedical;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dossierMedical", "consultations" }, allowSetters = true)
    private Patient patient;

    @OneToMany(mappedBy = "hospitalisation", orphanRemoval = true, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hospitalisation" }, allowSetters = true)
    private Set<SurveillanceSheet> surveillanceSheets = new HashSet<>();

    public Hospitalisation() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Override
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
        if (this.entryDate != null && releaseDate != null && releaseDate.isBefore(this.entryDate)) {
            throw new IllegalArgumentException("La date de sortie ne peut pas être avant la date d'entrée");
        }
        this.releaseDate = releaseDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        if (doctorName == null || doctorName.isBlank()) {
            throw new IllegalArgumentException("Le nom du médecin est obligatoire");
        }
        this.doctorName = doctorName;
    }

    public HospitalisationStatus getStatus() {
        return status;
    }

    public void setStatus(HospitalisationStatus status) {
        if (this.status == HospitalisationStatus.DONE && status == HospitalisationStatus.STARTED) {
            throw new IllegalStateException("Impossible de rouvrir une hospitalisation clôturée");
        }
        this.status = status;
    }

    public String getAdmissionReason() {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason) {
        this.admissionReason = admissionReason;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        this.dossierMedical = dossierMedical;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Un patient doit être associé à l'hospitalisation");
        }
        this.patient = patient;
    }

    public Set<SurveillanceSheet> getSurveillanceSheets() {
        return surveillanceSheets;
    }

    public void setSurveillanceSheets(Set<SurveillanceSheet> surveillanceSheets) {
        if (this.surveillanceSheets != null) {
            this.surveillanceSheets.forEach(i -> i.setHospitalisation(null));
        }
        if (surveillanceSheets != null) {
            surveillanceSheets.forEach(i -> i.setHospitalisation(this));
        }
        this.surveillanceSheets = surveillanceSheets;
    }

    public void addSurveillanceSheet(SurveillanceSheet surveillanceSheet) {
        this.surveillanceSheets.add(surveillanceSheet);
        surveillanceSheet.setHospitalisation(this);
    }

    public void removeSurveillanceSheet(SurveillanceSheet surveillanceSheet) {
        this.surveillanceSheets.remove(surveillanceSheet);
        surveillanceSheet.setHospitalisation(null);
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
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

    // ============================
    // Builder Methods
    // ============================

    public Hospitalisation id(Long id) {
        this.setId(id);
        return this;
    }

    public Hospitalisation entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public Hospitalisation releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public Hospitalisation admissionReason(String admissionReason) {
        this.setAdmissionReason(admissionReason);
        return this;
    }

    public Hospitalisation dossierMedical(DossierMedical dossierMedical) {
        this.setDossierMedical(dossierMedical);
        return this;
    }

    public Hospitalisation entryDiagnosis(String entryDiagnosis) {
        this.setEntryDiagnosis(entryDiagnosis);
        return this;
    }

    public Hospitalisation finalDiagnosis(String finalDiagnosis) {
        this.setFinalDiagnosis(finalDiagnosis);
        return this;
    }

    public Hospitalisation service(String service) {
        this.setService(service);
        return this;
    }

    public Hospitalisation doctorName(String doctorName) {
        this.setDoctorName(doctorName);
        return this;
    }

    public Hospitalisation status(HospitalisationStatus status) {
        this.setStatus(status);
        return this;
    }

    public Hospitalisation patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Hospitalisation surveillanceSheets(Set<SurveillanceSheet> surveillanceSheets) {
        this.setSurveillanceSheets(surveillanceSheets);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospitalisation)) {
            return false;
        }
        return id != null && id.equals(((Hospitalisation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "Hospitalisation{" +
            "id=" + id +
            ", entryDate=" + entryDate +
            ", releaseDate=" + releaseDate +
            ", doctorName='" + doctorName + '\'' +
            ", status=" + status +
            ", admissionReason='" + admissionReason + '\'' +
            ", dossierMedicalId=" + (dossierMedical != null ? dossierMedical.getId() : null) +
            ", entryDiagnosis='" + entryDiagnosis + '\'' +
            ", finalDiagnosis='" + finalDiagnosis + '\'' +
            ", service='" + service + '\'' +
            ", patientId=" + (patient != null ? patient.getId() : null) +
            ", surveillanceSheetsCount=" + (surveillanceSheets == null ? 0 : surveillanceSheets.size()) +
            '}';
    }
}
