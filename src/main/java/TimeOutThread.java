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
            if (!Application.repeatChecking()) {
                System.out.println("all right");
//                parentThread.resume();
                try {
                    parentThread.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
