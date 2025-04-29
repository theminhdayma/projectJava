package ra.edu.validate;

import ra.edu.utils.Color;

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
