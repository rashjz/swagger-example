package rashjz.info.app.sw.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import rashjz.info.app.sw.respositories.PersonRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepository;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;


    @Autowired
    public CustomAuthenticationProvider(PersonRepository personRepository,
                                        LoginAttemptService loginAttemptService,
                                        HttpServletRequest request) {
        this.personRepository = personRepository;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new LockedException("account locked for 10 minutes");
        }
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (checkLdapUser(name, password)) {
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
        if (persons.size() > 0) {
            return persons.get(0) != null;
        } else {
            throw new BadCredentialsException("Invalid credentials please check your username and password!");
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}