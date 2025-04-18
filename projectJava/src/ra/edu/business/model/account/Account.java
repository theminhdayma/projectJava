package ra.edu.business.model.account;

import ra.edu.validate.StringRule;
import ra.edu.validate.Validator;

import java.io.Serializable;

import static ra.edu.MainApplication.scanner;

public class Account implements Serializable {
    private int id;
    private String username;
    private String password;
    private Role role;
    private AccountStatus status;

    public Account() {};

    public Account(int id, String username, String password, Role role, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void inputData() {
        this.username = Validator.validateInputString(scanner, "Nhập tên đăng nhập:", new StringRule(0, 255));
        this.password = Validator.validateInputString(scanner, "Nhập mật khẩu:", new StringRule(0, 255));
    }
}
