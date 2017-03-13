import java.util.Collection;

/**
 * Created by Karina on 26.02.2017.
 */
class User {
    private final String login;
    private final byte[] password;
    private SystemAccess accessType;
    private PermissionLevel level;

    User(String login, byte[] password, PermissionLevel level) {
        this.login = login;
        this.password = password;
        this.level = level;
    }

    enum SystemAccess {
        Allow,
        Deny
    }

    enum PermissionLevel {
        READ,
        WRITE
    }

    String getLogin() {
        return login;
    }

    byte[] getPassword() {
        return password;
    }

    SystemAccess getPermissionType() {
        return accessType;
    }

    void setAccessType(SystemAccess accessType) {
        this.accessType = accessType;
    }

    public PermissionLevel getLevel() {
        return level;
    }
}
