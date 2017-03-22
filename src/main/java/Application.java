import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

class Application {

    private static User user;
    public static final int MIN_PASS_LENGTH = 4;
    public static final int MAX_USERS_NUM = 9;
    public static final int MAX_SECRET_FUNCTION_TRY_NUM = 5;


    private static float secretFunction(int x){
        return x*x;
//        return (float) (Math.tan(x*4));
    }

    public static void checkSecretFunction() throws IllegalArgumentException {
        short x = (short)(Math.random()*10);
        float secretValue;
        System.out.println("Enter secret answer (request = " + x + " ) ");
        boolean flag;
        int cnt = 0;
        do {
            secretValue = scanSecretFunction();
            flag = checkSecretAnswer(x, secretValue);
            if (!flag){
                cnt++;
            }
            if(cnt >= MAX_SECRET_FUNCTION_TRY_NUM){
                changePass();
                flag = true;
            }
        }while (!flag);

    }

    static User authenticate() {
        try {
            boolean flag;
            String login;
            do {
                login = scanLogin();
                String pass = scanPassword();
                byte[] passBytes = new byte[pass.length()];
                for (int i = 0; i < pass.length(); i++) {
                    passBytes[i] = (byte) pass.charAt(i);
                }
                flag = authUser(login, passBytes);

            } while(!flag);
            user = JsonUtils.getUser(login);
            user.setAccessType(User.SystemAccess.Allow);
            successAuth();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }
    private static boolean checkSecretAnswer(short secretRequest, float secretAnswer) throws IllegalArgumentException {
        return ((secretFunction(secretRequest)) == secretAnswer) ;

    }

    private static void successAuth() {
        System.out.println("\nAuthentification successful\n");
    }

    static User registerNewUser() {
        try {
            String login = scanLogin();
            String pass;
            String secondPass;
            boolean flag1;
            boolean flag2;
            byte[] passBytes = createPass();
            User.PermissionLevel[] pLevel = {User.PermissionLevel.NONE, User.PermissionLevel.NONE, User.PermissionLevel.NONE};

            user = new User(login, passBytes, pLevel);
            checkSecretFunction();
            JsonUtils.addUser(user);
            user.setAccessType(User.SystemAccess.Allow);
            successAuth();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            user.setAccessType(User.SystemAccess.Deny);
        }

        String time = Calendar.getInstance().getTime().toString();
        FileUtils.writeLog(time + " Account " + user.getLogin() + " created");
        return user;
    }

    private static boolean changePass(){
        System.out.println("Input old password");
        String oldPass;
        byte[] oldPassByte;
        boolean flag = false;
        boolean equals = false;
        do {
            oldPass = justScan();
            oldPassByte = castToBytes(oldPass);
            if (oldPassByte.length == user.getPassword().length) {
                for (int i = 0; i < user.getPassword().length; i++) {
                    if (oldPassByte[i] != user.getPassword()[i]) {
                        equals = false;
                        break;
                    }
                }
                equals = true;
            } else equals = false;
            if (equals) {
                user.setPassword(createPass());
//                JsonUtils.changeUser(user);    don't open. dead inside
                flag = true;
            } else System.out.println("False. Try again");
        } while (!flag);
        return true;
    }

    private static byte[] createPass(){
        String pass;
        String secondPass;
        boolean flag1;
        boolean flag2;
        do {
            do {
                System.out.print("Input password: ");
                pass = justScan();
                if (pass.length() < MIN_PASS_LENGTH) {
                    System.out.println("Your password is too short");
                    flag1 = true;
                } else {
                    flag1 = false;
                }
            } while (flag1);

            System.out.print("Confirm password: ");
            secondPass = justScan();
            if(secondPass.equals(pass)){
                flag2 = true;
            } else {
                flag2 = false;
                System.out.println("Passwords do not match. Try again.");
            }
        } while (!flag2);

        return castToBytes(pass);
    }

    private static byte[] castToBytes(String s){
        byte b[] = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            b[i] = (byte)s.charAt(i);
        }
        return b;
    }

    private static boolean authUser(String login, byte[] pass) {
        try {
            if (!JsonUtils.isUserExist(login, pass)) {
                System.out.println("User does not exist or the password is wrong");
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File does not exist");
        }
    }

    private static String scanLogin() throws IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input login: ");
        String login;
        if (sc.hasNext()) {
            login = sc.nextLine();
        } else {
            throw new IllegalArgumentException("Invalid login value");
        }
        return login;
    }


    private static String scanPassword() throws IllegalArgumentException {
        System.out.print("Input password: ");
        Scanner sc = new Scanner(System.in);
        String password;
        if (sc.hasNext()) {
            password = sc.nextLine();

        } else {
            throw new IllegalArgumentException("Invalid password value");
        }
        return password;
    }

    private static String justScan() throws IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        String password;
        if (sc.hasNext()) {
            password = sc.nextLine();

        } else {
            throw new IllegalArgumentException("Invalid password value");
        }
        return password;
    }

    private static int scanSecretFunction() throws IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input secret function value: ");
        int secretValue;
        if (sc.hasNext()) {
            secretValue = sc.nextInt();
        } else {
            throw new IllegalArgumentException("Invalid secret value");
        }
        return secretValue;
    }

}
