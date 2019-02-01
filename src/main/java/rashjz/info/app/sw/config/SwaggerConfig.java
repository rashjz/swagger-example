package rashjz.info.app.sw.config;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rashjz.info.app.sw.config.properties.ApplicationProperties;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .select().apis(RequestHandlerSelectors.basePackage("rashjz.info.app.sw.controllers"))
                .paths(regex("/api.*"))
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()));

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
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}