package ra.edu.validate.technology;

public class TechnologyValidate {
    public static boolean checkTechnologyName(String technologyName) {
        if (!technologyName.matches("^[\\p{L}0-9 ]+$")) {
            System.out.println("Tên công nghệ chỉ được chứa chữ cái, số và khoảng trắng.");
            return false;
        }

        return true;
    }
}
