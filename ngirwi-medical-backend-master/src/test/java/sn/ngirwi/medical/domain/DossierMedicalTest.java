package sn.ngirwi.medical.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class DossierMedicalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierMedical.class);
        DossierMedical dossierMedical1 = new DossierMedical();
        dossierMedical1.setId(1L);
        DossierMedical dossierMedical2 = new DossierMedical();
        dossierMedical2.setId(dossierMedical1.getId());
        assertThat(dossierMedical1).isEqualTo(dossierMedical2);
        dossierMedical2.setId(2L);
        assertThat(dossierMedical1).isNotEqualTo(dossierMedical2);
        dossierMedical1.setId(null);
        assertThat(dossierMedical1).isNotEqualTo(dossierMedical2);
    }
}
