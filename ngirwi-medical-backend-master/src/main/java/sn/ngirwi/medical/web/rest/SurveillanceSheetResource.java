package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ngirwi.medical.repository.HospitalisationRepository;
import sn.ngirwi.medical.repository.SurveillanceSheetRepository;
import sn.ngirwi.medical.service.SurveillanceSheetService;
import sn.ngirwi.medical.service.dto.SurveillanceSheetDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.SurveillanceSheet}.
 *
 * Contraintes métier appliquées :
 * - Unicité (hospitalisationId, sheetDate) : 1 fiche par jour et par hospitalisation.
 * - Interdiction de déplacer une fiche vers une autre hospitalisation (hospitalisationId immuable après création).
 */
@RestController
@RequestMapping("/api")
public class SurveillanceSheetResource {

    private static final String ENTITY_NAME = "surveillanceSheet";
    private final Logger log = LoggerFactory.getLogger(SurveillanceSheetResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveillanceSheetService surveillanceSheetService;
    private final SurveillanceSheetRepository surveillanceSheetRepository;
    private final HospitalisationRepository hospitalisationRepository;

    public SurveillanceSheetResource(
        SurveillanceSheetService surveillanceSheetService,
        SurveillanceSheetRepository surveillanceSheetRepository,
        HospitalisationRepository hospitalisationRepository
    ) {
        this.surveillanceSheetService = surveillanceSheetService;
        this.surveillanceSheetRepository = surveillanceSheetRepository;
        this.hospitalisationRepository = hospitalisationRepository;
    }

    // --------------------------------------------------------------------------------------------
    // CREATE
    // --------------------------------------------------------------------------------------------
    @PostMapping("/surveillance-sheets")
    public ResponseEntity<SurveillanceSheetDTO> create(@Valid @RequestBody SurveillanceSheetDTO dto) throws URISyntaxException {
        log.debug("REST to create SurveillanceSheet : {}", dto);

        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new surveillanceSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (dto.getHospitalisationId() == null) {
            throw new BadRequestAlertException("hospitalisationId is required", ENTITY_NAME, "hospitalisationidnull");
        }
        // Hospitalisation must exist
        if (!hospitalisationRepository.existsById(dto.getHospitalisationId())) {
            throw new BadRequestAlertException("Hospitalisation not found", ENTITY_NAME, "hospitalisationnotfound");
        }
        // Unicity (hospitalisationId, sheetDate)
        if (dto.getSheetDate() == null) {
            throw new BadRequestAlertException("sheetDate is required", ENTITY_NAME, "sheetdatenull");
        }
        if (surveillanceSheetRepository.existsByHospitalisationIdAndSheetDate(dto.getHospitalisationId(), dto.getSheetDate())) {
            throw new BadRequestAlertException(
                "A sheet already exists for this hospitalisation and date",
                ENTITY_NAME,
                "duplicate_hosp_date"
            );
        }

        try {
            final SurveillanceSheetDTO result = surveillanceSheetService.save(dto);
            return ResponseEntity
                .created(new URI("/api/surveillance-sheets/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
        } catch (IllegalStateException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "business_rule_violation");
        }
    }

    // --------------------------------------------------------------------------------------------
    // UPDATE (PUT): full update except hospitalisationId (immutable)
    // --------------------------------------------------------------------------------------------
    @PutMapping("/surveillance-sheets/{id}")
    public ResponseEntity<SurveillanceSheetDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SurveillanceSheetDTO dto
    ) {
        log.debug("REST to update SurveillanceSheet : {}, {}", id, dto);

        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!surveillanceSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // hospitalisationId is immutable once created
        final Long currentHospId = surveillanceSheetRepository.findById(id).map(ss -> ss.getHospitalisation().getId()).orElse(null);
        if (dto.getHospitalisationId() != null && !Objects.equals(dto.getHospitalisationId(), currentHospId)) {
            throw new BadRequestAlertException("Cannot change hospitalisation of a sheet", ENTITY_NAME, "hospitalisation_immutable");
        }
        // Ensure uniqueness if sheetDate changed
        if (dto.getSheetDate() == null) {
            throw new BadRequestAlertException("sheetDate is required", ENTITY_NAME, "sheetdatenull");
        }
        surveillanceSheetRepository
            .findByHospitalisationIdAndSheetDate(currentHospId, dto.getSheetDate())
            .filter(found -> !Objects.equals(found.getId(), id))
            .ifPresent(found -> {
                throw new BadRequestAlertException(
                    "A sheet already exists for this hospitalisation and date",
                    ENTITY_NAME,
                    "duplicate_hosp_date"
                );
            });

        final SurveillanceSheetDTO result = surveillanceSheetService.update(dto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, String.valueOf(result.getId())))
            .body(result);
    }

