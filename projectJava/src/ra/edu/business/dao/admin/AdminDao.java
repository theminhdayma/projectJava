package ra.edu.business.dao.admin;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.account.Account;

public interface AdminDao extends AppDao<Account> {
    boolean loginAdmin(String username, String password);
    void initAdmin();
}
