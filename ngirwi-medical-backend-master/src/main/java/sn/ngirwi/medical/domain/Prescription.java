package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prescription.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "author")
    private String author;

    @JsonIgnoreProperties(value = { "patient", "ordonance" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Consultation consultation;

    @OneToMany(mappedBy = "ordonance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordonance" }, allowSetters = true)
    private Set<Medecine> medecines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prescription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Prescription creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return this.author;
    }

    public Prescription author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Consultation getConsultation() {
        return this.consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Prescription consultation(Consultation consultation) {
        this.setConsultation(consultation);
        return this;
    }

    public Set<Medecine> getMedecines() {
        return this.medecines;
    }

    public void setMedecines(Set<Medecine> medecines) {
        if (this.medecines != null) {
            this.medecines.forEach(i -> i.setOrdonance(null));
        }
        if (medecines != null) {
            medecines.forEach(i -> i.setOrdonance(this));
        }
        this.medecines = medecines;
    }

    public Prescription medecines(Set<Medecine> medecines) {
        this.setMedecines(medecines);
        return this;
    }

    public Prescription addMedecine(Medecine medecine) {
        this.medecines.add(medecine);
        medecine.setOrdonance(this);
        return this;
    }

    public Prescription removeMedecine(Medecine medecine) {
        this.medecines.remove(medecine);
        medecine.setOrdonance(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prescription)) {
            return false;
        }
        return id != null && id.equals(((Prescription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prescription{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
}
