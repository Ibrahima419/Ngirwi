package sn.ngirwi.medical.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.repository.BillRepository;

@SpringBootTest
class BillServiceAggregationTest {

    @Autowired
    private BillService billService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillElementRepository billElementRepository;

    @Test
    @Transactional
    void calculateTotal_usesDbAggregation_whenBillHasId() {
        Bill bill = new Bill();
        bill.setDate(Instant.now());
        bill.setAuthor("tester");
        bill = billRepository.saveAndFlush(bill);

        BillElement e1 = new BillElement();
        e1.setName("Act A");
        e1.setPrice(1000.0);
        e1.setQuantity(2);
        e1.setPercentage(10.0);
        e1.setBill(bill);
        BillElement e2 = new BillElement();
        e2.setName("Act B");
        e2.setPrice(500.0);
        e2.setQuantity(3);
        e2.setPercentage(0.0);
        e2.setBill(bill);
        billElementRepository.saveAll(Arrays.asList(e1, e2));

        // e1 net = 1000*2 * (1-0.10) = 1800; e2 net = 500*3 = 1500; total = 3300 => rounded 0 => 3300
        BigDecimal total = billService.calculateTotal(bill);
        assertThat(total.intValue()).isEqualTo(3300);
    }
}
