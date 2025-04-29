package ra.edu.presentation.admin.recruitmentPosition;

import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.recruitmentPositionTechnology.RecruitmentPositionTechnology;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPosition.RecruitmentPositionServiceImp;
import ra.edu.business.service.recruitmentPositionTechnology.RecruitmentPositionTechnologyService;
import ra.edu.business.service.recruitmentPositionTechnology.RecruitmentPositionTechnologyServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.presentation.admin.candidate.CandidateUI;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;
import ra.edu.validate.recruitmentPosition.RecruitmentPositionValidate;

import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.*;

public class RecruitmentPositionUI {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    private static final TechnologyService technologyService = new TechnologyServiceImp();
    private static final RecruitmentPositionTechnologyService recruitmentPositionTechnologyService = new RecruitmentPositionTechnologyServiceImp();
    public static void displayMenuRecruitmentPosition() {
        int choice;
        do {
            System.out.println("\n" + Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);
            System.out.println(Color.BOLD + Color.center("QUẢN LÝ VỊ TRÍ TUYỂN DỤNG", Color.WIDTH) + Color.RESET);
            System.out.println(Color.GREEN + Color.repeat("=", Color.WIDTH) + Color.RESET);

            System.out.printf("| %-3s | %-50s |\n", "1", "Thêm vị trí tuyển dụng mới");
            System.out.printf("| %-3s | %-50s |\n", "2", "Cập nhật vị trí tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "3", "Xóa vị trí tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "4", "Xem danh sách vị trí tuyển dụng");
            System.out.printf("| %-3s | %-50s |\n", "0", "Quay lại menu chính");

            System.out.println(Color.GREEN + Color.repeat("-", Color.WIDTH) + Color.RESET);
            choice = Validator.validateInputInt(scanner, Color.CYAN + "Mời bạn chọn: " + Color.RESET);

            switch (choice) {
                case 1:
                    addNewRecruitmentPosition();
                    break;
                case 2:
                    updateRecruitmentPosition();
                    break;
                case 3:
                    deleteRecruitmentPosition();
                    break;
                case 4:
                    showAllRecruitmentPosition();
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


    private static RecruitmentPosition getRecruitmentPositionById() {
        RecruitmentPosition recruitmentPosition = null;
        do {
            int id = Validator.validateInputInt(scanner, "Nhập ID vị trí tin tuyển dụng cần xử lý: ");
            recruitmentPosition = recruitmentPositionService.getRecruitmentPositionById(id);
            if (recruitmentPosition == null) {
                System.out.println( Color.RED + "Không tìm thấy vị trí tuyển dụng với ID: " + id + ". Vui lòng thử lại." + Color.RESET);
            }
        } while (recruitmentPosition == null);

        return recruitmentPosition;
    }

    private static void showRecruitmentPositionById(RecruitmentPosition recruitmentPosition) {
        System.out.println(Color.CYAN + "\n== THÔNG TIN VỊ TRÍ TUYỂN DỤNG ==" + Color.RESET);

        int labelWidth = 30;
        int valueWidth = 40;

        String top = "┌" + "─".repeat(labelWidth + 2) + "┬" + "─".repeat(valueWidth + 2) + "┐";
        String bot = "└" + "─".repeat(labelWidth + 2) + "┴" + "─".repeat(valueWidth + 2) + "┘";

        System.out.println(Color.BOLD + Color.BOLD + top + Color.RESET);

        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "ID", recruitmentPosition.getId());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "Vị trí tuyển dụng", recruitmentPosition.getName());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40.1f │\n", "Min Lương ($)", recruitmentPosition.getMinSalary());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40.1f │\n", "Max Lương ($)", recruitmentPosition.getMaxSalary());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40d │\n", "Kinh Nghiệm (Năm)", recruitmentPosition.getMinExperience());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "Ngày Tạo", recruitmentPosition.getCreatedDate());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "Hết Hạn", recruitmentPosition.getExpiredDate());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "Trạng Thái", recruitmentPosition.getStatus());
        System.out.printf(Color.BOLD + "│ %-30s │ %-40s │\n", "Mô Tả", truncate(recruitmentPosition.getDescription(), 40));

        System.out.println(Color.BOLD + Color.BOLD + bot + Color.RESET);
    }


