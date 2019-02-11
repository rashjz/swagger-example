package rashjz.info.app.sw.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.ldap.embedded")
public class LdapProperties {

    private String ldif;
    private String baseDn;
    private String port;
}
