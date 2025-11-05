package sn.ngirwi.medical.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrescriptionMapperTest {

    private PrescriptionMapper prescriptionMapper;

    @BeforeEach
    public void setUp() {
        prescriptionMapper = new PrescriptionMapperImpl();
    }
}
