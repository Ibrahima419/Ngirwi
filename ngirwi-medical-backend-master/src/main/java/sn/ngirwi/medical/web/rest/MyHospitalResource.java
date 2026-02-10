package sn.ngirwi.medical.web.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.repository.HospitalRepository;
import sn.ngirwi.medical.service.CurrentHospitalProvider;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for getting the current user's hospital.
 * This endpoint is secure because it only returns the hospital
 * associated with the authenticated user's JWT token.
 * 
 * Security: The hospitalId comes from the server (via CurrentHospitalProvider),
 * not from the client request. Users cannot access other hospitals.
 */
@RestController
@RequestMapping("/api")
public class MyHospitalResource {

    private final Logger log = LoggerFactory.getLogger(MyHospitalResource.class);

    private final HospitalRepository hospitalRepository;
    private final CurrentHospitalProvider currentHospitalProvider;

    public MyHospitalResource(HospitalRepository hospitalRepository, CurrentHospitalProvider currentHospitalProvider) {
        this.hospitalRepository = hospitalRepository;
        this.currentHospitalProvider = currentHospitalProvider;
    }

    /**
     * {@code GET  /my-hospital} : get the current user's hospital.
     *
     * This uses the same pattern as HospitalisationService.generatePdf():
     * - Gets hospitalId from CurrentHospitalProvider (based on JWT)
     * - Fetches hospital from repository
     * - No user-controllable parameters
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospital,
     *         or with status {@code 404 (Not Found)} if the user has no hospital.
     */
    @GetMapping("/my-hospital")
    public ResponseEntity<Hospital> getMyHospital() {
        log.debug("REST request to get current user's hospital");
        
        Long hospitalId = currentHospitalProvider.getCurrentHospitalId().orElse(null);
        if (hospitalId == null) {
            log.debug("Current user has no hospitalId");
            return ResponseEntity.notFound().build();
        }
        
        Optional<Hospital> hospital = hospitalRepository.findById(hospitalId);
        return ResponseUtil.wrapOrNotFound(hospital);
    }
}
