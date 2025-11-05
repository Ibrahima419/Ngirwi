package sn.ngirwi.medical.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.service.dto.MiniConsultationDTO;

@SpringBootTest
class MiniConsultationServiceTest {

    @Autowired
    private MiniConsultationService miniConsultationService;

    @Test
    @Transactional
    void save_shouldRejectMissingSurveillanceSheet() {
        MiniConsultationDTO dto = new MiniConsultationDTO();
        dto.setSummary("x");
        dto.setDiagnosis("y");
        dto.setSurveillanceSheetId(999999L);

        assertThatThrownBy(() -> miniConsultationService.save(dto)).isInstanceOf(IllegalArgumentException.class);
    }
}
