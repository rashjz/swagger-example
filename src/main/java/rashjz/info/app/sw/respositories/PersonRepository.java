package rashjz.info.app.sw.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.mappings.PersonAttributesMapper;
import rashjz.info.app.sw.mappings.PersonContextMapper;
import rashjz.info.app.sw.util.LdapSchema;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class PersonRepository {
    private static final Integer THREE_SECONDS = 3000;
    private final LdapTemplate ldapTemplate;


    @Autowired
    public PersonRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }


    public List<Person> getPersonByUsernameAndPassword(String username, String password) {
        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes(LdapSchema.USER_NAME.getValue())
                .base(LdapUtils.emptyLdapName())
                .where(LdapSchema.OBJECT_CLASS.getValue()).is("person")
                .and(LdapSchema.UID.getValue()).is(username)
                .and(LdapSchema.PASSWORD.getValue()).is(password);
        return ldapTemplate.search(query, new PersonAttributesMapper());
    }


    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }


    public List<Person> findByUIDAndPassword(String name, String passw) {
        LdapQuery q = query()
                .where(LdapSchema.OBJECT_CLASS.getValue()).is("person")
                .and(LdapSchema.UID.getValue()).whitespaceWildcardsLike(name)
                .and(LdapSchema.PASSWORD.getValue()).whitespaceWildcardsLike(passw);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    private Name buildDn(Person p) {
        return LdapNameBuilder.newInstance()
                .add(LdapSchema.OU.getValue(), "people")
                .add(LdapSchema.UID.getValue(), p.getUid())
                .build();
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute objectClass = new BasicAttribute(LdapSchema.OBJECT_CLASS.getValue());
        objectClass.add("top");
        objectClass.add("person");

        attrs.put(objectClass);
        attrs.put(LdapSchema.OU.getValue(), "people");
        attrs.put(LdapSchema.USER_NAME.getValue(), p.getFullName());
        attrs.put(LdapSchema.LAST_NAME.getValue(), p.getLastName());
        attrs.put(LdapSchema.UID.getValue(), p.getUid());
        attrs.put(LdapSchema.PASSWORD.getValue(), p.getUserPassword());
        return attrs;
    }


}
