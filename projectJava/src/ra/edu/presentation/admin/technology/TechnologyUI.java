package ra.edu.presentation.admin.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.utils.Color;
import ra.edu.utils.Util;
import ra.edu.validate.Validator;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;

public class TechnologyUI {
    private static final TechnologyServiceImp technologyService = new TechnologyServiceImp();
    public static void displayMenuTechnology() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("MENU CÔNG NGHỆ", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Xem danh sách công nghệ");
            System.out.printf("| %-3s | %-50s |\n", "2", "Thêm công nghệ");
            System.out.printf("| %-3s | %-50s |\n", "3", "Sửa công nghệ");
            System.out.printf("| %-3s | %-50s |\n", "4", "Xóa công nghệ");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay về menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

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
                case 0:
                    System.out.println("\nĐang quay về menu chính...");
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ, vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);
    }


    private static Technology findTechnologyById(int id) {
        Technology tech = technologyService.getTechnologyById(id);
        if (tech == null) {
            System.out.println("Không tìm thấy công nghệ với ID: " + id + " Vui lòng thử lại.");
        }
        return tech;
    }

    private static void showAllTechnology() {
        int limit = Validator.validateInputInt(scanner, Color.CYAN + "Nhập số công nghệ muốn hiển thị trên 1 trang: " + Color.RESET);
        int totalPage = technologyService.getTotalPage(limit);

        if (totalPage == 0) {
            System.out.println(Color.YELLOW + "Không có công nghệ nào để hiển thị." + Color.RESET);
            return;
        }

        int currentPage = 1;
        int idWidth = 5;
        int nameWidth = 40;

        while (true) {
            System.out.println("\n" + Color.BOLD + Color.BLUE + "== CÔNG NGHỆ TRANG " + currentPage + " / " + totalPage + " ==" + Color.RESET);

            String top = "┌" + "─".repeat(idWidth + 2) + "┬" + "─".repeat(nameWidth + 2) + "┐";
            String mid = "├" + "─".repeat(idWidth + 2) + "┼" + "─".repeat(nameWidth + 2) + "┤";
            String bot = "└" + "─".repeat(idWidth + 2) + "┴" + "─".repeat(nameWidth + 2) + "┘";
            String header = String.format("│ %-"+idWidth+"s │ %-"+nameWidth+"s │", "ID", "Tên Công Nghệ");

            System.out.println(Color.BOLD + top + Color.RESET);
            System.out.println(Color.BOLD + header + Color.RESET);
            System.out.println(Color.BOLD + mid + Color.RESET);

            technologyService.getTechnologyByPage(currentPage, limit)
                    .forEach(tech -> System.out.printf("│ %-" + idWidth + "d │ %-" + nameWidth + "s │\n", tech.getId(), tech.getName()));

            System.out.println(Color.BOLD + bot + Color.RESET);

            // Hiển thị số trang
            System.out.print("Trang: ");
            for (int i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                } else {
                    System.out.print("[" + i + "] ");
                }
            }
            System.out.println();

            if (totalPage == 1) {
                System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
            } else {
                if (currentPage > 1) System.out.println(Color.CYAN + "p. Trang trước" + Color.RESET);
                if (currentPage < totalPage) System.out.println(Color.CYAN + "n. Trang tiếp theo" + Color.RESET);
                System.out.println(Color.CYAN + "s. Nhập trang muốn hiển thị" + Color.RESET);
                System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
            }

            String choice = Util.ValidateChoicePagination(scanner);
            switch (choice) {
                case "0" -> {
                    System.out.println(Color.YELLOW + "Thoát khỏi hiển thị công nghệ." + Color.RESET);
                    pause(1);
                    return;
                }
                case "p" -> {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.err.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                    }
                }
                case "n" -> {
                    if (currentPage < totalPage) {
                        currentPage++;
                    } else {
                        System.err.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                    }
                }
                case "s" -> {
                    int newPage = Validator.validateInputInt(scanner, Color.CYAN + "Nhập trang muốn xem (1 - " + totalPage + "): " + Color.RESET);
                    if (newPage >= 1 && newPage <= totalPage) {
                        currentPage = newPage;
                    } else {
                        System.err.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                    }
                }
                default -> System.err.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
            }
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
