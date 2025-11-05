package sn.ngirwi.medical.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.ngirwi.medical.IntegrationTest;
import sn.ngirwi.medical.domain.BillElement;
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.service.dto.BillElementDTO;
import sn.ngirwi.medical.service.mapper.BillElementMapper;

/**
 * Integration tests for the {@link BillElementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillElementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/bill-elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BillElementRepository billElementRepository;

    @Autowired
    private BillElementMapper billElementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillElementMockMvc;

    private BillElement billElement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillElement createEntity(EntityManager em) {
        BillElement billElement = new BillElement()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .percentage(DEFAULT_PERCENTAGE)
            .quantity(DEFAULT_QUANTITY);
        return billElement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillElement createUpdatedEntity(EntityManager em) {
        BillElement billElement = new BillElement()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .percentage(UPDATED_PERCENTAGE)
            .quantity(UPDATED_QUANTITY);
        return billElement;
    }

    @BeforeEach
    public void initTest() {
        billElement = createEntity(em);
    }

    @Test
    @Transactional
    void createBillElement() throws Exception {
        int databaseSizeBeforeCreate = billElementRepository.findAll().size();
        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);
        restBillElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeCreate + 1);
        BillElement testBillElement = billElementList.get(billElementList.size() - 1);
        assertThat(testBillElement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBillElement.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBillElement.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testBillElement.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createBillElementWithExistingId() throws Exception {
        // Create the BillElement with an existing ID
        billElement.setId(1L);
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        int databaseSizeBeforeCreate = billElementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillElements() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        // Get all the billElementList
        restBillElementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billElement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getBillElement() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        // Get the billElement
        restBillElementMockMvc
            .perform(get(ENTITY_API_URL_ID, billElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billElement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingBillElement() throws Exception {
        // Get the billElement
        restBillElementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillElement() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();

        // Update the billElement
        BillElement updatedBillElement = billElementRepository.findById(billElement.getId()).get();
        // Disconnect from session so that the updates on updatedBillElement are not directly saved in db
        em.detach(updatedBillElement);
        updatedBillElement.name(UPDATED_NAME).price(UPDATED_PRICE).percentage(UPDATED_PERCENTAGE).quantity(UPDATED_QUANTITY);
        BillElementDTO billElementDTO = billElementMapper.toDto(updatedBillElement);

        restBillElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billElementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
        BillElement testBillElement = billElementList.get(billElementList.size() - 1);
        assertThat(testBillElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBillElement.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBillElement.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testBillElement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billElementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(billElementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillElementWithPatch() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();

        // Update the billElement using partial update
        BillElement partialUpdatedBillElement = new BillElement();
        partialUpdatedBillElement.setId(billElement.getId());

        partialUpdatedBillElement.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);

        restBillElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillElement))
            )
            .andExpect(status().isOk());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
        BillElement testBillElement = billElementList.get(billElementList.size() - 1);
        assertThat(testBillElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBillElement.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBillElement.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testBillElement.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateBillElementWithPatch() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();

        // Update the billElement using partial update
        BillElement partialUpdatedBillElement = new BillElement();
        partialUpdatedBillElement.setId(billElement.getId());

        partialUpdatedBillElement.name(UPDATED_NAME).price(UPDATED_PRICE).percentage(UPDATED_PERCENTAGE).quantity(UPDATED_QUANTITY);

        restBillElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBillElement))
            )
            .andExpect(status().isOk());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
        BillElement testBillElement = billElementList.get(billElementList.size() - 1);
        assertThat(testBillElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBillElement.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBillElement.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testBillElement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billElementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillElement() throws Exception {
        int databaseSizeBeforeUpdate = billElementRepository.findAll().size();
        billElement.setId(count.incrementAndGet());

        // Create the BillElement
        BillElementDTO billElementDTO = billElementMapper.toDto(billElement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillElementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(billElementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillElement in the database
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillElement() throws Exception {
        // Initialize the database
        billElementRepository.saveAndFlush(billElement);

        int databaseSizeBeforeDelete = billElementRepository.findAll().size();

        // Delete the billElement
        restBillElementMockMvc
            .perform(delete(ENTITY_API_URL_ID, billElement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillElement> billElementList = billElementRepository.findAll();
        assertThat(billElementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
