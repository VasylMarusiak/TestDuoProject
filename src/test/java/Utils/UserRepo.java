package Utils;

import lombok.experimental.UtilityClass;

import java.util.ResourceBundle;

@UtilityClass
public class UserRepo {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("user");
    public static User getVasylMarusyakUser() {
        return User
                .builder()
                .email(getIfExist(bundle, "vasyl.marusyak.email"))
                .password(getIfExist(bundle, "vasyl.marusyak.pass"))
                .build();
    }

    public static User getIrunaShemraiUser() {
        return User
                .builder()
                .email(getIfExist(bundle, "iruna.shemrai.email"))
                .password(getIfExist(bundle, "iruna.shemrai.pass"))
                .build();
    }

    private static String getIfExist(String key) {
        return getIfExist(bundle, key);
    }

    private static String getIfExist(ResourceBundle bundle, String key) {
        return bundle != null && bundle.containsKey(key) && !bundle.getString(key).isEmpty()
                ? bundle.getString(key)
                : System.getProperty(key);
    }

    private static String get(String property) {
        return bundle.getString(property);
    }
}
