package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ngirwi.medical.domain.enumeration.HospitalisationStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.repository.HospitalRepository;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.service.HospitalisationService;
import sn.ngirwi.medical.service.PdfGenerator;
import sn.ngirwi.medical.service.dto.HospitalisationDTO;
import sn.ngirwi.medical.service.dto.HospitalisationResumeDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Hospitalisation (DTO-based).
 */
@RestController
@RequestMapping("/api")
public class HospitalisationResource {

    private final Logger log = LoggerFactory.getLogger(HospitalisationResource.class);

    private static final String ENTITY_NAME = "hospitalisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalisationService hospitalisationService;
    private final HospitalisationRepository hospitalisationRepository;
    private final HospitalRepository hospitalRepository;

    public HospitalisationResource(
            HospitalisationService hospitalisationService,
            HospitalisationRepository hospitalisationRepository,
            HospitalRepository hospitalRepository) {
        this.hospitalisationService = hospitalisationService;
        this.hospitalisationRepository = hospitalisationRepository;
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * POST  /hospitalisations : Create a new Hospitalisation.
     */
    @PostMapping("/hospitalisations")
    public ResponseEntity<HospitalisationDTO> createHospitalisation(@Valid @RequestBody HospitalisationDTO dto) throws URISyntaxException {
        log.debug("REST request to create Hospitalisation for patientId={}", dto.getPatientId());
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new hospitalisation cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            HospitalisationDTO result = hospitalisationService.save(dto);
            return ResponseEntity
                .created(new URI("/api/hospitalisations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validation");
        }
    }

    /**
     * PUT  /hospitalisations/:id : Update an existing hospitalisation.
     */
    @PutMapping("/hospitalisations/{id}")
    public ResponseEntity<HospitalisationDTO> updateHospitalisation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HospitalisationDTO dto
    ) {
        log.debug("REST request to update Hospitalisation : id={}, patientId={}", id, dto.getPatientId());
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!hospitalisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            HospitalisationDTO result = hospitalisationService.update(dto);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dto.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validation");
        }
    }

    /**
     * PATCH  /hospitalisations/:id : Partial update.
     */
    @PatchMapping(value = "/hospitalisations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HospitalisationDTO> partialUpdateHospitalisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HospitalisationDTO dto
    ) {
        log.debug("REST request to partial update Hospitalisation : id={}, dto={}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!hospitalisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            Optional<HospitalisationDTO> result = hospitalisationService.partialUpdate(dto);
            return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dto.getId().toString())
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validation");
        }
    }

    /**
     * GET  /hospitalisations : get all hospitalisations (paged).
     */
    @GetMapping("/hospitalisations")
    public ResponseEntity<List<HospitalisationDTO>> getAllHospitalisations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Hospitalisations");
        Page<HospitalisationDTO> page = hospitalisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /hospitalisations/{id} : get the "id" hospitalisation.
     */
    @GetMapping("/hospitalisations/{id}")
    public ResponseEntity<HospitalisationDTO> getHospitalisation(@PathVariable Long id) {
        log.debug("REST request to get Hospitalisation : {}", id);
        Optional<HospitalisationDTO> dto = hospitalisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    /**
     * GET /hospitalisations-patient/{patientId} : get a patient's latest active hospitalisation (or latest).
     *
     * This endpoint exists to support UI screens that expect a single object.
     * If no hospitalisation exists for the patient, returns an empty JSON object.
     */
    @GetMapping("/hospitalisations-patient/{patientId}")
    public ResponseEntity<Object> getHospitalisationForPatient(@PathVariable Long patientId) {
        log.debug("REST request to get Hospitalisation for patientId={}", patientId);
        Optional<HospitalisationDTO> dto = hospitalisationService.findLatestForPatient(patientId);
        return dto.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(Collections.emptyMap()));
    }

    /**
     * DELETE  /hospitalisations/{id} : delete the "id" hospitalisation.
     */
    @DeleteMapping("/hospitalisations/{id}")
    public ResponseEntity<Void> deleteHospitalisation(@PathVariable Long id) {
        log.debug("REST request to delete Hospitalisation : {}", id);
        if (!hospitalisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        hospitalisationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * POST /hospitalisations/{id}/close : close an hospitalisation
     */
    @PostMapping("/hospitalisations/{id}/close")
    public ResponseEntity<HospitalisationDTO> closeHospitalisation(
        @PathVariable Long id,
        @RequestParam Instant releaseDate,
        @RequestParam(required = false) String finalDiagnosis,
        @RequestParam(defaultValue = "false") boolean generateBill
    ) {
        log.debug("REST request to close Hospitalisation id={}, releaseDate={}, generateBill={}", id, releaseDate, generateBill);
        if (!hospitalisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            HospitalisationDTO result = hospitalisationService.close(id, releaseDate, finalDiagnosis, generateBill);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validation");
        } catch (NoSuchElementException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "idnotfound");
        } catch (IllegalStateException e) {
            // e.g. already closed
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidstate");
        }
    }

    /**
     * GET /hospitalisations/by-patient/{patientId}
     */
    @GetMapping("/hospitalisations/by-patient/{patientId}")
    public ResponseEntity<List<HospitalisationDTO>> getByPatient(
        @PathVariable Long patientId,
        @RequestParam(required = false) String service,
        @RequestParam(required = false) HospitalisationStatus status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Hospitalisations by patientId={}, service={}, status={}", patientId, service, status);
        Page<HospitalisationDTO> page = hospitalisationService.findByPatient(
            patientId,
            service,
            status != null ? status.name() : null,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET /hospitalisations/active
     */
    @GetMapping("/hospitalisations/active")
    public ResponseEntity<List<HospitalisationDTO>> getActive(
        @RequestParam(required = false) String service,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get active Hospitalisations service={}", service);
        Page<HospitalisationDTO> page = hospitalisationService.findActive(service, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET /hospitalisations/search
     */
    @GetMapping("/hospitalisations/search")
    public ResponseEntity<List<HospitalisationDTO>> search(
        @RequestParam(required = false) Long patientId,
        @RequestParam(required = false) HospitalisationStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @RequestParam(required = false) String service,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug(
            "REST request to search Hospitalisations patientId={}, status={}, from={}, to={}, service={}",
            patientId,
            status,
            from,
            to,
            service
        );
        Page<HospitalisationDTO> page = hospitalisationService.search(
            patientId,
            status != null ? status.name() : null,
            from,
            to,
            service,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET /hospitalisations/{id}/resume : calcule et retourne le résumé de facturation SANS persister.
     */
    @GetMapping("/hospitalisations/{id}/resume")
    public ResponseEntity<HospitalisationResumeDTO> getHospitalisationResume(@PathVariable Long id) {
        log.debug("REST request to get billing resume for Hospitalisation : {}", id);
        HospitalisationResumeDTO dto = hospitalisationService.calculateResume(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * POST /hospitalisations/{id}/finalize : calcule et PERSISTE le totalAmount (fin d'hospitalisation).
     */
    @PostMapping("/hospitalisations/{id}/finalize")
    public ResponseEntity<HospitalisationResumeDTO> finalizeHospitalisationBilling(@PathVariable Long id) {
        log.debug("REST request to finalize billing for Hospitalisation : {}", id);
        HospitalisationResumeDTO dto = hospitalisationService.finalizeBilling(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /hospitalisations/{id}/resume.pdf : exporte le résumé d'hospitalisation au format PDF.
     *
     * Ported from production JAR. @Transactional ensures LAZY collections
     * (surveillanceSheets, medications, acts) are accessible during PDF generation.
     */
    @GetMapping("/hospitalisations/{id}/resume.pdf")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> exportHospitalisationPdf(@PathVariable Long id) {
        log.debug("REST request to export PDF for Hospitalisation : {}", id);
        Hospitalisation h = hospitalisationRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Hospitalisation non trouvée", ENTITY_NAME, "idnotfound"));

        // Load hospital from patient's hospitalId (for logo/header)
        Hospital hospital = null;
        if (h.getPatient() != null && h.getPatient().getHospitalId() != null) {
            hospital = hospitalRepository.findById(h.getPatient().getHospitalId()).orElse(null);
        }

        HospitalisationResumeDTO resume = hospitalisationService.calculateResume(id);

        try {
            byte[] pdf = PdfGenerator.generate(h, hospital, resume);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hospitalisation_" + id + ".pdf");
            headers.setContentLength(pdf.length);

            return new ResponseEntity<>(pdf, headers, org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to generate PDF for hospitalisation {}: {}", id, e.getMessage(), e);
            throw new BadRequestAlertException(
                "Erreur lors de la génération du PDF: " + e.getMessage(), ENTITY_NAME, "pdferror");
        }
    }
}
