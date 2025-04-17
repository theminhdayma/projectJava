package ra.edu.business.dao.admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.admin.Admin;

import java.sql.*;
import java.util.ArrayList;
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
        ResultSet rs = null;
        boolean isLoggedIn = false;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call login_admin(?, ?)}");
            callSt.setString(1, username);
            callSt.setString(2, password);

            rs = callSt.executeQuery();
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
    public List<Admin> readAll() {
        List<Admin> admins = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            String query = "SELECT * FROM admin";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setAdminName(rs.getString("adminName"));
                admin.setPassword(rs.getString("password"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return admins;
    }

    @Override
    public boolean save(Admin admin) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionDB.openConnection();
            String query = "INSERT INTO admin(adminName, password) VALUES (?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, admin.getAdminName());
            ps.setString(2, admin.getPassword());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    public boolean update(Admin admin) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionDB.openConnection();
            String query = "UPDATE admin SET adminName = ?, password = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, admin.getAdminName());
            ps.setString(2, admin.getPassword());
            ps.setInt(3, admin.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    public boolean delete(Admin admin) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionDB.openConnection();
            String query = "DELETE FROM admin WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, admin.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong CSDL: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
