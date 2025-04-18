package ra.edu;

import ra.edu.business.model.account.Account;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.presentation.admin.AdminMain;

import java.util.Scanner;

import static ra.edu.utils.FileUtil.readFromFile;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class MainApplication {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String token = readFromFile();
        if (token != null) {
            System.out.println("\nDuy trì đăng nhập với tài khoản: " + token);
            AdminMain.displayMenuManagentAdmin();
        }
        displayMenuApplication();
    }

    public static void displayMenuApplication() {
        int choice;
        do {
            System.out.println("\n============= HỆ THỐNG TUYỂN DỤNG =============");
            System.out.println("1. Đăng nhập với quản trị viên");
            System.out.println("2. Đăng nhập với ứng viên");
            System.out.println("3. Đăng ký với ứng viên");
            System.out.println("4. Thoát");
            System.out.print("Mời bạn chọn: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    loginAdmin();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("Tạm biệt!");
                    pause(1);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        } while (choice != 4);
    }

    public static void loginAdmin() {
        Account admin = null;
        AdminService adminService = new AdminServiceImp();
        adminService.initAdmin();

        do {
            System.out.println("==== Đăng nhập quản trị viên ====");
            Account inputAdmin = new Account();
            inputAdmin.inputData();

            boolean isLogin = adminService.loginAdmin(inputAdmin.getUsername(), inputAdmin.getPassword());
            if (isLogin) {
                String token = "ADMIN: " + inputAdmin.getUsername();
                writeToFile(token);
                System.out.println("Đăng nhập thành công với vai trò quản trị viên.");
                pause(1);
                admin = inputAdmin;
                AdminMain.displayMenuManagentAdmin();
            } else {
                System.out.println("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.\n");
            }
        } while (admin == null);
    }
}