    // --------------------------------------------------------------------------------------------
    // PARTIAL UPDATE (PATCH): hospitalisationId remains immutable
    // --------------------------------------------------------------------------------------------
    @PatchMapping(value = "/surveillance-sheets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurveillanceSheetDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SurveillanceSheetDTO dto
    ) {
        log.debug("REST to partial update SurveillanceSheet : {}, {}", id, dto);

        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!surveillanceSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        final Long currentHospId = surveillanceSheetRepository.findById(id).map(ss -> ss.getHospitalisation().getId()).orElse(null);

        // Forbid changing hospitalisationId
        if (dto.getHospitalisationId() != null && !Objects.equals(dto.getHospitalisationId(), currentHospId)) {
            throw new BadRequestAlertException("Cannot change hospitalisation of a sheet", ENTITY_NAME, "hospitalisation_immutable");
        }

        // If sheetDate is provided and changes, re-check uniqueness
        if (dto.getSheetDate() != null) {
            surveillanceSheetRepository
                .findByHospitalisationIdAndSheetDate(currentHospId, dto.getSheetDate())
                .filter(found -> !Objects.equals(found.getId(), id))
                .ifPresent(found -> {
                    throw new BadRequestAlertException(
                        "A sheet already exists for this hospitalisation and date",
                        ENTITY_NAME,
                        "duplicate_hosp_date"
                    );
                });
        }

        Optional<SurveillanceSheetDTO> result = surveillanceSheetService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, String.valueOf(id))
        );
    }

    // --------------------------------------------------------------------------------------------
    // LIST & QUERY
    // --------------------------------------------------------------------------------------------

    /**
     * GET /surveillance-sheets : list all sheets, optionally filtered by hospitalisationId.
     */
    @GetMapping("/surveillance-sheets")
    public ResponseEntity<Page<SurveillanceSheetDTO>> getAll(
        @RequestParam(value = "hospitalisationId", required = false) Long hospitalisationId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST to get page of SurveillanceSheets (hospitalisationId={})", hospitalisationId);

        Page<SurveillanceSheetDTO> page = (hospitalisationId == null)
            ? surveillanceSheetService.findAll(pageable)
            : surveillanceSheetService.findByHospitalisation(hospitalisationId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * GET /hospitalisations/{hospitalisationId}/surveillance-sheets : list sheets for a hospitalisation.
     */
    @GetMapping("/hospitalisations/{hospitalisationId}/surveillance-sheets")
    public ResponseEntity<Page<SurveillanceSheetDTO>> getByHospitalisation(
        @PathVariable Long hospitalisationId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST to get page of SurveillanceSheets for hospitalisation {}", hospitalisationId);

        if (!hospitalisationRepository.existsById(hospitalisationId)) {
            throw new BadRequestAlertException("Hospitalisation not found", ENTITY_NAME, "hospitalisationnotfound");
        }

        Page<SurveillanceSheetDTO> page = surveillanceSheetService.findByHospitalisation(hospitalisationId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * GET /hospitalisations/{hospitalisationId}/surveillance-sheets/by-date?date=YYYY-MM-DD :
     * fetch single sheet for an hospitalisation and a given date.
     */
    @GetMapping("/hospitalisations/{hospitalisationId}/surveillance-sheets/by-date")
    public ResponseEntity<SurveillanceSheetDTO> getByHospitalisationAndDate(
        @PathVariable Long hospitalisationId,
        @RequestParam("date") LocalDate date
    ) {
        log.debug("REST to get SurveillanceSheet by (hospitalisationId={}, date={})", hospitalisationId, date);

        if (!hospitalisationRepository.existsById(hospitalisationId)) {
            throw new BadRequestAlertException("Hospitalisation not found", ENTITY_NAME, "hospitalisationnotfound");
        }
        Optional<SurveillanceSheetDTO> dto = surveillanceSheetService.findByHospitalisationAndDate(hospitalisationId, date);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    // --------------------------------------------------------------------------------------------
    // GET ONE
    // --------------------------------------------------------------------------------------------
    @GetMapping("/surveillance-sheets/{id}")
    public ResponseEntity<SurveillanceSheetDTO> get(@PathVariable Long id) {
        log.debug("REST to get SurveillanceSheet : {}", id);
        Optional<SurveillanceSheetDTO> dto = surveillanceSheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    // --------------------------------------------------------------------------------------------
    // DELETE
    // --------------------------------------------------------------------------------------------
    @DeleteMapping("/surveillance-sheets/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST to delete SurveillanceSheet : {}", id);
        surveillanceSheetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, String.valueOf(id)))
            .build();
    }
}
