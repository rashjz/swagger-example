package rashjz.info.app.sw.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import rashjz.info.app.sw.respositories.PersonRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepository;

    @Autowired
    public CustomAuthenticationProvider(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (checkLdapUser(name, password)) {
            //  authenticate against the third-party system
            log.info("Authentication passed with user : {} and password {}", name, password);
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                    authentication.getCredentials(),
                    Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));
        } else {
            return null;
        }
    }

    private boolean checkLdapUser(String name, String password) {
        List persons = personRepository.findByNameAndPassword(name, password);
        return persons != null && persons.get(0) != null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}