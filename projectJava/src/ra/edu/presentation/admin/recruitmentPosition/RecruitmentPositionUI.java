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
import ra.edu.utils.Color;
import ra.edu.validate.Validator;
import ra.edu.validate.recruitmentPosition.RecruitmentPositionValidate;

import static ra.edu.utils.Util.truncate;

import java.util.List;

import static ra.edu.MainApplication.scanner;
import static ra.edu.utils.ThreadUtil.pause;
import static ra.edu.utils.Util.LIMIT;

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
                System.out.println("Không tìm thấy vị trí tuyển dụng với ID: " + id + ". Vui lòng thử lại.");
            }
        } while (recruitmentPosition == null);

        return recruitmentPosition;
    }

    private static void showAllRecruitmentPosition() {
        int totalPage = recruitmentPositionService.getTotalPage(LIMIT);

        if (totalPage == 0) {
            System.out.println("Không có vị trí tuyển dụng nào để hiển thị.");
            return;
        }

        while (true) {
            System.out.println("\n== DANH SÁCH VỊ TRÍ TUYỂN DỤNG ==");

            for (int i = 1; i <= totalPage; i++) {
                System.out.printf("%d. Trang %d\n", i, i);
            }
            System.out.println("0. Thoát");

            int pageChoice;
            do {
                pageChoice = Validator.validateInputInt(scanner, "Chọn trang muốn xem: ");
            } while (pageChoice < 0 || pageChoice > totalPage);

            if (pageChoice == 0) {
                System.out.println("Thoát khỏi hiển thị vị trí tuyển dụng.");
                pause(1);
                break;
            }

            List<RecruitmentPosition> listRecruitmentPosition = recruitmentPositionService.getRecruitmentPositionByPage(pageChoice, LIMIT);

            System.out.println("\n== VỊ TRÍ TUYỂN DỤNG - TRANG " + pageChoice + " ==");

            String line = "+-----+-----------------------------+------------+------------+--------------+-------------+-------------+------------+------------------------------------------+";
            System.out.println(line);
            System.out.printf("| %-3s | %-27s | %-10s | %-10s | %-12s | %-11s | %-11s | %-10s | %-40s |\n",
                    "ID", "Vị trí tuyển dụng", "Min Lương ($)", "Max Lương($)", "Kinh Nghiệm (Năm)", "Ngày Tạo", "Hết Hạn", "Trạng Thái", "Mô Tả");
            System.out.println(line);

            for (RecruitmentPosition recruitmentPosition : listRecruitmentPosition) {
                System.out.printf("| %-3d | %-27s | %-10.1f | %-10.1f | %-12d | %-11s | %-11s | %-10s | %-40s |\n",
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
            }

            System.out.println(line);
        }
    }

    private static List<Technology> getAllTechnology() {
        return technologyService.getAllTechnology();
    }

    private static void choiceTechnologyName(int recruitmentPositionId) {
        List<Technology> technologyList = getAllTechnology();
        int choice;

        do {
            System.out.println("\n=======================================");
            System.out.println("ĐĂNG KÝ CÔNG NGHỆ CHO VỊ TRÍ TUYỂN DỤNG");
            System.out.println("========================================");
            for (int i = 0; i < technologyList.size(); i++) {
                System.out.printf("%2d. %s\n", (i + 1), technologyList.get(i).getName());
            }
            System.out.println(" 0. Thoát");
            System.out.println("=============================");

            choice = Validator.validateInputInt(scanner, "Nhập số tương ứng với công nghệ muốn đăng ký: ");

            if (choice < 0 || choice > technologyList.size()) {
                System.out.printf("Lựa chọn không hợp lệ. Vui lòng chọn từ 0 đến %d.\n", technologyList.size());
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Loading...");
                    pause(1);
                    break;
                default:
                    Technology selectedTech = technologyList.get(choice - 1);
                    RecruitmentPositionTechnology recruitmentPositionTechnology= new RecruitmentPositionTechnology();
                    recruitmentPositionTechnology.setRecruitmentPositionId(recruitmentPositionId);
                    recruitmentPositionTechnology.setTechnologyId(selectedTech.getId());

                    if (recruitmentPositionTechnologyService.addRecruitmentPositionTechnology(recruitmentPositionTechnology)) {
                        System.out.println("Đăng ký công nghệ \"" + selectedTech.getName() + "\" thành công cho vị trí tuyển dụng.");
                    } else {
                        System.err.println("Đăng ký công nghệ thất bại. Vui lòng thử lại.");
                    }

                    break;
            }

        } while (choice != 0);
    }

    private static void addNewRecruitmentPosition() {
        System.out.println("\n=== Thêm vị trí tuyển dụng mới ===");
        RecruitmentPosition newPosition = new RecruitmentPosition();
        newPosition.inputData();

        boolean isSuccess = recruitmentPositionService.save(newPosition);

        if (isSuccess) {
            choiceTechnologyName(newPosition.getId());
            System.out.println("Thêm vị trí tuyển dụng thành công!");
        } else {
            System.err.println("Thêm vị trí tuyển dụng thất bại! Không thể chọn công nghệ.");
        }
    }


    private static void updateRecruitmentPosition() {
        System.out.println("\n=== Cập nhật vị trí tuyển dụng ===");
        RecruitmentPosition existing = getRecruitmentPositionById();

        int choice;
        do {
            System.out.println("\n=== Chọn thông tin muốn cập nhật ===");
            System.out.println("1. Tên vị trí tuyển dụng" );
            System.out.println("2. Mô tả vị trí tuyển dụng");
            System.out.println("3. Lương tối thiểu ");
            System.out.println("4. Lương tối đa ");
            System.out.println("5. Kinh nghiệm tối thiểu ");
            System.out.println("6. Ngày hết hạn ");
            System.out.println("0. Lưu và thoát");

            choice = Validator.validateInputInt(scanner, "Chọn mục muốn cập nhật: ");

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
                    System.out.println("Đã lưu thay đổi và thoát.");
                    System.out.println("\nLoading...");
                    pause(1);
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (choice != 0);

        boolean isUpdated = recruitmentPositionService.update(existing);
        if (isUpdated) {
            System.out.println("Cập nhật vị trí tuyển dụng thành công!");
        } else {
            System.err.println("Cập nhật thất bại.");
        }
    }

    private static void deleteRecruitmentPosition() {
        System.out.println("\n=== Xóa vị trí tuyển dụng ===");
        RecruitmentPosition existing = getRecruitmentPositionById();

        do {
            System.out.println("\nBạn có chắc chắn muốn xóa vị trí \"" + existing.getName() + "\"?");
            System.out.println("1. Xác nhận xóa");
            System.out.println("2. Hủy bỏ");

            int choice = Validator.validateInputInt(scanner, "Chọn thao tác: ");

            switch (choice) {
                case 1:
                    boolean isDeleted = recruitmentPositionService.delete(existing);
                    if (isDeleted) {
                        System.out.println("Đã xóa vị trí tuyển dụng thành công!");
                    } else {
                        System.err.println("Xóa thất bại.");
                    }
                    return;
                case 2:
                    System.out.println("Hủy thao tác xóa.");
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (true);
    }
}
