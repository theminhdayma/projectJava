package ra.edu.business.dao.admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;

import java.sql.*;
import java.util.List;

public class AdminDaoImpl implements AdminDao {

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
    public boolean loginAdmin(String username, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        boolean isLoggedIn = false;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call login_admin(?, ?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);

            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                isLoggedIn = true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return isLoggedIn;
    }

    @Override
    public List<Account> readAll() {
        return null;
    }

    @Override
    public boolean save(Account admin) {
        return false;
    }

    @Override
    public boolean update(Account admin) {
        return false;
    }

    @Override
    public boolean delete(Account admin) {
        return false;
    }
}