    private static void showTableRecruitmentPosition(List<RecruitmentPosition> recruitmentPositions) {
        if (recruitmentPositions.isEmpty()) {
            System.out.println(Color.RED + "Không có vị trí tuyển dụng nào để hiển thị." + Color.RESET);
            return;
        }

        int idWidth = 5;
        int nameWidth = 30;
        int minSalaryWidth = 15;
        int maxSalaryWidth = 15;
        int experienceWidth = 20;
        int createdDateWidth = 15;
        int expiredDateWidth = 15;
        int statusWidth = 10;
        int descriptionWidth = 45;

        String top = "┌" + "─".repeat(idWidth + 2)
                + "┬" + "─".repeat(nameWidth + 2)
                + "┬" + "─".repeat(minSalaryWidth + 2)
                + "┬" + "─".repeat(maxSalaryWidth + 2)
                + "┬" + "─".repeat(experienceWidth + 2)
                + "┬" + "─".repeat(createdDateWidth + 2)
                + "┬" + "─".repeat(expiredDateWidth + 2)
                + "┬" + "─".repeat(statusWidth + 2)
                + "┬" + "─".repeat(descriptionWidth + 2)
                + "┐";

        String mid = "├" + "─".repeat(idWidth + 2)
                + "┼" + "─".repeat(nameWidth + 2)
                + "┼" + "─".repeat(minSalaryWidth + 2)
                + "┼" + "─".repeat(maxSalaryWidth + 2)
                + "┼" + "─".repeat(experienceWidth + 2)
                + "┼" + "─".repeat(createdDateWidth + 2)
                + "┼" + "─".repeat(expiredDateWidth + 2)
                + "┼" + "─".repeat(statusWidth + 2)
                + "┼" + "─".repeat(descriptionWidth + 2)
                + "┤";

        String bot = "└" + "─".repeat(idWidth + 2)
                + "┴" + "─".repeat(nameWidth + 2)
                + "┴" + "─".repeat(minSalaryWidth + 2)
                + "┴" + "─".repeat(maxSalaryWidth + 2)
                + "┴" + "─".repeat(experienceWidth + 2)
                + "┴" + "─".repeat(createdDateWidth + 2)
                + "┴" + "─".repeat(expiredDateWidth + 2)
                + "┴" + "─".repeat(statusWidth + 2)
                + "┴" + "─".repeat(descriptionWidth + 2)
                + "┘";

        String header = String.format("│ %-" + idWidth + "s │ %-" + nameWidth + "s │ %-" + minSalaryWidth + "s │ %-" + maxSalaryWidth + "s │ %-" + experienceWidth + "s │ %-" + createdDateWidth + "s │ %-" + expiredDateWidth + "s │ %-" + statusWidth + "s │ %-" + descriptionWidth + "s │",
                "ID", "Vị trí tuyển dụng", "Min Lương ($)", "Max Lương($)", "Kinh Nghiệm (Năm)", "Ngày Tạo", "Hết Hạn", "Trạng Thái", "Mô Tả");

        System.out.println(Color.BOLD + top + Color.RESET);
        System.out.println(Color.BOLD + header + Color.RESET);
        System.out.println(Color.BOLD + mid + Color.RESET);

        recruitmentPositions.forEach(recruitmentPosition -> {
            System.out.printf("│ %-5d │ %-30s │ %-15.1f │ %-15.1f │ %-20d │ %-15s │ %-15s │ %-10s │ %-45s │\n",
                    recruitmentPosition.getId(),
                    recruitmentPosition.getName(),
                    recruitmentPosition.getMinSalary(),
                    recruitmentPosition.getMaxSalary(),
                    recruitmentPosition.getMinExperience(),
                    recruitmentPosition.getCreatedDate(),
                    recruitmentPosition.getExpiredDate(),
                    recruitmentPosition.getStatus(),
                    truncate(recruitmentPosition.getDescription(), 40)
            );
        });

        System.out.println(Color.BOLD + bot + Color.RESET);
    }

