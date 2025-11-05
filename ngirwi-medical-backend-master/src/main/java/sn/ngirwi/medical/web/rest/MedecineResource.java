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
import sn.ngirwi.medical.repository.MedecineRepository;
import sn.ngirwi.medical.service.MedecineService;
import sn.ngirwi.medical.service.dto.MedecineDTO;
//import tech.jhipster.web.util.HeaderUtil;
import sn.ngirwi.medical.utils.HeaderUtil;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.Medecine}.
 */
@RestController
@RequestMapping("/api")
public class MedecineResource {

    private final Logger log = LoggerFactory.getLogger(MedecineResource.class);

    private static final String ENTITY_NAME = "medecine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedecineService medecineService;

    private final MedecineRepository medecineRepository;

    public MedecineResource(MedecineService medecineService, MedecineRepository medecineRepository) {
        this.medecineService = medecineService;
        this.medecineRepository = medecineRepository;
    }

    /**
     * {@code POST  /medecines} : Create a new medecine.
     *
     * @param medecineDTO the medecineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medecineDTO, or with status {@code 400 (Bad Request)} if the medecine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medecines")
    public ResponseEntity<MedecineDTO> createMedecine(@RequestBody MedecineDTO medecineDTO) throws URISyntaxException {
        log.debug("REST request to save Medecine : {}", medecineDTO);
        if (medecineDTO.getId() != null) {
            throw new BadRequestAlertException("A new medecine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedecineDTO result = medecineService.save(medecineDTO);
        return ResponseEntity
            .created(new URI("/api/medecines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medecines/:id} : Updates an existing medecine.
     *
     * @param id the id of the medecineDTO to save.
     * @param medecineDTO the medecineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medecineDTO,
     * or with status {@code 400 (Bad Request)} if the medecineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medecineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medecines/{id}")
    public ResponseEntity<MedecineDTO> updateMedecine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedecineDTO medecineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medecine : {}, {}", id, medecineDTO);
        if (medecineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medecineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medecineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedecineDTO result = medecineService.update(medecineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medecineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medecines/:id} : Partial updates given fields of an existing medecine, field will ignore if it is null
     *
     * @param id the id of the medecineDTO to save.
     * @param medecineDTO the medecineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medecineDTO,
     * or with status {@code 400 (Bad Request)} if the medecineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medecineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medecineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medecines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedecineDTO> partialUpdateMedecine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MedecineDTO medecineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medecine partially : {}, {}", id, medecineDTO);
        if (medecineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medecineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medecineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedecineDTO> result = medecineService.partialUpdate(medecineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medecineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medecines} : get all the medecines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medecines in body.
     */
    @GetMapping("/medecines/prescription/{id}")
    public List<MedecineDTO> getAllMedecinesByPrescriptionID(@PathVariable Long id) {
        log.debug("REST request to get a page of Medecines");
        return medecineService.findAll(id);
    }

    @GetMapping("/medecines")
    public ResponseEntity<List<MedecineDTO>> getAllMedecines(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Medecines");
        Page<MedecineDTO> page = medecineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medecines/:id} : get the "id" medecine.
     *
     * @param id the id of the medecineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medecineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medecines/{id}")
    public ResponseEntity<MedecineDTO> getMedecine(@PathVariable Long id) {
        log.debug("REST request to get Medecine : {}", id);
        Optional<MedecineDTO> medecineDTO = medecineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medecineDTO);
    }

    /**
     * {@code DELETE  /medecines/:id} : delete the "id" medecine.
     *
     * @param id the id of the medecineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medecines/{id}")
    public ResponseEntity<Void> deleteMedecine(@PathVariable Long id) {
        log.debug("REST request to delete Medecine : {}", id);
        medecineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
