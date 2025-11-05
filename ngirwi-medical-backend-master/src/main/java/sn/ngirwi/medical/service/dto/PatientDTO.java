package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import sn.ngirwi.medical.domain.enumeration.BLOODTYPE;
import sn.ngirwi.medical.domain.enumeration.GENDER;
import sn.ngirwi.medical.domain.enumeration.MARITALSTATUS;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.Patient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthday;

    private String birthplace;

    @NotNull
    private GENDER gender;

    @NotNull
    private String adress;

    @NotNull
    private String phone;

    @NotNull
    private String cni;

    private String job;

    private BLOODTYPE bloodType;

    private MARITALSTATUS maritialStatus;

    private Instant dateCreated;

    private Instant dateUpdated;

    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BLOODTYPE getBloodType() {
        return bloodType;
    }

    public void setBloodType(BLOODTYPE bloodType) {
        this.bloodType = bloodType;
    }

    public MARITALSTATUS getMaritialStatus() {
        return maritialStatus;
    }

    public void setMaritialStatus(MARITALSTATUS maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDTO)) {
            return false;
        }

        PatientDTO patientDTO = (PatientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDTO{" +
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
