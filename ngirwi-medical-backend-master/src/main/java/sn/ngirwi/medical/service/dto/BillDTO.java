package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.Bill} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillDTO implements Serializable {

    private Long id;

    private Instant date;

    private String author;

    private String insurance;

    @Lob
    private String desc;

    private String ipm;

    private BigDecimal total;

    private PatientDTO patient;

    private Set<BillElementDTO> billElements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIpm() {
        return ipm;
    }

    public void setIpm(String ipm) {
        this.ipm = ipm;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public Set<BillElementDTO> getBillElements() {
        return billElements;
    }

    public void setBillElements(Set<BillElementDTO> billElements) {
        this.billElements = billElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillDTO)) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "BillDTO{" +
            "id=" +
            id +
            ", date=" +
            date +
            ", author='" +
            author +
            '\'' +
            ", insurance='" +
            insurance +
            '\'' +
            ", desc='" +
            desc +
            '\'' +
            ", ipm='" +
            ipm +
            '\'' +
            ", total=" +
            total +
            ", patient=" +
            patient +
            ", elements=" +
            billElements +
            '}'
        );
    }
}
