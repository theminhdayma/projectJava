package ra.edu.business.service.admin;

import ra.edu.business.dao.admin.AdminDao;
import ra.edu.business.dao.admin.AdminDaoImpl;
import ra.edu.business.model.account.Account;

import java.util.List;

public class AdminServiceImp implements AdminService {
    private AdminDao adminDao;

    public AdminServiceImp() {
        adminDao = new AdminDaoImpl();
    }

    @Override
    public List<Account> readAll() {
        return adminDao.readAll();
    }

    @Override
    public boolean save(Account admin) {
        return adminDao.save(admin);
    }

    @Override
    public boolean update(Account admin) {
        return adminDao.update(admin);
    }

    @Override
    public boolean delete(Account admin) {
        return adminDao.delete(admin);
    }

    @Override
    public boolean loginAdmin(String username, String password) {
        return adminDao.loginAdmin(username, password);
    }

    @Override
    public void initAdmin() {
        adminDao.initAdmin();
    }
}
