package ra.edu.validate;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Validator {

    public static int validateInputInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.err.println("Không được để trống! Vui lòng nhập lại.");
                    continue;
                }

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Không phải số nguyên, yêu cầu nhập lại!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String validateInputString(Scanner scanner, String message, StringRule rule) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine()
                        .replaceAll("\\s+", " ")
                        .trim();

                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Không được để trống! Vui lòng nhập lại");
                }

                if (!rule.isValidString(input)) {
                    throw new IllegalArgumentException("Độ dài không hợp lệ (tối thiểu " + rule.minLength + ", tối đa " + rule.maxLength + " ký tự).");
                }

                return input;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static double validateInputDouble(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.err.println("Không được để trống! Vui lòng nhập lại.");
                    continue;
                }

                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.println("Không phải số thực double, yêu cầu nhập lại!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean validateInputBoolean(Scanner scanner, String message) {
        while (true) {
            System.out.print(message + " (Nhập true/false): ");
            try {
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.isEmpty()) {
                    System.err.println("Không được để trống! Vui lòng nhập lại.");
                    continue;
                }

                if (!input.equals("true") && !input.equals("false")) {
                    throw new InputMismatchException("Không phải giá trị boolean hợp lệ, yêu cầu nhập lại (true/false)!");
                }

                return Boolean.parseBoolean(input);
            } catch (InputMismatchException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
