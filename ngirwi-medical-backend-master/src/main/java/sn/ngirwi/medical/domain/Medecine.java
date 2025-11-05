package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medecine.
 */
@Entity
@Table(name = "medecine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medecine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "frequency")
    private Double frequency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "consultation", "medecines" }, allowSetters = true)
    private Prescription ordonance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medecine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Medecine name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Medecine duration(Long duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getFrequency() {
        return this.frequency;
    }

    public Medecine frequency(Double frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public Prescription getOrdonance() {
        return this.ordonance;
    }

    public void setOrdonance(Prescription prescription) {
        this.ordonance = prescription;
    }

    public Medecine ordonance(Prescription prescription) {
        this.setOrdonance(prescription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medecine)) {
            return false;
        }
        return id != null && id.equals(((Medecine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medecine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", duration=" + getDuration() +
            ", frequency=" + getFrequency() +
            "}";
    }
}
