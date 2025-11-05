package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ngirwi.medical.domain.DossierMedical;
import sn.ngirwi.medical.repository.DossierMedicalRepository;
import sn.ngirwi.medical.security.AuthoritiesConstants;
import sn.ngirwi.medical.service.DossierMedicalService;
import sn.ngirwi.medical.service.dto.DossierMedicalDTO;
//import tech.jhipster.web.util.HeaderUtil;
import sn.ngirwi.medical.utils.HeaderUtil;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.DossierMedical}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.DOCTOR + "\")")
public class DossierMedicalResource {

    private final Logger log = LoggerFactory.getLogger(DossierMedicalResource.class);

    private static final String ENTITY_NAME = "dossierMedical";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierMedicalService dossierMedicalService;

    private final DossierMedicalRepository dossierMedicalRepository;

    public DossierMedicalResource(DossierMedicalService dossierMedicalService, DossierMedicalRepository dossierMedicalRepository) {
        this.dossierMedicalService = dossierMedicalService;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    /**
     * {@code POST  /dossier-medicals} : Create a new dossierMedical.
     *
     * @param dossierMedicalDTO the dossierMedicalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierMedicalDTO, or with status {@code 400 (Bad Request)} if the dossierMedical has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossier-medicals")
    public ResponseEntity<DossierMedicalDTO> createDossierMedical(@Valid @RequestBody DossierMedicalDTO dossierMedicalDTO)
        throws URISyntaxException {
        log.debug("REST request to save DossierMedical : {}", dossierMedicalDTO);
        if (dossierMedicalDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossierMedical cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierMedicalDTO result = dossierMedicalService.save(dossierMedicalDTO);
        return ResponseEntity
            .created(new URI("/api/dossier-medicals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dossier-medicals/:id} : Updates an existing dossierMedical.
     *
     * @param id the id of the dossierMedicalDTO to save.
     * @param dossierMedicalDTO the dossierMedicalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierMedicalDTO,
     * or with status {@code 400 (Bad Request)} if the dossierMedicalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierMedicalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossier-medicals/{id}")
    public ResponseEntity<DossierMedicalDTO> updateDossierMedical(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DossierMedicalDTO dossierMedicalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DossierMedical : {}, {}", id, dossierMedicalDTO);
        if (dossierMedicalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierMedicalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierMedicalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DossierMedicalDTO result = dossierMedicalService.update(dossierMedicalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dossierMedicalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dossier-medicals/:id} : Partial updates given fields of an existing dossierMedical, field will ignore if it is null
     *
     * @param id the id of the dossierMedicalDTO to save.
     * @param dossierMedicalDTO the dossierMedicalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierMedicalDTO,
     * or with status {@code 400 (Bad Request)} if the dossierMedicalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dossierMedicalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierMedicalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossier-medicals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DossierMedicalDTO> partialUpdateDossierMedical(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DossierMedicalDTO dossierMedicalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DossierMedical partially : {}, {}", id, dossierMedicalDTO);
        if (dossierMedicalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierMedicalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierMedicalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DossierMedicalDTO> result = dossierMedicalService.partialUpdate(dossierMedicalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dossierMedicalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dossier-medicals} : get all the dossierMedicals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierMedicals in body.
     */
    @GetMapping("/dossier-medicals")
    public ResponseEntity<List<DossierMedicalDTO>> getAllDossierMedicals(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DossierMedicals");
        Page<DossierMedicalDTO> page = dossierMedicalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dossier-medicals/:id} : get the "id" dossierMedical.
     *
     * @param id the id of the dossierMedicalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierMedicalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossier-medicals/{id}")
    public ResponseEntity<DossierMedicalDTO> getDossierMedical(@PathVariable Long id) {
        log.debug("REST request to get DossierMedical : {}", id);
        Optional<DossierMedicalDTO> dossierMedicalDTO = dossierMedicalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierMedicalDTO);
    }

    @GetMapping("/dossier-medicals-patient/{id}")
    public ResponseEntity<DossierMedical> getDossierPatient(@PathVariable Long id) {
        log.debug("REST request to get DossierMedical for specific patient: {}", id);
        Optional<DossierMedical> dossierMedicalDTO = dossierMedicalService.findPatient(id);
        return ResponseUtil.wrapOrNotFound(dossierMedicalDTO);
    }

    /**
     * {@code DELETE  /dossier-medicals/:id} : delete the "id" dossierMedical.
     *
     * @param id the id of the dossierMedicalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossier-medicals/{id}")
    public ResponseEntity<Void> deleteDossierMedical(@PathVariable Long id) {
        log.debug("REST request to delete DossierMedical : {}", id);
        dossierMedicalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
