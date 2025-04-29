package ra.edu.presentation.candidate.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;
import ra.edu.validate.application.ApplicationValidate;

import java.time.LocalDateTime;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.formatDateTime;
import static ra.edu.utils.Util.getCandidateLogin;

public class ApplicationApplied {
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();

    public static void menuApplicationApplied() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("MENU ỨNG TUYỂN", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Xem các vị trí đã ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "2", "Chi tiết các vị trí đã ứng tuyển");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay lại menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);

            choice = Validator.validateInputInt(scanner, Color.CYAN + "Nhập lựa chọn của bạn: " + Color.RESET);

            switch (choice) {
                case 1:
                    showRecruitmentApply();
                    break;
                case 2:
                    showApplicationAppliedDetail();
                    break;
                case 0:
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
            }
        } while (choice != 0);
    }

    private static List<Application> getAllApplicationCandidateLogin() {
        int candidateId = getCandidateLogin().getId();
        return applicationService.getAllApplicationCandidateLogin(candidateId);
    }

    private static void showRecruitmentApply() {
        List<Application> applications = getAllApplicationCandidateLogin();

        if (applications.isEmpty()) {
            System.out.println(Color.RED + "Bạn chưa ứng tuyển vào vị trí nào." + Color.RESET);
            return;
        }

        System.out.println(Color.BLUE + "\n╔══════════════════╦════════════════════════════════════════════╗" + Color.RESET);
        System.out.printf(Color.BLUE + "║ " + Color.YELLOW + "%-17s" + Color.BLUE + "║ " + Color.YELLOW + "%-43s" + Color.BLUE + "║\n" + Color.RESET,
                "Mã đơn", "Tên vị trí tuyển dụng");
        System.out.println(Color.BLUE + "╠══════════════════╬════════════════════════════════════════════╣" + Color.RESET);

        for (Application app : applications) {
            RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-17d" + Color.BLUE + "║ " + Color.GREEN + "%-43s" + Color.BLUE + "║\n" + Color.RESET,
                    app.getId(), position.getName());
        }

        System.out.println(Color.BLUE + "╚══════════════════╩════════════════════════════════════════════╝" + Color.RESET);
    }

    private static void showApplicationAppliedDetail() {
        List<Application> applications = getAllApplicationCandidateLogin();
        if (applications.isEmpty()) {
            System.out.println(Color.RED + "Bạn chưa ứng tuyển vào vị trí nào." + Color.RESET);
            return;
        }

        showRecruitmentApply();

        Application selectedApp = null;

        while (selectedApp == null) {
            int applicationId = Validator.validateInputInt(scanner, Color.CYAN + "Nhập mã đơn ứng tuyển bạn muốn xem: " + Color.RESET);

            for (Application app : applications) {
                if (app.getId() == applicationId) {
                    selectedApp = app;
                    break;
                }
            }

            if (selectedApp == null) {
                System.out.println(Color.RED + "Không tìm thấy đơn ứng tuyển với mã: " + applicationId + ". Vui lòng nhập lại." + Color.RESET);
            }
        }

        RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(selectedApp.getRecruitmentPositionId());

        System.out.println(Color.BLUE + "\n╔════════════════════════════════════════════════════════════════╗" + Color.RESET);
        System.out.println(Color.BLUE + "║                  " + Color.YELLOW + "THÔNG TIN CHI TIẾT ĐƠN ỨNG TUYỂN" + Color.BLUE + "              ║" + Color.RESET);
        System.out.println(Color.BLUE + "╠════════════════════════════════════════════════════════════════╣" + Color.RESET);
        System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                "Mã đơn", selectedApp.getId());
        System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                "CV URL", selectedApp.getCvUrl());
        System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                "Tiến trình", selectedApp.getProgress().getDisplayName());
        System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                "Ngày tạo", selectedApp.getCreateAt());
        System.out.println(Color.BLUE + "╚════════════════════════════════════════════════════════════════╝" + Color.RESET);

        if (position != null) {
            System.out.println(Color.BLUE + "\n╔════════════════════════════════════════════════════════════════╗" + Color.RESET);
            System.out.println(Color.BLUE + "║                     " + Color.YELLOW + "VỊ TRÍ TUYỂN DỤNG" + Color.BLUE + "                          ║" + Color.RESET);
            System.out.println(Color.BLUE + "╠════════════════════════════════════════════════════════════════╣" + Color.RESET);
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "ID", position.getId());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Tên", position.getName());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Mô tả", position.getDescription());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Lương ($)", position.getMinSalary() + " - " + position.getMaxSalary());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Kinh nghiệm", position.getMinExperience() + " năm");
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Ngày bắt đầu", position.getCreatedDate());
            System.out.printf(Color.BLUE + "║ " + Color.GREEN + "%-15s" + Color.BLUE + ": " + Color.RESET + "%-46s" + Color.BLUE + "║\n",
                    "Ngày kết thúc", position.getExpiredDate());
            System.out.println(Color.BLUE + "╚════════════════════════════════════════════════════════════════╝" + Color.RESET);
        }

        displayApplicationDetails(selectedApp.getProgress(), selectedApp);
    }

    private static void displayApplicationDetails(Progress progress, Application selectedApp) {
        switch (progress) {
            case WAITING_FOR_INTERVIEW_CONFIRM:
                System.out.println(Color.BOLD + Color.BLUE + "=== LỊCH PHỎNG VẤN ===" + Color.RESET);
                System.out.println(Color.YELLOW + "\nNgày phỏng vấn: " + formatDateTime(selectedApp.getInterviewDate()) + Color.RESET);

                int confirm;
                do {
                    System.out.println(Color.CYAN + "Bạn có xác nhận tham gia phỏng vấn không?" + Color.RESET);

                    System.out.println(Color.BOLD + "┌───┬─────────────────────────────────────────────┐" + Color.RESET);
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "1" + Color.RESET + " │" + Color.BOLD + Color.GREEN + " Xác nhận tham gia phỏng vấn                 " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "2" + Color.RESET + " │" + Color.BOLD + Color.YELLOW + " Yêu cầu thay đổi lịch phỏng vấn             " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "3" + Color.RESET + " │" + Color.BOLD + Color.RED + " Hủy bỏ đơn ứng tuyển                        " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "0" + Color.RESET + " │" + Color.BOLD + Color.BLUE + " Quay lại menu trước                         " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "└───┴─────────────────────────────────────────────┘" + Color.RESET);

                    confirm = Validator.validateInputInt(scanner, Color.BOLD + "Nhập lựa chọn: " + Color.RESET);

                    switch (confirm) {
                        case 1:
                            applicationService.candidateConfirmInterviewDate(selectedApp.getId());
                            System.out.println(Color.GREEN + "Bạn đã xác nhận tham gia phỏng vấn." + Color.RESET);
                            return;
                        case 2:
                            LocalDateTime timeInterview = ApplicationValidate.validateInterviewDateTime(scanner);
                            String reasonChange = ApplicationValidate.validateReason(scanner);
                            applicationService.updateProgressConfirmInterviewDate(selectedApp.getId(), timeInterview, reasonChange);
                            System.out.println(Color.YELLOW + "Bạn đã yêu cầu thay đổi lịch phỏng vấn." + Color.RESET);
                            return;
                        case 3:
                            String reasonDestroy = ApplicationValidate.validateReason(scanner);
                            applicationService.updateProgressDestroy(selectedApp.getId(), reasonDestroy);
                            System.out.println(Color.RED + "Bạn đã hủy bỏ đơn ứng tuyển." + Color.RESET);
                            return;
                        case 0:
                            return;
                        default:
                            System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại." + Color.RESET);
                    }
                } while (confirm != 0);
                break;

            case DESTROYED:
                System.out.println(Color.RED + "Bạn đã hủy bỏ đơn." + Color.RESET);
                System.out.println(Color.YELLOW + "Ngày huỷ: " + selectedApp.getDestroyDate() + Color.RESET);
                System.out.println(Color.YELLOW + "Lý do huỷ: " + selectedApp.getDestroyReason() + Color.RESET);
                break;

            case DONE:
                System.out.println(Color.GREEN + "Kết quả phỏng vấn: " + selectedApp.getResultInterview().getDisplayName() + Color.RESET);
                break;

            case INTERVIEWING:
                System.out.println(Color.GREEN + "Đơn đã được xác nhận lịch phỏng vấn và chờ phỏng vấn" + Color.RESET);
                System.out.println(Color.BLUE + "Lịch phỏng vấn: " + formatDateTime(selectedApp.getInterviewDate()) + Color.RESET);
                break;

            case HANDING:
                System.out.println(Color.CYAN + "Đơn đang chờ phản hồi từ nhà tuyển dụng." + Color.RESET);
                break;

            case REJECTED:
                System.out.println(Color.RED + "Đơn đã bị từ chối." + Color.RESET);
                System.out.println(Color.YELLOW + "Lý do từ chối: " + selectedApp.getRejectedReason() + Color.RESET);
                break;

            case REQUEST_RESCHEDULE:
                System.out.println(Color.YELLOW + "Bạn đã yêu cầu thay đổi lịch phỏng vấn. Vui lòng đợi xác nhận từ nhà tuyển dụng." + Color.RESET);
                System.out.println(Color.BLUE + "Lịch phỏng vấn yêu cầu: " + formatDateTime(selectedApp.getConfirmInterviewDate()) + Color.RESET);
                System.out.println(Color.BLUE + "Lý do: " + selectedApp.getConfirmInterviewDateReason() + Color.RESET);
                break;

            case INTERVIEW_SCHEDULED:
                System.out.println(Color.GREEN + "Bạn đã xác nhận lịch phỏng vấn. Vui lòng đợi" + Color.RESET);
                System.out.println(Color.BLUE + "Lịch phỏng vấn: " + formatDateTime(selectedApp.getInterviewDate()) + Color.RESET);
                break;

            case PENDING:
                System.out.println(Color.CYAN + "⌛ Đơn đang trong quá trình xử lý." + Color.RESET);
                break;

            default:
                System.out.println(Color.RED + "Không có thông tin phù hợp cho trạng thái này." + Color.RESET);
                break;
        }
    }
}
