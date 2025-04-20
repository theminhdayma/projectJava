package ra.edu.business.model.candidate;

import ra.edu.business.model.account.Account;
import ra.edu.validate.Validator;
import ra.edu.validate.candidate.CandidateValidate;

import java.io.Serializable;
import java.time.LocalDate;

import static ra.edu.MainApplication.scanner;

public class Candidate implements Serializable {
    private int id;
    private Account account;
    private String name;
    private String email;
    private String phone;
    private int experience;
    private Gender gender;
    private String description;
    private LocalDate dob;

    public Candidate() {
    }

    public Candidate(int id, Account account, String name, String email, String phone, int experience, Gender gender, String description, LocalDate dob) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.experience = experience;
        this.gender = gender;
        this.description = description;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void inputData() {
        this.account = new Account();
        this.name = CandidateValidate.inputValidName(scanner);
        this.email = CandidateValidate.inputValidEmail(scanner);
        this.phone = CandidateValidate.inputValidPhone(scanner);
        this.experience = Validator.validateInputInt(scanner, "Nhập vào năm kinh nghiệm: ");
        this.gender = CandidateValidate.inputValidGender(scanner);
        this.description = CandidateValidate.inputValidDes(scanner);
        this.dob = CandidateValidate.inputValidDob(scanner);
        this.account.setUsername(this.email);
        this.account.setPassword(CandidateValidate.inputValidPassword(scanner, "Nhập mật khẩu: "));
    }
}
