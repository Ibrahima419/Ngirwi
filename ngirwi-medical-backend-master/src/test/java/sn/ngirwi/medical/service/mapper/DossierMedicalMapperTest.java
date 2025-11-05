package sn.ngirwi.medical.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DossierMedicalMapperTest {

    private DossierMedicalMapper dossierMedicalMapper;

    @BeforeEach
    public void setUp() {
        dossierMedicalMapper = new DossierMedicalMapperImpl();
    }
}
