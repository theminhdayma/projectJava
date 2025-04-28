package ra.edu.presentation.candidate.profile;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidateTenology.CandidateTechnology;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyService;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.presentation.candidate.application.ApplicationApplied;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;
import ra.edu.validate.candidate.CandidateValidate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.getCandidateLogin;

public class ProfileUI {
    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final CandidateTechnologyService candidateTechnologyService = new CandidateTechnologyServiceImp();
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    public static void displayMenuProfile() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("TRANG CÁ NHÂN", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Thay đổi thông tin cá nhân");
            System.out.printf("| %-3s | %-50s |\n", "2", "Cập nhật công nghệ cá nhân");
            System.out.printf("| %-3s | %-50s |\n", "3", "Đổi mật khẩu");
            System.out.printf("| %-3s | %-50s |\n", "4", "Hiển thị thông tin cá nhân");
            System.out.printf("| %-3s | %-50s |\n", "5", "Xem các thông báo tuyển dụng đã ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay về menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1:
                    updateCandidateInfo();
                    break;
                case 2:
                    Candidate candidate = candidateLogin();
                    choiceTechnologyName(candidate.getId());
                    break;
                case 3:
                    changePassword();
                    break;
                case 4:
                    showProfile();
                    break;
                case 5:
                    ApplicationApplied.menuApplicationApplied();
                    break;
                case 0:
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);
    }

    private static void showProfile() {
        Candidate candidate = getCandidateLogin();
        if (candidate == null) {
            System.out.println(Color.RED + "Không tìm thấy ứng viên." + Color.RESET);
            return;
        }

        int labelWidth = 20;
        int valueWidth = 35;
        int totalWidth = labelWidth + valueWidth + 5;

        String top = "┌" + "─".repeat(totalWidth) + "┐";
        String bottom = "└" + "─".repeat(totalWidth) + "┘";
        String separator = "├" + "─".repeat(totalWidth) + "┤";
        String title = "│" + Color.center("THÔNG TIN ỨNG VIÊN HIỆN TẠI", totalWidth) + "│";

        System.out.println(top);
        System.out.println(title);
        System.out.println(separator);

        printRow("Họ và tên", candidate.getName(), labelWidth, valueWidth);
        printRow("Số điện thoại", candidate.getPhone(), labelWidth, valueWidth);
        printRow("Kinh nghiệm (năm)", candidate.getExperience(), labelWidth, valueWidth);
        printRow("Giới tính", candidate.getGender().getDisplayName(), labelWidth, valueWidth);
        printRow("Mô tả", candidate.getDescription(), labelWidth, valueWidth);
        printRow("Ngày sinh", candidate.getDob(), labelWidth, valueWidth);

        System.out.println(bottom);
    }

    private static void printRow(String label, Object value, int labelWidth, int valueWidth) {
        String coloredLabel = Color.GREEN + label + Color.RESET;
        String labelCell = Color.padRight(coloredLabel, labelWidth);
        String valueStr = (value != null) ? value.toString() : "";
        String valueCell = Color.padRight(valueStr, valueWidth);

        System.out.println("│ " + labelCell + " : " + valueCell + " │");
    }

