package rashjz.info.app.sw.mappings;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.util.LdapProperties;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class PersonAttributesMapper implements AttributesMapper<Person> {
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
        Person person = new Person();
        person.setFullName((String) attrs.get(LdapProperties.USER_NAME.getValue()).get());
        person.setUserPassword(attrs.get(LdapProperties.PASSWORD.getValue()) != null ?
                String.valueOf(attrs.get(LdapProperties.PASSWORD.getValue()).get()) : StringUtils.EMPTY);
        person.setUid(attrs.get(LdapProperties.UID.getValue()) != null ?
                String.valueOf(attrs.get(LdapProperties.UID.getValue()).get()) : StringUtils.EMPTY);
        person.setLastName(attrs.get(LdapProperties.LAST_NAME.getValue()) != null ?
                String.valueOf(attrs.get(LdapProperties.LAST_NAME.getValue()).get()) : StringUtils.EMPTY);
        return person;
    }
}