package sn.ngirwi.medical.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedecineMapperTest {

    private MedecineMapper medecineMapper;

    @BeforeEach
    public void setUp() {
        medecineMapper = new MedecineMapperImpl();
    }
}
