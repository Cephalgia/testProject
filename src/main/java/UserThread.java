import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Karina on 26.02.2017.
 */
class UserThread extends Thread {
    private User user;
//    private int commandInterval = 3;
//    private int commandCounter = 0;

    private final long TIME_INTERVAL = 60000 / 2;
    private long lastTime = System.currentTimeMillis();

    UserThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        boolean flag = true;
        System.out.println("USER: " + user.getLogin());
//        new TimeOutThread(this);
        do {
//            if(commandCounter == commandInterval) {
//                if(Application.repeatChecking()) {
//                    flag = false;
//                }
//                commandCounter = 0;
//            }
//            commandCounter++;
            isTimeOut();
            System.out.println("Input command: >");
            String command = "";
            Scanner sc = new Scanner(System.in);
            if (sc.hasNext()) {
                command = sc.nextLine();
            }
            isTimeOut();
            executeCommand(command);
        } while (flag);
    }

    private void executeCommand(String line) {
        String[] divide = line.split(" ");
        String command = divide[0];




        if(divide.length < 2){
            System.out.println("Wrong command format");
            return;
        }

        if(command.equals("chmod")){
            if(JsonUtils.getUser(divide[1]).equals(null)){
                System.out.println("No user with login " + divide[1]);
            } else {
                if(divide.length >=4){
                    User patient = JsonUtils.getUser(divide[1]);
                    int discNum;

                    switch (divide[2]){
//                        case "c":
                        case "C":
                            discNum = 0;
                            break;
//                        case "d":
                        case "D":
                            discNum = 1;
                            break;
//                        case "e":
                        case "E":
                            discNum = 2;
                            break;
                        default:
                            System.out.println("Disc " + divide[2] + " not found");
                            return;
                    }

                    switch (divide[3]){
                        case "r": patient.getDiscPermission()[discNum] = User.PermissionLevel.READ;
                            break;
                        case "w": patient.getDiscPermission()[discNum] = User.PermissionLevel.WRITE;
                            break;
                        case "e": patient.getDiscPermission()[discNum] = User.PermissionLevel.EXECUTE;
                            break;
                        case "n": patient.getDiscPermission()[discNum] = User.PermissionLevel.NONE;
                            break;
                    }
                    JsonUtils.changeUser(patient);
                    System.out.println("User " + divide[2] + " now has " + patient.getDiscPermission()[discNum] + "for disc " + divide[2]);
                } else {
                    System.out.println("Wrong arguments");
                }
            }
            return;
        }

        String[] path = divide[1].split("/");

        boolean wrongCommand = false;
        switch (command) {
            case "cat":
                if((path[0].equals("C") && user.getDiscPermission()[0].compareTo(User.PermissionLevel.NONE)>0) ||
                        (path[0].equals("D") && user.getDiscPermission()[1].compareTo(User.PermissionLevel.NONE)>0) ||
                        (path[0].equals("E") && user.getDiscPermission()[2].compareTo(User.PermissionLevel.NONE)>0)){
                    user.setAccessType(User.SystemAccess.Allow);
                } else {
                    user.setAccessType(User.SystemAccess.Deny);
                }
                break;
            case "nano":
                if((path[0].equals("C") && user.getDiscPermission()[0].compareTo(User.PermissionLevel.WRITE)>-1) ||
                        (path[0].equals("D") && user.getDiscPermission()[1].compareTo(User.PermissionLevel.WRITE)>-1) ||
                        (path[0].equals("E") && user.getDiscPermission()[2].compareTo(User.PermissionLevel.WRITE)>-1)){
                    user.setAccessType(User.SystemAccess.Allow);
                } else {
                    user.setAccessType(User.SystemAccess.Deny);
                }
                break;
            case "sh":
                if((path[0].equals("C") && user.getDiscPermission()[0].compareTo(User.PermissionLevel.EXECUTE)>-1) ||
                        (path[0].equals("D") && user.getDiscPermission()[1].compareTo(User.PermissionLevel.EXECUTE)>-1) ||
                        (path[0].equals("E") && user.getDiscPermission()[2].compareTo(User.PermissionLevel.EXECUTE)>-1)){
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

    private void isTimeOut() {
        if (System.currentTimeMillis() - lastTime >= TIME_INTERVAL) {
            lastTime = System.currentTimeMillis();
            Application.checkSecretFunction();
        }
    }

}
