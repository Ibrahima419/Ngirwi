package sn.ngirwi.medical.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.repository.PatientRepository;
import sn.ngirwi.medical.service.dto.SurveillanceSheetDTO;
import sn.ngirwi.medical.web.rest.TestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class SurveillanceSheetResourceUniquenessIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HospitalisationRepository hospitalisationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Transactional
    void creatingSecondSheetSameDaySameHospShouldFail() throws Exception {
        Patient p = new Patient();
        p.setFirstName("A");
        p.setLastName("B");
        p = patientRepository.saveAndFlush(p);
        Hospitalisation h = new Hospitalisation();
        h.setDoctorName("DrX");
        h.setStatus(HospitalisationStatus.STARTED);
        h.setPatient(p);
        h = hospitalisationRepository.saveAndFlush(h);

        SurveillanceSheetDTO dto = new SurveillanceSheetDTO();
        dto.setHospitalisationId(h.getId());
        dto.setSheetDate(LocalDate.of(2024, 1, 10));

        mockMvc
            .perform(
                post("/api/surveillance-sheets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto))
            )
            .andExpect(status().isCreated());

        SurveillanceSheetDTO dto2 = new SurveillanceSheetDTO();
        dto2.setHospitalisationId(h.getId());
        dto2.setSheetDate(LocalDate.of(2024, 1, 10));

        mockMvc
            .perform(
                post("/api/surveillance-sheets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto2))
            )
            .andExpect(status().isBadRequest());
    }
}
