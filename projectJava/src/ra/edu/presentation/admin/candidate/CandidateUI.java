package ra.edu.presentation.admin.candidate;

import ra.edu.business.model.candidate.Active;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.validate.Validator;

import java.security.SecureRandom;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;

public class CandidateUI {
    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final int LIMIT = 5;
    public static void displayMenuCadidate() {
        int choice;
        do {
            System.out.println("\n====== QUẢN LÝ ỨNG VIÊN ======");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc ứng viên theo yêu cầu");
            System.out.println("6. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Chọn: ");

            switch (choice) {
                case 1:
                    showAllCandidate();
                    break;
                case 2:
                    lockOrUnlockCandidate();
                    break;
                case 3:
                    resetCandidatePassword();
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 6.");
            }
        } while (choice != 6);
    }

    private static Candidate findCandidateById(int id) {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            System.out.println("Không tìm thấy công nghệ với ID: " + id + " Vui lòng thử lại.");
        }
        return candidate;
    }

    private static void showAllCandidate() {
        int totalPage = candidateService.getTotalPage(LIMIT);

        if (totalPage == 0) {
            System.out.println("Không có ứng viên nào để hiển thị.");
            return;
        }

        while (true) {
            System.out.println("\n== DANH SÁCH TRANG ỨNG VIÊN ==");
            for (int i = 1; i <= totalPage; i++) {
                System.out.printf("%d. Trang %d\n", i, i);
            }
            System.out.println("0. Thoát");

            int pageChoice;
            do {
                System.out.print("Chọn trang muốn xem: ");
                try {
                    pageChoice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    pageChoice = -1;
                }
            } while (pageChoice < 0 || pageChoice > totalPage);

            if (pageChoice == 0) {
                System.out.println("Thoát khỏi hiển thị công nghệ.");
                pause(1);
                break;
            }

            System.out.println("\n== ỨNG VIÊN TRANG " + pageChoice + " ==");
            System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
            System.out.printf("| %-3s | %-20s | %-23s | %-12s | %-8s | %-10s |\n", "ID", "Tên", "Email", "SĐT", "Exp", "Trạng thái");
            System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
            candidateService.getCandidateByPage(pageChoice, LIMIT)
                    .forEach(candidate -> System.out.printf("| %-3d | %-20s | %-23s | %-12s | %-8d | %-10s |\n",
                            candidate.getId(),
                            candidate.getName(),
                            candidate.getEmail(),
                            candidate.getPhone(),
                            candidate.getExperience(),
                            candidate.getActive() == Active.UNLOCKED ? "Hoạt động" : "Đã khóa"
                    ));
            System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
        }
    }

    private static void lockOrUnlockCandidate() {
        int id = Validator.validateInputInt(scanner, "Nhập ID ứng viên cần xử lý: ");
        Candidate candidate = findCandidateById(id);
        if (candidate == null) {
            System.out.println("Ứng viên không tồn tại.");
            return;
        }

        int choice;
        do {
            System.out.println("Chọn hành động:");
            System.out.println("1. Khóa tài khoản");
            System.out.println("2. Mở khóa tài khoản");
            System.out.println("3. Thoát");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn (1/2/3): ");

            switch (choice) {
                case 1:
                    if (candidate.getActive() == Active.UNLOCKED) {
                        if (candidateService.lockCandidateAccount(id)) {
                            System.out.println("Đã khóa tài khoản thành công.");
                        } else {
                            System.out.println("Khóa tài khoản thất bại.");
                        }
                    } else {
                        System.out.println("Tài khoản này đã bị khóa rồi.");
                    }
                    break;

                case 2:
                    if (candidate.getActive() == Active.LOCKED) {
                        if (candidateService.unlockCandidateAccount(id)) {
                            System.out.println("Đã mở khóa tài khoản thành công.");
                        } else {
                            System.out.println("Mở khóa tài khoản thất bại.");
                        }
                    } else {
                        System.out.println("Tài khoản này đang hoạt động.");
                    }
                    break;

                case 3:
                    System.out.println("Thoát khỏi chức năng xử lý tài khoản.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
        } while (choice != 3);
    }

    private static void resetCandidatePassword() {
        int id = Validator.validateInputInt(scanner, "Nhập ID ứng viên cần reset mật khẩu: ");
        Candidate candidate = findCandidateById(id);
        if (candidate == null) return;

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+[]{}|;:,.<>?/~";
        SecureRandom random = new SecureRandom();
        StringBuilder newPassword = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            newPassword.append(characters.charAt(index));
        }

        String password = newPassword.toString();
        System.out.println("Mật khẩu mới cho ứng viên là: " + password);

        boolean success = candidateService.resetCandidatePassword(id, password);
        if (success) {
            System.out.println("Reset mật khẩu thành công.");
        } else {
            System.out.println("Reset mật khẩu thất bại.");
        }
    }

    private static void searchCandidateByName() {
        System.out.print("Nhập tên ứng viên cần tìm: ");
        String keyword = scanner.nextLine().trim();
        List<Candidate> candidates = candidateService.searchCandidateByName(keyword);

        if (candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên nào với từ khóa: " + keyword);
            return;
        }

        System.out.println("\n== KẾT QUẢ TÌM KIẾM ==");
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
        System.out.printf("| %-3s | %-20s | %-23s | %-12s | %-8s | %-10s |\n", "ID", "Tên", "Email", "SĐT", "Exp", "Trạng thái");
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
        candidates.forEach(candidate -> System.out.printf("| %-3d | %-20s | %-23s | %-12s | %-8d | %-10s |\n",
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getPhone(),
                candidate.getExperience(),
                candidate.getActive() == Active.UNLOCKED ? "Hoạt động" : "Đã khóa"
        ));
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
    }

}
