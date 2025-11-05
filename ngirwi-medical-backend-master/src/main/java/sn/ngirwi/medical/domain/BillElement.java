package sn.ngirwi.medical.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillElement.
 */
@Entity
@Table(name = "bill_element")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "patient", "billElements" }, allowSetters = true)
    private Bill bill;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillElement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BillElement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public BillElement price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public BillElement percentage(Double percentage) {
        this.setPercentage(percentage);
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BillElement quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Bill getBill() {
        return this.bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public BillElement bill(Bill bill) {
        this.setBill(bill);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillElement)) {
            return false;
        }
        return id != null && id.equals(((BillElement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillElement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", percentage=" + getPercentage() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
