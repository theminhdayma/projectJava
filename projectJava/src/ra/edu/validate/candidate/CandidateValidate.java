package ra.edu.validate.candidate;

import ra.edu.business.model.candidate.Gender;
import ra.edu.business.service.candidate.CandidateService;
import ra.edu.business.service.candidate.CandidateServiceImp;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class CandidateValidate {
    private static CandidateService candidateService;

    public static CandidateService getCandidateService() {
        if (candidateService == null) {
            candidateService = new CandidateServiceImp();
        }
        return candidateService;
    }

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

    public static String inputValidPassword(Scanner scanner, String massage) {
        String password;
        do {
            System.out.print(massage);
            password = scanner.nextLine();
            if (password == null || password.trim().isEmpty()) {
                System.err.println("Mật khẩu không được để trống.");
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
            description = scanner.nextLine();
            if (description == null || description.trim().isEmpty()) {
                System.err.println("Mô tả bản thân không được để trống.");
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
                System.err.println("Email không được để trống.");
            } else if (!Pattern.matches(emailRegex, email)) {
                System.err.println("Email không đúng định dạng.");
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
            if (phone == null || phone.trim().isEmpty()) {
                System.err.println("Số điện thoại không hợp lệ.");
            } else if (!Pattern.matches(phoneRegex, phone)) {
                System.err.println("Số điện thoại không hợp lệ. Vui lòng nhập lại.");
            }else {
                break;
            }
        } while (true);
        return phone;
    }

    public static LocalDate inputValidDob(Scanner scanner) {
        LocalDate dob = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (dob == null) {
            System.out.print("Nhập ngày sinh (định dạng dd-MM-yyyy): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.err.println("Không được để trống ngày sinh.\n");
                continue;
            }

            try {
                dob = LocalDate.parse(input, formatter);

                if (dob.isAfter(LocalDate.now())) {
                    System.err.println("Ngày sinh không được lớn hơn ngày hiện tại.\n");
                    dob = null;
                }
            } catch (DateTimeParseException e) {
                System.err.println("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy.\n");
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

            int choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");
            switch (choice) {
                case 1:
                    return Gender.MALE;
                case 2:
                    return Gender.FEMALE;
                case 3:
                    return Gender.OTHER;
                default:
                    System.err.println("Lựa chọn không hợp lệ, vui lòng chọn lại (1-3).");
            }
        }
    }

    public static int inputValidExperience (Scanner scanner) {
        int experience;
        do {
            try {
                experience = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm: ");
                if (experience < 0) {
                    System.err.println("Số năm kinh nghiệm không được âm.");
                } else {
                    return experience;
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập đúng định dạng số.");
            }
        } while (true);
    }

}
