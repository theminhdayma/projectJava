package ra.edu.business.service.Account;

import ra.edu.business.dao.Account.AccountDao;
import ra.edu.business.dao.Account.AccountDaoImp;

public class AccountServiceImp implements AccountService {
    public AccountDao accountDao;

    public AccountServiceImp() {
        accountDao = new AccountDaoImp();
    }
    @Override
    public int checkIsAccount(String username) {
        return accountDao.checkIsAccount(username);
    }
}
