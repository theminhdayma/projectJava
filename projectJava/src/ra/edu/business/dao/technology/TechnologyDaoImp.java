package ra.edu.business.dao.technology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.StatusTechnology;
import ra.edu.business.model.technology.Technology;
import ra.edu.utils.Color;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDaoImp implements TechnologyDao {

    @Override
    public boolean checkNameTechnology(Technology technology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL check_name_technology(?)}");
            callSt.setString(1, technology.getName());

            callSt.execute();

            return true;
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println(Color.RED + e.getMessage() + Color.RESET);
            } else {
                System.err.println(Color.RED + "Lỗi khác: " + e.getMessage() + Color.RESET);
            }
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<Technology> getAllTechnology() {
        List<Technology> technologyList = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_all_technology()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(StatusTechnology.ACTIVE);
                technologyList.add(technology);
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi lấy tất cả công nghệ: " + e.getMessage() + Color.RESET);
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return technologyList;
    }

    @Override
    public List<Technology> getTechnologyByRecruitmentPositionId(int recruitmentPositionId) {
        List<Technology> technologyList = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_technology_by_recruitment_position_id(?)}");
            callSt.setInt(1, recruitmentPositionId);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    technology.setStatus(StatusTechnology.valueOf(statusStr));
                }
                technologyList.add(technology);
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi lấy công nghệ theo vị trí tuyển dụng: " + e.getMessage() + Color.RESET);
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return technologyList;
    }

    @Override
    public boolean save(Technology technology) {
        if (!checkNameTechnology(technology)) {
            return false;
        }

        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL add_technology(?)");
            callSt.setString(1, technology.getName());

            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi lưu công nghệ: " + e.getMessage() + Color.RESET);
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean update(Technology technology) {
        if (!checkNameTechnology(technology)) {
            return false;
        }

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
            System.err.println(Color.RED + "Lỗi khi cập nhật công nghệ: " + e.getMessage() + Color.RESET);
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
            if ("45000".equals(e.getSQLState())) {
                System.out.println(Color.RED + "Không thể xóa công nghệ này vì nó đang được sử dụng." + Color.RESET);
            } else {
                System.err.println(Color.RED + "Lỗi khi xóa công nghệ: " + e.getMessage() + Color.RESET);
            }
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
            System.err.println(Color.RED + "Lỗi khi lấy tổng số trang: " + e.getMessage() + Color.RESET);
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
                    tech.setStatus(StatusTechnology.valueOf(rs.getString("status")));
                    technologies.add(tech);
                }
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi lấy công nghệ theo trang: " + e.getMessage() + Color.RESET);
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
            System.err.println(Color.RED + "Lỗi khi lấy công nghệ theo id: " + e.getMessage() + Color.RESET);
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return tech;
    }
}
