import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Andrew on 22.10.2016.
 */
class Application {

    private static ScheduledExecutorService executorService;
    private static int failedNumber = 0;
    private static final int failedCount = 5;
    private static User user;
    public static final int MIN_PASS_LENGTH = 6;
    public static final int MAX_USERS_NUM = 6;

    private static float secretFunction(int x){
        return x*x;
//        return (float) (10*Math.sin((x)));
//        return (float) (Math.sin((10*x)));
    }


    public static boolean repeatChecking() {
        boolean fail = false;
        try {
            checkSecretFunction();
        } catch (IllegalArgumentException e) {
            failedNumber++;
            if (failedCount > failedNumber) {
                repeatChecking();
            } else {
                fail = true;
                System.err.println(e.getMessage());
            }
        }
        return fail;
    }

    private static void checkSecretFunction() throws IllegalArgumentException {
        short x = (short)(Math.random()*10);
        System.out.println("Enter secret answer (request = " + x + " ) ");
        float secretValue = scanSecretFunction();
        checkSecretAnswer(x, secretValue);
    }

    static User authenticate() {
        try {
            boolean flag = false;
            do {
                String login = scanLogin();
                String pass = scanPassword();
                byte[] passBytes = new byte[pass.length()];
                for (int i = 0; i < pass.length(); i++) {
                    passBytes[i] = (byte) pass.charAt(i);
                }
                authUser(login, passBytes);
                checkSecretFunction();
                user = JsonUtils.getUser(login);
                user.setAccessType(User.SystemAccess.Allow);
                successAuth();
            } while(flag);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }
    private static void checkSecretAnswer(short secretRequest, float secretAnswer) throws IllegalArgumentException {
        if ((secretFunction(secretRequest)) != secretAnswer) {
            throw new IllegalArgumentException("Incorrect secret answer");
        }
    }

    private static void successAuth() {
        System.out.println("\nSuccess auth!\n");
    }

    static User registerNewUser() {
        try {
            String login = scanLogin();
            String pass;
            String secondPass;
            boolean flag1;
            boolean flag2;
            byte[] passBytes = createPass();
            User.PermissionLevel[] pLevel = {User.PermissionLevel.NONE, User.PermissionLevel.NONE};

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
        FileUtils.writeLog(time + " User " + user.getLogin() + " registered");
        return user;
    }

    private static void changePass(){
        System.out.println("Input old password");
        String oldPass = scanPassword();
        if(user.getPassword().equals(castToBytes(oldPass))){
            user.setPassword(createPass());
        }
    }

    private static byte[] createPass(){
        String pass;
        String secondPass;
        boolean flag1;
        boolean flag2;
        do {
            do {
                System.out.print("Input password: ");
                pass = scanPassword();
                if (pass.length() < MIN_PASS_LENGTH) {
                    System.out.println("Your password is too short");
                    flag1 = true;
                } else {
                    flag1 = false;
                }
            } while (flag1);

            System.out.print("Confirm password: ");
            secondPass = scanPassword();
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

    private static void authUser(String login, byte[] pass) {
        try {
            if (!JsonUtils.isUserExist(login, pass))
                throw new IllegalArgumentException("User does not exist");
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