    private static void showAllRecruitmentPosition() {
        int limit = Validator.validateInputInt(scanner, Color.CYAN + "Nhập số vị trí ứng tuyển muốn hiển thị trên 1 trang: " + Color.RESET);
        int totalPage = recruitmentPositionService.getTotalPage(limit);

        if (totalPage == 0) {
            System.out.println(Color.RED + "Không có vị trí tuyển dụng nào để hiển thị." + Color.RESET);
            return;
        }

        int currentPage = 1;

        while (true) {
            System.out.println("\n" + Color.BOLD + Color.BLUE + "== DANH SÁCH VỊ TRÍ TUYỂN DỤNG TRANG " + currentPage + " / " + totalPage + " ==" + Color.RESET);

            List<RecruitmentPosition> recruitmentPositions = recruitmentPositionService.getRecruitmentPositionByPage(currentPage, limit);
            showTableRecruitmentPosition(recruitmentPositions);

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

            String choice = ValidateChoicePagination(scanner);
            switch (choice) {
                case "0" -> {
                    System.out.println(Color.YELLOW + "Thoát khỏi hiển thị ứng viên." + Color.RESET);
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

    private static List<Technology> getAllTechnology() {
        return technologyService.getAllTechnology();
    }

    private static void choiceTechnologyName(int recruitmentPositionId) {
        List<Technology> technologyList = getAllTechnology();
        int choice;

        do {
            CandidateUI.displayAllTechnologies();
            System.out.println(Color.YELLOW + "0. Thoát" + Color.RESET);
            choice = Validator.validateInputInt(scanner, "Nhập ID công nghệ muốn đăng ký: ");

            if (choice < 0 || choice > technologyList.size()) {
                System.out.printf(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn từ 0 đến %d.\n", technologyList.size() + Color.RESET);
                continue;
            }

            switch (choice) {
                case 0:
                    pause(1);
                    break;
                default:
                    Technology selectedTech = technologyList.get(choice - 1);
                    RecruitmentPositionTechnology recruitmentPositionTechnology= new RecruitmentPositionTechnology();
                    recruitmentPositionTechnology.setRecruitmentPositionId(recruitmentPositionId);
                    recruitmentPositionTechnology.setTechnologyId(selectedTech.getId());

                    if (recruitmentPositionTechnologyService.addRecruitmentPositionTechnology(recruitmentPositionTechnology)) {
                        System.out.println(Color.GREEN + "Đăng ký công nghệ \"" + selectedTech.getName() + "\" thành công cho vị trí tuyển dụng." + Color.RESET);
                    } else {
                        System.out.println(Color.RED + "Đăng ký công nghệ \"" + selectedTech.getName() + "\" thất bại cho vị trí tuyển dụng." + Color.RESET);
                    }
                    break;
            }

        } while (choice != 0);
    }

    private static void addNewRecruitmentPosition() {
        System.out.println(Color.CYAN + "\n=== Thêm Vị Trí Tuyển Dụng Mới ===" + Color.RESET);

        RecruitmentPosition newPosition = new RecruitmentPosition();
        newPosition.inputData();

        boolean isSuccess = recruitmentPositionService.save(newPosition);

        if (isSuccess) {
            choiceTechnologyName(newPosition.getId());
            System.out.println(Color.GREEN + "Thêm vị trí tuyển dụng thành công!" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Thêm vị trí tuyển dụng thất bại! Không thể chọn công nghệ." + Color.RESET);
        }
    }

    private static void updateRecruitmentPosition() {
        System.out.println(Color.BLUE + "\n=== CẬP NHẬT VỊ TRÍ TUYỂN DỤNG ===" + Color.RESET);
        RecruitmentPosition existing = getRecruitmentPositionById();
        showRecruitmentPositionById(existing);

        int choice;

        do {
            System.out.println(Color.CYAN + "\n=== CHỌN THÔNG TIN MUỐN CẬP NHẬT ===" + Color.RESET);
            System.out.println(Color.GREEN + "1. Tên vị trí tuyển dụng");
            System.out.println(Color.GREEN + "2. Mô tả vị trí tuyển dụng");
            System.out.println(Color.GREEN + "3. Lương tối thiểu");
            System.out.println(Color.GREEN + "4. Lương tối đa");
            System.out.println(Color.GREEN + "5. Kinh nghiệm tối thiểu");
            System.out.println(Color.GREEN + "6. Ngày hết hạn");
            System.out.println(Color.YELLOW + "0. Lưu và thoát");

            choice = Validator.validateInputInt(scanner, Color.YELLOW + "\nChọn mục muốn cập nhật: " + Color.RESET);

            switch (choice) {
                case 1:
                    existing.setName(RecruitmentPositionValidate.inputValidName(scanner));
                    break;
                case 2:
                    existing.setDescription(RecruitmentPositionValidate.inputValidDes(scanner));
                    break;
                case 3:
                    existing.setMinSalary(RecruitmentPositionValidate.inputValidMinSalary(scanner));
                    break;
                case 4:
                    existing.setMaxSalary(RecruitmentPositionValidate.inputValidMaxSalary(scanner, existing.getMinSalary()));
                    break;
                case 5:
                    existing.setMinExperience(RecruitmentPositionValidate.inputValidMinExperience(scanner));
                    break;
                case 6:
                    existing.setExpiredDate(RecruitmentPositionValidate.inputValidExpiredDate(scanner, existing.getExpiredDate()));
                    break;
                case 0:
                    pause(1);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
            }
        } while (choice != 0);

        boolean isUpdated = recruitmentPositionService.update(existing);
        if (isUpdated) {
            System.out.println(Color.GREEN + "\nCập nhật vị trí tuyển dụng thành công!" + Color.RESET);
        } else {
            System.out.println(Color.RED + "\nCập nhật thất bại." + Color.RESET);
        }
    }

    private static void deleteRecruitmentPosition() {
        System.out.println(Color.CYAN + "\n=== XÓA VỊ TRÍ TUYỂN DỤNG ===" + Color.RESET);
        RecruitmentPosition existing = getRecruitmentPositionById();
        showRecruitmentPositionById(existing);

        do {
            System.out.println(Color.ITALIC + "\nBạn có chắc chắn muốn xóa vị trí \"" + existing.getName() + "\"?" + Color.RESET);
            System.out.println(Color.GREEN + "1. Xác nhận xóa" + Color.RESET);
            System.out.println(Color.CYAN + "2. Hủy bỏ" + Color.RESET);

            int choice = Validator.validateInputInt(scanner, Color.YELLOW + "\nChọn thao tác: " + Color.RESET);

            switch (choice) {
                case 1:
                    boolean isDeleted = recruitmentPositionService.delete(existing);
                    if (isDeleted) {
                        System.out.println(Color.GREEN + "\nĐã xóa vị trí tuyển dụng thành công!" + Color.RESET);
                    } else {
                        System.out.println(Color.RED + "\nXóa thất bại." + Color.RESET);
                    }
                    return;
                case 2:
                    System.out.println(Color.CYAN + "\nĐã hủy thao tác xóa." + Color.RESET);
                    return;
                default:
                    System.out.println(Color.RED + "\nLựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
            }
        } while (true);
    }

}
