package ra.edu.presentation.admin.application;

import ra.edu.business.model.application.Application;
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

import static ra.edu.utils.Util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;

public class ApplicationUI {
    private static final ApplicationService applicationService = new ApplicationServiceImp();
    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    public static void displayMenuApplication() {
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ ĐƠN TUYỂN DỤNG =====");
            System.out.println("1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái");
            System.out.println("3. Hủy đơn ứng tuyển");
            System.out.println("4. Xem chi tiết đơn ứng tuyển");
            System.out.println("5. Gửi thông tin phỏng vấn");
            System.out.println("6. Cập nhật kết quả phỏng vấn");
            System.out.println("7. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    showAllApplication();
                    break;
                case 2:
                    menuFilterApplicationByProgress();
                    break;
                case 3:
                    destroyApplication();
                    break;
                case 4:
                    showApplicationDetail();
                    break;
                case 5:
                    interviewConfirmation();
                    break;
                case 6:
                    doneApplication();
                    break;
                case 7:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 7);
    }

    private static Application getApplicationById() {
        int applicationId = Validator.validateInputInt(scanner, "Nhập ID đơn ứng tuyển cần xử lý: ");
        Application application = applicationService.getApplicationById(applicationId);
        if (application == null) {
            System.out.println("Không tìm thấy đơn ứng tuyển với ID: " + applicationId);
            return null;
        }
        return application;
    }

    private static void showAllApplication() {
        int totalPage = applicationService.getTotalPage(LIMIT);

        if (totalPage == 0) {
            System.out.println("Không có ứng viên nào để hiển thị.");
            return;
        }

        while (true) {
            System.out.println("\n== DANH SÁCH TRANG ĐƠN ỨNG TUYỂN ==");
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

            List<Application> applications = applicationService.getApplicationByPage(pageChoice, LIMIT);
            System.out.println("\n== ĐƠN ỨNG TUYỂN TRANG " + pageChoice + " ==");
            String line = "+--------+----------------------+--------------------------+--------------------------+------------------+------------+---------------------+---------------------+--------------------------+--------------------------+---------------------+-------------------+----------------+-------------+-----------+-----------+---------------------+---------------------+";

            System.out.println(line);
            System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s | %-10s | %-19s | %-19s | %-24s | %-24s | %-19s | %-17s | %-14s | %-11s | %-9s | %-9s | %-21s | %-17s |\n",
                    "ID đơn", "Ứng viên", "CV URL", "Vị trí tuyển dụng", "Tiến trình", "Ngày tạo", "Ngày phỏng vấn", "Ngày hủy", "Lý do hủy", "Mức lương ($)", "Kinh nghiệm yêu cầu", "Email", "Số điện thoại", "Kinh nghiệm", "Giới tính", "Tuổi", "Ngày tạo vị trí", "Hạn ứng tuyển");
            System.out.println(line);

            for (Application app : applications) {
                Candidate candidate = candidateService.getCandidateById(app.getCandidateId());
                RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
                int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
                System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s | %-10s | %-19s | %-19s | %-24s | %-24s | %-19s | %-17s | %-14s | %-11s | %-9s | %-9d | %-21s | %-17s |\n",
                        app.getId(),
                        candidate.getName(),
                        app.getCvUrl(),
                        position.getName(),
                        app.getProgress().getDisplayName(),
                        app.getCreateAt(),
                        (app.getInterviewDate() != null ? app.getInterviewDate().toString() : "N/A"),
                        (app.getDestroyDate() != null ? app.getDestroyDate().toString() : "N/A"),
                        (app.getDestroyReason() != null ? app.getDestroyReason() : "N/A"),
                        position.getMinSalary() + " - " + position.getMaxSalary(),
                        position.getMinExperience() + " năm",
                        candidate.getEmail(),
                        candidate.getPhone(),
                        candidate.getExperience() + " năm",
                        candidate.getGender().getDisplayName(),
                        age,
                        position.getCreatedDate(),
                        position.getExpiredDate());
            }

