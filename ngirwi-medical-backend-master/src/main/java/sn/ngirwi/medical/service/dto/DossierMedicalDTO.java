package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.DossierMedical} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierMedicalDTO implements Serializable {

    private Long id;

    @NotNull
    private String motifConsultation;

    @NotNull
    private String histoireMaladie;

    @NotNull
    private String terrain;

    @NotNull
    private String antecedantsPersonnels;

    @NotNull
    private String antecedantsChirurgicaux;

    @NotNull
    private String antecedantsFamiliaux;

    private String gynecoObstretrique;

    @NotNull
    private String syndromique;

    private String dad;

    private String mom;

    private String siblings;

    private String descendants;

    private Instant dateCreated;

    private Instant dateUpdated;

    private String author;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotifConsultation() {
        return motifConsultation;
    }

    public void setMotifConsultation(String motifConsultation) {
        this.motifConsultation = motifConsultation;
    }

    public String getHistoireMaladie() {
        return histoireMaladie;
    }

    public void setHistoireMaladie(String histoireMaladie) {
        this.histoireMaladie = histoireMaladie;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getAntecedantsPersonnels() {
        return antecedantsPersonnels;
    }

    public void setAntecedantsPersonnels(String antecedantsPersonnels) {
        this.antecedantsPersonnels = antecedantsPersonnels;
    }

    public String getAntecedantsChirurgicaux() {
        return antecedantsChirurgicaux;
    }

    public void setAntecedantsChirurgicaux(String antecedantsChirurgicaux) {
        this.antecedantsChirurgicaux = antecedantsChirurgicaux;
    }

    public String getAntecedantsFamiliaux() {
        return antecedantsFamiliaux;
    }

    public void setAntecedantsFamiliaux(String antecedantsFamiliaux) {
        this.antecedantsFamiliaux = antecedantsFamiliaux;
    }

    public String getGynecoObstretrique() {
        return gynecoObstretrique;
    }

    public void setGynecoObstretrique(String gynecoObstretrique) {
        this.gynecoObstretrique = gynecoObstretrique;
    }

    public String getSyndromique() {
        return syndromique;
    }

    public void setSyndromique(String syndromique) {
        this.syndromique = syndromique;
    }

    public String getDad() {
        return dad;
    }

    public void setDad(String dad) {
        this.dad = dad;
    }

    public String getMom() {
        return mom;
    }

    public void setMom(String mom) {
        this.mom = mom;
    }

    public String getSiblings() {
        return siblings;
    }

    public void setSiblings(String siblings) {
        this.siblings = siblings;
    }

    public String getDescendants() {
        return descendants;
    }

    public void setDescendants(String descendants) {
        this.descendants = descendants;
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

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierMedicalDTO)) {
            return false;
        }

        DossierMedicalDTO dossierMedicalDTO = (DossierMedicalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dossierMedicalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierMedicalDTO{" +
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
            ", patient=" + getPatient() +
            "}";
    }
}
