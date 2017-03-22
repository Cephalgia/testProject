
public class TimeOutThread extends Thread {
    UserThread parentThread;

    public TimeOutThread(UserThread parentThread) {
        this.parentThread = parentThread;
        run();
    }

    @Override
    public void run() {
        while (true) {
            Application.checkSecretFunction();
                try {
                    sleep(240000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

