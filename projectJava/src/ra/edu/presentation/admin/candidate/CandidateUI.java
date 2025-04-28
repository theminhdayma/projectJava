package ra.edu.presentation.admin.candidate;

import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.utils.Color;
import ra.edu.utils.SendEmail;
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
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    public static void displayMenuCandidate() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("QUẢN LÝ ỨNG VIÊN", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Hiển thị danh sách ứng viên");
            System.out.printf("| %-3s | %-50s |\n", "2", "Khóa/mở khóa tài khoản ứng viên");
            System.out.printf("| %-3s | %-50s |\n", "3", "Reset mật khẩu ứng viên");
            System.out.printf("| %-3s | %-50s |\n", "4", "Tìm kiếm ứng viên theo tên");
            System.out.printf("| %-3s | %-50s |\n", "5", "Lọc ứng viên theo yêu cầu");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay về menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

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
                case 0:
                    System.out.println("\nĐang quay về menu chính...");
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);
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
            System.out.println("0. Thoát");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");

            switch (choice) {
                case 1:
                    lockCandidate(candidate);
                    return;
                case 2:
                    unlockCandidate(candidate);
                    return;
                case 0:
                    System.out.println("Thoát khỏi chức năng xử lý tài khoản.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    break;
            }
        } while (choice != 0);
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
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
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
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
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

                        SendEmail.sendPasswordEmail(candidate.getEmail(), password);
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
                candidate.getAccount().getStatus().getDisplayName()
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
            System.out.println("4. Lọc theo công nghệ ứng viên");
            System.out.println("0. Quay lại menu chính");

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
                    filterByCandidateTechnology();
                    break;
                case 0:
                    System.out.println("Quay lại menu chính...");
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    break;
            }
        } while (choice != 0);
    }

    private static void filterByGender() {
        int genderChoice;
        do {
            System.out.println("=== LỌC ỨNG VIÊN THEO GIỚI TÍNH ===");
            System.out.println("1. NAM");
            System.out.println("2. NỮ");
            System.out.println("3. KHÁC");
            System.out.println("0. Quay lại");

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
                case 0:
                    System.out.println("Quay lại menu chính.");
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    break;
            }
        } while (genderChoice != 0);
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

    private static void displayAllTechnologies() {
        List<Technology> technologies = technologyService.getAllTechnology();
        if (technologies.isEmpty()) {
            System.out.println(Color.RED + "Không có công nghệ nào để hiển thị." + Color.RESET);
            return;
        }

        System.out.println(Color.BLUE + Color.BOLD + "\nDANH SÁCH CÔNG NGHỆ HIỆN CÓ" + Color.RESET);

        int idWidth = 4;
        int nameWidth = technologies.stream()
                .mapToInt(t -> t.getName().length())
                .max()
                .orElse(20);
        nameWidth = Math.max(nameWidth, 20);

        String top = "┌" + "─".repeat(idWidth + 2) + "┬" + "─".repeat(nameWidth + 2) + "┐";
        String mid = "├" + "─".repeat(idWidth + 2) + "┼" + "─".repeat(nameWidth + 2) + "┤";
        String bot = "└" + "─".repeat(idWidth + 2) + "┴" + "─".repeat(nameWidth + 2) + "┘";

        // In bảng
        System.out.println(Color.YELLOW + top);
        String header = String.format("│ %-"+idWidth+"s │ %-"+nameWidth+"s │", "ID", "Tên Công Nghệ");
        System.out.println(header);
        System.out.println(mid);

        for (Technology tech : technologies) {
            String row = String.format("│ %-"+idWidth+"d │ %-"+nameWidth+"s │",
                    tech.getId(), tech.getName());
            System.out.println(row);
        }

        System.out.println(bot + Color.RESET);
    }

    private static void filterByCandidateTechnology() {
        int choice;
        do {
            List<Technology> technologies = technologyService.getAllTechnology();
            displayAllTechnologies();
            System.out.println(Color.CYAN + "0. Quay lại" + Color.RESET);

            choice = Validator.validateInputInt(scanner, "Mời bạn chọn ID công nghệ để lọc ứng viên: ");
            if (choice == 0) {
                System.out.println(Color.GREEN + "Quay lại menu chính." + Color.RESET);
                pause(1);
                break;
            }

            int choiceId = choice;
            boolean isValidTechnology = technologies.stream().anyMatch(t -> t.getId() == choiceId);
            if (!isValidTechnology) {
                System.out.println(Color.RED + "ID công nghệ không hợp lệ, vui lòng chọn lại!" + Color.RESET);
                continue;
            }

            List<Candidate> filteredCandidates = candidateService.filterByCandidateTechnology(choice);
            displayFilteredCandidates(filteredCandidates);

        } while (true);
    }
}