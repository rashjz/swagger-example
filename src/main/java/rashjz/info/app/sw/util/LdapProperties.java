package rashjz.info.app.sw.util;

import java.util.Arrays;

public enum LdapProperties {

    USER_NAME("cn"),
    LAST_NAME("sn"),
    PASSWORD("userPassword"),
    UID("uid"),
    OBJECT_CLASS("objectclass"),
    NONE("");

    LdapProperties(final String value) {
        this.value = value;
    }

      private final String value;

    public static LdapProperties findByVal(String filter) {
        return Arrays.stream(values())
                .filter(val -> filter.equalsIgnoreCase(val.value))
                .findFirst()
                .orElse(LdapProperties.NONE);
    }

    public String getValue() {
        return value;
    }

}
