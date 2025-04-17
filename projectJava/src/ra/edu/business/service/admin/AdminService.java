package ra.edu.business.service.admin;

import ra.edu.business.model.admin.Admin;
import ra.edu.business.service.AppService;

public interface AdminService extends AppService<Admin> {
    boolean loginAdmin(String username, String password);
    void initAdmin();
}
