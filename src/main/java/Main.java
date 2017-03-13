import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        Scanner sc = new Scanner(System.in);
        int choice = 1;
        if (sc.hasNext()) {
            choice = sc.nextInt();
        }
        do {
            User user = (choice == 1) ? Application.authenticate() : Application.registerNewUser();
            UserThread userThread = new UserThread(user);
            userThread.start();
            try {
                userThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(true);
    }
}
