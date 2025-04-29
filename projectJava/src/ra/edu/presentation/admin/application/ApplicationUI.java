package ra.edu.presentation.admin.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.application.ResultInterview;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.presentation.admin.application.ApplicationDetail.showApplicationDetail;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;
import static ra.edu.utils.Util.ValidateChoicePagination;

public class ApplicationUI {
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    public static void displayMenuApplication() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("QUẢN LÝ ĐƠN TUYỂN DỤNG", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Xem danh sách đơn ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "2", "Xem chi tiết đơn ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "3", "Lọc đơn theo trạng thái");
            System.out.printf("| %-3s | %-50s |\n", "4", "Lọc đơn theo kết quả phỏng vấn");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay về menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1:
                    showAllApplication();
                    break;
                case 2:
                    showApplicationDetail();
                    break;
                case 3:
                    menuFilterApplicationByProgress();
                    break;
                case 4:
                    showApplicationByResultInterview();
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

    public static Application getApplicationById() {
        Application application = null;
        do {
            int applicationId = Validator.validateInputInt(scanner, "Nhập ID đơn ứng tuyển cần xử lý: ");
            application = applicationService.getApplicationById(applicationId);
            if (application == null) {
                System.out.println(Color.RED + "Không tìm thấy đơn ứng tuyển với ID: " + applicationId + ". Vui lòng nhập lại!" + Color.RESET);
            }
        } while (application == null);
        return application;
    }

    private static void showTableApplication(List<Application> applications) {
        if (applications.isEmpty()) {
            System.out.println(Color.RED + "Không có đơn ứng tuyển nào trong hệ thống." + Color.RESET);
            return;
        }

        int idWidth = 6;
        int nameWidth = 25;
        int cvWidth = 28;
        int positionWidth = 30;
        int progressWidth = 35;

        String top = "┌" + "─".repeat(idWidth + 2)
                + "┬" + "─".repeat(nameWidth + 2)
                + "┬" + "─".repeat(cvWidth + 2)
                + "┬" + "─".repeat(positionWidth + 2)
                + "┬" + "─".repeat(progressWidth + 2)
                + "┐";

        String mid = "├" + "─".repeat(idWidth + 2)
                + "┼" + "─".repeat(nameWidth + 2)
                + "┼" + "─".repeat(cvWidth + 2)
                + "┼" + "─".repeat(positionWidth + 2)
                + "┼" + "─".repeat(progressWidth + 2)
                + "┤";

        String bot = "└" + "─".repeat(idWidth + 2)
                + "┴" + "─".repeat(nameWidth + 2)
                + "┴" + "─".repeat(cvWidth + 2)
                + "┴" + "─".repeat(positionWidth + 2)
                + "┴" + "─".repeat(progressWidth + 2)
                + "┘";

        String header = String.format("│ %-" + idWidth + "s │ %-" + nameWidth + "s │ %-" + cvWidth + "s │ %-" + positionWidth + "s │ %-" + progressWidth + "s │",
                "ID đơn", "Ứng viên", "CV URL", "Vị trí tuyển dụng", "Tiến trình");

        System.out.println(Color.BOLD + top + Color.RESET);
        System.out.println(Color.BOLD + header + Color.RESET);
        System.out.println(Color.BOLD + mid + Color.RESET);

        for (Application app : applications) {
            Candidate candidate = candidateService.getCandidateById(app.getCandidateId());
            RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
            System.out.printf("│ %-" + idWidth + "d │ %-" + nameWidth + "s │ %-" + cvWidth + "s │ %-" + positionWidth + "s │ %-" + progressWidth + "s │\n",
                    app.getId(),
                    candidate.getName(),
                    app.getCvUrl(),
                    position.getName(),
                    app.getProgress().getDisplayName()
            );
        }

        System.out.println(Color.BOLD + bot + Color.RESET);
    }

    private static void showAllApplication() {
        int limit = Validator.validateInputInt(scanner, Color.CYAN + "Nhập số lượng đơn ứng tuyển muốn hiển thị: " + Color.RESET);
        int totalPage = applicationService.getTotalPage(limit);

        if (totalPage == 0) {
            System.out.println("Không có đơn ứng tuyển nào trong hệ thống.");
            return;
        }

        int currentPage = 1;

        while (true) {
            System.out.println("\n" + Color.BOLD + Color.BLUE + "== DANH SÁCH ĐƠN ỨNG TUYỂN TRANG " + currentPage + " / " + totalPage + " ==" + Color.RESET);
            List<Application> applications = applicationService.getApplicationByPage(currentPage, limit);
            showTableApplication(applications);
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

    private static void menuFilterApplicationByProgress() {
        int choice;
        do {
            System.out.println(Color.CYAN + "\n╔══════════════════════════════════════════════╗" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + Color.BOLD + "       LỌC ĐƠN ỨNG TUYỂN THEO TRẠNG THÁI      " + Color.RESET + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "╠══════════════════════════════════════════════╣" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 1. " + Color.GREEN + "PENDING" + Color.RESET + "                                   " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 2. " + Color.GREEN + "HANDING" + Color.RESET + "                                   " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 3. " + Color.GREEN + "WAITING_FOR_INTERVIEW_CONFIRM" + Color.RESET + "             " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 4. " + Color.GREEN + "REQUEST_RESCHEDULE" + Color.RESET + "                        " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 5. " + Color.GREEN + "INTERVIEW_SCHEDULED" + Color.RESET + "                       " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 6. " + Color.GREEN + "INTERVIEWING" + Color.RESET + "                              " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 7. " + Color.GREEN + "REJECTED" + Color.RESET + "                                  " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 8. " + Color.GREEN + "DESTROYED" + Color.RESET + "                                 " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 9. " + Color.GREEN + "DONE" + Color.RESET + "                                      " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 0. " + Color.YELLOW + "Quay về menu chính" + Color.RESET + "                        " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "╚══════════════════════════════════════════════╝" + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "➤ Mời bạn chọn: " + Color.RESET);
            switch (choice) {
                case 1 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.PENDING.name()));
                case 2 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.HANDING.name()));
                case 3 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.WAITING_FOR_INTERVIEW_CONFIRM.name()));
                case 4 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.REQUEST_RESCHEDULE.name()));
                case 5 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.INTERVIEW_SCHEDULED.name()));
                case 6 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.INTERVIEWING.name()));
                case 7 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.REJECTED.name()));
                case 8 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.DESTROYED.name()));
                case 9 -> showTableApplication(applicationService.getAllApplicationByProgress(Progress.DONE.name()));
                case 0 -> {
                    System.out.println(Color.YELLOW + "Đang quay về menu chính..." + Color.RESET);
                    pause(1);
                }
                default -> System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại!" + Color.RESET);
            }
        } while (choice != 0);
    }

    private static void showApplicationByResultInterview() {
        int choice;
        do {
            System.out.println(Color.CYAN + "\n╔═══════════════════════════════════════════╗" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + Color.BOLD + "  LỌC ĐƠN ỨNG TUYỂN THEO KẾT QUẢ PHỎNG VẤN " + Color.RESET + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "╠═══════════════════════════════════════════╣" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 1. " + Color.GREEN + "Đậu phỏng vấn" + Color.RESET + "                          " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 2. " + Color.RED + "Trượt phỏng vấn" + Color.RESET + "                        " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "║" + Color.RESET + " 0. " + Color.YELLOW + "Quay về menu chính" + Color.RESET + "                     " + Color.CYAN + "║" + Color.RESET);
            System.out.println(Color.CYAN + "╚═══════════════════════════════════════════╝" + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "➤ Mời bạn chọn: " + Color.RESET);
            switch (choice) {
                case 1 -> showTableApplication(applicationService.getAllApplicationByResultInterview(ResultInterview.PASSED.name()));
                case 2 -> showTableApplication(applicationService.getAllApplicationByResultInterview(ResultInterview.FAILED.name()));
                case 0 -> {
                    System.out.println(Color.YELLOW + "Đang quay về menu chính..." + Color.RESET);
                    pause(1);
                }
                default -> System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại!" + Color.RESET);
            }
        } while (choice != 0);
    }
}
