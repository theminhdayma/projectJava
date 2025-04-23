package ra.edu.utils;

import ra.edu.MainApplication;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class Util {
    public final static int LIMIT = 5;

    public final static void logout() {
        writeToFile("");

        System.out.println("Bạn đã đăng xuất thành công.");
        pause(1);

        MainApplication.displayMenuApplication();
    }

    public final static String getAccountLogin() {
        String token = FileUtil.readFromFile();
        if (token != null && !token.isBlank()) {
            String[] parts = token.split(":");
            return parts[1];
        } else {
            return null;
        }
    }

    public static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
