package ra.edu.utils;

import ra.edu.MainApplication;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.validate.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.FileUtil.readFromFile;
import static ra.edu.utils.FileUtil.writeToFile;
import static ra.edu.utils.ThreadUtil.pause;

public class Util {
    public final static int LIMIT = 5;
    private static final CandidateService candidateService = new CandidateServiceImp();

    public final static void logout() {
        System.out.println(Color.YELLOW + "Bạn có chắc chắn muốn đăng xuất?" + Color.RESET);
        System.out.println("1. Có");
        System.out.println("2. Không");

        int choice;
        do {
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    writeToFile("");
                    System.out.println(Color.GREEN + "Bạn đã đăng xuất thành công." + Color.RESET);
                    pause(1);
                    MainApplication.menuMainApplication();
                    return;
                case 2:
                    System.out.println(Color.CYAN + "Hủy đăng xuất. Quay lại chương trình..." + Color.RESET);
                    pause(1);
                    return;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
            }
        } while (true);
    }

    public final static String getAccountLogin() {
        String token = FileUtil.readFromFile();
        if (token != null && !token.isBlank()) {
            String[] parts = token.split(":");
            return parts[1];
        } else {
            return null;
        }
    }

    public static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    public static String ValidateChoicePagination(Scanner scanner) {
        String choice;
        while (true) {
            System.out.print("Chọn chức năng: ");
            choice = scanner.nextLine().trim().toLowerCase();

            if (choice.matches("[nps0]")) {
                return choice;
            } else {
                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
            }
        }
    }

    public static Candidate getCandidateLogin() {

        String token = readFromFile();
        Candidate candidate = null;

        if (token != null && !token.isBlank()) {
            String[] parts = token.split(":");
            candidate = candidateService.getCandidateByEmail(parts[1]);
        }

        return candidate;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }


}
