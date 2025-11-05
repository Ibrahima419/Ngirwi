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
import sn.ngirwi.medical.repository.BillElementRepository;
import sn.ngirwi.medical.service.BillElementService;
import sn.ngirwi.medical.service.dto.BillElementDTO;
import sn.ngirwi.medical.service.dto.MedecineDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.BillElement}.
 */
@RestController
@RequestMapping("/api")
public class BillElementResource {

    private final Logger log = LoggerFactory.getLogger(BillElementResource.class);

    private static final String ENTITY_NAME = "billElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillElementService billElementService;

    private final BillElementRepository billElementRepository;

    public BillElementResource(BillElementService billElementService, BillElementRepository billElementRepository) {
        this.billElementService = billElementService;
        this.billElementRepository = billElementRepository;
    }

    /**
     * {@code POST  /bill-elements} : Create a new billElement.
     *
     * @param billElementDTO the billElementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billElementDTO, or with status {@code 400 (Bad Request)} if the billElement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bill-elements")
    public ResponseEntity<BillElementDTO> createBillElement(@RequestBody BillElementDTO billElementDTO) throws URISyntaxException {
        log.debug("REST request to save BillElement : {}", billElementDTO);
        if (billElementDTO.getId() != null) {
            throw new BadRequestAlertException("A new billElement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillElementDTO result = billElementService.save(billElementDTO);
        return ResponseEntity
            .created(new URI("/api/bill-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-elements/:id} : Updates an existing billElement.
     *
     * @param id the id of the billElementDTO to save.
     * @param billElementDTO the billElementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billElementDTO,
     * or with status {@code 400 (Bad Request)} if the billElementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billElementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bill-elements/{id}")
    public ResponseEntity<BillElementDTO> updateBillElement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillElementDTO billElementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillElement : {}, {}", id, billElementDTO);
        if (billElementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billElementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillElementDTO result = billElementService.update(billElementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billElementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bill-elements/:id} : Partial updates given fields of an existing billElement, field will ignore if it is null
     *
     * @param id the id of the billElementDTO to save.
     * @param billElementDTO the billElementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billElementDTO,
     * or with status {@code 400 (Bad Request)} if the billElementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billElementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billElementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bill-elements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillElementDTO> partialUpdateBillElement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillElementDTO billElementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillElement partially : {}, {}", id, billElementDTO);
        if (billElementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billElementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillElementDTO> result = billElementService.partialUpdate(billElementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billElementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-elements} : get all the billElements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billElements in body.
     */
    @GetMapping("/bill-elements")
    public ResponseEntity<List<BillElementDTO>> getAllBillElements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BillElements");
        Page<BillElementDTO> page = billElementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bill-elements/:id} : get the "id" billElement.
     *
     * @param id the id of the billElementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billElementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bill-elements/{id}")
    public ResponseEntity<BillElementDTO> getBillElement(@PathVariable Long id) {
        log.debug("REST request to get BillElement : {}", id);
        Optional<BillElementDTO> billElementDTO = billElementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billElementDTO);
    }

    /**
     * {@code DELETE  /bill-elements/:id} : delete the "id" billElement.
     *
     * @param id the id of the billElementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bill-elements/{id}")
    public ResponseEntity<Void> deleteBillElement(@PathVariable Long id) {
        log.debug("REST request to delete BillElement : {}", id);
        billElementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/bill-elements/bill/{id}")
    public List<BillElementDTO> getAllElementsByBillID(@PathVariable Long id) {
        log.debug("REST request to get all lines for a specific bill");
        return billElementService.findAll(id);
    }
}
