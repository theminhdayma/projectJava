
package ra.edu.validate.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.utils.Color;

import java.util.Scanner;

public class TechnologyValidate {
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    public static String checkTechnologyName(Scanner scanner) {
        String technologyName;

        while (true) {
            System.out.print("Nhập tên công nghệ: ");
            technologyName = scanner.nextLine().trim();

            if (technologyName.isEmpty()) {
                System.out.println(Color.RED + "Tên công nghệ không được để trống!" + Color.RESET);
                continue;

            }

            if (!technologyName.matches("^[\\p{L}0-9 ]+$")) {
                System.out.println(Color.RED + "Tên công nghệ chỉ được chứa chữ cái, số và khoảng trắng." + Color.RESET);
                continue;
            }

            if (technologyService.checkNameTechnology(technologyName)) {
                continue;
            }

            return technologyName;
        }
    }

}
