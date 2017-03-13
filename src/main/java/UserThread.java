import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Karina on 26.02.2017.
 */
class UserThread extends Thread {
    private User user;
    private int commandInterval = 3;
    private int commandCounter = 0;

    UserThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        boolean flag = true;
        do {
            if(commandCounter == commandInterval) {
                if(Application.repeatChecking()) {
                    flag = false;
                }
                commandCounter = 0;
            }
            commandCounter++;
            System.out.println("USER: " + user.getLogin());
            System.out.println("Input command: >");
            String command = "";
            Scanner sc = new Scanner(System.in);
            if (sc.hasNext()) {
                command = sc.nextLine();
            }
            executeCommand(command);
        } while (flag);
    }

    private void executeCommand(String line) {
        String[] divide = line.split(" ");
        String command = divide[0];
        String path = divide[1];
        boolean wrongCommand = false;
        switch (command) {
            case "cat":
                user.setAccessType(User.SystemAccess.Allow);
                break;
            case "nano":
                if(user.getLevel() == User.PermissionLevel.WRITE) {
                    user.setAccessType(User.SystemAccess.Allow);
                } else {
                    user.setAccessType(User.SystemAccess.Deny);
                }
                break;
            default:
                wrongCommand = true;
                System.out.println("Wrong command!");
        }
        if(!wrongCommand) {
            String time = Calendar.getInstance().getTime().toString();
            if (user.getPermissionType() == User.SystemAccess.Allow) {
                System.out.println("Access granted");
                FileUtils.writeLog(time + "  User " + user.getLogin() + "; got access to the file " + path + "; Status: " + user.getPermissionType().name() + "\n");
            } else {
                System.out.println("Access denied");
                FileUtils.writeLog("Access denied. " + time + " User " + user.getLogin() + "; tried to get access to the file " + path + "\n");
            }
        }

    }

}
