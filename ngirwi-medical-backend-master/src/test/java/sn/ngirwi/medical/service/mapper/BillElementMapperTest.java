package sn.ngirwi.medical.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillElementMapperTest {

    private BillElementMapper billElementMapper;

    @BeforeEach
    public void setUp() {
        billElementMapper = new BillElementMapperImpl();
    }
}
