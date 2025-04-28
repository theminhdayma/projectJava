package ra.edu.utils;

public class ThreadUtil {
    public static void pause(int seconds) {
        System.out.print("Loading");
        int dotDelay = (seconds * 1000) / 3;

        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(dotDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print(".");
        }
        System.out.println();
    }

}
