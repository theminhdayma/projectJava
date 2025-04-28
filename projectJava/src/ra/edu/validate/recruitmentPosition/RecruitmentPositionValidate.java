package ra.edu.validate.recruitmentPosition;

import ra.edu.utils.Color;
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
                System.out.println(Color.RED + "Tên vị trí tuyển dụng không được để trống." + Color.RESET);
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
                System.out.println(Color.RED + "Mô tả vị trí tuyển dụng không được để trống." + Color.RESET);
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
                System.out.println(Color.RED + "Mức lương tối thiểu phải lớn hơn hoặc bằng 0." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return minSalary;
    }

    public static double inputValidMaxSalary(Scanner scanner, double minSalary) {
        double maxSalary;
        do {
            maxSalary = Validator.validateInputDouble(scanner, "Nhập mức lương tối đa: ");
            if (maxSalary <= minSalary) {
                System.out.println(Color.RED + "Mức lương tối đa phải lớn hơn mức lương tối thiểu." + Color.RESET);
            } else {
                break;
            }
        } while (true);
        return maxSalary;
    }

    public static int inputValidMinExperience(Scanner scanner) {
        int minExperience;
        do {
            minExperience = Validator.validateInputInt(scanner, "Nhập số năm kinh nghiệm tối thiểu: ");
            if (minExperience < 0) {
                System.out.println(Color.RED + "Số năm kinh nghiệm tối thiểu không được âm." + Color.RESET);
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
                System.out.println(Color.RED + "Ngày hết hạn không được để trống." + Color.RESET);
                continue;
            }

            try {
                expiredDate = LocalDate.parse(input, formatter);
                if (expiredDate.isAfter(createdDate)) {
                    return expiredDate;
                } else {
                    System.out.println(Color.RED +  "Ngày hết hạn phải sau ngày tạo (" + createdDate.format(formatter) + ")." + Color.RESET);
                }
            } catch (DateTimeParseException e) {
                System.out.println(Color.RED + "Định dạng ngày không hợp lệ. Vui lòng nhập lại (dd-MM-yyyy)." + Color.RESET);
            }
        } while (true);
    }

}
