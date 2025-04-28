package ra.edu.business.model.technology;

import ra.edu.validate.StringRule;
import ra.edu.validate.technology.TechnologyValidate;

import static ra.edu.MainApplication.scanner;
import static ra.edu.validate.Validator.validateInputString;
import static ra.edu.validate.technology.TechnologyValidate.checkTechnologyName;

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
        this.name = TechnologyValidate.checkTechnologyName(scanner);
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-30s |", id, name);
    }
}
