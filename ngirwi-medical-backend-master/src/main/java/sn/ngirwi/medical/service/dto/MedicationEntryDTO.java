package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MedicationEntryDTO implements Serializable {

    private String name;
    private BigDecimal unitPrice;
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


