package rashjz.info.app.sw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathBeanPostProcessor;
import org.springframework.ldap.core.support.LdapContextSource;
import rashjz.info.app.sw.config.properties.LdapProperties;

@Configuration
public class LdapConfig {

    private final LdapProperties ldapProperties;

    @Autowired
    public LdapConfig(LdapProperties ldapProperties) {
        this.ldapProperties = ldapProperties;
    }

    @Bean
    public BaseLdapPathBeanPostProcessor ldapPathBeanPostProcessor() {
        BaseLdapPathBeanPostProcessor baseLdapPathBeanPostProcessor = new BaseLdapPathBeanPostProcessor();
        baseLdapPathBeanPostProcessor.setBasePath(ldapProperties.getBaseDn());
        return baseLdapPathBeanPostProcessor;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://localhost:" + ldapProperties.getPort());
        ldapContextSource.setBase(ldapProperties.getBaseDn());
        ldapContextSource.afterPropertiesSet();
        return new LdapTemplate(ldapContextSource);
    }
}