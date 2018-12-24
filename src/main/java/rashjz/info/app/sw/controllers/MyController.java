package rashjz.info.app.sw.controllers;


import java.security.Principal;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Arrays.*;


@Controller
public class MyController {

    @GetMapping("/")
    public String init(Map<String, Object> model, Principal principal) {
        model.put("title", "PUBLIC AREA");
        model.put("message", "Only Authorised user can view this page");
        model.put("username", getUserName(principal));
        model.put("userroles", getUserRoles(principal));
        return "home";
    }



    private String getUserName(Principal principal) {
        if (principal == null) {
            return "anonymous";
        } else {

            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println(grantedAuthority.getAuthority());
            }
            return principal.getName();
        }
    }

    private Collection<String> getUserRoles(Principal principal) {
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