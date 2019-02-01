package rashjz.info.app.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class SwaggerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerExampleApplication.class, args);
    }


}

