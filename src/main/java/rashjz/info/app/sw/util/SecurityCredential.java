package rashjz.info.app.sw.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SecurityCredential {
    private SecurityCredential() {
    }

    public static String getUserName(Principal principal) {
        return principal == null ? "anonymous" : principal.getName();
    }

    public static Collection<String> getUserRoles(Principal principal) {
        if (principal == null) {
            return Collections.singletonList("none");
        } else {
            Set<String> roles = new HashSet<>();
            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }
            return roles;
        }
    }

}
