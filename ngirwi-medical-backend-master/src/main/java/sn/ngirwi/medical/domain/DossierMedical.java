package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DossierMedical.
 */
@Entity
@Table(name = "dossier_medical")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierMedical implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "motif_consultation", nullable = false)
    private String motifConsultation;

    @NotNull
    @Column(name = "histoire_maladie", nullable = false)
    private String histoireMaladie;

    @NotNull
    @Column(name = "terrain", nullable = false)
    private String terrain;

    @NotNull
    @Column(name = "antecedants_personnels", nullable = false)
    private String antecedantsPersonnels;

    @NotNull
    @Column(name = "antecedants_chirurgicaux", nullable = false)
    private String antecedantsChirurgicaux;

    @NotNull
    @Column(name = "antecedants_familiaux", nullable = false)
    private String antecedantsFamiliaux;

    @Column(name = "gyneco_obstretrique")
    private String gynecoObstretrique;

    @NotNull
    @Column(name = "syndromique", nullable = false)
    private String syndromique;

    @Column(name = "dad")
    private String dad;

    @Column(name = "mom")
    private String mom;

    @Column(name = "siblings")
    private String siblings;

    @Column(name = "descendants")
    private String descendants;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @Column(name = "author")
    private String author;

    @JsonIgnoreProperties(value = { "dossierMedical", "consultations" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DossierMedical id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotifConsultation() {
        return this.motifConsultation;
    }

    public DossierMedical motifConsultation(String motifConsultation) {
        this.setMotifConsultation(motifConsultation);
        return this;
    }

    public void setMotifConsultation(String motifConsultation) {
        this.motifConsultation = motifConsultation;
    }

    public String getHistoireMaladie() {
        return this.histoireMaladie;
    }

    public DossierMedical histoireMaladie(String histoireMaladie) {
        this.setHistoireMaladie(histoireMaladie);
        return this;
    }

    public void setHistoireMaladie(String histoireMaladie) {
        this.histoireMaladie = histoireMaladie;
    }

    public String getTerrain() {
        return this.terrain;
    }

    public DossierMedical terrain(String terrain) {
        this.setTerrain(terrain);
        return this;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getAntecedantsPersonnels() {
        return this.antecedantsPersonnels;
    }

    public DossierMedical antecedantsPersonnels(String antecedantsPersonnels) {
        this.setAntecedantsPersonnels(antecedantsPersonnels);
        return this;
    }

    public void setAntecedantsPersonnels(String antecedantsPersonnels) {
        this.antecedantsPersonnels = antecedantsPersonnels;
    }

    public String getAntecedantsChirurgicaux() {
        return this.antecedantsChirurgicaux;
    }

    public DossierMedical antecedantsChirurgicaux(String antecedantsChirurgicaux) {
        this.setAntecedantsChirurgicaux(antecedantsChirurgicaux);
        return this;
    }

    public void setAntecedantsChirurgicaux(String antecedantsChirurgicaux) {
        this.antecedantsChirurgicaux = antecedantsChirurgicaux;
    }

    public String getAntecedantsFamiliaux() {
        return this.antecedantsFamiliaux;
    }

    public DossierMedical antecedantsFamiliaux(String antecedantsFamiliaux) {
        this.setAntecedantsFamiliaux(antecedantsFamiliaux);
        return this;
    }

    public void setAntecedantsFamiliaux(String antecedantsFamiliaux) {
        this.antecedantsFamiliaux = antecedantsFamiliaux;
    }

    public String getGynecoObstretrique() {
        return this.gynecoObstretrique;
    }

    public DossierMedical gynecoObstretrique(String gynecoObstretrique) {
        this.setGynecoObstretrique(gynecoObstretrique);
        return this;
    }

    public void setGynecoObstretrique(String gynecoObstretrique) {
        this.gynecoObstretrique = gynecoObstretrique;
    }

    public String getSyndromique() {
        return this.syndromique;
    }

    public DossierMedical syndromique(String syndromique) {
        this.setSyndromique(syndromique);
        return this;
    }

    public void setSyndromique(String syndromique) {
        this.syndromique = syndromique;
    }

    public String getDad() {
        return this.dad;
    }

    public DossierMedical dad(String dad) {
        this.setDad(dad);
        return this;
    }

    public void setDad(String dad) {
        this.dad = dad;
    }

    public String getMom() {
        return this.mom;
    }

    public DossierMedical mom(String mom) {
        this.setMom(mom);
        return this;
    }

    public void setMom(String mom) {
        this.mom = mom;
    }

    public String getSiblings() {
        return this.siblings;
    }

    public DossierMedical siblings(String siblings) {
        this.setSiblings(siblings);
        return this;
    }

    public void setSiblings(String siblings) {
        this.siblings = siblings;
    }

    public String getDescendants() {
        return this.descendants;
    }

    public DossierMedical descendants(String descendants) {
        this.setDescendants(descendants);
        return this;
    }

    public void setDescendants(String descendants) {
        this.descendants = descendants;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public DossierMedical dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return this.dateUpdated;
    }

    public DossierMedical dateUpdated(Instant dateUpdated) {
        this.setDateUpdated(dateUpdated);
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAuthor() {
        return this.author;
    }

    public DossierMedical author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DossierMedical patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierMedical)) {
            return false;
        }
        return id != null && id.equals(((DossierMedical) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierMedical{" +
            "id=" + getId() +
            ", motifConsultation='" + getMotifConsultation() + "'" +
            ", histoireMaladie='" + getHistoireMaladie() + "'" +
            ", terrain='" + getTerrain() + "'" +
            ", antecedantsPersonnels='" + getAntecedantsPersonnels() + "'" +
            ", antecedantsChirurgicaux='" + getAntecedantsChirurgicaux() + "'" +
            ", antecedantsFamiliaux='" + getAntecedantsFamiliaux() + "'" +
            ", gynecoObstretrique='" + getGynecoObstretrique() + "'" +
            ", syndromique='" + getSyndromique() + "'" +
            ", dad='" + getDad() + "'" +
            ", mom='" + getMom() + "'" +
            ", siblings='" + getSiblings() + "'" +
            ", descendants='" + getDescendants() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
}
