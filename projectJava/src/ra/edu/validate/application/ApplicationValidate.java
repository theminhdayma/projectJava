package ra.edu.validate.application;

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
                System.err.println("cvUrl không được để trống.");
            } else if (!url.toLowerCase().matches(regex)) {
                System.err.println("cvUrl không đúng định dạng hoặc không kết thúc bằng .pdf. Vui lòng nhập URL hợp lệ (ví dụ: https://example.com/cv.pdf).");
            } else {
                break;
            }
        } while (true);

        return url;
    }

    public static String validateDestroyReason(Scanner scanner) {
        String reason;

        do {
            System.out.print("Nhập lý do hủy đơn ứng tuyển: ");
            reason = scanner.nextLine().trim();

            if (reason.isEmpty()) {
                System.err.println("Lý do không được để trống.");
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
                    System.err.println("Ngày không được để trống.");
                    continue;
                }

                System.out.print("Nhập giờ phỏng vấn (HH:mm): ");
                String timeInput = scanner.nextLine().trim();

                if (timeInput.isEmpty()) {
                    System.err.println("Giờ không được để trống.");
                    continue;
                }

                String dateTimeString = dateInput + " " + timeInput;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                interviewDateTime = LocalDateTime.parse(dateTimeString, formatter);

                if (interviewDateTime.isBefore(LocalDateTime.now())) {
                    System.err.println("Ngày giờ phỏng vấn phải lớn hơn hoặc bằng thời gian hiện tại.");
                    continue;
                }

                break;

            } catch (DateTimeParseException e) {
                System.err.println("Ngày hoặc giờ không hợp lệ. Vui lòng nhập đúng định dạng và giá trị thực tế.");
            }
        }

        return interviewDateTime;
    }

}
