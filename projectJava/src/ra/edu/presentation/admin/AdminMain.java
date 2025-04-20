package ra.edu.presentation.admin;

import ra.edu.MainApplication;
import ra.edu.presentation.admin.application.ApplicationUI;
import ra.edu.presentation.admin.candidate.CandidateUI;
import ra.edu.presentation.admin.recruitmentPosition.RecruitmentPositionUI;
import ra.edu.presentation.admin.technology.TechnologyUI;
import ra.edu.validate.Validator;

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
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    TechnologyUI.displayMenuTechnology();
                    break;
                case 2:
                    CandidateUI.displayMenuCandidate();
                    break;
                case 3:
                    RecruitmentPositionUI.displayMenuRecruitmentPosition();
                    break;
                case 4:
                    ApplicationUI.displayMenuApplication();
                    break;
                case 5:
                    System.out.println("\nLoading...");
                    pause(1);
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
