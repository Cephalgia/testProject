import java.util.Collection;

class User {
    private final String login;
    private byte[] password;
    private SystemAccess accessType;
    private PermissionLevel level;
    private PermissionLevel[] discPermission;
    private boolean adminPermission;

    User(String login, byte[] password, PermissionLevel[] permissions) {
        this.login = login;
        this.password = password;
        this.discPermission = permissions;
        adminPermission = false;
    }

    enum SystemAccess {
        Allow,
        Deny
    }

    enum PermissionLevel {
        NONE {
            int compare(PermissionLevel c){
                return (c == NONE) ? 0 : -1;
            }
        },
        READ {
            int compare(PermissionLevel c){
               switch (c){
                   case NONE: return 1;
                   case READ: return 0;
                   case EXECUTE:
                   case WRITE:
                       return -1;
                   default:
                       return -2;
               }
            }
        },
        WRITE {
            int compare(PermissionLevel c){
                switch (c){
                    case EXECUTE: return -1;
                    case WRITE: return 0;
                    case NONE:
                    case READ:
                        return 1;
                    default:return -2;
                }
            }
        },
        EXECUTE {
            int compare(PermissionLevel c){
               switch (c){
                   case NONE:
                   case READ:
                   case WRITE:
                       return 1;
                   case EXECUTE:
                       return 0;
                   default:
                       return -2;
               }
            }
        }
    }

    String getLogin() {
        return login;
    }

    byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] newPass){
        password = newPass;
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

    public PermissionLevel[] getDiscPremissions(){
        return discPermission;
    }
}
