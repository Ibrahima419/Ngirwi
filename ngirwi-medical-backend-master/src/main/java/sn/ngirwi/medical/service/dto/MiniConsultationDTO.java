package sn.ngirwi.medical.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MiniConsultationDTO implements Serializable {

    private Long id;

    private String summary;

    private String diagnosis;

    private BigDecimal price;

    private Long surveillanceSheetId;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSurveillanceSheetId() {
        return surveillanceSheetId;
    }

    public void setSurveillanceSheetId(Long surveillanceSheetId) {
        this.surveillanceSheetId = surveillanceSheetId;
    }

    @Override
    public String toString() {
        return (
            "MiniConsultationDTO{" +
            "id=" +
            id +
            ", summary='" +
            summary +
            '\'' +
            ", diagnosis='" +
            diagnosis +
            '\'' +
            ", price=" +
            price +
            ", surveillanceSheetId=" +
            surveillanceSheetId +
            '}'
        );
    }
}
