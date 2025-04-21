package ra.edu.business.model.recruitmentPosition;

import ra.edu.validate.recruitmentPosition.RecruitmentPositionValidate;

import java.time.LocalDate;

import static ra.edu.MainApplication.scanner;

public class RecruitmentPosition {
    private int id;
    private String name;
    private String description;
    private double minSalary;
    private double maxSalary;
    private int minExperience;
    private LocalDate createdDate;
    private LocalDate expiredDate;
    private RecruitmentPositionStatus status;

    public RecruitmentPosition() {};

    public RecruitmentPosition(int id, String name, String description, double minSalary, double maxSalary, int minExperience, LocalDate createdDate, LocalDate expiredDate, RecruitmentPositionStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getMinSalary() {
        return minSalary;
    }
    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }
    public double getMaxSalary() {
        return maxSalary;
    }
    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }
    public int getMinExperience() {
        return minExperience;
    }
    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public LocalDate getExpiredDate() {
        return expiredDate;
    }
    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }
    public RecruitmentPositionStatus getStatus() {
        return status;
    }
    public void setStatus(RecruitmentPositionStatus status) {
        this.status = status;
    }

    public void inputData() {
        this.name = RecruitmentPositionValidate.inputValidName(scanner);
        this.description = RecruitmentPositionValidate.inputValidDes(scanner);
        this.minSalary = RecruitmentPositionValidate.inputValidMinSalary(scanner);
        this.maxSalary = RecruitmentPositionValidate.inputValidMaxSalary(scanner, this.minSalary);
        this.minExperience = RecruitmentPositionValidate.inputValidMinExperience(scanner);
        this.createdDate = LocalDate.now();
        this.expiredDate = RecruitmentPositionValidate.inputValidExpiredDate(scanner, this.createdDate);
    }
}
