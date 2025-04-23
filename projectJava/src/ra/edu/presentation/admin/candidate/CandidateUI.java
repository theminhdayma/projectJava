package ra.edu.presentation.admin.candidate;

import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidate.Gender;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.validate.Validator;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;

public class CandidateUI {
    private static final CandidateService candidateService = new CandidateServiceImp();
    public static void displayMenuCandidate() {
        int choice;
        do {
            System.out.println("\n====== QUẢN LÝ ỨNG VIÊN ======");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc ứng viên theo yêu cầu");
            System.out.println("6. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

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
                    searchCandidateByName();
                    break;
                case 5:
                    displayMenuFilter();
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

    private static Candidate findCandidateById() {
        Candidate candidate = null;
        do {
            int id = Validator.validateInputInt(scanner, "Nhập ID ứng viên cần xử lý: ");
            candidate = candidateService.getCandidateById(id);
            if (candidate == null) {
                System.out.println("Không tìm thấy ứng viên với ID: " + id + ". Vui lòng thử lại.");
            }
        } while (candidate == null);
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
                pageChoice = Validator.validateInputInt(scanner, "Chọn trang muốn xem: ");
                if (pageChoice < 0 || pageChoice > totalPage) {
                    System.err.println("Số trang không hợp lệ. Vui lòng chọn lại!");
                }
            } while (pageChoice < 0 || pageChoice > totalPage);

            if (pageChoice == 0) {
                System.out.println("Thoát khỏi hiển thị ứng viên.");
                pause(1);
                break;
            }

            System.out.println("\n== ỨNG VIÊN TRANG " + pageChoice + " ==");
            System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");
            System.out.printf("| %-3s | %-20s | %-23s | %-12s | %-8s | %-8s | %-10s | %-4s | %-10s |\n",
                    "ID", "Tên", "Email", "SĐT", "Exp", "Giới tính", "Ngày sinh", "Tuổi", "Trạng thái");
            System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");

            candidateService.getCandidateByPage(pageChoice, LIMIT)
                    .forEach(candidate -> {
                        int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
                        System.out.printf("| %-3d | %-20s | %-23s | %-12s | %-8d | %-8s | %-10s | %-4d | %-10s |\n",
                                candidate.getId(),
                                candidate.getName(),
                                candidate.getEmail(),
                                candidate.getPhone(),
                                candidate.getExperience(),
                                candidate.getGender().getDisplayName(),
                                candidate.getDob(),
                                age,
                                candidate.getAccount().getStatus().getDisplayName()
                        );
                    });

            System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");
        }
    }

    private static void lockOrUnlockCandidate() {
        Candidate candidate = findCandidateById();
        if (candidate == null) {
            System.out.println("Ứng viên không tồn tại.");
            return;
        }

        int choice;
        do {
            System.out.println("1. Khóa tài khoản");
            System.out.println("2. Mở khóa tài khoản");
            System.out.println("3. Thoát");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    lockCandidate(candidate);
                    return;
                case 2:
                    unlockCandidate(candidate);
                    return;
                case 3:
                    System.out.println("Thoát khỏi chức năng xử lý tài khoản.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
        } while (choice != 3);
    }

    private static void lockCandidate(Candidate candidate) {
        if (candidate.getAccount().getStatus() == AccountStatus.ACTIVE) {
            System.out.println("Bạn có chắc chắn muốn khóa tài khoản này?");
            System.out.println("1. Đồng ý");
            System.out.println("2. Hủy bỏ");
            int confirmation = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (confirmation) {
                case 1:
                    if (candidateService.lockCandidateAccount(candidate.getId())) {
                        System.out.println("Đã khóa tài khoản thành công.");
                    } else {
                        System.out.println("Khóa tài khoản thất bại.");
                    }
                    break;
                case 2:
                    System.out.println("Đã hủy thao tác khóa tài khoản.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } else {
            System.out.println("Tài khoản này đã bị khóa rồi.");
        }
    }

    private static void unlockCandidate(Candidate candidate) {
        if (candidate.getAccount().getStatus() == AccountStatus.INACTIVE) {
            System.out.println("Bạn có chắc chắn muốn mở khóa tài khoản này?");
            System.out.println("1. Đồng ý");
            System.out.println("2. Hủy bỏ");
            int confirmation = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (confirmation) {
                case 1:
                    if (candidateService.unlockCandidateAccount(candidate.getId())) {
                        System.out.println("Đã mở khóa tài khoản thành công.");
                    } else {
                        System.out.println("Mở khóa tài khoản thất bại.");
                    }
                    break;
                case 2:
                    System.out.println("Đã hủy thao tác mở khóa tài khoản.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } else {
            System.out.println("Tài khoản này đang hoạt động.");
        }
    }

    private static void resetCandidatePassword() {
        Candidate candidate = findCandidateById();
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên.");
            return;
        }

        int choice;
        do {
            System.out.println("Bạn có chắc chắn muốn reset mật khẩu cho ứng viên này?");
            System.out.println("1. Đồng ý");
            System.out.println("2. Hủy bỏ");

            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+[]{}|;:,.<>?/~";
                    SecureRandom random = new SecureRandom();
                    StringBuilder newPassword = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        int index = random.nextInt(characters.length());
                        newPassword.append(characters.charAt(index));
                    }

                    String password = newPassword.toString();

                    boolean success = candidateService.resetCandidatePassword(candidate.getId(), password);
                    if (success) {
                        System.out.println("Reset mật khẩu thành công.");
                        System.out.println("Mật khẩu mới cho ứng viên là: " + password);
                    } else {
                        System.out.println("Reset mật khẩu thất bại.");
                    }
                    return;

                case 2:
                    System.out.println("Hủy thao tác reset mật khẩu.");
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    break;
            }
        } while (true);
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
                candidate.getAccount().getStatus() == AccountStatus.ACTIVE ? "Hoạt động" : "Đã khóa"
        ));
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+------------+");
    }

    private static void displayMenuFilter() {
        int choice;
        do {
            System.out.println("\n\n=== LỌC ỨNG VIÊN ===");
            System.out.println("1. Lọc theo giới tính");
            System.out.println("2. Lọc theo số năm kinh nghiệm");
            System.out.println("3. Lọc theo độ tuổi");
            System.out.println("4. Quay lại menu chính");

            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    filterByGender();
                    break;
                case 2:
                    filterByExperience();
                    break;
                case 3:
                    filterByAge();
                    break;
                case 4:
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    break;
            }
        } while (choice != 4);
    }

