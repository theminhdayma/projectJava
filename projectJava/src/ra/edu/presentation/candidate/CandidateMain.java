package ra.edu.presentation.candidate;

import ra.edu.MainApplication;
import ra.edu.presentation.candidate.profile.ProfileUI;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class CandidateMain {
    public static void displayMenuCadidateManagent() {
        int choice;
        do {
            System.out.println("\n====== HỆ THỐNG TUYỂN DỤNG ======");
            System.out.println("1. Quản lý thông tin cá nhân");
            System.out.println("2. Xem và nộp đơn ứng tuyển");
            System.out.println("3. Xem đơn đã ứng tuyển");
            System.out.println("4. Đăng xuất");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    ProfileUI.displayMenuProfile();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("\nLoading...");
                    pause(1);
                    logoutCandidate();
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 4.");
            }
        } while (choice != 4);
    }

    public static void logoutCandidate() {
        writeToFile("");

        System.out.println("Bạn đã đăng xuất thành công.");
        pause(1);

        MainApplication.displayMenuApplication();
    }
}
