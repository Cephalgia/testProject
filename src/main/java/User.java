import java.util.Collection;

/**
 * Created by Karina on 26.02.2017.
 */
class User {
    private final String login;
    private final byte[] password;
    private SystemAccess accessType;
    private PermissionLevel level;
    private PermissionLevel[] discPermission;

    User(String login, byte[] password, PermissionLevel[] permissions) {
        this.login = login;
        this.password = password;
        this.discPermission = permissions;
    }

    enum SystemAccess {
        Allow,
        Deny
    }

    enum PermissionLevel {
        NONE,
        READ,
        WRITE,
        EXECUTE
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

    public PermissionLevel[] getDiscPermission() {
        return discPermission;
    }

    public void setDiscPermission(PermissionLevel[] discPermission) {
        this.discPermission = discPermission;
    }
}
