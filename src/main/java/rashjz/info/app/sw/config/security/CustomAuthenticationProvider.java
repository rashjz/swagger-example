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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rashjz.info.app.sw.domain.User;
import rashjz.info.app.sw.respositories.PersonRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepository;
    private final LoginAttemptService loginAttemptService;


    @Autowired
    public CustomAuthenticationProvider(PersonRepository personRepository,
                                        LoginAttemptService loginAttemptService) {
        this.personRepository = personRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new LockedException("Account locked for 10 minutes");
        }
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List persons = personRepository.getPersonByUsernameAndPassword(username, password);
        if (persons.isEmpty()) {
            throw new BadCredentialsException("Invalid credentials please check your username and password!");
        } else {
            log.info("Authentication passed with user : {} and password {}", username, password);

            return new UsernamePasswordAuthenticationToken(User.builder().isEnabled(true)
                    .password(password)
                    .username(username)
                    .build(),
                    authentication.getCredentials(),
                    Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));
        }
    }


    private String getClientIP() {
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}