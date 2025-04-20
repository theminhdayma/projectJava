package ra.edu.presentation.candidate.profile;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.presentation.admin.AdminMain;
import ra.edu.presentation.candidate.CandidateMain;
import ra.edu.validate.Validator;
import ra.edu.validate.candidate.CandidateValidate;

import java.time.LocalDate;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.FileUtil.readFromFile;
import static ra.edu.utils.ThreadUtil.pause;

public class ProfileUI {
    private static final CandidateService candidateService = new CandidateServiceImp();
    public static void displayMenuProfile() {
        int choice;
        do {
            System.out.println("\n====== TRANG CÁ NHÂN ======");
            System.out.println("1. Thay đổi thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Hiểm thị thông tin cá nhân");
            System.out.println("4. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    updateCandidateInfo();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    showProfile();
                    break;
                case 4:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 4.");
            }
        } while (choice != 4);
    }

    private static Candidate getCandidateLogin() {
        String token = readFromFile();
        Candidate candidate = null;

        if (token != null && !token.isBlank()) {
            String[] parts = token.split(":");
            candidate = candidateService.getCandidateByEmail(parts[1]);
        }

        return candidate;
    }

    private static void showProfile () {
        Candidate candidate = getCandidateLogin();
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên.");
            return;
        }

        System.out.println("\n== THÔNG TIN ỨNG VIÊN HIỆN TẠI ==");
        System.out.println("+----------------------+------------------------------+");
        System.out.printf("| %-20s | %-28s |\n", "Họ và tên", candidate.getName());
        System.out.printf("| %-20s | %-28s |\n", "Số điện thoại", candidate.getPhone());
        System.out.printf("| %-20s | %-28s |\n", "Kinh nghiệm (năm)", candidate.getExperience());
        System.out.printf("| %-20s | %-28s |\n", "Giới tính", candidate.getGender());
        System.out.printf("| %-20s | %-28s |\n", "Mô tả", candidate.getDescription());
        System.out.printf("| %-20s | %-28s |\n", "Ngày sinh", candidate.getDob());
        System.out.println("+----------------------+------------------------------+");
    }

    private static void updateCandidateInfo() {
        Candidate candidate = getCandidateLogin();
        showProfile();

        int choice;
        do {
            System.out.println("\n== CẬP NHẬT THÔNG TIN ỨNG VIÊN ==");
            System.out.println("1. Cập nhật tên");
            System.out.println("2. Cập nhật số điện thoại");
            System.out.println("3. Cập nhật số năm kinh nghiệm");
            System.out.println("4. Cập nhật giới tính");
            System.out.println("5. Cập nhật mô tả");
            System.out.println("6. Cập nhật ngày sinh");
            System.out.println("7. Lưu và thoát");

            choice = Validator.validateInputInt(scanner, "Chọn thông tin muốn cập nhật: ");

            switch (choice) {
                case 1:
                    candidate.setName(CandidateValidate.inputValidName(scanner));
                    break;
                case 2:
                    candidate.setPhone(CandidateValidate.inputValidPhone(scanner));
                    break;
                case 3:
                    candidate.setExperience(Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm mới: "));
                    break;
                case 4:
                    candidate.setGender(CandidateValidate.inputValidGender(scanner));
                    break;
                case 5:
                    candidate.setDescription(CandidateValidate.inputValidDes(scanner));
                    break;
                case 6:
                    candidate.setDob(CandidateValidate.inputValidDob(scanner));
                    break;
                case 7:
                    if (candidateService.update(candidate)) {
                        System.out.println("Cập nhật thông tin ứng viên thành công.");
                    } else {
                        System.err.println("Cập nhật thất bại.");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        } while (choice != 7);
    }


    private static void changePassword() {
        Candidate candidate = getCandidateLogin();
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên.");
            return;
        }

        String oldPassword = CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu hiện tại: ");
        if (!candidate.getAccount().getPassword().equals(oldPassword)) {
            System.err.println("Mật khẩu hiện tại không đúng. Vui lòng thử lại.");
            return;
        }

        String newPassword = CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu mới: ");
        String confirmPassword;

        do {
            confirmPassword = CandidateValidate.inputValidPassword(scanner, "Xác nhận mật khẩu mới: ");

            if (!newPassword.equals(confirmPassword)) {
                System.err.println("Mật khẩu xác nhận không khớp. Vui lòng thử lại.");
            }
        } while (!newPassword.equals(confirmPassword));

        boolean result = candidateService.changePasswordCandidate(candidate.getAccount().getId(), newPassword);
        if (result) {
            System.out.println("Đổi mật khẩu thành công!");
        } else {
            System.err.println("Có lỗi xảy ra khi đổi mật khẩu.");
        }
    }



}
