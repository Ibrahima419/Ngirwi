package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.SurveillanceSheet;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.repository.SurveillanceSheetRepository;
import sn.ngirwi.medical.service.dto.HospitalisationResumeDTO;

@SpringBootTest
@AutoConfigureMockMvc
class HospitalisationFinalizeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HospitalisationRepository hospitalisationRepository;

    @Autowired
    private SurveillanceSheetRepository surveillanceSheetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void finalize_shouldComputeAndPersistTotalAmount() throws Exception {
        Hospitalisation hosp = new Hospitalisation();
        hosp.setEntryDate(Instant.now().minusSeconds(24 * 3600));
        hosp.setDoctorName("DR");
        hosp.setDailyRate(new BigDecimal("1000"));
        hosp.setComfortFees(new BigDecimal("200"));
        hosp.setFeeOverrun(new BigDecimal("0"));
        hosp.setInsuranceCoveragePercent(new BigDecimal("0"));
        hosp = hospitalisationRepository.saveAndFlush(hosp);

        SurveillanceSheet sheet = new SurveillanceSheet();
        sheet.setHospitalisation(hosp);
        sheet.setSheetDate(LocalDate.now(ZoneOffset.UTC));
        surveillanceSheetRepository.saveAndFlush(sheet);

        String payload = mockMvc
            .perform(post("/api/hospitalisations/" + hosp.getId() + "/finalize").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        HospitalisationResumeDTO resume = objectMapper.readValue(payload, HospitalisationResumeDTO.class);
        Hospitalisation reloaded = hospitalisationRepository.findById(hosp.getId()).orElseThrow();

        assertThat(resume.getTotalAmount()).isNotNull();
        assertThat(reloaded.getTotalAmount()).isNotNull();
        assertThat(reloaded.getTotalAmount().intValue()).isEqualTo(resume.getTotalAmount().intValue());
    }
}
