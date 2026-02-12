package sn.ngirwi.medical.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Regression test: every authority constant that represents a real role
 * (i.e. not ANONYMOUS) must use the ROLE_ prefix so it matches what is
 * stored in jhi_authority / jhi_user_authority and what Spring Security
 * puts into the JWT "auth" claim.
 *
 * This test exists because a past bug had DOCTOR = "DOCTOR" while the
 * DB stored "ROLE_DOCTOR", causing menu items and @PreAuthorize checks
 * to silently fail.
 */
class AuthoritiesConstantsTest {

    @Test
    void allNonAnonymousAuthorityConstantsMustHaveRolePrefix() throws Exception {
        List<String> violations = new ArrayList<>();

        for (Field field : AuthoritiesConstants.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (field.getType() != String.class) {
                continue;
            }
            String name = field.getName();
            String value = (String) field.get(null);

            // ANONYMOUS is a special case — Spring Security convention
            if ("ANONYMOUS".equals(name)) {
                continue;
            }

            if (!value.startsWith("ROLE_")) {
                violations.add(name + " = \"" + value + "\" (expected ROLE_ prefix)");
            }
        }

        assertThat(violations)
            .as("Authority constants without ROLE_ prefix — this WILL cause menu/security mismatches with jhi_authority")
            .isEmpty();
    }

    @Test
    void doctorConstantMatchesDbValue() {
        assertThat(AuthoritiesConstants.DOCTOR)
            .as("DOCTOR constant must be ROLE_DOCTOR to match jhi_authority table")
            .isEqualTo("ROLE_DOCTOR");
    }

    @Test
    void adminConstantMatchesDbValue() {
        assertThat(AuthoritiesConstants.ADMIN).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void userConstantMatchesDbValue() {
        assertThat(AuthoritiesConstants.USER).isEqualTo("ROLE_USER");
    }
}
