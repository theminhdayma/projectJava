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
            System.out.println(Color.RED + "Không tìm thấy đơn ứng tuyển." + Color.RESET);
            return;
        }
        if (application.getProgress().name().equals("PENDING")) {
            applicationService.updateProgressHanding(application.getId());
            application.setProgress(Progress.HANDING);
        }

        // Application Info Table
        System.out.println(Color.CYAN + "\n== THÔNG TIN ĐƠN ỨNG TUYỂN ==" + Color.RESET);
        System.out.println(Color.CYAN + "┌────────────────────┬────────────────────────────────┐" + Color.RESET);
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "ID đơn ứng tuyển", application.getId());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "CV URL", application.getCvUrl());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Tiến trình", application.getProgress().getDisplayName());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Ngày tạo", application.getCreateAt());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Ngày phỏng vấn", (application.getInterviewDate() != null ? application.getInterviewDate() : "N/A"));
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Ngày hủy", (application.getDestroyDate() != null ? application.getDestroyDate() : "N/A"));
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Lý do hủy", (application.getDestroyReason() != null ? application.getDestroyReason() : "N/A"));
        System.out.println(Color.CYAN + "└────────────────────┴────────────────────────────────┘" + Color.RESET);

        // Candidate Info Table
        Candidate candidate = candidateService.getCandidateById(application.getCandidateId());
        int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
        System.out.println(Color.CYAN + "\n== THÔNG TIN ỨNG VIÊN ==" + Color.RESET);
        System.out.println(Color.CYAN + "┌────────────────────┬────────────────────────────────┐" + Color.RESET);
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Họ và tên", candidate.getName());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Email", candidate.getEmail());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Số điện thoại", candidate.getPhone());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Kinh nghiệm (năm)", candidate.getExperience());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Giới tính", candidate.getGender().getDisplayName());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Ngày sinh", candidate.getDob());
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30d │\n", "Tuổi", age);
        System.out.printf(Color.GREEN + "│ %-18s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Mô tả", truncate(candidate.getDescription(), 30));
        System.out.println(Color.CYAN + "└────────────────────┴────────────────────────────────┘" + Color.RESET);

        // Recruitment Position Info Table
        RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(application.getRecruitmentPositionId());
        System.out.println(Color.CYAN + "\n== THÔNG TIN VỊ TRÍ ỨNG TUYỂN ==" + Color.RESET);
        System.out.println(Color.CYAN + "┌──────────────────────┬────────────────────────────────┐" + Color.RESET);
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Tên vị trí", position.getName());
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Mức lương ($)", position.getMinSalary() + " - " + position.getMaxSalary());
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Kinh nghiệm yêu cầu", position.getMinExperience() + " năm");
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Ngày tạo", position.getCreatedDate());
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Hạn ứng tuyển", position.getExpiredDate());
        System.out.printf(Color.GREEN + "│ %-20s │ " + Color.RESET + Color.BOLD + "%-30s │\n", "Mô tả", position.getDescription());
        System.out.println(Color.CYAN + "└──────────────────────┴────────────────────────────────┘" + Color.RESET);

        updateApplicationByProgress(application);
    }

    private static void updateApplicationByProgress(Application application) {
        String progress = application.getProgress().name();

        while (true) {
            switch (progress) {
                case "HANDING":
                    System.out.println(Color.CYAN + "Đơn ứng tuyển đang trong quá trình xử lý" + Color.RESET);

                    System.out.println(Color.BOLD + "┌───┬─────────────────────────────────────────────┐" + Color.RESET);
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "1" + Color.RESET + " │" + Color.BOLD + Color.GREEN + " Hẹn lịch phỏng vấn                          " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "2" + Color.RESET + " │" + Color.BOLD + Color.YELLOW + " Hủy đơn                                     " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "0" + Color.RESET + " │" + Color.BOLD + Color.BLUE + " Quay lại menu trước                         " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "└───┴─────────────────────────────────────────────┘" + Color.RESET);
                    int choiceHanding = Validator.validateInputInt(scanner, Color.BOLD + "Nhập lựa chọn: " + Color.RESET);
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
                            System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                    }
                    break;

                case "REQUEST_RESCHEDULE":
                    System.out.println(Color.CYAN + "Đơn ứng tuyển đang yêu cầu đặt lại lịch." + Color.RESET);
                    System.out.println(Color.BLUE + "Lịch ứng viên mong muốn: " + application.getConfirmInterviewDate() + Color.RESET);
                    System.out.println( Color.BLUE + "Lý do hẹn lịch khác: " + application.getConfirmInterviewDateReason() + Color.RESET);

                    System.out.println(Color.BOLD + "┌───┬─────────────────────────────────────────────┐" + Color.RESET);
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "1" + Color.RESET + " │" + Color.BOLD + Color.GREEN + " Xác nhận lịch mới                           " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "2" + Color.RESET + " │" + Color.BOLD + Color.YELLOW + " Hủy đơn                                     " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "0" + Color.RESET + " │" + Color.BOLD + Color.BLUE + " Quay lại menu trước                         " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "└───┴─────────────────────────────────────────────┘" + Color.RESET);

                    int choiceReschedule = Validator.validateInputInt(scanner, Color.BOLD + "Nhập lựa chọn: " + Color.RESET);
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
                            System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                    }
                    break;

                case "INTERVIEWING":
                    System.out.println(Color.CYAN + "Đơn ứng tuyển đang trong quá trình phỏng vấn." + Color.RESET);

                    System.out.println(Color.BOLD + "┌───┬─────────────────────────────────────────────┐" + Color.RESET);
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "1" + Color.RESET + " │" + Color.BOLD + Color.GREEN + " Cập nhật kết quả phỏng vấn                  " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "2" + Color.RESET + " │" + Color.BOLD + Color.YELLOW + " Hủy đơn                                     " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "0" + Color.RESET + " │" + Color.BOLD + Color.BLUE + " Quay lại menu trước                         " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "└───┴─────────────────────────────────────────────┘" + Color.RESET);

                    int choiceInterviewing = Validator.validateInputInt(scanner, Color.BOLD + "Nhập lựa chọn: " + Color.RESET);
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
                            System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                    }
                    break;
                case "INTERVIEW_SCHEDULED":
                    System.out.println(Color.CYAN + "Đơn ứng tuyển đã xác nhận lịch phỏng vấn." + Color.RESET);

                    System.out.println(Color.BOLD + "┌───┬─────────────────────────────────────────────┐" + Color.RESET);
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "1" + Color.RESET + " │" + Color.BOLD + Color.GREEN + " Xác nhận phỏng vấn                          " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "2" + Color.RESET + " │" + Color.BOLD + Color.YELLOW + " Hủy đơn                                     " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "│ " + Color.BOLD + "0" + Color.RESET + " │" + Color.BOLD + Color.BLUE + " Quay lại menu trước                         " + Color.RESET + Color.BOLD + "│");
                    System.out.println(Color.BOLD + "└───┴─────────────────────────────────────────────┘" + Color.RESET);

                    int choiceScheduled = Validator.validateInputInt(scanner, Color.BOLD + "Nhập lựa chọn: " + Color.RESET);
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
                            System.out.println( Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                    }
                    break;
                case "DONE":
                    System.out.println( Color.GREEN + "Đơn ứng tuyển đã hoàn thành." + Color.RESET);
                    return;
                case "REJECTED":
                    System.out.println(Color.YELLOW + "Đơn ứng tuyển đã bị từ chối." + Color.RESET);
                    System.out.println(Color.ITALIC + "Lý do từ chối: " + application.getRejectedReason() + Color.RESET);
                    return;
                case "DESTROYED":
                    System.out.println(Color.YELLOW + "Đơn ứng tuyển đã bị ứng viên hủy." + Color.RESET);
                    System.out.println(Color.ITALIC + "Lý do hủy: " + application.getDestroyReason() + Color.RESET);
                    return;
                case "WAITING_FOR_INTERVIEW_CONFIRM":
                    System.out.println(Color.YELLOW + "Đơn ứng tuyển đang chờ xác nhận lịch phỏng vấn từ ứng viên." + Color.RESET);
                    return;

                default:
                    System.out.println(Color.RED + "Trạng thái không hợp lệ hoặc chưa xử lý." + Color.RESET);
                    return;
            }
        }
    }

    private static void rejectApplication(Application application) {
        String reason = ApplicationValidate.validateReason(scanner);
        boolean isCheck = applicationService.updateProgressReject(application.getId(), reason);
        if (isCheck) {
            System.out.println(Color.GREEN + "Đã từ chối đơn ứng tuyển." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Từ chối thất bại." + Color.RESET);
        }
    }

    private static void interviewApplication(Application application) {
        LocalDateTime timeInterview = ApplicationValidate.validateInterviewDateTime(scanner);
        boolean isCheck = applicationService.updateProgressInterviewing(application.getId(), timeInterview);
        if (isCheck) {
            System.out.println(Color.GREEN + "Hẹn phỏng vấn thành công." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Hẹn phỏng vấn thất bại." + Color.RESET);
        }
    }

    private static void confirmInterviewApplication(Application application) {
        boolean isCheck = applicationService.adminConfirmInterviewDate(application.getId(), application.getConfirmInterviewDate());
        if (isCheck) {
            System.out.println(Color.GREEN + "Xác nhận yêu cầu thời gian phỏng vấn thành công." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Xác nhận yêu cầu thời gian phỏng vấn thất bại." + Color.RESET);
        }
    }

    private static void doneApplication(Application application) {
        boolean isCheck;
        do {
            int result = Validator.validateInputInt(scanner, "Nhập kết quả phỏng vấn (1: Đậu, 2: Rớt): ");
            if (result == 1) {
                isCheck = applicationService.updateProgressDone(application.getId(), ResultInterview.PASSED.name());
            } else if (result == 2) {
                isCheck = applicationService.updateProgressDone(application.getId(), ResultInterview.FAILED.name());
            } else {
                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
                continue;
            }
            break;
        } while (true);

        if (isCheck) {
            System.out.println(Color.GREEN + "Hoàn thành đơn ứng tuyển." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Thất bại." + Color.RESET);
        }
    }
}
