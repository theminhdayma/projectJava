package ra.edu.validate.recruitmentPosition;

import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RecruitmentPositionValidate {
    public static String inputValidName(Scanner scanner) {
        String name;
        do {
            System.out.print("Nhập tên vị trí tuyển dụng: ");
            name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                System.err.println("Tên vị trí tuyển dụng không được để trống.");
            } else {
                break;
            }
        } while (true);
        return name;
    }

    public static String inputValidDes(Scanner scanner) {
        String description;
        do {
            System.out.print("Nhập mô tả vị trí tuyển dụng: ");
            description = scanner.nextLine();
            if (description == null || description.trim().isEmpty()) {
                System.err.println("Mô tả vị trí tuyển dụng không được để trống.");
            } else {
                break;
            }
        } while (true);
        return description;
    }

    public static double inputValidMinSalary(Scanner scanner) {
        double minSalary;
        do {
            minSalary = Validator.validateInputDouble(scanner, "Nhập mức lương tối thiểu: ");
            if (minSalary <= 0) {
                System.err.println("Mức lương tối thiểu phải lớn hơn hoặc bằng 0.");
            } else {
                break;
            }
        } while (true);
        return minSalary;
    }

    public static double inputValidMaxSalary(Scanner scanner, double minSalary) {
        double maxSalary;
        do {
            System.out.print("Nhập lương tối đa: ");
            try {
                maxSalary = Double.parseDouble(scanner.nextLine());
                if (maxSalary > minSalary) {
                    return maxSalary;
                } else {
                    System.out.println("Lương tối đa phải lớn hơn lương tối thiểu (" + minSalary + ")!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập đúng định dạng số!");
            }
        } while (true);
    }

    public static int inputValidMinExperience(Scanner scanner) {
        int minExperience;
        do {
            minExperience = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm tối thiểu: ");
            if (minExperience < 0) {
                System.err.println("Số năm kinh nghiệm tối thiểu không được âm.");
            } else {
                break;
            }
        } while (true);
        return minExperience;
    }

    public static LocalDate inputValidExpiredDate(Scanner scanner, LocalDate createdDate) {
        LocalDate expiredDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        do {
            System.out.print("Nhập ngày hết hạn (dd-MM-yyyy): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.err.println("Không được để trống ngày hết hạn.");
                continue;
            }

            try {
                expiredDate = LocalDate.parse(input, formatter);
                if (expiredDate.isAfter(createdDate)) {
                    return expiredDate;
                } else {
                    System.err.println("Ngày hết hạn phải sau ngày tạo (" + createdDate.format(formatter) + ").");
                }
            } catch (DateTimeParseException e) {
                System.err.println("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng dd-MM-yyyy.");
            }
        } while (true);
    }

}
