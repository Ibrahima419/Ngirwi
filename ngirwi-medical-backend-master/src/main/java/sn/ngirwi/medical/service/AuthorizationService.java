package sn.ngirwi.medical.service;


import sn.ngirwi.medical.security.RolePermissions;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public boolean hasPermission(Authentication auth, String permission) {
        return auth.getAuthorities().stream()
            .anyMatch(authority ->
                RolePermissions.PERMISSIONS_BY_ROLE
                    .getOrDefault(authority.getAuthority(), java.util.List.of())
                    .contains(permission)
            );
    }
}

