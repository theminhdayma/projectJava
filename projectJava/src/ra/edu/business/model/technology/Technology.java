package ra.edu.business.model.technology;

import static ra.edu.MainApplication.scanner;

public class Technology {
    private int id;
    private String name;
    private StatusTechnology status;

    public Technology() {};
    public Technology(int id, String name, StatusTechnology status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusTechnology getStatus() {
        return status;
    }

    public void setStatus(StatusTechnology status) {
        this.status = status;
    }

    public void inputData() {
        System.out.print("Nhập tên công nghệ: ");
        this.name = scanner.nextLine();
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-30s |", id, name);
    }
}
