package ra.edu.presentation.admin;

import ra.edu.MainApplication;
import ra.edu.presentation.technology.TechnologyUI;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class AdminMain {
    public static void displayMenuManagentAdmin() {
        int choice;
        do {
            System.out.println("\n===== HỆ THỐNG QUẢN LÝ TUYỂN DỤNG =====");
            System.out.println("1. Quản lý công nghệ tuyển dụng");
            System.out.println("2. Quản lý ứng viên");
            System.out.println("3. Quản lý vị trí tuyển dụng");
            System.out.println("4. Quản lý đơn tuyển dụng");
            System.out.println("5. Đăng xuất");
            System.out.print("Mời bạn chọn chức năng: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    TechnologyUI.displayMenuTechnology();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    logoutAdmin();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 5);
    }

    public static void logoutAdmin() {
        writeToFile("");

        System.out.println("Bạn đã đăng xuất thành công.");
        pause(1);

        MainApplication.displayMenuApplication();
    }
}
