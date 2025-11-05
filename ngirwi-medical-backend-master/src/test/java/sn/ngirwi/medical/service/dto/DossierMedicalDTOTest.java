package sn.ngirwi.medical.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class DossierMedicalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierMedicalDTO.class);
        DossierMedicalDTO dossierMedicalDTO1 = new DossierMedicalDTO();
        dossierMedicalDTO1.setId(1L);
        DossierMedicalDTO dossierMedicalDTO2 = new DossierMedicalDTO();
        assertThat(dossierMedicalDTO1).isNotEqualTo(dossierMedicalDTO2);
        dossierMedicalDTO2.setId(dossierMedicalDTO1.getId());
        assertThat(dossierMedicalDTO1).isEqualTo(dossierMedicalDTO2);
        dossierMedicalDTO2.setId(2L);
        assertThat(dossierMedicalDTO1).isNotEqualTo(dossierMedicalDTO2);
        dossierMedicalDTO1.setId(null);
        assertThat(dossierMedicalDTO1).isNotEqualTo(dossierMedicalDTO2);
    }
}
