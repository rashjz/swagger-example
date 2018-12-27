package rashjz.info.app.sw.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;
import rashjz.info.app.sw.config.properties.LdapProperties;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.mappings.PersonAttributesMapper;
import rashjz.info.app.sw.mappings.PersonContextMapper;
import rashjz.info.app.sw.util.LdapSchema;

import javax.annotation.PostConstruct;
import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class PersonRepository implements BaseLdapNameAware {
    private static final Integer THREE_SECONDS = 3000;

    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    private final LdapProperties ldapProperties;

    @Autowired
    public PersonRepository(LdapProperties ldapProperties) {
        this.ldapProperties = ldapProperties;
    }

    @PostConstruct
    private void init() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://localhost:" + ldapProperties.getPort());
        ldapContextSource.setBase(ldapProperties.getBaseDn());
        ldapContextSource.afterPropertiesSet();
        ldapTemplate = new LdapTemplate(ldapContextSource);
    }


    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public List<Person> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes(LdapSchema.USER_NAME.getValue())
                .base(LdapUtils.emptyLdapName())
                .where(LdapSchema.OBJECT_CLASS.getValue()).is("person")
                .and(LdapSchema.LAST_NAME.getValue()).not().is(lastName)
                .and(LdapSchema.LAST_NAME.getValue()).like("j*hn")
                .and(LdapSchema.UID.getValue()).isPresent();

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{LdapSchema.USER_NAME.getValue(),
                LdapSchema.UID.getValue(),
                LdapSchema.PASSWORD.getValue(),
                LdapSchema.LAST_NAME.getValue()});

        String filter = "(&(objectclass=top)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{LdapSchema.USER_NAME.getValue()});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LdapSchema.OBJECT_CLASS.getValue(), "person"));
        filter.and(new EqualsFilter(LdapSchema.LAST_NAME.getValue(), lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PersonAttributesMapper());
    }


    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<Person> findByName(String name) {
        LdapQuery q = query()
                .where(LdapSchema.OBJECT_CLASS.getValue()).is("person")
                .and(LdapSchema.USER_NAME.getValue()).whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    public List<Person> findByNameAndPassword(String name, String passw) {
        LdapQuery q = query()
                .where(LdapSchema.OBJECT_CLASS.getValue()).is("person")
                .and(LdapSchema.USER_NAME.getValue()).whitespaceWildcardsLike(name)
                .and("userPassword").whitespaceWildcardsLike(passw);
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
        BasicAttribute objectClass = new BasicAttribute("objectclass");
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
