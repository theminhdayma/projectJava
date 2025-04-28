package ra.edu.business.dao.Account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.account.Role;

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

    @Override
    public void initAdmin() {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call check_account_admin()}");
            callSt.execute();
        } catch (Exception e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public Account getAccountById(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        Account account = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_account_by_id(?)}");
            callSt.setInt(1, id);

            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));

                String role = rs.getString("role");
                if (role != null) {
                    account.setRole(Role.valueOf(role));
                }

                String status = rs.getString("status");
                if (status != null) {
                    account.setStatus(AccountStatus.valueOf(status));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tài khoản: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return account;
    }

    @Override
    public Account login(String username, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        Account account = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call login(?, ?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);

            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));

                String role = rs.getString("role");
                if (role != null) {
                    account.setRole(Role.valueOf(role));
                }

                String status = rs.getString("status");
                if (status != null) {
                    account.setStatus(AccountStatus.valueOf(status));
                }
            }
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if ("45000".equals(sqlState)) {
                System.err.println(e.getMessage());
            }
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return account;
    }
}
