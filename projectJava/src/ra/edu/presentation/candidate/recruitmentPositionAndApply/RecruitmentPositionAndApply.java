package ra.edu.presentation.candidate.recruitmentPositionAndApply;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.util.List;

import static ra.edu.utils.ThreadUtil.pause;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.Util.*;

public class RecruitmentPositionAndApply {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final CandidateService candidateService = new CandidateServiceImp();
    public static void recruitmentPositionAndApply() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("MENU ỨNG TUYỂN", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Xem các vị trí ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "2", "Chi tiết vị trí ứng tuyển và ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay lại menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Nhập lựa chọn của bạn: " + Color.RESET);

            switch (choice) {
                case 1:
                    showRecruitmentPosition();
                    break;
                case 2:
                    showRecruitmentPositionDetails();
                    break;
                case 0:
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
            }
        } while (choice != 0);
    }

    private static void showRecruitmentPosition() {
        int limit = Validator.validateInputInt(scanner, Color.CYAN + "Nhập số vị trí tuyển dụng muốn hiển thị trên 1 trang: " + Color.RESET);
        int totalPage = recruitmentPositionService.getTotalPage(limit);

        if (totalPage == 0) {
            System.out.println("Không có vị trí tuyển dụng nào để hiển thị.");
            return;
        }

        int currentPage = 1;
        int idWidth = 5;
        int nameWidth = 30;
        int descWidth = 50;
        int salaryWidth = 20;

        while (true) {
            System.out.println("\n" + Color.BOLD + Color.BLUE + "====== VỊ TRÍ TUYỂN DỤNG " + currentPage + " / " + totalPage + " ======" + Color.RESET);

            String top = "┌" + "─".repeat(idWidth + 2) + "┬" + "─".repeat(nameWidth + 2) + "┬" + "─".repeat(descWidth + 2) + "┬" + "─".repeat(salaryWidth + 2) + "┬" + "─".repeat(salaryWidth + 2) + "┐";
            String mid = "├" + "─".repeat(idWidth + 2) + "┼" + "─".repeat(nameWidth + 2) + "┼" + "─".repeat(descWidth + 2) + "┼" + "─".repeat(salaryWidth + 2) + "┼" + "─".repeat(salaryWidth + 2) + "┤";
            String bot = "└" + "─".repeat(idWidth + 2) + "┴" + "─".repeat(nameWidth + 2) + "┴" + "─".repeat(descWidth + 2) + "┴" + "─".repeat(salaryWidth + 2) + "┴" + "─".repeat(salaryWidth + 2) + "┘";

            System.out.println(Color.BOLD + top + Color.RESET);
            System.out.printf(Color.BOLD + "│ %-" + idWidth + "s │ %-" + nameWidth + "s │ %-" + descWidth + "s │ %-" + salaryWidth + "s │ %-" + salaryWidth + "s │\n" + Color.RESET,
                    "ID", "Tên vị trí", "Mô tả", "Lương tối thiểu($)", "Lương tối đa($)");
            System.out.println(Color.BOLD + mid + Color.RESET);

            recruitmentPositionService.getRecruitmentPositionByPage(currentPage, limit).forEach(recruitmentPosition -> {
                System.out.printf("│ %-" + idWidth + "d │ %-" + nameWidth + "s │ %-" + descWidth + "s │ %-" + salaryWidth + ".2f │ %-" + salaryWidth + ".2f │\n",
                        recruitmentPosition.getId(),
                        truncate(recruitmentPosition.getName(), nameWidth),
                        truncate(recruitmentPosition.getDescription(), descWidth),
                        recruitmentPosition.getMinSalary(),
                        recruitmentPosition.getMaxSalary());
            });

            System.out.println(Color.BOLD + bot + Color.RESET);

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
                    System.out.println(Color.YELLOW + "Thoát khỏi hiển thị vị trí tuyển dụng." + Color.RESET);
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


    private static List<RecruitmentPosition> getAllRecruitmentPosition() {
        return recruitmentPositionService.getAllRecruitmentPosition();
    }

    private static void showRecruitmentPositionDetails() {
        List<RecruitmentPosition> recruitmentPositions = getAllRecruitmentPosition();
        if (recruitmentPositions.isEmpty()) {
            System.out.println(Color.RED + "Không có vị trí tuyển dụng nào." + Color.RESET);
            return;
        }

        System.out.println(Color.CYAN + "\n╔════════════╦══════════════════════════════════════╗" + Color.RESET);
        System.out.println(Color.CYAN + "║     ID     ║             TÊN VỊ TRÍ               ║" + Color.RESET);
        System.out.println(Color.CYAN + "╠════════════╬══════════════════════════════════════╣" + Color.RESET);

        for (RecruitmentPosition rp : recruitmentPositions) {
            System.out.printf(Color.CYAN + "║ " + Color.YELLOW + "%-10d" + Color.CYAN + " ║ " + Color.YELLOW + "%-36s" + Color.CYAN + " ║\n" + Color.RESET,
                    rp.getId(), truncate(rp.getName(), 36));
        }

        System.out.println(Color.CYAN + "╚════════════╩══════════════════════════════════════╝" + Color.RESET);

        int choiceId = Validator.validateInputInt(scanner, Color.CYAN + "\nNhập ID vị trí tuyển dụng muốn xem chi tiết: " + Color.RESET);
        RecruitmentPosition selectedPosition = null;
        for (RecruitmentPosition rp : recruitmentPositions) {
            if (rp.getId() == choiceId) {
                selectedPosition = rp;
                break;
            }
        }

        if (selectedPosition == null) {
            System.out.println(Color.RED + "Không tìm thấy vị trí tuyển dụng với ID đã nhập." + Color.RESET);
            return;
        }

        List<Technology> technologys = technologyService.getTechnologyByRecruitmentPositionId(selectedPosition.getId());

        System.out.println(Color.BLUE + "\n╔════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
        System.out.println(Color.BLUE + "║                         " + Color.BOLD + "CHI TIẾT VỊ TRÍ TUYỂN DỤNG" + Color.RESET + Color.BLUE + "                             ║" + Color.RESET);
        System.out.println(Color.BLUE + "╠════════════════════════════════════════════════════════════════════════════════╣" + Color.RESET);
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                "Tên vị trí", selectedPosition.getName());
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                "Mô tả", truncate(selectedPosition.getDescription(), 40));
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "$%-45.2f" + Color.BLUE + " ║\n" + Color.RESET,
                "Mức lương tối thiểu", selectedPosition.getMinSalary());
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "$%-45.2f" + Color.BLUE + " ║\n" + Color.RESET,
                "Mức lương tối đa", selectedPosition.getMaxSalary());
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                "Năm kinh nghiệm tối thiểu", selectedPosition.getMinExperience() + " năm");
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                "Ngày bắt đầu", selectedPosition.getCreatedDate());
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                "Ngày kết thúc", selectedPosition.getExpiredDate());

        if (technologys != null && !technologys.isEmpty()) {
            System.out.println(Color.BLUE + "╠════════════════════════════════════════════════════════════════════════════════╣" + Color.RESET);

            StringBuilder techNames = new StringBuilder();
            for (int i = 0; i < technologys.size(); i++) {
                techNames.append(technologys.get(i).getName());
                if (i != technologys.size() - 1) {
                    techNames.append(", ");
                }
            }

            System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-30s" + Color.BLUE + ": " + Color.GREEN + "%-46s" + Color.BLUE + " ║\n" + Color.RESET,
                    "Yêu cầu công nghệ", techNames.toString());
        }

        System.out.println(Color.BLUE + "╚════════════════════════════════════════════════════════════════════════════════╝" + Color.RESET);

        int applyChoice;
        do {
            applyChoice = Validator.validateInputInt(scanner, Color.CYAN + "\nBạn có muốn ứng tuyển vào vị trí này không? (1: Có, 2: Không): " + Color.RESET);
            if (applyChoice != 1 && applyChoice != 2) {
                System.out.println(Color.RED + "Vui lòng chỉ nhập 1 (Có) hoặc 2 (Không)." + Color.RESET);
            }
        } while (applyChoice != 1 && applyChoice != 2);

        if (applyChoice == 1) {
            Application application = new Application();
            application.setRecruitmentPositionId(selectedPosition.getId());
            application.setCandidateId(getIdCandidateLogin());
            application.inputData();

            boolean isSuccess = applicationService.addApplication(application);
            if (isSuccess) {
                System.out.println(Color.GREEN + "Ứng tuyển thành công!" + Color.RESET);
            } else {
                System.out.println(Color.RED + "Ứng tuyển thất bại." + Color.RESET);
            }
        } else {
            System.out.println(Color.YELLOW + "Bạn đã chọn không ứng tuyển." + Color.RESET);
        }
    }

    private static int getIdCandidateLogin() {
        String email = getAccountLogin();
        return candidateService.getCandidateByEmail(email).getId();
    }
}
