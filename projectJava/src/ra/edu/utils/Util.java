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
}
