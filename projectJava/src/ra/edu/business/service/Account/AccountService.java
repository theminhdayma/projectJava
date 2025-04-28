package ra.edu.business.service.Account;

import ra.edu.business.model.account.Account;

public interface AccountService {
    int checkIsAccount(String username);
    Account login(String username, String password);
    void initAdmin();
    Account getAccountById(int id);

}
