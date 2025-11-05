package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ngirwi.medical.domain.Bill;
import sn.ngirwi.medical.repository.BillRepository;
import sn.ngirwi.medical.service.BillService;
import sn.ngirwi.medical.service.dto.BillDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.Bill}.
 */
@RestController
@RequestMapping("/api")
public class BillResource {

    private final Logger log = LoggerFactory.getLogger(BillResource.class);

    private static final String ENTITY_NAME = "bill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillService billService;

    private final BillRepository billRepository;

    public BillResource(BillService billService, BillRepository billRepository) {
        this.billService = billService;
        this.billRepository = billRepository;
    }

    /**
     * {@code POST  /bills} : Create a new bill.
     *
     * @param billDTO the billDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billDTO, or with status {@code 400 (Bad Request)} if the bill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bills")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) throws URISyntaxException {
        log.debug("REST request to save Bill : {}", billDTO);
        if (billDTO.getId() != null) {
            throw new BadRequestAlertException("A new bill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillDTO result = billService.saveBis(billDTO);
        return ResponseEntity
            .created(new URI("/api/bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    //    @PostMapping("/billsbis")
    //    public ResponseEntity<BillDTO> createBillBis(@RequestBody BillDTO billDTO) throws URISyntaxException {
    //        log.debug("REST request to save Bill : {}", billDTO);
    //        if (billDTO.getId() != null) {
    //            throw new BadRequestAlertException("A new bill cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //        BillDTO result = billService.saveBis(billDTO);
    //        return ResponseEntity
    //            .created(new URI("/api/bills/" + result.getId()))
    //            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
    //            .body(result);
    //    }

    /**
     * {@code PUT  /bills/:id} : Updates an existing bill.
     *
     * @param id the id of the billDTO to save.
     * @param billDTO the billDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billDTO,
     * or with status {@code 400 (Bad Request)} if the billDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bills/{id}")
    public ResponseEntity<BillDTO> updateBill(@PathVariable(value = "id", required = false) final Long id, @RequestBody BillDTO billDTO)
        throws URISyntaxException {
        log.debug("REST request to update Bill : {}, {}", id, billDTO);
        if (billDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillDTO result = billService.updateBis(billDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bills/:id} : Partial updates given fields of an existing bill, field will ignore if it is null
     *
     * @param id the id of the billDTO to save.
     * @param billDTO the billDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billDTO,
     * or with status {@code 400 (Bad Request)} if the billDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bills/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillDTO> partialUpdateBill(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillDTO billDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bill partially : {}, {}", id, billDTO);
        if (billDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillDTO> result = billService.partialUpdate(billDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bills} : get all the bills.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bills in body.
     */
    @GetMapping("/bills")
    public ResponseEntity<List<Bill>> getAllBills(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Bills");
        Page<Bill> page = billService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/billsbis/{id}")
    public ResponseEntity<List<Bill>> getAllBills(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get a page of Bills " + id);
        Page<Bill> page = billService.findAll(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bills/:id} : get the "id" bill.
     *
     * @param id the id of the billDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bills/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable Long id) {
        log.debug("REST request to get Bill : {}", id);
        Optional<Bill> billDTO = billService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billDTO);
    }

    /**
     * {@code DELETE  /bills/:id} : delete the "id" bill.
     *
     * @param id the id of the billDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.debug("REST request to delete Bill : {}", id);
        billService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
