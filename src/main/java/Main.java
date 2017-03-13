import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        Scanner sc = new Scanner(System.in);
        int choice = 1;
        User user;

        do {
            if (sc.hasNext()) {
                choice = sc.nextInt();
            }
            if(choice == 1){
                user = Application.authenticate();
            } else {
                if(JsonUtils.getUsersNum() > Application.MAX_USERS_NUM){
                    System.out.println("Reached the limit of users in the system");
                    System.out.println("You can only log in");

                    user = Application.authenticate();
                } else {
                    user = Application.registerNewUser();
                }
            }
//            User user = (choice == 1) ? Application.authenticate() : Application.registerNewUser();
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
