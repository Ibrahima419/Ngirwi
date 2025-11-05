package sn.ngirwi.medical.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class BillElementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillElementDTO.class);
        BillElementDTO billElementDTO1 = new BillElementDTO();
        billElementDTO1.setId(1L);
        BillElementDTO billElementDTO2 = new BillElementDTO();
        assertThat(billElementDTO1).isNotEqualTo(billElementDTO2);
        billElementDTO2.setId(billElementDTO1.getId());
        assertThat(billElementDTO1).isEqualTo(billElementDTO2);
        billElementDTO2.setId(2L);
        assertThat(billElementDTO1).isNotEqualTo(billElementDTO2);
        billElementDTO1.setId(null);
        assertThat(billElementDTO1).isNotEqualTo(billElementDTO2);
    }
}
