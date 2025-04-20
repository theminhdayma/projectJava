package ra.edu.presentation.admin.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;

public class TechnologyUI {
    private static final TechnologyServiceImp technologyService = new TechnologyServiceImp();
    public static void displayMenuTechnology() {
        int choice;
        do {
            System.out.println("\n====== MENU CÔNG NGHỆ ======");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xóa công nghệ");
            System.out.println("5. Quay về menu chính");
            System.out.print("Chọn: ");
            choice = Validator.validateInputInt(scanner, "Mời bạn chọn: ");

            switch (choice) {
                case 1:
                    showAllTechnology();
                    break;
                case 2:
                    addTechnology();
                    break;
                case 3:
                    updateTechnology();
                    break;
                case 4:
                    deleteTechnology();
                    break;
                case 5:
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.out.println("Không hợp lệ, vui lòng chọn từ 1 đến 5.");
            }
        } while (choice != 5);
    }

    private static Technology findTechnologyById(int id) {
        Technology tech = technologyService.getTechnologyById(id);
        if (tech == null) {
            System.out.println("Không tìm thấy công nghệ với ID: " + id + " Vui lòng thử lại.");
        }
        return tech;
    }

    private static void showAllTechnology() {
        int totalPage = technologyService.getTotalPage(LIMIT);

        if (totalPage == 0) {
            System.out.println("Không có công nghệ nào để hiển thị.");
            return;
        }

        while (true) {
            System.out.println("\n== DANH SÁCH TRANG CÔNG NGHỆ ==");
            for (int i = 1; i <= totalPage; i++) {
                System.out.printf("%d. Trang %d\n", i, i);
            }
            System.out.println("0. Thoát");

            int pageChoice;
            do {
                try {
                    pageChoice = Validator.validateInputInt(scanner, "Chọn trang muốn xem: ");
                } catch (NumberFormatException e) {
                    pageChoice = -1;
                }
            } while (pageChoice < 0 || pageChoice > totalPage);

            if (pageChoice == 0) {
                System.out.println("Thoát khỏi hiển thị công nghệ.");
                pause(1);
                break;
            }

            System.out.println("\n== CÔNG NGHỆ TRANG " + pageChoice + " ==");
            System.out.println("+-----+--------------------------------+");
            System.out.printf("| %-3s | %-30s |\n", "ID", "Tên Công Nghệ");
            System.out.println("+-----+--------------------------------+");
            technologyService.getTechnologyByPage(pageChoice, LIMIT)
                    .forEach(tech -> System.out.printf("| %-3d | %-30s |\n", tech.getId(), tech.getName()));
            System.out.println("+-----+--------------------------------+");
        }
    }

    private static void addTechnology() {
        int quantity = Validator.validateInputInt(scanner, "Nhập số lượng công nghệ muốn thêm: ");

        for (int i = 1; i <= quantity; i++) {
            System.out.println("\n== Nhập công nghệ thứ " + i + " ==");
            Technology newTech = new Technology();
            newTech.inputData();

            if (technologyService.save(newTech)) {
                System.out.println("Thêm công nghệ thành công.");
            } else {
                System.out.println("Thêm công nghệ thất bại.");
            }
        }
    }

    private static void updateTechnology() {
        int id;
        Technology isTechnology;
        int attempts = 0;

        while (attempts < 3) {
            id = Validator.validateInputInt(scanner, "Nhập ID công nghệ muốn sửa: ");

            isTechnology = findTechnologyById(id);

            if (isTechnology != null) {
                System.out.print("Nhập tên mới: ");
                String name = scanner.nextLine();

                isTechnology.setName(name);
                if (technologyService.update(isTechnology)) {
                    System.out.println("Cập nhật thành công.");
                } else {
                    System.out.println("Cập nhật thất bại.");
                }
                return;
            } else {
                attempts++;
                if (attempts == 3) {
                    System.out.println("Quá 3 lần sai ID.");
                }
            }
        }
    }

    private static void deleteTechnology() {
        int id;
        Technology isTechnology;
        int attempts = 0;

        while (attempts < 3) {
            id = Validator.validateInputInt(scanner, "Nhập ID công nghệ muốn xóa: ");

            isTechnology = findTechnologyById(id);

            if (isTechnology != null) {
                System.out.print("Bạn có chắc chắn muốn xóa công nghệ này? (1: Có, 2: Không): ");
                int confirmation = Integer.parseInt(scanner.nextLine());

                if (confirmation == 1) {
                    if (technologyService.delete(isTechnology)) {
                        System.out.println("Xóa thành công.");
                    } else {
                        System.out.println("Xóa thất bại.");
                    }
                    return;
                } else if (confirmation == 2) {
                    System.out.println("Hủy bỏ thao tác xóa.");
                    return;
                } else {
                    System.out.println("Lựa chọn không hợp lệ !");
                }
            } else {
                attempts++;
                if (attempts == 3) {
                    System.out.println("Quá 3 lần sai ID.");
                }
            }
        }
    }
}
