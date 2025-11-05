package sn.ngirwi.medical.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultationMapperTest {

    private ConsultationMapper consultationMapper;

    @BeforeEach
    public void setUp() {
        consultationMapper = new ConsultationMapperImpl();
    }
}
