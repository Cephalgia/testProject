/**
 * Created by cephalgia on 13.03.17.
 */
public class Admin extends User {

    public Admin(String login, byte[] password, PermissionLevel[] permissions) {
        super(login, password, permissions);
    }

}
