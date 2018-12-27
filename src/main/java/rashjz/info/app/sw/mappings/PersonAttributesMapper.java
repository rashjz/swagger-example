package rashjz.info.app.sw.mappings;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.util.LdapSchema;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class PersonAttributesMapper implements AttributesMapper<Person> {
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
        Person person = new Person();
        person.setFullName((String) attrs.get(LdapSchema.USER_NAME.getValue()).get());
        person.setUserPassword(attrs.get(LdapSchema.PASSWORD.getValue()) != null ?
                String.valueOf(attrs.get(LdapSchema.PASSWORD.getValue()).get()) : StringUtils.EMPTY);
        person.setUid(attrs.get(LdapSchema.UID.getValue()) != null ?
                String.valueOf(attrs.get(LdapSchema.UID.getValue()).get()) : StringUtils.EMPTY);
        person.setLastName(attrs.get(LdapSchema.LAST_NAME.getValue()) != null ?
                String.valueOf(attrs.get(LdapSchema.LAST_NAME.getValue()).get()) : StringUtils.EMPTY);
        return person;
    }
}