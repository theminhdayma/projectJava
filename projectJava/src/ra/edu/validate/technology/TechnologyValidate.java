package ra.edu.validate.technology;

import ra.edu.utils.Color;

import java.util.Scanner;

public class TechnologyValidate {
    public static String checkTechnologyName(Scanner scanner) {
        System.out.print("Nhập tên công nghệ: ");
        String technologyName = scanner.nextLine().trim();

        if (technologyName.isEmpty()) {
            System.out.println(Color.RED + "Tên công nghệ không được để trống!" + Color.RESET);
            return "";
        }

        if (!technologyName.matches("^[\\p{L}0-9 ]+$")) {
            System.out.println(Color.RED + "Tên công nghệ chỉ được chứa chữ cái, số và khoảng trắng." + Color.RESET);
            return "";
        }

        return technologyName;
    }


}
