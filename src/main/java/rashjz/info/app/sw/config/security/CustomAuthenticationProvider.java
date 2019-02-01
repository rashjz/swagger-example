package rashjz.info.app.sw.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rashjz.info.app.sw.domain.Authority;
import rashjz.info.app.sw.domain.User;
import rashjz.info.app.sw.respositories.PersonRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PersonRepository personRepository;
    private final LoginAttemptService loginAttemptService;


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

            User principal = User.builder().isEnabled(true)
                    .password(password)
                    .username(username)
                    .authorities(Collections.singleton(Authority
                            .builder()
                            .authority("ROLE_USER")
                            .build()))
                    .build();
            return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());

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