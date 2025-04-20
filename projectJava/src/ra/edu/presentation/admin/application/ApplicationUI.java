package ra.edu.presentation.admin.application;

import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;

public class ApplicationUI {
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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
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
}
