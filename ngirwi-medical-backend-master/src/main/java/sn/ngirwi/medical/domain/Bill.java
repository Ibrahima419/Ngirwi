package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "author")
    private String author;

    @Column(name = "insurance")
    private String insurance;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "ipm")
    private String ipm;

    @Column(name = "total", precision = 21, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dossierMedical", "consultations" }, allowSetters = true)
    private Patient patient;

    @OneToMany(mappedBy = "bill")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bill" }, allowSetters = true)
    private Set<BillElement> billElements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Bill date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getAuthor() {
        return this.author;
    }

    public Bill author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInsurance() {
        return this.insurance;
    }

    public Bill insurance(String insurance) {
        this.setInsurance(insurance);
        return this;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getDesc() {
        return this.desc;
    }

    public Bill desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIpm() {
        return this.ipm;
    }

    public Bill ipm(String ipm) {
        this.setIpm(ipm);
        return this;
    }

    public void setIpm(String ipm) {
        this.ipm = ipm;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Bill total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Bill patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public Set<BillElement> getBillElements() {
        return this.billElements;
    }

    public void setBillElements(Set<BillElement> billElements) {
        if (this.billElements != null) {
            this.billElements.forEach(i -> i.setBill(null));
        }
        if (billElements != null) {
            billElements.forEach(i -> i.setBill(this));
        }
        this.billElements = billElements;
    }

    public Bill billElements(Set<BillElement> billElements) {
        this.setBillElements(billElements);
        return this;
    }

    public Bill addBillElement(BillElement billElement) {
        this.billElements.add(billElement);
        billElement.setBill(this);
        return this;
    }

    public Bill removeBillElement(BillElement billElement) {
        this.billElements.remove(billElement);
        billElement.setBill(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", author='" + getAuthor() + "'" +
            ", insurance='" + getInsurance() + "'" +
            ", desc='" + getDesc() + "'" +
            ", ipm='" + getIpm() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
