package rashjz.info.app.sw.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import rashjz.info.app.sw.config.properties.ApplicationProperties;

import java.util.Arrays;
import java.util.Collections;

import static rashjz.info.app.sw.util.AppConstraints.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String COMMA_SEPARATOR = ",";
    private static final String[] IGNORED_REQUEST_CONFIGURER = {
            "/resources/**",
            "/static/**",
            "/v2/api-docs",
            "/webjars/**",
            "/error"};

    private final AuthenticationStrategyFactory authenticationStrategyFactory;
    private final ApplicationProperties applicationProperties;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/error", "/api/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        configure(http.logout());
        configure(http.headers());
        configure(http.csrf());
        configure(http.cors());

    }

    private void configure(HeadersConfigurer<HttpSecurity> headers) {
        headers.httpStrictTransportSecurity().disable();
    }

    private void configure(LogoutConfigurer<HttpSecurity> logout) {
        logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //not recommended by spring better use post request to logout
                .logoutSuccessUrl("/login")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies(SESSION_COOKIE_NAME)
                .permitAll();
    }

    private void configure(CsrfConfigurer<HttpSecurity> csrf) {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_HEADER_NAME);
        csrf.csrfTokenRepository(repository);
    }

    private void configure(CorsConfigurer<HttpSecurity> cors) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        if (applicationProperties.getCors().isAllowed()) {
            corsConfiguration.setAllowCredentials(applicationProperties.getCors().isAllowCredentials());
            corsConfiguration.setAllowedOrigins(Arrays.asList(applicationProperties.getCors().getAllowOrigin().split(COMMA_SEPARATOR)));
            corsConfiguration.setAllowedHeaders(Arrays.asList(applicationProperties.getCors().getAllowHeaders().split(COMMA_SEPARATOR)));
            corsConfiguration.setAllowedMethods(Arrays.asList(applicationProperties.getCors().getAllowMethods().split(COMMA_SEPARATOR)));
        }
        cors.configurationSource(httpServletRequest -> corsConfiguration);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(IGNORED_REQUEST_CONFIGURER);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationStrategyFactory
                .getAuthenticationProvider(applicationProperties.getAuthType()));
    }


    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(
                Collections.singletonList("ldap://localhost:8389/"),
                "dc=springframework,dc=org");
    }
}