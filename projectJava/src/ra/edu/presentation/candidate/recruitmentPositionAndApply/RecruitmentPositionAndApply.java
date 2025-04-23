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
            System.out.println("\n====== MENU ỨNG TUYỂN ======");
            System.out.println("1. Xem các vị trị ứng tuyển");
            System.out.println("2. Chi tiết vị trí ứng tuyển và ứng tuyển");
            System.out.println("3. Quay lại menu chính");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    showRecruitmentPosition();
                    break;
                case 2:
                    showRecruitmentPositionDetails();
                    break;
                case 3:
                    System.out.println("Quay lại menu chính.");
                    System.out.println("Loading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (choice != 3);
    }

    private static void showRecruitmentPosition() {
        int totalPage = recruitmentPositionService.getTotalPage(LIMIT);

        if (totalPage == 0) {
            System.out.println("Không có vị trí tuyển dụng nào để hiển thị.");
            return;
        }

        while (true) {
            System.out.println("\n====== DANH SÁCH VỊ TRÍ TUYỂN DỤNG ======");
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
                System.out.println("Thoát khỏi hiển thị vị trí tuyển dụng.");
                pause(1);
                break;
            }

            System.out.println("\n\t\t\t\t\t\t\t\t\t\t== TRANG " + pageChoice + " ==");
            System.out.println("+--------------------------------+----------------------------------------------------+--------------------+-------------------+");
            System.out.printf("| %-30s | %-50s | %-17s | %-17s |\n", "Tên vị trí", "Mô tả", "Lương tối thiểu($)", "Lương tối đa($)");
            System.out.println("+--------------------------------+----------------------------------------------------+--------------------+-------------------+");

            recruitmentPositionService.getRecruitmentPositionByPage(pageChoice, LIMIT).forEach(recruitmentPosition -> {
                System.out.printf("| %-30s | %-50s | %-17.2f | %-17.2f |\n",
                        truncate(recruitmentPosition.getName(), 30),
                        truncate(recruitmentPosition.getDescription(), 50),
                        recruitmentPosition.getMinSalary(),
                        recruitmentPosition.getMaxSalary());
            });

            System.out.println("+--------------------------------+----------------------------------------------------+--------------------+-------------------+");
        }
    }

    private static List<RecruitmentPosition> getAllRecruitmentPosition() {
        return recruitmentPositionService.getAllRecruitmentPosition();
    }

    private static void showRecruitmentPositionDetails() {
        List<RecruitmentPosition> recruitmentPositions = getAllRecruitmentPosition();
        if (recruitmentPositions.isEmpty()) {
            System.out.println("Không có vị trí tuyển dụng nào.");
            return;
        }

        System.out.println("Danh sách vị trí tuyển dụng:");
        System.out.println("+--------------------------------------------+");
        for (int i = 0; i < recruitmentPositions.size(); i++) {
            RecruitmentPosition rp = recruitmentPositions.get(i);
            System.out.printf("%d. [ID: %d] %s\n", i + 1, rp.getId(), rp.getName());
        }
        System.out.println("+--------------------------------------------+");

        int choice = Validator.validateInputInt(scanner, "Chọn vị trí tuyển dụng muốn xem chi tiết: ");
        if (choice < 1 || choice > recruitmentPositions.size()) {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        RecruitmentPosition selectedPosition = recruitmentPositions.get(choice - 1);
        List<Technology> technologys = technologyService.getTechnologyByRecruitmentPositionId(selectedPosition.getId());

        System.out.println("\n--- Chi tiết vị trí tuyển dụng ---");
        System.out.println("Tên vị trí: " + selectedPosition.getName());
        System.out.println("Mô tả: " + selectedPosition.getDescription());
        System.out.println("Mức lương tối thiểu ($): " + selectedPosition.getMinSalary());
        System.out.println("Mức lương tối đa ($): " + selectedPosition.getMaxSalary());
        System.out.println("Năm kinh nghiệm tối thiểu: " + selectedPosition.getExpiredDate());
        System.out.println("Ngày bắt đầu: " + selectedPosition.getCreatedDate());
        System.out.println("Ngày kết thúc: " + selectedPosition.getExpiredDate());

        if (technologys != null && !technologys.isEmpty()) {
            System.out.println("\nCông nghệ yêu cầu:");
            for (Technology tech : technologys) {
                System.out.println("- " + tech.getName());
            }
        }

        int applyChoice;
        do {
            applyChoice = Validator.validateInputInt(scanner, "Bạn có muốn ứng tuyển vào vị trí này không? (1: Có, 2: Không): ");
            if (applyChoice != 1 && applyChoice != 2) {
                System.out.println("Vui lòng chỉ nhập 1 (Có) hoặc 2 (Không).");
            }
        } while (applyChoice != 1 && applyChoice != 2);

        if (applyChoice == 1) {
            Application application = new Application();
            application.setRecruitmentPositionId(selectedPosition.getId());
            application.setCandidateId(getIdCandidateLogin());
            application.inputData();

            boolean isSuccess = applicationService.addApplication(application);
            if (isSuccess) {
                System.out.println("Ứng tuyển thành công!");
            } else {
                System.out.println("Ứng tuyển thất bại.");
            }
        } else {
            System.out.println("Bạn đã chọn không ứng tuyển.");
        }
    }

    private static int getIdCandidateLogin() {
        String email = getAccountLogin();
        return candidateService.getCandidateByEmail(email).getId();
    }
}
