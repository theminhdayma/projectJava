package ra.edu;

import ra.edu.business.model.account.Account;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidateTenology.CandidateTechnology;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.Account.AccountService;
import ra.edu.business.service.Account.AccountServiceImp;
import ra.edu.business.service.admin.AdminService;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyService;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.presentation.admin.AdminMain;
import ra.edu.presentation.candidate.CandidateMain;
import ra.edu.validate.StringRule;
import ra.edu.validate.Validator;
import ra.edu.validate.candidate.CandidateValidate;

import java.util.List;
import java.util.Scanner;

import static ra.edu.utils.FileUtil.readFromFile;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class MainApplication {
    public static final Scanner scanner = new Scanner(System.in);
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    private static final CandidateTechnologyService candidateTechnologyService = new CandidateTechnologyServiceImp();
    private static final AccountService accountService = new AccountServiceImp();
    public static void main(String[] args) {
        checkLoginAndDisplayMenu();
        displayMenuApplication();
    }

    public static void checkLoginAndDisplayMenu() {
        String token = readFromFile();
        if (token != null && !token.isEmpty()) {
            String[] parts = token.split(":");
            if (parts.length == 2) {
                String role = parts[0];
                String username = parts[1];
                int count = accountService.checkIsAccount(username);
                if (count == 0) {
                    displayMenuApplication();
                }
                System.out.println("\nDuy trì đăng nhập với tài khoản: " + username);
                if ("ADMIN".equalsIgnoreCase(role)) {
                    AdminMain.displayMenuManagentAdmin();
                } else if ("CANDIDATE".equalsIgnoreCase(role)) {
                    CandidateMain.displayMenuCadidateManagent();
                } else {
                    System.out.println("Vai trò không hợp lệ trong token.");
                }
            } else {
                System.out.println("Định dạng token không hợp lệ.");
            }
        }
    }

    public static void displayMenuApplication() {
        int choice;
        do {
            System.out.println("\n============= HỆ THỐNG TUYỂN DỤNG =============");
            System.out.println("1. Đăng nhập với quản trị viên");
            System.out.println("2. Đăng nhập với ứng viên");
            System.out.println("3. Đăng ký với ứng viên");
            System.out.println("4. Thoát");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    loginAdmin();
                    break;
                case 2:
                    loginCandidate();
                    break;
                case 3:
                    registerCandidate();
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
            String username = Validator.validateInputString(scanner, "Nhập tên đăng nhập: ", new StringRule(0, 255));
            String password = Validator.validateInputString(scanner, "Nhập mật khẩu: ", new StringRule(0, 255));

            boolean isLogin = adminService.loginAdmin(username, password);
            if (isLogin) {
                String token = "ADMIN:" + username;
                writeToFile(token);
                System.out.println("Đăng nhập thành công với vai trò quản trị viên.");
                pause(1);
                admin = new Account();
                admin.setUsername(username);
                admin.setPassword(password);
                AdminMain.displayMenuManagentAdmin();
            } else {
                System.out.println("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.\n");
            }
        } while (admin == null);
    }

    public static void loginCandidate() {
        Account candidate = null;
        CandidateService candidateService = new CandidateServiceImp();

        do {
            System.out.println("==== Đăng nhập ứng viên ====");
            String username = CandidateValidate.inputValidEmail(scanner);
            String password = Validator.validateInputString(scanner, "Nhập mật khẩu: ", new StringRule(0, 255));

            boolean isLogin = candidateService.loginCandidate(username, password);
            if (isLogin) {
                String token = "CANDIDATE:" + username;
                writeToFile(token);
                System.out.println("Đăng nhập thành công với vai trò ứng viên.");
                pause(1);
                candidate = new Account();
                candidate.setUsername(username);
                candidate.setPassword(password);
                CandidateMain.displayMenuCadidateManagent();
            } else {
                System.err.println("Tên đăng nhập hoặc mật khẩu không đúng.");
                int choice;
                do {
                    System.out.println("1. Có - Đăng ký tài khoản");
                    System.out.println("2. Không - Thử lại đăng nhập");
                    System.out.println("3. Thoát");
                    try {
                        choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
                        switch (choice) {
                            case 1:
                                registerCandidate();
                                return;
                            case 2:
                                System.out.println("Vui lòng nhập lại thông tin đăng nhập.\n");
                                break;
                            case 3:
                                System.out.println("Thoát đăng nhập !");
                                return;
                            default:
                                System.err.println("Không hợp lệ. Vui lòng chọn lại.");
                                choice = -1;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                        choice = -1;
                    }
                } while (choice != 2);
            }
        } while (candidate == null);
    }

    public static List<Technology> getAllTechnology() {
        return technologyService.getAllTechnology();
    }

    public static void choiceTechnologyName(int candidateId) {
        List<Technology> technologyList = getAllTechnology();
        int choice;

        do {
            System.out.println("\n=============================");
            System.out.println("DANH SÁCH CÔNG NGHỆ HIỆN CÓ");
            System.out.println("=============================");
            for (int i = 0; i < technologyList.size(); i++) {
                System.out.printf("%2d. %s\n", (i + 1), technologyList.get(i).getName());
            }
            System.out.println(" 0. Thoát");
            System.out.println("=============================");

            choice = Validator.validateInputInt(scanner, "Nhập số tương ứng với công nghệ muốn đăng ký: ");

            if (choice < 0 || choice > technologyList.size()) {
                System.out.printf("Lựa chọn không hợp lệ. Vui lòng chọn từ 0 đến %d.\n", technologyList.size());
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Đăng kí công nghệ thành công.");
                    System.out.println("Loading...");
                    pause(1);
                    break;
                default:
                    Technology selectedTech = technologyList.get(choice - 1);
                    CandidateTechnology candidateTech = new CandidateTechnology();
                    candidateTech.setCandidateId(candidateId);
                    candidateTech.setTechnologyId(selectedTech.getId());

                    candidateTechnologyService.addCandidateTechnology(candidateTech);
                    break;
            }

        } while (choice != 0);
    }

    public static void registerCandidate() {
        Candidate candidate = null;
        CandidateService candidateService = new CandidateServiceImp();

        do {
            System.out.println("==== Đăng ký ứng viên ====");
            Candidate inputCandidate = new Candidate();
            inputCandidate.inputData();
            choiceTechnologyName(inputCandidate.getId());

            boolean isRegister = candidateService.save(inputCandidate);
            if (isRegister) {
                System.out.println("Đăng ký thành công với vai trò ứng viên.");
                pause(1);
                candidate = inputCandidate;
            } else {
                System.out.println("Đăng ký thất bại. Vui lòng thử lại.\n");
            }
        } while (candidate == null);
    }
}
