package ra.edu.validate;

import ra.edu.utils.Color;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Validator {

    public static int validateInputInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println(Color.RED + "Không được để trống! Vui lòng nhập lại." + Color.RESET);
                    continue;
                }

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Không phải số nguyên, yêu cầu nhập lại!" + Color.RESET);
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
                    System.out.println(Color.RED + "Không được để trống! Vui lòng nhập lại." + Color.RESET);
                }

                if (!rule.isValidString(input)) {
                    throw new IllegalArgumentException("Độ dài không hợp lệ (tối thiểu " + rule.minLength + ", tối đa " + rule.maxLength + " ký tự).");
                }

                return input;
            } catch (IllegalArgumentException e) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);
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
                    System.out.println(Color.RED + "Không được để trống! Vui lòng nhập lại." + Color.RESET);
                    continue;
                }

                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Không phải số thực double, yêu cầu nhập lại!" + Color.RESET);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
