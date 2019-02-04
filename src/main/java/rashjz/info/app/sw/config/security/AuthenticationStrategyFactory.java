package rashjz.info.app.sw.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationStrategyFactory {

    private final Map<String, AuthenticationProvider> authenticationProviderMap;


    AuthenticationProvider getAuthenticationProvider(String authenticationProvider) {
        return authenticationProviderMap.get(authenticationProvider);
    }
}