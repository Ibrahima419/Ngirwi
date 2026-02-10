package sn.ngirwi.medical.web.rest;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.ngirwi.medical.domain.User;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.security.AuthoritiesConstants;
import sn.ngirwi.medical.security.SecurityUtils;
import sn.ngirwi.medical.service.dto.DoctorDTO;

/**
 * REST controller for getting doctors from the same hospital.
 * Accessible by users with DOCTOR or ADMIN role.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private final UserRepository userRepository;

    public DoctorResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@code GET /doctors} : get all doctors from the same hospital as the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctors.
     */
    @GetMapping("/doctors")
    @PreAuthorize("hasAnyAuthority('" + AuthoritiesConstants.DOCTOR + "', '" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<List<DoctorDTO>> getDoctorsFromSameHospital() {
        log.debug("REST request to get doctors from same hospital");

        // Get current user's hospital ID
        Long currentUserHospitalId = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneWithAuthoritiesByLogin)
            .map(User::getHospitalId)
            .orElse(null);

        if (currentUserHospitalId == null) {
            log.warn("Current user has no hospital assigned");
            return ResponseEntity.ok(List.of());
        }

        // Get all users from the same hospital with ROLE_DOCTOR authority
        List<User> doctors = userRepository.findByHospitalId(currentUserHospitalId)
            .stream()
            .filter(user -> user.getAuthorities().stream()
                .anyMatch(auth -> AuthoritiesConstants.DOCTOR.equals(auth.getName())))
            .collect(Collectors.toList());

        // Convert to DTOs
        List<DoctorDTO> doctorDTOs = doctors.stream()
            .map(DoctorDTO::new)
            .collect(Collectors.toList());

        log.debug("Found {} doctors in hospital {}", doctorDTOs.size(), currentUserHospitalId);
        return ResponseEntity.ok(doctorDTOs);
    }
}



