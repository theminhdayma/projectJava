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
            System.out.println("Bạn chưa ứng tuyển vào vị trí nào.");
            return;
        }

        System.out.println("\n+-----------------+----------------------------------------+");
        System.out.printf("| %-15s | %-38s |\n", "Mã đơn", "Tên vị trí tuyển dụng");
        System.out.println("+-----------------+----------------------------------------+");

        for (Application app : applications) {
            RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
            System.out.printf("| %-15d | %-38s |\n", app.getId(), position.getName());
        }

        System.out.println("+-----------------+----------------------------------------+");
    }

    private static void showApplicationAppliedDetail() {
        List<Application> applications = getAllApplicationCandidateLogin();
        if (applications.isEmpty()) {
            System.out.println("Bạn chưa ứng tuyển vào vị trí nào.");
            return;
        }
        showRecruitmentApply();

        int applicationId = Validator.validateInputInt(scanner, "Nhập mã đơn ứng tuyển bạn muốn xem: ");
        Application selectedApp = null;

        for (Application app : applications) {
            if (app.getId() == applicationId) {
                selectedApp = app;
                break;
            }
        }

        if (selectedApp == null) {
            System.out.println("Không tìm thấy đơn ứng tuyển với mã: " + applicationId);
            return;
        }

        RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(selectedApp.getRecruitmentPositionId());

        System.out.println("\n--- THÔNG TIN CHI TIẾT ĐƠN ỨNG TUYỂN ---");
        System.out.println("Mã đơn: " + selectedApp.getId());
        System.out.println("CV URL: " + selectedApp.getCvUrl());
        System.out.println("Tiến trình xử lý: " + selectedApp.getProgress().getDisplayName());
        System.out.println("Ngày tạo: " + selectedApp.getCreateAt());

        if (position != null) {
                System.out.println("\n--- VỊ TRÍ TUYỂN DỤNG ---");
                System.out.println("ID: " + position.getId());
                System.out.println("Tên: " + position.getName());
                System.out.println("Mô tả: " + position.getDescription());
                System.out.println("Lương ($): " + position.getMinSalary() + " - " + position.getMaxSalary());
                System.out.println("Kinh nghiệm yêu cầu: " + position.getMinExperience() + " năm");
                System.out.println("Ngày bắt đầu: " + position.getCreatedDate());
                System.out.println("Ngày kết thúc: " + position.getExpiredDate());
        }

        displayApplicationDetails(selectedApp.getProgress(), selectedApp);
    }

    private static void displayApplicationDetails(Progress progress, Application selectedApp) {
        switch (progress) {
            case WAITING_FOR_INTERVIEW_CONFIRM:
                System.out.println("Lịch phỏng vấn: " + selectedApp.getInterviewDate());
                int confirm;
                do {
                    System.out.println("Bạn có xác nhận tham gia phỏng vấn không?");
                    System.out.println("1. Xác nhận tham gia phỏng vấn");
                    System.out.println("2. Yêu cầu thay đổi lịch phỏng vấn");
                    System.out.println("3. Hủy bỏ đơn ứng tuyển");
                    System.out.println("0. Quay lại menu trước");
                    confirm = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");

                    switch (confirm) {
                        case 1:
                            applicationService.candidateConfirmInterviewDate(selectedApp.getId());
                            System.out.println("Bạn đã xác nhận tham gia phỏng vấn.");
                            break;
                        case 2:
                            LocalDateTime timeInterview = ApplicationValidate.validateInterviewDateTime(scanner);
                            String reasonChange = ApplicationValidate.validateReason(scanner);
                            applicationService.updateProgressConfirmInterviewDate(selectedApp.getId(), timeInterview, reasonChange);
                            System.out.println("Bạn đã yêu cầu thay đổi lịch phỏng vấn.");
                            break;
                        case 3:
                            String reasonDestroy = ApplicationValidate.validateReason(scanner);
                            applicationService.updateProgressDestroy(selectedApp.getId(), reasonDestroy);
                            System.out.println("Bạn đã hủy bỏ đơn ứng tuyển.");
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                    }
                } while (confirm != 1 && confirm != 2);
                break;

            case DESTROYED:
                System.out.println("Bạn đã hủy bỏ đơn.");
                System.out.println("Ngày huỷ: " + selectedApp.getDestroyDate());
                System.out.println("Lý do huỷ: " + selectedApp.getDestroyReason());
                break;

            case DONE:
                System.out.println("Kết quả phỏng vấn: " + selectedApp.getResultInterview().getDisplayName());
                break;
            case INTERVIEWING:
                System.out.println("Lịch phỏng vấn: " + formatDateTime(selectedApp.getInterviewDate()));
                break;
            case HANDING:
                System.out.println("Đơn đang chờ phản hồi từ nhà tuyển dụng.");
                break;
            case REJECTED:
                System.out.println("Đơn đã bị từ chối.");
                System.out.println("Lý do từ chối: " + selectedApp.getRejectedReason());
                break;
            case REQUEST_RESCHEDULE:
                System.out.println("Bạn đã yêu cầu thay đổi lịch phỏng vấn. Vui lòng đợi xác nhận từ nhà tuyển dụng.");
                System.out.println("Ngày phỏng vấn: " + selectedApp.getConfirmInterviewDate());
                System.out.println("Lý do yêu cầu: " + selectedApp.getConfirmInterviewDateReason());
                break;
            case INTERVIEW_SCHEDULED:
                System.out.println("Đơn đã xác nhận lịch phỏng vấn.");
                System.out.println("Ngày phỏng vấn: " + selectedApp.getInterviewDate());
                break;
            case PENDING:
                System.out.println("Đơn đang trong quá trình xử lý.");
                break;

            default:
                System.out.println("Không có thông tin phù hợp cho trạng thái này.");
                break;
        }
    }
}
