package sn.ngirwi.medical.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "mini_consultation")
public class MiniConsultation extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Column(name = "diagnosis", length = 512)
    private String diagnosis;

    /**
     * Relation ManyToOne : plusieurs mini-consultations peuvent être liées à une fiche.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveillance_sheet_id")
    private SurveillanceSheet surveillanceSheet;

    /** Prix de la mini-consultation (FCFA) */
    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    public MiniConsultation() {}

    // Getters / setters
    @Override
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

    public SurveillanceSheet getSurveillanceSheet() {
        return surveillanceSheet;
    }

    public void setSurveillanceSheet(SurveillanceSheet surveillanceSheet) {
        this.surveillanceSheet = surveillanceSheet;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
