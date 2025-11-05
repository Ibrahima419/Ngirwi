package sn.ngirwi.medical.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ngirwi.medical.web.rest.TestUtil;

class SurveillanceSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveillanceSheet.class);
        SurveillanceSheet surveillanceSheet1 = new SurveillanceSheet();
        surveillanceSheet1.setId(1L);
        SurveillanceSheet surveillanceSheet2 = new SurveillanceSheet();
        surveillanceSheet2.setId(surveillanceSheet1.getId());
        assertThat(surveillanceSheet1).isEqualTo(surveillanceSheet2);
        surveillanceSheet2.setId(2L);
        assertThat(surveillanceSheet1).isNotEqualTo(surveillanceSheet2);
        surveillanceSheet1.setId(null);
        assertThat(surveillanceSheet1).isNotEqualTo(surveillanceSheet2);
    }
}
