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
        int secretValue = scanSecretFunction();
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

    private static void checkSecretAnswer(short secretRequest, int secretAnswer) throws IllegalArgumentException {
        if ((secretRequest*secretRequest) != secretAnswer) {
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
            boolean flag;
            do {
                pass = scanPassword();
                if(pass.length() < 6) {
                    System.out.println("Your password is too short");
                    flag = true;
                } else {
                    flag = false;
                }
            } while(flag);
            byte[] passBytes = new byte[pass.length()];
            for (int i = 0; i < pass.length(); i++) {
               passBytes[i] = (byte)pass.charAt(i);
            }
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
        return user;
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
        Scanner sc = new Scanner(System.in);
        System.out.print("Input password: ");
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
