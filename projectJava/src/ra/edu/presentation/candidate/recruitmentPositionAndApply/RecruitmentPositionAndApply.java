package ra.edu.presentation.candidate.recruitmentPositionAndApply;

import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.validate.Validator;

import java.util.List;

import static ra.edu.utils.ThreadUtil.pause;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.Util.LIMIT;

public class RecruitmentPositionAndApply {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    public static void recruitmentPositionAndApply() {
        int choice;
        do {
            System.out.println("1. Xem các vị trị ứng tuyển");
            System.out.println("2. Chi tiết vị trí ứng tuyển và ứng tuyển");
            System.out.println("3. Quay lại menu chính");
            choice = Validator.validateInputInt(scanner, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    showRecruitmentPosition();
                    break;
                case 2:

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

            System.out.println("\n== TRANG " + pageChoice + " ==");
            System.out.println("+--------------------------------+----------------------------------------------------+-------------------+-------------------+");
            System.out.printf("| %-30s | %-50s | %-17s | %-17s |\n", "Tên vị trí", "Mô tả", "Lương tối thiểu", "Lương tối đa");
            System.out.println("+--------------------------------+----------------------------------------------------+-------------------+-------------------+");

            recruitmentPositionService.getRecruitmentPositionByPage(pageChoice, LIMIT).forEach(recruitmentPosition -> {
                System.out.printf("| %-30s | %-50s | %-17.2f | %-17.2f |\n",
                        truncate(recruitmentPosition.getName(), 30),
                        truncate(recruitmentPosition.getDescription(), 50),
                        recruitmentPosition.getMinSalary(),
                        recruitmentPosition.getMaxSalary());
            });

            System.out.println("+--------------------------------+----------------------------------------------------+-------------------+-------------------+");
        }
    }

    private static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
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
        for (int i = 0; i < recruitmentPositions.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, recruitmentPositions.get(i).getName());
        }

        int choice = Validator.validateInputInt(scanner, "Chọn vị trí muốn xem chi tiết: ");
        if (choice < 1 || choice > recruitmentPositions.size()) {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        RecruitmentPosition selectedPosition = recruitmentPositions.get(choice - 1);
        System.out.println("Chi tiết vị trí tuyển dụng:");
    }
}
