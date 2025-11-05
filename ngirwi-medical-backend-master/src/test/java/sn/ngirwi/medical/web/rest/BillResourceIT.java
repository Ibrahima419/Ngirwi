package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.ngirwi.medical.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import sn.ngirwi.medical.IntegrationTest;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.repository.BillRepository;
import sn.ngirwi.medical.service.dto.BillDTO;
import sn.ngirwi.medical.service.mapper.BillMapper;

/**
 * Integration tests for the {@link BillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_INSURANCE = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_IPM = "AAAAAAAAAA";
    private static final String UPDATED_IPM = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillMockMvc;

    private Bill bill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill()
            .date(DEFAULT_DATE)
            .author(DEFAULT_AUTHOR)
            .insurance(DEFAULT_INSURANCE)
            .desc(DEFAULT_DESC)
            .ipm(DEFAULT_IPM)
            .total(DEFAULT_TOTAL);
        return bill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createUpdatedEntity(EntityManager em) {
        Bill bill = new Bill()
            .date(UPDATED_DATE)
            .author(UPDATED_AUTHOR)
            .insurance(UPDATED_INSURANCE)
            .desc(UPDATED_DESC)
            .ipm(UPDATED_IPM)
            .total(UPDATED_TOTAL);
        return bill;
    }

    @BeforeEach
    public void initTest() {
        bill = createEntity(em);
    }

    @Test
    @Transactional
    void createBill() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();
        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);
        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isCreated());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate + 1);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBill.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBill.getInsurance()).isEqualTo(DEFAULT_INSURANCE);
        assertThat(testBill.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testBill.getIpm()).isEqualTo(DEFAULT_IPM);
        assertThat(testBill.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createBillWithExistingId() throws Exception {
        // Create the Bill with an existing ID
        bill.setId(1L);
        BillDTO billDTO = billMapper.toDto(bill);

        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBills() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].insurance").value(hasItem(DEFAULT_INSURANCE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].ipm").value(hasItem(DEFAULT_IPM)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))));
    }

    @Test
    @Transactional
    void getBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc
            .perform(get(ENTITY_API_URL_ID, bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.insurance").value(DEFAULT_INSURANCE))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.ipm").value(DEFAULT_IPM))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill
        Bill updatedBill = billRepository.findById(bill.getId()).get();
        // Disconnect from session so that the updates on updatedBill are not directly saved in db
        em.detach(updatedBill);
        updatedBill
            .date(UPDATED_DATE)
            .author(UPDATED_AUTHOR)
            .insurance(UPDATED_INSURANCE)
            .desc(UPDATED_DESC)
            .ipm(UPDATED_IPM)
            .total(UPDATED_TOTAL);
        BillDTO billDTO = billMapper.toDto(updatedBill);

        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBill.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBill.getInsurance()).isEqualTo(UPDATED_INSURANCE);
        assertThat(testBill.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testBill.getIpm()).isEqualTo(UPDATED_IPM);
        assertThat(testBill.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillWithPatch() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill.desc(UPDATED_DESC).ipm(UPDATED_IPM).total(UPDATED_TOTAL);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBill.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBill.getInsurance()).isEqualTo(DEFAULT_INSURANCE);
        assertThat(testBill.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testBill.getIpm()).isEqualTo(UPDATED_IPM);
        assertThat(testBill.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateBillWithPatch() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill
            .date(UPDATED_DATE)
            .author(UPDATED_AUTHOR)
            .insurance(UPDATED_INSURANCE)
            .desc(UPDATED_DESC)
            .ipm(UPDATED_IPM)
            .total(UPDATED_TOTAL);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBill.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBill.getInsurance()).isEqualTo(UPDATED_INSURANCE);
        assertThat(testBill.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testBill.getIpm()).isEqualTo(UPDATED_IPM);
        assertThat(testBill.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();
        bill.setId(count.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeDelete = billRepository.findAll().size();

        // Delete the bill
        restBillMockMvc
            .perform(delete(ENTITY_API_URL_ID, bill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
