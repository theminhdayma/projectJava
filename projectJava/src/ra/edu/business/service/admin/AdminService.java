package ra.edu.business.service.admin;

import ra.edu.business.model.account.Account;
import ra.edu.business.service.AppService;

public interface AdminService extends AppService<Account> {
    boolean loginAdmin(String username, String password);
    void initAdmin();
}
