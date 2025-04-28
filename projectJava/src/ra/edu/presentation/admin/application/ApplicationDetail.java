package ra.edu.presentation.admin.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.ResultInterview;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.validate.Validator;
import ra.edu.validate.application.ApplicationValidate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static ra.edu.MainApplication.scanner;
import static ra.edu.presentation.admin.application.ApplicationUI.getApplicationById;
import static ra.edu.utils.Util.truncate;

public class ApplicationDetail {
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final CandidateService candidateService = new CandidateServiceImp();

    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    public static void showApplicationDetail() {
        Application application = getApplicationById();
        if (application == null) {
            System.out.println("Không tìm thấy đơn ứng tuyển.");
            return;
        }
        if (application.getProgress().name().equals("PENDING")) {
            applicationService.updateProgressHanding(application.getId());
        }

        System.out.println("\n== THÔNG TIN ĐƠN ỨNG TUYỂN ==");
        System.out.println("+----------------------+------------------------------+");
        System.out.printf("| %-20s | %-28s |\n", "ID đơn ứng tuyển", application.getId());
        System.out.printf("| %-20s | %-28s |\n", "CV URL", application.getCvUrl());
        System.out.printf("| %-20s | %-28s |\n", "Tiến trình", application.getProgress().getDisplayName());
        System.out.printf("| %-20s | %-28s |\n", "Ngày tạo", application.getCreateAt());
        System.out.printf("| %-20s | %-28s |\n", "Ngày phỏng vấn", (application.getInterviewDate() != null ? application.getInterviewDate() : "N/A"));
        System.out.printf("| %-20s | %-28s |\n", "Ngày hủy", (application.getDestroyDate() != null ? application.getDestroyDate() : "N/A"));
        System.out.printf("| %-20s | %-28s |\n", "Lý do hủy", (application.getDestroyReason() != null ? application.getDestroyReason() : "N/A"));
        System.out.println("+----------------------+------------------------------+");

        Candidate candidate = candidateService.getCandidateById(application.getCandidateId());
        int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
        System.out.println("\n== THÔNG TIN ỨNG VIÊN ==");
        System.out.println("+----------------------+------------------------------+");
        System.out.printf("| %-20s | %-28s |\n", "Họ và tên", candidate.getName());
        System.out.printf("| %-20s | %-28s |\n", "Email", candidate.getEmail());
        System.out.printf("| %-20s | %-28s |\n", "Số điện thoại", candidate.getPhone());
        System.out.printf("| %-20s | %-28s |\n", "Kinh nghiệm (năm)", candidate.getExperience());
        System.out.printf("| %-20s | %-28s |\n", "Giới tính", candidate.getGender().getDisplayName());
        System.out.printf("| %-20s | %-28s |\n", "Ngày sinh", candidate.getDob());
        System.out.printf("| %-20s | %-28s |\n", "Tuổi", age);
        System.out.printf("| %-20s | %-28s |\n", "Mô tả", truncate(candidate.getDescription(), 30));
        System.out.println("+----------------------+------------------------------+");

        RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(application.getRecruitmentPositionId());
        System.out.println("\n== THÔNG TIN VỊ TRÍ ỨNG TUYỂN ==");
        System.out.println("+----------------------+------------------------------+");
        System.out.printf("| %-20s | %-28s |\n", "Tên vị trí", position.getName());
        System.out.printf("| %-20s | %-28s |\n", "Mức lương ($)", position.getMinSalary() + " - " + position.getMaxSalary());
        System.out.printf("| %-20s | %-28s |\n", "Kinh nghiệm yêu cầu", position.getMinExperience() + " năm");
        System.out.printf("| %-20s | %-28s |\n", "Ngày tạo", position.getCreatedDate());
        System.out.printf("| %-20s | %-28s |\n", "Hạn ứng tuyển", position.getExpiredDate());
        System.out.printf("| %-20s | %-28s |\n", "Mô tả", position.getDescription());
        System.out.println("+----------------------+------------------------------+");

        updateApplicationByProgress(application);
    }

