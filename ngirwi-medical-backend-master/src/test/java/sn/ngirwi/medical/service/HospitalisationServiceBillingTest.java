package sn.ngirwi.medical.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.*;
import sn.ngirwi.medical.repository.*;
import sn.ngirwi.medical.service.dto.HospitalisationResumeDTO;

@SpringBootTest
class HospitalisationServiceBillingTest {

    @Autowired
    private HospitalisationService hospitalisationService;

    @MockBean
    private HospitalisationRepository hospitalisationRepository;

    @MockBean
    private SurveillanceSheetRepository surveillanceSheetRepository;

    @Test
    @Transactional
    void calculateResume_sumsMedsActsAndMiniConsultations_andRoundsToZero() {
        Hospitalisation h = new Hospitalisation();
        h.setId(1L);
        h.setEntryDate(Instant.parse("2024-01-10T00:00:00Z"));
        h.setReleaseDate(Instant.parse("2024-01-12T00:00:00Z")); // 2 days
        h.setDailyRate(new BigDecimal("10000")); // per day
        h.setComfortFees(new BigDecimal("500"));
        h.setFeeOverrun(new BigDecimal("250"));
        h.setInsuranceCoveragePercent(new BigDecimal("10")); // 10%

        Mockito.when(hospitalisationRepository.findById(1L)).thenReturn(java.util.Optional.of(h));

        SurveillanceSheet s1 = new SurveillanceSheet();
        s1.setId(11L);
        s1.setSheetDate(LocalDate.of(2024, 1, 10));
        MedicationEntry m1 = new MedicationEntry();
        m1.setNom("Parac");
        m1.setPrixUnitaire(new BigDecimal("1000"));
        m1.setQuantite(2);
        ActEntry a1 = new ActEntry();
        a1.setNom("ECG");
        a1.setPrixUnitaire(new BigDecimal("5000"));
        a1.setQuantite(1);
        s1.setMedications(Arrays.asList(m1));
        s1.setActs(Arrays.asList(a1));
        MiniConsultation mc1 = new MiniConsultation();
        mc1.setPrice(new BigDecimal("3000"));
        s1.setMiniConsultations(Arrays.asList(mc1));

        SurveillanceSheet s2 = new SurveillanceSheet();
        s2.setId(12L);
        s2.setSheetDate(LocalDate.of(2024, 1, 11));
        MedicationEntry m2 = new MedicationEntry();
        m2.setNom("AB");
        m2.setPrixUnitaire(new BigDecimal("1500"));
        m2.setQuantite(1);
        ActEntry a2 = new ActEntry();
        a2.setNom("TA");
        a2.setPrixUnitaire(new BigDecimal("2000"));
        a2.setQuantite(3);
        s2.setMedications(Arrays.asList(m2));
        s2.setActs(Arrays.asList(a2));
        MiniConsultation mc2 = new MiniConsultation();
        mc2.setPrice(new BigDecimal("4500"));
        s2.setMiniConsultations(Arrays.asList(mc2));

        Mockito.when(surveillanceSheetRepository.findByHospitalisation_Id(1L)).thenReturn(Arrays.asList(s1, s2));

        HospitalisationResumeDTO dto = hospitalisationService.calculateResume(1L);

        // forfait = 10000 * 2 = 20000; comfort 500; overrun 250
        // meds = 1000*2 + 1500*1 = 3500
        // acts = 5000*1 + 2000*3 = 11000
        // minis = 3000 + 4500 = 7500
        // subtotal = 20000 + 500 + 250 + 3500 + 11000 + 7500 = 42750
        // insurance 10% => 38475 -> rounded 0 decimals
        assertThat(dto.getSubtotal().intValue()).isEqualTo(42750);
        assertThat(dto.getTotalAmount().intValue()).isEqualTo(38475);
    }
}
