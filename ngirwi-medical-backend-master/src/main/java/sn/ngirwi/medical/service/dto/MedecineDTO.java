package sn.ngirwi.medical.service.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.Medecine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MedecineDTO implements Serializable {

    private Long id;

    private String name;

    private Long duration;

    private Double frequency;

    private PrescriptionDTO ordonance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public PrescriptionDTO getOrdonance() {
        return ordonance;
    }

    public void setOrdonance(PrescriptionDTO ordonance) {
        this.ordonance = ordonance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedecineDTO)) {
            return false;
        }

        MedecineDTO medecineDTO = (MedecineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medecineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedecineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", duration=" + getDuration() +
            ", frequency=" + getFrequency() +
            ", ordonance=" + getOrdonance() +
            "}";
    }
}
