package ra.edu.validate.candidate;

import ra.edu.business.model.candidate.Gender;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;


public class CandidateValidate {
    private static final CandidateService candidateService = new CandidateServiceImp();

    public static String inputValidName(Scanner scanner) {
        String name;
        do {
            System.out.print("Nhập tên ứng viên: ");
            name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                System.out.println(Color.RED + "Tên ứng viên không được để trống." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return name;
    }


    public static String inputValidPassword(Scanner scanner, String message) {
        String password;
        do {
            System.out.print(message);
            password = scanner.nextLine().trim();

            if (password == null || password.trim().isEmpty()) {
                System.out.println(Color.RED + "Mật khẩu không được để trống." + Color.RESET);
            } else if (password.length() < 6) {
                System.out.println(Color.RED + "Mật khẩu phải có ít nhất 6 ký tự." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return password;
    }

    public static String inputValidDes(Scanner scanner) {
        String description;
        do {
            System.out.print("Nhập mô tả bản thân: ");
            description = scanner.nextLine().trim();
            if (description == null || description.trim().isEmpty()) {
                System.out.println(Color.RED + "Mô tả bản thân không được để trống." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return description;
    }


    public static String inputValidEmail(Scanner scanner) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String email;
        do {
            System.out.print("Nhập email: ");
            email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println(Color.RED + "Email không được để trống." + Color.RESET);
            } else if (!Pattern.matches(emailRegex, email)) {
                System.out.println(Color.RED + "Email không đúng định dạng." + Color.RESET);
            } else if (candidateService.checkEmailCandidate(email)) {
                System.out.println(Color.RED + "Email đã tồn tại. Vui lòng nhập email khác." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return email;
    }

    public static String inputValidEmailLogin(Scanner scanner) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String email;
        do {
            System.out.print("Nhập email: ");
            email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println(Color.RED + "Email không được để trống." + Color.RESET);
            } else if (!Pattern.matches(emailRegex, email)) {
                System.out.println(Color.RED + "Email không đúng định dạng." + Color.RESET);
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
            phone = scanner.nextLine().trim();

            if (phone == null || phone.trim().isEmpty()) {
                System.out.println(Color.RED + "Số điện thoại không hợp lệ." + Color.RESET);
            } else if (!Pattern.matches(phoneRegex, phone)) {
                System.out.println(Color.RED + "Số điện thoại không hợp lệ. Vui lòng nhập lại." + Color.RESET);
            } else if (candidateService.checkPhoneCandidate(phone)) {
                System.out.println(Color.RED + "Số điện thoại đã tồn tại. Vui lòng nhập số điện thoại khác." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return phone;
    }

    public static LocalDate inputValidDob(Scanner scanner) {
        LocalDate dob = null;

        while (dob == null) {
            System.out.print("Nhập ngày sinh (dd-MM-yyyy): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(Color.RED + "Không được để trống ngày sinh." + Color.RESET);
                continue;
            }

            if (!input.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                System.out.println(Color.RED + "Định dạng phải là dd-MM-yyyy (ngày và tháng 2 chữ số, năm 4 chữ số)." + Color.RESET);
                continue;
            }

            String[] parts = input.split("-");
            int day, month, year;

            try {
                day = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);

                if (year < 1950 || year > LocalDate.now().getYear()) {
                    System.out.println(Color.RED + "Năm không hợp lệ (1950 - năm hiện tại)." + Color.RESET);
                    continue;
                }

                if (month < 1 || month > 12) {
                    System.out.println(Color.RED + "Tháng không hợp lệ (01 - 12)." + Color.RESET);
                    continue;
                }

                dob = LocalDate.of(year, month, day);

                if (dob.isAfter(LocalDate.now())) {
                    System.out.println(Color.RED + "Ngày sinh không được lớn hơn ngày hiện tại." + Color.RESET);
                    dob = null;
                }

            } catch (DateTimeException e) {
                System.out.println(Color.RED + "Ngày không hợp lệ với tháng hoặc năm." + Color.RESET);
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

            int choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    return Gender.MALE;
                case 2:
                    return Gender.FEMALE;
                case 3:
                    return Gender.OTHER;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng chọn lại (1-3)." + Color.RESET);  // Thông báo lỗi màu đỏ
            }
        }
    }

    public static int inputValidExperience(Scanner scanner) {
        int experience;
        do {
            try {
                experience = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm: ");
                if (experience < 0) {
                    System.out.println(Color.RED + "Số năm kinh nghiệm không được âm." + Color.RESET);
                } else {
                    return experience;
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Vui lòng nhập đúng định dạng số." + Color.RESET);
            }
        } while (true);
    }
}
