package rashjz.info.app.sw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public SwaggerConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .select().apis(RequestHandlerSelectors.basePackage("rashjz.info.app.sw.controllers"))
                .paths(regex("/product.*"))
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                applicationProperties.getTitle(),
                applicationProperties.getDescription(),
                applicationProperties.getVersion(),
                "",
                new Contact(applicationProperties.getAuthor(),
                        applicationProperties.getUrl(),
                        applicationProperties.getAuthorEmail()),
                applicationProperties.getLicense(),
                applicationProperties.getLicenseUrl());
    }

}