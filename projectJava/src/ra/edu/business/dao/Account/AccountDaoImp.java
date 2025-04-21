package ra.edu.business.dao.Account;

import ra.edu.business.config.ConnectionDB;

import java.sql.*;

public class AccountDaoImp implements AccountDao {
    @Override
    public int checkIsAccount(String username) {
        Connection conn = null;
        CallableStatement callSt = null;
        int count = 0;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL check_account_by_username(?, ?)");
            callSt.setString(1, username);
            callSt.registerOutParameter(2, Types.INTEGER);

            callSt.execute();
            count = callSt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra tài khoản: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return count;
    }
}
