package ra.edu.validate.application;

import ra.edu.utils.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ApplicationValidate {
    public static String validateCvUrl(Scanner scanner) {
        String url;
        String regex = "^(https?://)?([\\w.-]+)+(:\\d+)?(/.*)?\\.pdf$";

        do {
            System.out.print("Nhập cvUrl dạng pdf: ");
            url = scanner.nextLine().trim();

            if (url.isEmpty()) {
                System.out.println(Color.RED + "cvUrl không được để trống." + Color.RESET);
            } else if (!url.toLowerCase().matches(regex)) {
                System.out.println(Color.RED + "cvUrl không đúng định dạng hoặc không kết thúc bằng .pdf. Vui lòng nhập URL hợp lệ (ví dụ: https://example.com/cv.pdf)." + Color.RESET);
            } else {
                break;
            }
        } while (true);

        return url;
    }

    public static String validateReason(Scanner scanner) {
        String reason;

        do {
            System.out.print("Nhập lý do: ");
            reason = scanner.nextLine().trim();

            if (reason.isEmpty()) {
                System.out.println(Color.RED + "Lý do không được để trống." + Color.RESET);
            } else {
                break;
            }
        } while (true);

        return reason;
    }

    public static LocalDateTime validateInterviewDateTime(Scanner scanner) {
        LocalDateTime interviewDateTime;

        while (true) {
            try {
                System.out.print("Nhập ngày phỏng vấn (dd-MM-yyyy): ");
                String dateInput = scanner.nextLine().trim();

                if (dateInput.isEmpty()) {
                    System.out.println(Color.RED + "Ngày không được để trống." + Color.RESET);
                    continue;
                }

                System.out.print("Nhập giờ phỏng vấn (HH:mm): ");
                String timeInput = scanner.nextLine().trim();

                if (timeInput.isEmpty()) {
                    System.out.println(Color.RED + "Giờ không được để trống." + Color.RESET);
                    continue;
                }

                String dateTimeString = dateInput + " " + timeInput;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                interviewDateTime = LocalDateTime.parse(dateTimeString, formatter);

                if (interviewDateTime.isBefore(LocalDateTime.now())) {
                    System.out.println(Color.RED + "Ngày giờ phỏng vấn phải lớn hơn hoặc bằng thời gian hiện tại." + Color.RESET);
                    continue;
                }

                break;

            } catch (DateTimeParseException e) {
                System.out.println(Color.RED + "Ngày hoặc giờ không hợp lệ. Vui lòng nhập đúng định dạng và giá trị thực tế." + Color.RESET);
            }
        }

        return interviewDateTime;
    }
}
