/**
 * Created by someone on 13.03.17.
 */
public class TimeOutThread extends Thread {
    UserThread parentThread;

    public TimeOutThread(UserThread parentThread) {
        this.parentThread = parentThread;
        run();
    }

    @Override
    public void run() {
        while (true) {
//            parentThread.suspend();
            System.out.println("hah");
            Application.checkSecretFunction();
//            parentThread.resume();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

