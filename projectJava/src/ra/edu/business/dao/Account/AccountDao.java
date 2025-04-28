package ra.edu.business.dao.Account;

import ra.edu.business.model.account.Account;

public interface AccountDao {
    int checkIsAccount(String username);
    Account login(String username, String password);
    void initAdmin();
    Account getAccountById(int id);
}
