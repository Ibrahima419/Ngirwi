package sn.ngirwi.medical.service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import sn.ngirwi.medical.repository.UserRepository;
import sn.ngirwi.medical.security.AuthoritiesConstants;
import sn.ngirwi.medical.security.SecurityUtils;

/**
 * Helper to resolve the current user's hospital context.
 * If the user has a hospitalId, it will be returned.
 * If the user is admin without hospitalId, no filter is applied (Optional.empty()).
 */
@Component
public class CurrentHospitalProvider {

    private final UserRepository userRepository;

    public CurrentHospitalProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @return Optional hospitalId for the current user. Empty if none or admin without hospital scope.
     */
    public Optional<Long> getCurrentHospitalId() {
        // Admin sans hospitalId : pas de restriction
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return Optional.empty();
        }
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .map(u -> u.getHospitalId())
            .filter(Objects::nonNull);
    }
}






