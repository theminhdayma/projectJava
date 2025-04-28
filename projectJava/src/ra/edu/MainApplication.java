package ra.edu;

import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.account.Role;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.Account.AccountService;
import ra.edu.business.service.Account.AccountServiceImp;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyService;
import ra.edu.business.service.candidateTechnology.CandidateTechnologyServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.presentation.admin.AdminMain;
import ra.edu.presentation.candidate.CandidateMain;
import ra.edu.validate.Validator;
import ra.edu.validate.candidate.CandidateValidate;
import ra.edu.utils.Color;

import java.util.Scanner;

import static ra.edu.utils.FileUtil.readFromFile;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class MainApplication {
    public static final Scanner scanner = new Scanner(System.in);
    private static final AccountService accountService = new AccountServiceImp();
    private static final CandidateService candidateService = new CandidateServiceImp();

    public static void main(String[] args) {
        checkLoginAndDisplayMenu();
        menuMainApplication();
    }

    private static void checkLoginAndDisplayMenu() {
        String token = readFromFile();
        if (token != null && !token.isEmpty()) {
            String[] parts = token.split(":");
            if (parts.length == 2) {
                String role = parts[0];
                String username = parts[1].trim();
                int count = accountService.checkIsAccount(username);
                if (count == 0) {
                    menuMainApplication();
                }
                if ("ADMIN".equalsIgnoreCase(role)) {
                    System.out.println(Color.CYAN + "\nDuy trì đăng nhập với với tư cách là quản trị viên: " + username + Color.RESET);
                    AdminMain.displayMenuManagentAdmin();
                } else if ("CANDIDATE".equalsIgnoreCase(role)) {
                    Candidate candidate = candidateService.getCandidateByEmail(username);
                    if (candidate != null && candidate.getAccount().getStatus() == AccountStatus.ACTIVE) {
                        System.out.println(Color.CYAN + "\nDuy trì đăng nhập với tài khoản: " + candidate.getName() + Color.RESET);
                        pause(1);
                        CandidateMain.displayMenuCadidateManagent();
                    } else {
                        System.out.println(Color.RED + "Tài khoản ứng viên không hợp lệ hoặc đã bị khóa." + Color.RESET);
                    }
                } else {
                    System.out.println(Color.RED + "Vai trò không hợp lệ trong token." + Color.RESET);
                }
            } else {
                System.out.println(Color.RED + "Định dạng token không hợp lệ." + Color.RESET);
            }
        }
    }

    public static void menuMainApplication () {
        int choice;
        do {
            System.out.println();
            System.out.println(Color.BOLD + Color.CYAN + "╔════════════════════════════════════════╗");
            System.out.println("║         HỆ THỐNG TUYỂN DỤNG            ║");
            System.out.println("╚════════════════════════════════════════╝" + Color.RESET);

            String top = "┌────┬────────────────────────────────────┐";
            String mid = "├────┼────────────────────────────────────┤";
            String bot = "└────┴────────────────────────────────────┘";

            System.out.println(top);
            System.out.printf("│ %-2s │ %-34s │%n", "1", "Đăng nhập hệ thống");
            System.out.println(mid);
            System.out.printf("│ %-2s │ %-34s │%n", "2", "Đăng ký ứng viên");
            System.out.println(bot);

            choice = Validator.validateInputInt(scanner, Color.YELLOW + "→ Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1 -> login();
                case 2 -> registerCandidate();
                default -> System.out.println(Color.RED + "⚠ Lựa chọn không hợp lệ. Vui lòng thử lại!" + Color.RESET);
            }
        } while (true);
    }

    public static void login() {
        accountService.initAdmin();
        Account account;

        do {
            System.out.println(Color.BLUE + "\n==== ĐĂNG NHẬP HỆ THỐNG TUYỂN DỤNG ====" + Color.RESET);
            String username = CandidateValidate.inputValidEmailLogin(scanner);
            String password = CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu: ");

            account = accountService.login(username, password);
            if (account != null) {
                String token = account.getRole() + ":" + username;
                writeToFile(token);

                if (account.getRole() == Role.CANDIDATE) {
                    Candidate candidate = candidateService.getCandidateByEmail(username);
                    System.out.println(Color.GREEN + "Đăng nhập thành công!");
                    System.out.println(Color.GREEN + "XIN CHÀO " + candidate.getName() + "!" + Color.RESET);
                    pause(1);
                    CandidateMain.displayMenuCadidateManagent();
                } else if (account.getRole() == Role.ADMIN) {
                    System.out.println(Color.GREEN + "Đăng nhập thành công!");
                    System.out.println(Color.GREEN + "XIN CHÀO QUẢN TRỊ VIÊN " + account.getUsername() + "!" + Color.RESET);
                    pause(1);
                    AdminMain.displayMenuManagentAdmin();
                }
            } else {
                System.out.println(Color.RED + "Tên đăng nhập hoặc mật khẩu không đúng." + Color.RESET);
                boolean validChoice = false;
                while (!validChoice) {
                    System.out.println(Color.YELLOW + "1. Có - Đăng ký tài khoản" + Color.RESET);
                    System.out.println(Color.YELLOW + "2. Không - Thử lại đăng nhập" + Color.RESET);
                    System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
                    int choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);
                    switch (choice) {
                        case 1 -> {
                            registerCandidate();
                            return;
                        }
                        case 2 -> {
                            validChoice = true;
                        }
                        case 0 -> {
                            System.out.println(Color.YELLOW + "Thoát đăng nhập!" + Color.RESET);
                            return;
                        }
                        default -> System.out.println(Color.RED + "Không hợp lệ. Vui lòng chọn lại." + Color.RESET);
                    }
                }
            }
        } while (account == null);
    }

    public static void registerCandidate() {
        Candidate candidate = null;

        do {
            System.out.println(Color.BLUE + "==== Đăng ký ứng viên ====" + Color.RESET);
            Candidate inputCandidate = new Candidate();
            inputCandidate.inputData();

            boolean isRegister = candidateService.save(inputCandidate);
            if (isRegister) {
                pause(1);
                candidate = inputCandidate;
                System.out.println(Color.GREEN + "Đăng ký thành công với vai trò ứng viên." + Color.RESET);
            } else {
                System.out.println(Color.RED + "Đăng ký thất bại. Vui lòng thử lại.\n" + Color.RESET);
            }
        } while (candidate == null);
    }
}
