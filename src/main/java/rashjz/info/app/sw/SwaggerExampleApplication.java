package rashjz.info.app.sw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import rashjz.info.app.sw.domain.Authority;
import rashjz.info.app.sw.domain.User;
import rashjz.info.app.sw.respositories.UserRepository;

import java.util.Collections;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SwaggerExampleApplication implements CommandLineRunner {
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SwaggerExampleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        userRepository.save(User.builder()
                .password("admin")
                .username("admin")
                .isAccountNonLocked(true)
                .isEnabled(true)
                .authorities(Collections.singleton(Authority.builder()
                        .authority("USER_ROLE")
                        .build()))
                .build());
    }



}

