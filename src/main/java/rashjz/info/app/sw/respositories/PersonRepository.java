package rashjz.info.app.sw.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
 import org.springframework.stereotype.Repository;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.mappings.PersonAttributesMapper;
import rashjz.info.app.sw.mappings.PersonContextMapper;
import rashjz.info.app.sw.util.LdapProperties;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class PersonRepository  implements BaseLdapNameAware {
    private static final Integer THREE_SECONDS = 3000;

    private final LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    @Autowired
    public PersonRepository(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }


    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public List<Person> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes(LdapProperties.USER_NAME.getValue())
                .base(LdapUtils.emptyLdapName())
                .where(LdapProperties.OBJECT_CLASS.getValue()).is("person")
                .and(LdapProperties.LAST_NAME.getValue()).not().is(lastName)
                .and(LdapProperties.LAST_NAME.getValue()).like("j*hn")
                .and(LdapProperties.UID.getValue()).isPresent();

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{LdapProperties.USER_NAME.getValue(),
                LdapProperties.UID.getValue(),
                LdapProperties.PASSWORD.getValue(),
                LdapProperties.LAST_NAME.getValue()});

        String filter = "(&(objectclass=top)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{LdapProperties.USER_NAME.getValue()});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LdapProperties.OBJECT_CLASS.getValue(), "person"));
        filter.and(new EqualsFilter(LdapProperties.LAST_NAME.getValue(), lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PersonAttributesMapper());
    }



    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }
    public List<Person> findByName(String name) {
        LdapQuery q = query()
                .where("objectclass").is("person")
                .and("cn").whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }
    private Name buildDn(Person p) {
        return LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("uid", p.getUid())
                .build();
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute objectClass = new BasicAttribute("objectclass");
        objectClass.add("top");
        objectClass.add("person");
        attrs.put(objectClass);
        attrs.put("ou", "people");
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        attrs.put("uid", p.getUid());
        attrs.put("userPassword", p.getUserPassword());
        return attrs;
    }



}