    private static void updateCandidateInfo() {
        Candidate candidate = getCandidateLogin();
        showProfile();

        int choice;
        do {
            System.out.println(Color.CYAN + Color.repeat("*", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("CẬP NHẬT THÔNG TIN ỨNG VIÊN", Color.WIDTH) + Color.RESET);
            System.out.println(Color.CYAN + Color.repeat("*", Color.WIDTH) + Color.RESET);

            System.out.println(Color.GREEN + "1. Cập nhật tên" + Color.RESET);
            System.out.println(Color.GREEN + "2. Cập nhật số điện thoại" + Color.RESET);
            System.out.println(Color.GREEN + "3. Cập nhật số năm kinh nghiệm" + Color.RESET);
            System.out.println(Color.GREEN + "4. Cập nhật giới tính" + Color.RESET);
            System.out.println(Color.GREEN + "5. Cập nhật mô tả" + Color.RESET);
            System.out.println(Color.GREEN + "6. Cập nhật ngày sinh" + Color.RESET);
            System.out.println(Color.RED + "0. Lưu và thoát" + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Chọn thông tin muốn cập nhật: " + Color.RESET);

            switch (choice) {
                case 1:
                    candidate.setName(CandidateValidate.inputValidName(scanner));
                    break;
                case 2:
                    candidate.setPhone(CandidateValidate.inputValidPhone(scanner));
                    break;
                case 3:
                    candidate.setExperience(CandidateValidate.inputValidExperience(scanner));
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
                case 0:
                    if (candidateService.update(candidate)) {
                        System.out.println(Color.GREEN + "Cập nhật thông tin ứng viên thành công." + Color.RESET);
                    } else {
                        System.err.println(Color.RED + "Cập nhật thất bại." + Color.RESET);
                    }
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
            }
        } while (choice != 0);
    }

    private static List<Technology> getAllTechnology() {
        return technologyService.getAllTechnology();
    }

    private static Candidate candidateLogin () {
        Candidate candidate = getCandidateLogin();
        if (candidate == null) {
            System.out.println(Color.RED + "Không tìm thấy ứng viên." + Color.RESET);
            return null;
        }
        return candidate;
    }

    public static List<CandidateTechnology> getAllCandidateTechnologyByCandidateId() {
        Candidate candidate = candidateLogin();
        List<CandidateTechnology> candidateTechnology = candidateTechnologyService.getAllCandidateTechnologyByCandidateId(candidate.getId());
        if (candidateTechnology == null) {
            System.out.println(Color.YELLOW + "Bạn chưa đăng ký công nghệ nào." + Color.RESET);
            return null;
        }
        return candidateTechnology;
    }

    public static void displayTechnologyList(List<Technology> technologyList) {
        List<CandidateTechnology> candidateTechnologies = getAllCandidateTechnologyByCandidateId();
        Set<Integer> registeredTechnologyIds = new HashSet<>();
        if (candidateTechnologies != null) {
            for (CandidateTechnology ct : candidateTechnologies) {
                registeredTechnologyIds.add(ct.getTechnologyId());
            }
        }

        System.out.println(Color.BLUE + Color.BOLD + "\nDANH SÁCH CÔNG NGHỆ HIỆN CÓ" + Color.RESET);

        int idWidth = 4;
        int nameWidth = technologyList.stream()
                .mapToInt(t -> t.getName().length())
                .max()
                .orElse(20);
        nameWidth = Math.max(nameWidth, 20);

        int statusWidth = 12;

        String top = "┌" + "─".repeat(idWidth + 2) + "┬" + "─".repeat(nameWidth + 2) + "┬" + "─".repeat(statusWidth + 2) + "┐";
        String mid = "├" + "─".repeat(idWidth + 2) + "┼" + "─".repeat(nameWidth + 2) + "┼" + "─".repeat(statusWidth + 2) + "┤";
        String bot = "└" + "─".repeat(idWidth + 2) + "┴" + "─".repeat(nameWidth + 2) + "┴" + "─".repeat(statusWidth + 2) + "┘";

        System.out.println(Color.YELLOW + top);

        String header = String.format("│ %-"+idWidth+"s │ %-"+nameWidth+"s │ %-"+statusWidth+"s │", "ID", "Tên Công Nghệ", "Trạng Thái");
        System.out.println(header);

        System.out.println(mid);

        for (Technology tech : technologyList) {
            boolean isRegistered = registeredTechnologyIds.contains(tech.getId());
            String statusText = isRegistered ? "Đã chọn" : "Chưa chọn";

            String row = String.format(
                    "│ %" + idWidth + "d │ %-"+ nameWidth +"s │ %-"+ statusWidth +"s │",
                    tech.getId(), tech.getName(), statusText
            );

            String coloredStatus = isRegistered ? (Color.GREEN + statusText + Color.YELLOW) : (Color.RED + statusText + Color.YELLOW);
            row = row.replace(statusText, coloredStatus);

            System.out.println(Color.YELLOW + row);
        }

        System.out.println(bot + Color.RESET);
    }

    public static void choiceTechnologyName(int candidateId) {
        final List<Technology> technologyList = getAllTechnology();

        int choice;
        do {
            displayTechnologyList(technologyList);

            System.out.printf(Color.YELLOW + " 0. Thoát%n" + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "→ Nhập ID công nghệ để Đăng ký / Bỏ chọn: " + Color.RESET);

            if (choice == 0) {
                System.out.println("⏳ Quay lại...");
                pause(1);
                break;
            }

            final int finalChoice = choice;

            Technology selectedTech = technologyList.stream()
                    .filter(t -> t.getId() == finalChoice)
                    .findFirst()
                    .orElse(null);

            if (selectedTech == null) {
                System.out.println(Color.RED + "Không tìm thấy công nghệ với ID bạn nhập. Vui lòng thử lại." + Color.RESET);
                continue;
            }

            List<CandidateTechnology> candidateTechnologies = getAllCandidateTechnologyByCandidateId();
            boolean isAlreadyRegistered = false;
            CandidateTechnology existingCandidateTech = null;

            if (candidateTechnologies != null) {
                for (CandidateTechnology ct : candidateTechnologies) {
                    if (ct.getTechnologyId() == selectedTech.getId()) {
                        isAlreadyRegistered = true;
                        existingCandidateTech = ct;
                        break;
                    }
                }
            }

            if (isAlreadyRegistered) {
                if (candidateTechnologyService.deleteCandidateTechnologyById(existingCandidateTech.getId())) {
                    System.out.println(Color.GREEN + "Bỏ chọn công nghệ thành công: " + selectedTech.getName() + Color.RESET);
                } else {
                    System.out.println(Color.RED + "Bỏ chọn công nghệ thất bại. Vui lòng thử lại." + Color.RESET);
                }
            } else {
                CandidateTechnology candidateTech = new CandidateTechnology();
                candidateTech.setCandidateId(candidateId);
                candidateTech.setTechnologyId(selectedTech.getId());

                if (candidateTechnologyService.addCandidateTechnology(candidateTech)) {
                    System.out.println(Color.GREEN + "Đăng ký công nghệ thành công: " + selectedTech.getName() + Color.RESET);
                } else {
                    System.out.println(Color.RED + "Đăng ký công nghệ thất bại. Vui lòng thử lại." + Color.RESET);
                }
            }

        } while (true);
    }

    private static void changePassword() {
        Candidate candidate = getCandidateLogin();
        if (candidate == null) {
            System.out.println(Color.RED + "Không tìm thấy ứng viên." + Color.RESET);
            return;
        }

        String currentEmail = candidate.getAccount().getUsername();
        while (true) {
            String confirmEmail = CandidateValidate.inputValidEmailLogin(scanner);
            if (currentEmail.equals(confirmEmail)) {
                break;
            }
            System.out.println(Color.RED + "Email không khớp với tài khoản đang đăng nhập. Vui lòng thử lại." + Color.RESET);
        }

        String oldPassword;
        while (true) {
            oldPassword = CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu hiện tại: ");
            if (candidate.getAccount().getPassword().equals(oldPassword)) {
                break;
            }
            System.out.println(Color.RED + "Mật khẩu hiện tại không đúng. Vui lòng thử lại." + Color.RESET);
        }

        String newPassword = CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu mới: ");
        while (true) {

            if (newPassword.equals(oldPassword)) {
                System.out.println(Color.RED + "Mật khẩu mới không được trùng với mật khẩu hiện tại. Vui lòng nhập lại." + Color.RESET);
                continue;
            }

            String confirmPassword = CandidateValidate.inputValidPassword(scanner, "Xác nhận mật khẩu mới: ");
            if (!newPassword.equals(confirmPassword)) {
                System.out.println(Color.RED + "Mật khẩu xác nhận không khớp. Vui lòng thử lại." + Color.RESET);
            } else {
                break;
            }
        }

        boolean result = candidateService.changePasswordCandidate(candidate.getAccount().getId(), newPassword);
        if (result) {
            System.out.println(Color.GREEN + "Đổi mật khẩu thành công!" + Color.RESET);
            pause(1);
        } else {
            System.out.println(Color.RED + "Có lỗi xảy ra khi đổi mật khẩu." + Color.RESET);
        }
    }
}
