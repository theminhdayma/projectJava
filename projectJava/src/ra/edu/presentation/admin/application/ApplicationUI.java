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
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.presentation.admin.application.ApplicationDetail.showApplicationDetail;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;

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
                System.out.println("Không tìm thấy đơn ứng tuyển với ID: " + applicationId + ". Vui lòng nhập lại!");
            }
        } while (application == null);
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
            String line = "+--------+----------------------+--------------------------+--------------------------+------------------+";

            System.out.println(line);
            System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s |\n",
                    "ID đơn", "Ứng viên", "CV URL", "Vị trí tuyển dụng", "Tiến trình");
            System.out.println(line);

            for (Application app : applications) {
                Candidate candidate = candidateService.getCandidateById(app.getCandidateId());
                RecruitmentPosition position = recruitmentPositionService.getRecruitmentPositionById(app.getRecruitmentPositionId());
                System.out.printf("| %-6s | %-20s | %-24s | %-24s | %-16s |\n",
                        app.getId(),
                        candidate.getName(),
                        app.getCvUrl(),
                        position.getName(),
                        app.getProgress().getDisplayName()
                );
            }

            System.out.println(line);
        }
    }

    private static void menuFilterApplicationByProgress() {
        int choice;
        do {
            System.out.println("\n===== LỌC ĐƠN ỨNG TUYỂN THEO TRẠNG THÁI =====");
            System.out.println("1. PENDING");
            System.out.println("2. INTERVIEWING");
            System.out.println("3. WAITCONFIRMINTERVIEWDATE");
            System.out.println("4. REJECTED");
            System.out.println("5. DESTROYED");
            System.out.println("6. DONE");
            System.out.println("0. Quay về menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    showAllApplicationByProgress("PENDING");
                    break;
                case 2:
                    showAllApplicationByProgress("INTERVIEWING");
                    break;
                case 3:
                    showAllApplicationByProgress("WAITCONFIRMINTERVIEWDATE");
                    break;
                case 4:
                    showAllApplicationByProgress("REJECTED");
                    break;
                case 5:
                    showAllApplicationByProgress("DESTROYED");
                    break;
                case 6:
                    showAllApplicationByProgress("DONE");
                    break;
                case 0:
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 0);
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


}
