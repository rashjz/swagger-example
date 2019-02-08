package rashjz.info.app.sw.util;

import java.util.stream.Stream;

public enum LdapSchema {

    USER_NAME("cn"),
    LAST_NAME("sn"),
    PASSWORD("userPassword"),
    UID("uid"),
    OU("ou"),
    OBJECT_CLASS("objectclass"),
    NONE("");

    LdapSchema(final String value) {
        this.value = value;
    }

      private final String value;

    public static LdapSchema findByVal(String filter) {
        return Stream.of(values())
                .filter(val -> filter.equalsIgnoreCase(val.value))
                .findFirst()
                .orElse(LdapSchema.NONE);
    }

    public String getValue() {
        return value;
    }

}
