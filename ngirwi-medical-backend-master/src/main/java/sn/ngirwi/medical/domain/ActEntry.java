package sn.ngirwi.medical.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.*;

@Embeddable
public class ActEntry implements Serializable {

    @NotBlank
    @Column(name = "act_name", length = 256, nullable = false)
    private String nom;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "act_unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixUnitaire;

    @NotNull
    @Min(1)
    @Column(name = "act_quantity", nullable = false)
    private Integer quantite;

    // total non stocké en base : calculé à la volée
    @Transient
    public BigDecimal getTotal() {
        if (prixUnitaire == null || quantite == null) return BigDecimal.ZERO;
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    // getters/setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActEntry)) return false;
        ActEntry that = (ActEntry) o;
        return Objects.equals(nom, that.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}
