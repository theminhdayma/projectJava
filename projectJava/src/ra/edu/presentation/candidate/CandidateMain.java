package ra.edu.presentation.candidate;

import ra.edu.presentation.candidate.application.ApplicationApplied;
import ra.edu.presentation.candidate.profile.ProfileUI;
import ra.edu.presentation.candidate.recruitmentPositionAndApply.RecruitmentPositionAndApply;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.logout;

public class CandidateMain {
    public static void displayMenuCadidateManagent() {
        int choice;
        do {
            System.out.println("\n" + Color.YELLOW + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("HỆ THỐNG TUYỂN DỤNG", Color.WIDTH) + Color.RESET);
            System.out.println(Color.YELLOW + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Quản lý thông tin cá nhân");
            System.out.printf("| %-3s | %-50s |\n", "2", "Xem và nộp đơn ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "3", "Xem đơn đã ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "0", "Đăng xuất");

            System.out.println(Color.YELLOW + Color.repeat("-", Color.WIDTH) + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1:
                    ProfileUI.displayMenuProfile();
                    break;
                case 2:
                    RecruitmentPositionAndApply.recruitmentPositionAndApply();
                    break;
                case 3:
                    ApplicationApplied.menuApplicationApplied();
                    break;
                case 0:
                    logout();
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);
    }

}
