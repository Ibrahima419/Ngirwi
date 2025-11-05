package sn.ngirwi.medical.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class HospitalisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hospitalisation.class);
        Hospitalisation hospitalisation1 = new Hospitalisation();
        hospitalisation1.setId(1L);
        Hospitalisation hospitalisation2 = new Hospitalisation();
        hospitalisation2.setId(hospitalisation1.getId());
        assertThat(hospitalisation1).isEqualTo(hospitalisation2);
        hospitalisation2.setId(2L);
        assertThat(hospitalisation1).isNotEqualTo(hospitalisation2);
        hospitalisation1.setId(null);
        assertThat(hospitalisation1).isNotEqualTo(hospitalisation2);
    }
}
