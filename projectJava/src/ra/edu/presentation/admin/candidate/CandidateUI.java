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
import static ra.edu.utils.Util.*;

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
                System.out.println(Color.RED + "Không tìm thấy ứng viên với ID: " + id + ". Vui lòng thử lại." + Color.RESET);
            }
        } while (candidate == null);
        return candidate;
    }

    private static void showTableCandidate (List<Candidate> candidates) {
        if (candidates.isEmpty()) {
            System.out.println(Color.RED + "Không có ứng viên nào để hiển thị." + Color.RESET);
            return;
        }
        int idWidth = 5;
        int nameWidth = 20;
        int emailWidth = 23;
        int phoneWidth = 12;
        int expWidth = 8;
        int genderWidth = 10;
        int dobWidth = 12;
        int ageWidth = 4;
        int statusWidth = 10;

        String top = "┌" + "─".repeat(idWidth + 2)
                + "┬" + "─".repeat(nameWidth + 2)
                + "┬" + "─".repeat(emailWidth + 2)
                + "┬" + "─".repeat(phoneWidth + 2)
                + "┬" + "─".repeat(expWidth + 2)
                + "┬" + "─".repeat(genderWidth + 2)
                + "┬" + "─".repeat(dobWidth + 2)
                + "┬" + "─".repeat(ageWidth + 2)
                + "┬" + "─".repeat(statusWidth + 2)
                + "┐";

        String mid = "├" + "─".repeat(idWidth + 2)
                + "┼" + "─".repeat(nameWidth + 2)
                + "┼" + "─".repeat(emailWidth + 2)
                + "┼" + "─".repeat(phoneWidth + 2)
                + "┼" + "─".repeat(expWidth + 2)
                + "┼" + "─".repeat(genderWidth + 2)
                + "┼" + "─".repeat(dobWidth + 2)
                + "┼" + "─".repeat(ageWidth + 2)
                + "┼" + "─".repeat(statusWidth + 2)
                + "┤";

        String bot = "└" + "─".repeat(idWidth + 2)
                + "┴" + "─".repeat(nameWidth + 2)
                + "┴" + "─".repeat(emailWidth + 2)
                + "┴" + "─".repeat(phoneWidth + 2)
                + "┴" + "─".repeat(expWidth + 2)
                + "┴" + "─".repeat(genderWidth + 2)
                + "┴" + "─".repeat(dobWidth + 2)
                + "┴" + "─".repeat(ageWidth + 2)
                + "┴" + "─".repeat(statusWidth + 2)
                + "┘";

        String header = String.format("│ %-" + idWidth + "s │ %-" + nameWidth + "s │ %-" + emailWidth + "s │ %-" + phoneWidth + "s │ %-" + expWidth + "s │ %-" + genderWidth + "s │ %-" + dobWidth + "s │ %-" + ageWidth + "s │ %-" + statusWidth + "s │",
                "ID", "Tên", "Email", "SĐT", "Exp", "Giới tính", "Ngày sinh", "Tuổi", "Trạng thái");

        System.out.println(Color.BOLD + top + Color.RESET);
        System.out.println(Color.BOLD + header + Color.RESET);
        System.out.println(Color.BOLD + mid + Color.RESET);

        candidates.forEach(candidate -> {
            int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
            System.out.printf("│ %-" + idWidth + "d │ %-" + nameWidth + "s │ %-" + emailWidth + "s │ %-" + phoneWidth + "s │ %-" + expWidth + "d │ %-" + genderWidth + "s │ %-" + dobWidth + "s │ %-" + ageWidth + "d │ %-" + statusWidth + "s │\n",
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

        System.out.println(Color.BOLD + bot + Color.RESET);
    }

    private static void showAllCandidate() {
        int limit = Validator.validateInputInt(scanner, Color.CYAN + "Nhập số ứng viên muốn hiển thị trên 1 trang: " + Color.RESET);
        int totalPage = candidateService.getTotalPage(limit);

        if (totalPage == 0) {
            System.out.println("Không có ứng viên nào để hiển thị.");
            return;
        }

        int currentPage = 1;

        while (true) {
            System.out.println("\n" + Color.BOLD + Color.BLUE + "== DANH SÁCH ỨNG VIÊN TRANG " + currentPage + " / " + totalPage + " ==" + Color.RESET);
            List<Candidate> candidates = candidateService.getCandidateByPage(currentPage, limit);
            showTableCandidate(candidates);

            System.out.print("Trang: ");
            for (int i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                } else {
                    System.out.print("[" + i + "] ");
                }
            }
            System.out.println();

            if (totalPage == 1) {
                System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
            } else {
                if (currentPage > 1) System.out.println(Color.CYAN + "p. Trang trước" + Color.RESET);
                if (currentPage < totalPage) System.out.println(Color.CYAN + "n. Trang tiếp theo" + Color.RESET);
                System.out.println(Color.CYAN + "s. Nhập trang muốn hiển thị" + Color.RESET);
                System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
            }

            String choice = ValidateChoicePagination(scanner);
            switch (choice) {
                case "0" -> {
                    System.out.println(Color.YELLOW + "Thoát khỏi hiển thị ứng viên." + Color.RESET);
                    pause(1);
                    return;
                }
                case "p" -> {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.err.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                    }
                }
                case "n" -> {
                    if (currentPage < totalPage) {
                        currentPage++;
                    } else {
                        System.err.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                    }
                }
                case "s" -> {
                    int newPage = Validator.validateInputInt(scanner, Color.CYAN + "Nhập trang muốn xem (1 - " + totalPage + "): " + Color.RESET);
                    if (newPage >= 1 && newPage <= totalPage) {
                        currentPage = newPage;
                    } else {
                        System.err.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                    }
                }
                default -> System.err.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
            }
        }
    }

    private static void showCandidateById(int id) {
        Candidate candidate = candidateService.getCandidateById(id);

        if (candidate == null) {
            System.out.println(Color.RED + "Không tìm thấy ứng viên với ID: " + id + Color.RESET);
            return;
        }

        System.out.println(Color.BLUE + Color.BOLD + "\n=========THÔNG TIN ỨNG VIÊN=========" + Color.RESET);

        System.out.print(Color.BLUE);
        System.out.println("┌──────────────────────┬─────────────────────────────────────────────┐");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "ID"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getId()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Tên"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getName()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Email"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getEmail()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "SĐT"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getPhone()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Kinh nghiệm"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getExperience() + " năm"));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Giới tính"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getGender().getDisplayName()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Ngày sinh"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getDob()));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Mô tả"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", truncate(candidate.getDescription(), 30)));
        System.out.println(Color.BLUE + " │");

        System.out.print("│ "); System.out.print(Color.RESET + String.format("%-20s", "Trạng thái tài khoản"));
        System.out.print(Color.BLUE + " │ "); System.out.print(Color.RESET + String.format("%-43s", candidate.getAccount().getStatus().getDisplayName()));
        System.out.println(Color.BLUE + " │");

        System.out.println("└──────────────────────┴─────────────────────────────────────────────┘");
        System.out.print(Color.RESET);
    }

    private static void lockOrUnlockCandidate() {
        Candidate candidate = findCandidateById();
        if (candidate == null) {
            System.out.println(Color.RED + "Ứng viên không tồn tại." + Color.RESET);
            return;
        }
        showCandidateById(candidate.getId());
        int choice;
        do {
            System.out.println(Color.BOLD + Color.BLUE);
            System.out.println("┌────┬───────────────────────────────┐");
            System.out.println("│  1 │ Khóa tài khoản                │");
            System.out.println("├────┼───────────────────────────────┤");
            System.out.println("│  2 │ Mở khóa tài khoản             │");
            System.out.println("├────┼───────────────────────────────┤");
            System.out.println("│  0 │ Thoát                         │");
            System.out.println("└────┴───────────────────────────────┘");
            System.out.print(Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Nhập lựa chọn: " + Color.RESET);

            switch (choice) {
                case 1 -> {
                    lockCandidate(candidate);
                    return;
                }
                case 2 -> {
                    unlockCandidate(candidate);
                    return;
                }
                case 0 -> {
                    System.out.println(Color.YELLOW + "Thoát khỏi chức năng xử lý tài khoản." + Color.RESET);
                }
                default -> {
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                }
            }
        } while (choice != 0);
    }

    private static void lockCandidate(Candidate candidate) {
        if (candidate.getAccount().getStatus() == AccountStatus.ACTIVE) {
            System.out.println(Color.BOLD + "\nBạn có chắc chắn muốn khóa tài khoản này?" + Color.RESET);
            System.out.println(Color.GREEN + "1. Đồng ý" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Hủy bỏ" + Color.RESET);
            int confirmation = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (confirmation) {
                case 1:
                    if (candidateService.lockCandidateAccount(candidate.getId())) {
                        System.out.println(Color.GREEN + "Đã khóa tài khoản thành công." + Color.RESET);
                    } else {
                        System.out.println(Color.RED + "Khóa tài khoản thất bại." + Color.RESET);
                    }
                    break;
                case 2:
                    System.out.println(Color.YELLOW + "Đã hủy thao tác khóa tài khoản." + Color.RESET);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
                    break;
            }
        } else {
            System.out.println(Color.RED + "Tài khoản này đã bị khóa." + Color.RESET);
        }
    }

    private static void unlockCandidate(Candidate candidate) {
        if (candidate.getAccount().getStatus() == AccountStatus.INACTIVE) {
            System.out.println(Color.BOLD + "\nBạn có chắc chắn muốn mở khóa tài khoản này?" + Color.RESET);
            System.out.println(Color.GREEN + "1. Đồng ý" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Hủy bỏ" + Color.RESET);
            int confirmation = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (confirmation) {
                case 1:
                    if (candidateService.unlockCandidateAccount(candidate.getId())) {
                        System.out.println( Color.GREEN + "Đã mở khóa tài khoản thành công." + Color.RESET);
                    } else {
                        System.out.println(Color.RED + "Mở khóa tài khoản thất bại." + Color.RESET);
                    }
                    break;
                case 2:
                    System.out.println(Color.YELLOW + "Đã hủy thao tác mở khóa tài khoản." + Color.RESET);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
                    break;
            }
        } else {
            System.out.println(Color.RED + "Tài khoản này đang hoạt động." + Color.RESET);
        }
    }

    private static void resetCandidatePassword() {
        Candidate candidate = findCandidateById();

        int choice;
        do {
            System.out.println(Color.BOLD + Color.CYAN + "\nBạn có chắc chắn muốn reset mật khẩu cho ứng viên này?" + Color.RESET);
            System.out.println(Color.GREEN + "1. Đồng ý" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Hủy bỏ" + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.BLUE + "Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1:
                    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                    SecureRandom random = new SecureRandom();
                    StringBuilder newPassword = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        int index = random.nextInt(characters.length());
                        newPassword.append(characters.charAt(index));
                    }

                    String password = newPassword.toString();

                    boolean success = candidateService.resetCandidatePassword(candidate.getId(), password);
                    if (success) {
                        System.out.println(Color.GREEN + "Reset mật khẩu thành công." + Color.RESET);

                        SendEmail.sendPasswordEmail(candidate.getEmail(), password);
                    } else {
                        System.out.println(Color.RED + "Reset mật khẩu thất bại." + Color.RESET);
                    }
                    return;

                case 2:
                    System.out.println(Color.YELLOW + "Hủy thao tác reset mật khẩu." + Color.RESET);
                    return;

                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
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

        System.out.println(Color.BLUE + "\n== KẾT QUẢ TÌM KIẾM ==" + Color.RESET);
        showTableCandidate(candidates);
    }

    private static void displayMenuFilter() {
        int choice;
        do {
            System.out.println(Color.CYAN + Color.BOLD + "\n\n╔══════════════════════════════════╗");
            System.out.println("║          LỌC ỨNG VIÊN            ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║ " + Color.GREEN + "1. Lọc theo giới tính            ║");
            System.out.println("║ " + Color.GREEN + "2. Lọc theo số năm kinh nghiệm   ║");
            System.out.println("║ " + Color.GREEN + "3. Lọc theo độ tuổi              ║");
            System.out.println("║ " + Color.GREEN + "4. Lọc theo công nghệ ứng viên   ║");
            System.out.println("║ " + Color.YELLOW + "0. Quay lại menu chính           ║");
            System.out.println("╚══════════════════════════════════╝" + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.BOLD + "Mời bạn chọn: " + Color.RESET);

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
                    System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
                    break;
            }
        } while (choice != 0);
    }

    private static void filterByGender() {
        int genderChoice;
        do {
            System.out.println(Color.CYAN +
                    "┌──────────────────────────────────────────┐\n" +
                    "│          LỌC ỨNG VIÊN THEO GIỚI TÍNH     │\n" +
                    "├──────────────────────────────────────────┤\n" +
                    "│ 1. NAM                                   │\n" +
                    "│ 2. NỮ                                    │\n" +
                    "│ 3. KHÁC                                  │\n" +
                    "│ 0. Quay lại                              │\n" +
                    "└──────────────────────────────────────────┘" + Color.RESET);

            genderChoice = Validator.validateInputInt(scanner, Color.BOLD + "Mời bạn chọn: " + Color.RESET);

            switch (genderChoice) {
                case 1:
                    showTableCandidate(candidateService.filterByGender("MALE"));
                    break;
                case 2:
                    showTableCandidate(candidateService.filterByGender("FEMALE"));
                    break;
                case 3:
                    showTableCandidate(candidateService.filterByGender("OTHER"));
                    break;
                case 0:
                    System.out.println(Color.YELLOW + "Quay lại menu chính." + Color.RESET);
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
                    break;
            }
        } while (genderChoice != 0);
    }

    private static void filterByExperience() {
        int years = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm: ");
        showTableCandidate(candidateService.filterByExperience(years));
    }

    private static void filterByAge() {
        int minAge = Validator.validateInputInt(scanner, "Nhập độ tuổi tối thiểu: ");
        int maxAge = Validator.validateInputInt(scanner, "Nhập độ tuổi tối đa: ");
        showTableCandidate(candidateService.filterByAgeRange(minAge, maxAge));
    }

    public static void displayAllTechnologies() {
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
            showTableCandidate(filteredCandidates);

        } while (true);
    }
}