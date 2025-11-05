package sn.ngirwi.medical.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class MedecineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedecineDTO.class);
        MedecineDTO medecineDTO1 = new MedecineDTO();
        medecineDTO1.setId(1L);
        MedecineDTO medecineDTO2 = new MedecineDTO();
        assertThat(medecineDTO1).isNotEqualTo(medecineDTO2);
        medecineDTO2.setId(medecineDTO1.getId());
        assertThat(medecineDTO1).isEqualTo(medecineDTO2);
        medecineDTO2.setId(2L);
        assertThat(medecineDTO1).isNotEqualTo(medecineDTO2);
        medecineDTO1.setId(null);
        assertThat(medecineDTO1).isNotEqualTo(medecineDTO2);
    }
}
