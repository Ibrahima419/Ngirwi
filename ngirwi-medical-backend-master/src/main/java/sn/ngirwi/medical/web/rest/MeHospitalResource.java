package sn.ngirwi.medical.web.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.repository.HospitalRepository;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.security.SecurityUtils;
import sn.ngirwi.medical.service.dto.MyHospitalDTO;

/**
 * Read-only endpoint to expose the current user's hospital minimal info.
 * This avoids opening /api/hospitals/** (admin-only) to non-admin users.
 */
@RestController
@RequestMapping("/api/me")
public class MeHospitalResource {

    private final Logger log = LoggerFactory.getLogger(MeHospitalResource.class);

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    public MeHospitalResource(UserRepository userRepository, HospitalRepository hospitalRepository) {
        this.userRepository = userRepository;
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * GET /api/me/hospital : get minimal info for current user's hospital.
     *
     * @return 200 with {name, adress, phone} when hospitalId exists, otherwise 204.
     */
    @GetMapping("/hospital")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyHospitalDTO> getMyHospital() {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        if (user.isEmpty()) {
            // Should not happen because endpoint is authenticated, but keep safe.
            return ResponseEntity.status(401).build();
        }

        Long hospitalId = user.get().getHospitalId();
        if (hospitalId == null) {
            return ResponseEntity.noContent().build();
        }

        Optional<Hospital> hospital = hospitalRepository.findById(hospitalId);
        if (hospital.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Hospital h = hospital.get();
        log.debug("REST request to get current user's hospital id={}", hospitalId);
        return ResponseEntity.ok(new MyHospitalDTO(h.getName(), h.getAdress(), h.getPhone()));
    }
}



