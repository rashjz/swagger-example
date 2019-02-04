package rashjz.info.app.sw.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import rashjz.info.app.sw.domain.User;
import rashjz.info.app.sw.respositories.UserRepository;

import java.util.List;

@Slf4j
@Component("dbAuthenticationProvider")
@RequiredArgsConstructor
public class DBAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<User> userList = userRepository.findByUsernameAndPassword(username, password);
        if (userList.isEmpty()) {
            throw new BadCredentialsException("invalid credentials");
        } else {
            User user = userList.get(0);
            log.info("user is {}", user.toString());
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