    private static void filterByGender() {
        int genderChoice;
        do {
            System.out.println("=== LỌC ỨNG VIÊN THEO GIỚI TÍNH ===");
            System.out.println("1. NAM");
            System.out.println("2. NỮ");
            System.out.println("3. KHÁC");
            System.out.println("4. Quay lại");

            genderChoice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (genderChoice) {
                case 1:
                    displayFilteredCandidates(candidateService.filterByGender("MALE"));
                    break;
                case 2:
                    displayFilteredCandidates(candidateService.filterByGender("FEMALE"));
                    break;
                case 3:
                    displayFilteredCandidates(candidateService.filterByGender("OTHER"));
                    break;
                case 4:
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    break;
            }
        } while (genderChoice != 4);
    }

    private static void filterByExperience() {
        int years = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm: ");
        displayFilteredCandidates(candidateService.filterByExperience(years));
    }

    private static void filterByAge() {
        int minAge = Validator.validateInputInt(scanner, "Nhập độ tuổi tối thiểu: ");
        int maxAge = Validator.validateInputInt(scanner, "Nhập độ tuổi tối đa: ");
        displayFilteredCandidates(candidateService.filterByAgeRange(minAge, maxAge));
    }

    private static void displayFilteredCandidates(List<Candidate> filteredList) {
        if (filteredList.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên nào phù hợp với tiêu chí lọc.");
            return;
        }

        System.out.println("\n== KẾT QUẢ LỌC ==");
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");
        System.out.printf("| %-3s | %-20s | %-23s | %-12s | %-8s | %-8s | %-10s | %-4s | %-10s |\n",
                "ID", "Tên", "Email", "SĐT", "Exp", "Giới tính", "Ngày sinh", "Tuổi", "Trạng thái");
        System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");

        filteredList.forEach(candidate -> {
            int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
            String statusStr = candidate.getAccount().getStatus() == AccountStatus.ACTIVE ? "Hoạt động" : "Đã khóa";

            String genderStr = candidate.getGender() == Gender.MALE ? "Nam"
                    : candidate.getGender() == Gender.FEMALE ? "Nữ"
                    : "Khác";

            System.out.printf("| %-3d | %-20s | %-23s | %-12s | %-8d | %-8s | %-10s | %-4d | %-10s |\n",
                    candidate.getId(),
                    candidate.getName(),
                    candidate.getEmail(),
                    candidate.getPhone(),
                    candidate.getExperience(),
                    genderStr,
                    candidate.getDob(),
                    age,
                    statusStr
            );
        });

        System.out.println("+-----+----------------------+-------------------------+--------------+----------+----------+------------+------+------------+");
    }
}