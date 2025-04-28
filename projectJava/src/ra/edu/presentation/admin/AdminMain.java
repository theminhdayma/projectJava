package ra.edu.presentation.admin;

import ra.edu.presentation.admin.application.ApplicationUI;
import ra.edu.presentation.admin.candidate.CandidateUI;
import ra.edu.presentation.admin.recruitmentPosition.RecruitmentPositionUI;
import ra.edu.presentation.admin.technology.TechnologyUI;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.Util.logout;

public class AdminMain {
    public static void displayMenuManagentAdmin() {
        int choice;
        do {
            System.out.println("\n" + Color.YELLOW + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("HỆ THỐNG QUẢN LÝ TUYỂN DỤNG", Color.WIDTH) + Color.RESET);
            System.out.println(Color.YELLOW + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Quản lý công nghệ tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "2", "Quản lý ứng viên");
            System.out.printf("| %-3s | %-50s |\n", "3", "Quản lý vị trí tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "4", "Quản lý đơn tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "0", "Đăng xuất");

            System.out.println(Color.YELLOW + Color.repeat("-", Color.WIDTH) + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

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
                case 0:
                    logout();
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);
    }

}
