package sn.ngirwi.medical.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Patient;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.repository.PatientRepository;
import sn.ngirwi.medical.service.dto.HospitalisationDTO;
import sn.ngirwi.medical.web.rest.TestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class HospitalisationResourceActiveGuardIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private HospitalisationRepository hospitalisationRepository;

    @Test
    @Transactional
    void creatingSecondActiveHospitalisationShouldFail() throws Exception {
        // Prepare a patient
        Patient p = new Patient();
        p.setFirstName("John");
        p.setLastName("Doe");
        p = patientRepository.saveAndFlush(p);

        // First active hospitalisation
        HospitalisationDTO dto1 = new HospitalisationDTO();
        dto1.setPatientId(p.getId());
        dto1.setDoctorName("Dr A");
        dto1.setStatus(HospitalisationStatus.STARTED);

        mockMvc
            .perform(post("/api/hospitalisations").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto1)))
            .andExpect(status().isCreated());

        // Second active hospitalisation must be rejected
        HospitalisationDTO dto2 = new HospitalisationDTO();
        dto2.setPatientId(p.getId());
        dto2.setDoctorName("Dr B");
        dto2.setStatus(HospitalisationStatus.STARTED);

        mockMvc
            .perform(post("/api/hospitalisations").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto2)))
            .andExpect(status().isBadRequest());
    }
}
