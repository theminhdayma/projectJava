package ra.edu.presentation.candidate.profile;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;

public class ProfileUI {
    public static void displayMenuProfile() {
        int choice;
        do {
            System.out.println("\n====== TRANG CÁ NHÂN ======");
            System.out.println("1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 3.");
            }
        } while (choice != 3);
    }
}