    private static void updateApplicationByProgress(Application application) {
        String progress = application.getProgress().name();

        while (true) {
            switch (progress) {
                case "HANDING":
                    System.out.println("Đơn ứng tuyển đang trong quá trình xử lý.");
                    System.out.println("1. Hẹn lịch phỏng vấn");
                    System.out.println("2. Hủy đơn");
                    System.out.println("0. Thoát");
                    int choiceHanding = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");
                    switch (choiceHanding) {
                        case 1:
                            interviewApplication(application);
                            return;
                        case 2:
                            rejectApplication(application);
                            return;
                        case 0:
                            return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    }
                    break;

                case "REQUEST_RESCHEDULE":
                    System.out.println("Đơn ứng tuyển đang yêu cầu đặt lại lịch.");
                    System.out.println("Lịch ứng viên mong muốn: " + application.getConfirmInterviewDate());
                    System.out.println("Lý do hẹn lịch khác: " + application.getConfirmInterviewDateReason());
                    System.out.println("1. Xác nhận lịch mới");
                    System.out.println("2. Hủy đơn");
                    System.out.println("0. Thoát");
                    int choiceReschedule = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");
                    switch (choiceReschedule) {
                        case 1:
                            confirmInterviewApplication(application);
                            return;
                        case 2:
                            rejectApplication(application);
                            return;
                        case 0:
                            return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    }
                    break;

                case "INTERVIEWING":
                    System.out.println("Đơn ứng tuyển đang trong quá trình phỏng vấn.");
                    System.out.println("1. Cập nhật kết quả phỏng vấn");
                    System.out.println("2. Hủy đơn");
                    System.out.println("0. Thoát");
                    int choiceInterviewing = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");
                    switch (choiceInterviewing) {
                        case 1:
                            doneApplication(application);
                            return;
                        case 2:
                            rejectApplication(application);
                            return;
                        case 0:
                            return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    }
                    break;
                case "INTERVIEW_SCHEDULED":
                    System.out.println("Đơn ứng tuyển đã xác nhận lịch phỏng vấn.");
                    System.out.println("1. Xác nhận phỏng vấn");
                    System.out.println("2. Hủy đơn");
                    System.out.println("0. Thoát");
                    int choiceScheduled = Validator.validateInputInt(scanner, "Nhập lựa chọn: ");
                    switch (choiceScheduled) {
                        case 1:
                            confirmInterviewApplication(application);
                            return;
                        case 2:
                            rejectApplication(application);
                            return;
                        case 0:
                            return;
                        default:
                            System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                    }
                    break;
                case "DONE":
                    System.out.println("Đơn ứng tuyển đã hoàn thành.");
                    return;
                case "REJECTED":
                    System.out.println("Đơn ứng tuyển đã bị từ chối.");
                    System.out.println("Lý do từ chối: " + application.getRejectedReason());
                    return;
                case "DESTROYED":
                    System.out.println("Đơn ứng tuyển đã bị ứng viên hủy.");
                    System.out.println("Lý do hủy: " + application.getDestroyReason());
                    return;
                case "WAITING_FOR_INTERVIEW_CONFIRM":
                    System.out.println("Đơn ứng tuyển đang chờ xác nhận lịch phỏng vấn từ ứng viên.");
                    return;

                default:
                    System.out.println("Trạng thái không hợp lệ hoặc chưa xử lý.");
                    return;
            }
        }
    }

    private static void rejectApplication(Application application) {
        String reason = ApplicationValidate.validateReason(scanner);
        boolean isCheck = applicationService.updateProgressReject(application.getId(), reason);
        if (isCheck) {
            System.out.println("Đã từ chối đơn ứng tuyển.");
        } else {
            System.out.println("Từ chối thất bại.");
        }
    }

    private static void interviewApplication (Application application) {

        LocalDateTime timeInterview = ApplicationValidate.validateInterviewDateTime(scanner);
        boolean isCheck = applicationService.updateProgressInterviewing(application.getId(), timeInterview);
        if (isCheck) {
            System.out.println("Hẹn phỏng vấn thành công.");
        } else {
            System.out.println("Hẹn phỏng vấn thất bại.");
        }
    }

    private static void confirmInterviewApplication (Application application) {
        boolean isCheck = applicationService.adminConfirmInterviewDate(application.getId(), application.getConfirmInterviewDate());
        if (isCheck) {
            System.out.println("Xác nhận yêu cầu thời gian phỏng vấn thành công.");
        } else {
            System.out.println("Xác nhận yêu cầu thời gian phỏng vấn thất bại.");
        }
    }

    private static void doneApplication (Application application) {
        boolean isCheck;
        do {
            int result = Validator.validateInputInt(scanner, "Nhập kết quả phỏng vấn (1: Đậu, 2: Rớt): ");
            if (result == 1) {
                isCheck = applicationService.updateProgressDone(application.getId(), ResultInterview.PASSED.name());
            } else if (result == 2) {
                isCheck = applicationService.updateProgressDone(application.getId(), ResultInterview.FAILED.name());
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                continue;
            }
            break;
        } while (true);

        if (isCheck) {
            System.out.println("Hoàn thành đơn ứng tuyển.");
        } else {
            System.out.println("Thất bại.");
        }
    }
}
