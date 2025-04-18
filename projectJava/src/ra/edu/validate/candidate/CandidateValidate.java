package ra.edu.validate.candidate;

import ra.edu.business.model.candidate.Gender;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CandidateValidate {

    public static String inputValidName(Scanner scanner) {
        String name;
        do {
            System.out.print("Nhập tên ứng viên: ");
            name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                System.err.println("Tên ứng viên không được để trống.");
            } else {
                break;
            }
        } while (true);
        return name;
    }

    public static String inputValidPassword(Scanner scanner) {
        String password;
        do {
            System.out.print("Nhập mật khẩu: ");
            password = scanner.nextLine();
            if (password == null || password.trim().isEmpty()) {
                System.err.println("Mật khẩu không được để trống.");
            } else {
                break;
            }
        } while (true);
        return password;
    }

    public static String inputValidEmail(Scanner scanner) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String email;
        do {
            System.out.print("Nhập email: ");
            email = scanner.nextLine();
            if (email == null || email.trim().isEmpty() || !Pattern.matches(emailRegex, email)) {
                System.err.println("Email không hợp lệ.");
            } else {
                break;
            }
        } while (true);
        return email;
    }

    public static String inputValidPhone(Scanner scanner) {
        String phoneRegex = "^0\\d{9}$";
        String phone;
        do {
            System.out.print("Nhập số điện thoại: ");
            phone = scanner.nextLine();
            if (phone == null || phone.trim().isEmpty() || !Pattern.matches(phoneRegex, phone)) {
                System.err.println("Số điện thoại không hợp lệ.");
            } else {
                break;
            }
        } while (true);
        return phone;
    }

    public static LocalDate inputValidDob(Scanner scanner) {
        LocalDate dob = null;
        while (dob == null) {
            try {
                System.out.print("Nhập ngày sinh (1 - 31): ");
                int day = Integer.parseInt(scanner.nextLine());

                System.out.print("Nhập tháng sinh (1 - 12): ");
                int month = Integer.parseInt(scanner.nextLine());

                System.out.print("Nhập năm sinh (yyyy): ");
                int year = Integer.parseInt(scanner.nextLine());

                dob = LocalDate.of(year, month, day);

                if (dob.isAfter(LocalDate.now())) {
                    System.err.println("Ngày sinh không được sau ngày hiện tại. Vui lòng nhập lại.\n");
                    dob = null;
                }

            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập đúng định dạng số.\n");
            } catch (DateTimeException e) {
                System.err.println("Ngày sinh không hợp lệ (ví dụ như 31/02). Vui lòng nhập lại.\n");
            }
        }
        return dob;
    }

    public static Gender inputValidGender(Scanner scanner) {
        while (true) {
            System.out.println("Chọn giới tính:");
            System.out.println("1. Nam");
            System.out.println("2. Nữ");
            System.out.println("3. Khác");
            System.out.print("Lựa chọn của bạn: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    return Gender.MALE;
                case "2":
                    return Gender.FEMALE;
                case "3":
                    return Gender.OTHER;
                default:
                    System.err.println("Lựa chọn không hợp lệ, vui lòng chọn lại (1-3).");
            }
        }
    }

}
