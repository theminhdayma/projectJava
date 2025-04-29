package ra.edu.utils;

public class ThreadUtil {
    public static void pause(int seconds) {
        System.out.print(Color.BLUE + "Loading" + Color.RESET);
        int dotDelay = (seconds * 1000) / 3;

        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(dotDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print(Color.BLUE + "." + Color.RESET);
        }
        System.out.println();
    }

}
