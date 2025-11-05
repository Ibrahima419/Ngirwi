package sn.ngirwi.medical.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ngirwi.medical.repository.ConsultationRepository;
import sn.ngirwi.medical.security.AuthoritiesConstants;
import sn.ngirwi.medical.service.ConsultationService;
import sn.ngirwi.medical.service.dto.ConsultationDTO;
import sn.ngirwi.medical.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ngirwi.medical.domain.Consultation}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.DOCTOR + "\")")
public class ConsultationResource {

    private final Logger log = LoggerFactory.getLogger(ConsultationResource.class);

    private static final String ENTITY_NAME = "consultation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultationService consultationService;

    private final ConsultationRepository consultationRepository;

    public ConsultationResource(ConsultationService consultationService, ConsultationRepository consultationRepository) {
        this.consultationService = consultationService;
        this.consultationRepository = consultationRepository;
    }

    /**
     * {@code POST  /consultations} : Create a new consultation.
     *
     * @param consultationDTO the consultationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultationDTO, or with status {@code 400 (Bad Request)} if the consultation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultations")
    public ResponseEntity<ConsultationDTO> createConsultation(@Valid @RequestBody ConsultationDTO consultationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Consultation : {}", consultationDTO);
        if (consultationDTO.getId() != null) {
            throw new BadRequestAlertException("A new consultation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsultationDTO result = consultationService.save(consultationDTO);
        return ResponseEntity
            .created(new URI("/api/consultations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultations/:id} : Updates an existing consultation.
     *
     * @param id the id of the consultationDTO to save.
     * @param consultationDTO the consultationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultationDTO,
     * or with status {@code 400 (Bad Request)} if the consultationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultations/{id}")
    public ResponseEntity<ConsultationDTO> updateConsultation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConsultationDTO consultationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Consultation : {}, {}", id, consultationDTO);
        if (consultationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsultationDTO result = consultationService.update(consultationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultations/:id} : Partial updates given fields of an existing consultation, field will ignore if it is null
     *
     * @param id the id of the consultationDTO to save.
     * @param consultationDTO the consultationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultationDTO,
     * or with status {@code 400 (Bad Request)} if the consultationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consultationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consultations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultationDTO> partialUpdateConsultation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConsultationDTO consultationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultation partially : {}, {}", id, consultationDTO);
        if (consultationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultationDTO> result = consultationService.partialUpdate(consultationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consultations} : get all the consultations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultations in body.
     */
    @GetMapping("/consultations")
    public ResponseEntity<List<ConsultationDTO>> getAllConsultations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("ordonance-is-null".equals(filter)) {
            log.debug("REST request to get all Consultations where ordonance is null");
            return new ResponseEntity<>(consultationService.findAllWhereOrdonanceIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Consultations");
        Page<ConsultationDTO> page;
        if (eagerload) {
            page = consultationService.findAllWithEagerRelationships(pageable);
        } else {
            page = consultationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/consultationsbis/{id}")
    public ResponseEntity<List<ConsultationDTO>> getAllConsultations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload,
        @PathVariable Long id
    ) {
        log.debug("REST request to get a page of Consultations " + id);
        Page<ConsultationDTO> page;
        page = consultationService.findAll(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultations/:id} : get the "id" consultation.
     *
     * @param id the id of the consultationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultations/{id}")
    public ResponseEntity<ConsultationDTO> getConsultation(@PathVariable Long id) {
        log.debug("REST request to get Consultation : {}", id);
        Optional<ConsultationDTO> consultationDTO = consultationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultationDTO);
    }

    /**
     * {@code DELETE  /consultations/:id} : delete the "id" consultation.
     *
     * @param id the id of the consultationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        log.debug("REST request to delete Consultation : {}", id);
        consultationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
