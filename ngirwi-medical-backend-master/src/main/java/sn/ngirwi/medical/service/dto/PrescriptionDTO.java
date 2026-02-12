package sn.ngirwi.medical.service.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import sn.ngirwi.medical.service.model.PrescriptionForm;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.Prescription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PrescriptionDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private String author;

    private ConsultationDTO consultation;

    private Set<PrescriptionForm> medecines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ConsultationDTO getConsultation() {
        return consultation;
    }

    public void setConsultation(ConsultationDTO consultation) {
        this.consultation = consultation;
    }

    public Set<PrescriptionForm> getMedecines() {
        return medecines;
    }

    public void setMedecines(Set<PrescriptionForm> medecines) {
        this.medecines = medecines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrescriptionDTO)) {
            return false;
        }

        PrescriptionDTO prescriptionDTO = (PrescriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prescriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "PrescriptionDTO{" +
            "id=" +
            id +
            ", creationDate=" +
            creationDate +
            ", author='" +
            author +
            '\'' +
            ", consultation=" +
            consultation +
            ", medecines=" +
            medecines +
            '}'
        );
    }
}
