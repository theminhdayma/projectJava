package ra.edu.business.service.Account;

import ra.edu.business.dao.Account.AccountDao;
import ra.edu.business.dao.Account.AccountDaoImp;
import ra.edu.business.model.account.Account;

public class AccountServiceImp implements AccountService {
    public AccountDao accountDao;

    public AccountServiceImp() {
        accountDao = new AccountDaoImp();
    }
    @Override
    public int checkIsAccount(String username) {
        return accountDao.checkIsAccount(username);
    }

    @Override
    public Account login(String username, String password) {
        return accountDao.login(username, password);
    }

    public void initAdmin() {
        accountDao.initAdmin();
    }

    @Override
    public Account getAccountById(int id) {
        return accountDao.getAccountById(id);
    }
}
