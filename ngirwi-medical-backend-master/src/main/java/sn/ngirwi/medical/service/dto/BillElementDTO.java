package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ngirwi.medical.domain.BillElement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillElementDTO implements Serializable {

    private Long id;

    private String name;

    private Double price;

    private Double percentage;

    private Integer quantity;

    private BillDTO bill;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BillDTO getBill() {
        return bill;
    }

    public void setBill(BillDTO bill) {
        this.bill = bill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillElementDTO)) {
            return false;
        }

        BillElementDTO billElementDTO = (BillElementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billElementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillElementDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", percentage=" + getPercentage() +
            ", quantity=" + getQuantity() +
            ", bill=" + getBill() +
            "}";
    }
}
