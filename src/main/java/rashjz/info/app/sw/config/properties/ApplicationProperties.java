package rashjz.info.app.sw.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String description;
    private String version;
    private String title;
    private String url;
    private String author;
    private String authorEmail;
    private String license;
    private String licenseUrl;
}
