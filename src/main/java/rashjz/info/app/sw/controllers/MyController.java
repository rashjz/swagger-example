package rashjz.info.app.sw.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.respositories.PersonRepository;

import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
public class MyController {
    private final PersonRepository personRepository;

    @Autowired
    public MyController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/")
    public String init(Map<String, Object> model, Principal principal) {
        model.put("title", "PUBLIC AREA");
        model.put("message", "Only Authorised user can view this page");
        model.put("username", getUserName(principal));
        model.put("userroles", getUserRoles(principal));


        personRepository.create(Person.builder()
                .fullName("rashad")
                .lastName("javadov")
                .uid("as")
                .userPassword("test")
                .build());

        List<Person> people = personRepository.findByName("rashad");
        log.info("person is {}", people.get(0).toString());
        return "home";
    }


    private String getUserName(Principal principal) {
        return principal == null ? "anonymous" : principal.getName();
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