package ra.edu.business.dao.admin;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.admin.Admin;

public interface AdminDao extends AppDao<Admin> {
    boolean loginAdmin(String username, String password);
    void initAdmin();
}
