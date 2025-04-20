package ra.edu.presentation.admin.recruitmentPosition;

import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;

public class RecruitmentPositionUI {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    public static void displayMenuRecruitmentPosition() {
        int choice;
        do {
            System.out.println("\n====== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG ======");
            System.out.println("1. Thêm vị trí tuyển dụng mới");
            System.out.println("2. Cập nhật vị trí tuyển dụng");
            System.out.println("3. Xóa vị trí tuyển dụng");
            System.out.println("4. Xem danh sách vị trí đang hoạt động");
            System.out.println("5. Quay lại menu chính");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:

                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 5.");
            }
        } while (choice != 5);
    }
}
