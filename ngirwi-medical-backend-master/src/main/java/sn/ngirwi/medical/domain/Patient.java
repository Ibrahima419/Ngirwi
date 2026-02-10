package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ngirwi.medical.domain.enumeration.BLOODTYPE;
import sn.ngirwi.medical.domain.enumeration.GENDER;
import sn.ngirwi.medical.domain.enumeration.MARITALSTATUS;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "birthplace")
    private String birthplace;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GENDER gender;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Pattern(regexp = "^[0-9]{13}$", message = "Le NIN doit être exactement 13 chiffres")
    @Column(name = "cni", nullable = false, unique = true)
    private String cni;

    @Column(name = "job")
    private String job;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BLOODTYPE bloodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "maritial_status")
    private MARITALSTATUS maritialStatus;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @Column(name = "author")
    private String author;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @JsonIgnoreProperties(value = { "patient" }, allowSetters = true)
    @OneToOne(mappedBy = "patient", fetch = FetchType.EAGER)
    private DossierMedical dossierMedical;

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "ordonance" }, allowSetters = true)
    private Set<Consultation> consultations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Patient firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Patient lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public Patient birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return this.birthplace;
    }

    public Patient birthplace(String birthplace) {
        this.setBirthplace(birthplace);
        return this;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public GENDER getGender() {
        return this.gender;
    }

    public Patient gender(GENDER gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getAdress() {
        return this.adress;
    }

    public Patient adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return this.phone;
    }

    public Patient phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCni() {
        return this.cni;
    }

    public Patient cni(String cni) {
        this.setCni(cni);
        return this;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getJob() {
        return this.job;
    }

    public Patient job(String job) {
        this.setJob(job);
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BLOODTYPE getBloodType() {
        return this.bloodType;
    }

    public Patient bloodType(BLOODTYPE bloodType) {
        this.setBloodType(bloodType);
        return this;
    }

    public void setBloodType(BLOODTYPE bloodType) {
        this.bloodType = bloodType;
    }

    public MARITALSTATUS getMaritialStatus() {
        return this.maritialStatus;
    }

    public Patient maritialStatus(MARITALSTATUS maritialStatus) {
        this.setMaritialStatus(maritialStatus);
        return this;
    }

    public void setMaritialStatus(MARITALSTATUS maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Patient dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return this.dateUpdated;
    }

    public Patient dateUpdated(Instant dateUpdated) {
        this.setDateUpdated(dateUpdated);
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAuthor() {
        return this.author;
    }

    public Patient author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DossierMedical getDossierMedical() {
        return this.dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        if (this.dossierMedical != null) {
            this.dossierMedical.setPatient(null);
        }
        if (dossierMedical != null) {
            dossierMedical.setPatient(this);
        }
        this.dossierMedical = dossierMedical;
    }

    public Patient dossierMedical(DossierMedical dossierMedical) {
        this.setDossierMedical(dossierMedical);
        return this;
    }

    public Set<Consultation> getConsultations() {
        return this.consultations;
    }

    public void setConsultations(Set<Consultation> consultations) {
        if (this.consultations != null) {
            this.consultations.forEach(i -> i.setPatient(null));
        }
        if (consultations != null) {
            consultations.forEach(i -> i.setPatient(this));
        }
        this.consultations = consultations;
    }

    public Patient consultations(Set<Consultation> consultations) {
        this.setConsultations(consultations);
        return this;
    }

    public Patient addConsultations(Consultation consultation) {
        this.consultations.add(consultation);
        consultation.setPatient(this);
        return this;
    }

    public Patient removeConsultations(Consultation consultation) {
        this.consultations.remove(consultation);
        consultation.setPatient(null);
        return this;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", birthplace='" + getBirthplace() + "'" +
            ", gender='" + getGender() + "'" +
            ", adress='" + getAdress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", cni='" + getCni() + "'" +
            ", job='" + getJob() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", maritialStatus='" + getMaritialStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
}
