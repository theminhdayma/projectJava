package ra.edu.presentation.candidate.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.validate.Validator;

import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.presentation.candidate.profile.ProfileUI.getCandidateLogin;
import static ra.edu.utils.ThreadUtil.pause;

public class ApplicationApplied {
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();

    public static void menuApplicationApplied () {
        int choice;
        do {
            System.out.println("\n====== MENU ỨNG TUYỂN ======");
            System.out.println("1. Xem các vị trí đã ứng tuyển");
            System.out.println("2. Chi tiết các vị trí đã ứng tuyển");
            System.out.println("3. Quay lại menu chính");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    showRecruitmentApply();
                    break;
                case 2:
                    showApplicationAppliedDetail();
                    break;
                case 3:
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (choice != 3);
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
        if (selectedApp.getProgress() == Progress.INTERVIEWING) {
            System.out.println("Ngày phỏng vấn: " + selectedApp.getInterviewDate());
            System.out.println("Lịch phỏng vấn: " + selectedApp.getInterviewDate());
        }
        if (selectedApp.getProgress() == Progress.DESTROYED) {
            System.out.println("Ngày huỷ: " + selectedApp.getDestroyDate());
            System.out.println("Lý do huỷ: " + selectedApp.getDestroyReason());
        }

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

        pause(1);

        if (selectedApp.getProgress() == Progress.INTERVIEWING) {
            System.out.println("Ngày phỏng vấn: " + selectedApp.getInterviewDate());
            System.out.println("Lịch phỏng vấn: " + selectedApp.getInterviewDate());

            int confirm;
            do {
                System.out.println("Bạn có xác nhận tham gia phỏng vấn không?");
                System.out.println("1. Có");
                System.out.println("2. Không");
                System.out.print("Lựa chọn của bạn: ");
                confirm = Integer.parseInt(scanner.nextLine());

                switch (confirm) {
                    case 1:
                        System.out.println("Bạn đã xác nhận tham gia phỏng vấn.");
                        applicationService.updateProgressDone(selectedApp.getId());
                        break;
                    case 2:
                        System.out.println("Bạn đã từ chối tham gia phỏng vấn.");
                        applicationService.updateProgressDestroy(selectedApp.getId(), "Bạn đã từ chối tham gia phỏng vấn.");
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                }
            } while (confirm != 1 && confirm != 2);
        }

    }

}
