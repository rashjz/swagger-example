package rashjz.info.app.sw.mappings;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;
import rashjz.info.app.sw.domain.Person;

public  class PersonContextMapper extends AbstractContextMapper<Person> {
    public Person doMapFromContext(DirContextOperations context) {
        Person person = new Person();
        person.setFullName(context.getStringAttribute("cn"));
        person.setLastName(context.getStringAttribute("sn"));
        person.setUid(context.getStringAttribute("uid"));
        return person;
    }
}