            System.out.println(line);
        }
    }

    private static void interviewConfirmation () {
        Application application = getApplicationById();

        LocalDateTime timeInterview = ApplicationValidate.validateInterviewDateTime(scanner);
        boolean isCheck = applicationService.updateProgressInterviewing(application.getId(), timeInterview);
        if (isCheck) {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thành công.");
        } else {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thất bại.");
        }
    }

    private static void destroyApplication() {
        Application application = getApplicationById();
        String reason = ApplicationValidate.validateDestroyReason(scanner);
        boolean isCheck = applicationService.updateProgressDestroy(application.getId(), reason);
        if (isCheck) {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thành công.");
        } else {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thất bại.");
        }
    }

    private static void doneApplication () {
        Application application = getApplicationById();
        boolean isCheck = applicationService.updateProgressDone(application.getId());
        if (isCheck) {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thành công.");
        } else {
            System.out.println("Cập nhật trạng thái đơn ứng tuyển thất bại.");
        }
    }

    private static void menuFilterApplicationByProgress() {
        int choice;
        do {
            System.out.println("\n===== LỌC ĐƠN ỨNG TUYỂN THEO TRẠNG THÁI =====");
            System.out.println("1. PENDING");
            System.out.println("2. INTERVIEWING");
            System.out.println("3. DESTROYED");
            System.out.println("4. DONE");
            System.out.println("5. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    showAllApplicationByProgress("PENDING");
                    break;
                case 2:
                    showAllApplicationByProgress("INTERVIEWING");
                    break;
                case 3:
                    showAllApplicationByProgress("DESTROYED");
                    break;
                case 4:
                    showAllApplicationByProgress("DONE");
                    break;
                case 5:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 5);
    }


    private static void showAllApplicationByProgress(String progress) {
        List<Application> applications = applicationService.getAllApplicationByProgress(progress);
        if (applications.isEmpty()) {
            System.out.println("Không có đơn ứng tuyển nào với trạng thái " + progress + ".");
            return;
        }

        int totalRecords = applications.size();
        int totalPages = (int) Math.ceil((double) totalRecords / LIMIT);

        while (true) {
            System.out.println("\n== DANH SÁCH TRANG ĐƠN ỨNG TUYỂN (" + progress + ") ==");
            for (int i = 1; i <= totalPages; i++) {
                System.out.printf("%d. Trang %d\n", i, i);
            }
            System.out.println("0. Thoát");

            int pageChoice;
            do {
                pageChoice = Validator.validateInputInt(scanner, "Chọn trang muốn xem: ");
                if (pageChoice < 0 || pageChoice > totalPages) {
                    System.err.println("Số trang không hợp lệ. Vui lòng chọn lại!");
                }
            } while (pageChoice < 0 || pageChoice > totalPages);

            if (pageChoice == 0) {
                System.out.println("Thoát khỏi hiển thị ứng viên.");
                pause(1);
                break;
            }

            int fromIndex = (pageChoice - 1) * LIMIT;
            int toIndex = Math.min(fromIndex + LIMIT, totalRecords);
            List<Application> pageApplications = applications.subList(fromIndex, toIndex);

            String line = "+--------+----------------------+--------------------------+--------------------------+------------------+------------+---------------------+---------------------+--------------------------+--------------------------+---------------------+-------------------+----------------+-------------+-----------+-----------+---------------------+---------------------+";
            System.out.println(line);
            System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s | %-10s | %-19s | %-19s | %-24s | %-24s | %-19s | %-17s | %-14s | %-11s | %-9s | %-9s | %-21s | %-17s |\n",
                    "ID đơn", "Ứng viên", "CV URL", "Vị trí tuyển dụng", "Tiến trình", "Ngày tạo", "Ngày phỏng vấn", "Ngày hủy", "Lý do hủy", "Mức lương ($)", "Kinh nghiệm yêu cầu", "Email", "Số điện thoại", "Kinh nghiệm", "Giới tính", "Tuổi", "Ngày tạo vị trí", "Hạn ứng tuyển");
            System.out.println(line);

            for (Application app : pageApplications) {
                Candidate candidate = candidateService.getCandidateById(app.getCandidateId());
                RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
                int age = Period.between(candidate.getDob(), LocalDate.now()).getYears();
                System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s | %-10s | %-19s | %-19s | %-24s | %-24s | %-19s | %-17s | %-14s | %-11s | %-9s | %-9d | %-21s | %-17s |\n",
                        app.getId(),
                        candidate.getName(),
                        app.getCvUrl(),
                        position.getName(),
                        app.getProgress().getDisplayName(),
                        app.getCreateAt(),
                        (app.getInterviewDate() != null ? app.getInterviewDate().toString() : "N/A"),
                        (app.getDestroyDate() != null ? app.getDestroyDate().toString() : "N/A"),
                        (app.getDestroyReason() != null ? app.getDestroyReason() : "N/A"),
                        position.getMinSalary() + " - " + position.getMaxSalary(),
                        position.getMinExperience() + " năm",
                        candidate.getEmail(),
                        candidate.getPhone(),
                        candidate.getExperience() + " năm",
                        candidate.getGender().getDisplayName(),
                        age,
                        position.getCreatedDate(),
                        position.getExpiredDate());
            }

            System.out.println(line);
        }
    }

    private static void showApplicationDetail() {
        Application application = getApplicationById();
        if (application == null) {
            System.out.println("Không tìm thấy đơn ứng tuyển.");
            return;
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
    }

}
