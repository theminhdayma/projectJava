package ra.edu.business.dao.technology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.StatusTechnology;
import ra.edu.business.model.technology.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDaoImp implements TechnologyDao {
    public static final int PAGE = 5;

    @Override
    public List<Technology> readAll() {
        return List.of();
    }

    @Override
    public boolean save(Technology technology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL add_technology(?)");
            callSt.setString(1, technology.getName());
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean update(Technology technology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL update_technology(?, ?)");
            callSt.setInt(1, technology.getId());
            callSt.setString(2, technology.getName());
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean delete(Technology technology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL delete_technology(?)");
            callSt.setInt(1, technology.getId());
            callSt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public int getTotalPage(int limit) {
        int totalPage = 0;
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_technology_page(?)}");
            callSt.setInt(1, limit);
            boolean hasResult = callSt.execute();
            if (hasResult) {
                try (ResultSet rs = callSt.getResultSet()) {
                    if (rs.next()) {
                        totalPage = rs.getInt("totalPage");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return totalPage;
    }

    @Override
    public List<Technology> getTechnologyByPage(int page, int limit) {
        List<Technology> technologies = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_technology(?, ?)}");
            callSt.setInt(1, page);
            callSt.setInt(2, limit);
            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Technology tech = new Technology();
                    tech.setId(rs.getInt("id"));
                    tech.setName(rs.getString("name"));
                    tech.setStatus(StatusTechnology.ACTIVE);
                    technologies.add(tech);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return technologies;
    }

    @Override
    public Technology getTechnologyById(int id) {
        Technology tech = null;
        Connection conn = null;
        CallableStatement callSt = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_technology_by_id(?)}");
            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();

            if (rs.next()) {
                tech = new Technology();
                tech.setId(rs.getInt("id"));
                tech.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return tech;
    }

}
