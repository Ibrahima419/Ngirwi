package sn.ngirwi.medical.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class BillElementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillElement.class);
        BillElement billElement1 = new BillElement();
        billElement1.setId(1L);
        BillElement billElement2 = new BillElement();
        billElement2.setId(billElement1.getId());
        assertThat(billElement1).isEqualTo(billElement2);
        billElement2.setId(2L);
        assertThat(billElement1).isNotEqualTo(billElement2);
        billElement1.setId(null);
        assertThat(billElement1).isNotEqualTo(billElement2);
    }
}
