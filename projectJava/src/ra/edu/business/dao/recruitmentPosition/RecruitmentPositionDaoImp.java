package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.recruitmentPosition.RecruitmentPositionStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentPositionDaoImp implements RecruitmentPositionDao {

    @Override
    public boolean save(RecruitmentPosition recruitmentPosition) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL add_recruitment_position(?, ?, ?, ?, ?, ?, ?)}");
            callSt.setString(1, recruitmentPosition.getName());
            callSt.setString(2, recruitmentPosition.getDescription());
            callSt.setDouble(3, recruitmentPosition.getMinSalary());
            callSt.setDouble(4, recruitmentPosition.getMaxSalary());
            callSt.setInt(5, recruitmentPosition.getMinExperience());
            callSt.setDate(6, Date.valueOf(recruitmentPosition.getExpiredDate()));

            callSt.registerOutParameter(7, Types.INTEGER);

            int result = callSt.executeUpdate();
            if (result > 0) {
                int generatedId = callSt.getInt(7);
                recruitmentPosition.setId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean update(RecruitmentPosition recruitmentPosition) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_recruitment_position(?, ?, ?, ?, ?, ?, ?)}");
            callSt.setInt(1, recruitmentPosition.getId());
            callSt.setString(2, recruitmentPosition.getName());
            callSt.setString(3, recruitmentPosition.getDescription());
            callSt.setDouble(4, recruitmentPosition.getMinSalary());
            callSt.setDouble(5, recruitmentPosition.getMaxSalary());
            callSt.setInt(6, recruitmentPosition.getMinExperience());
            callSt.setDate(7, Date.valueOf(recruitmentPosition.getExpiredDate()));

            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean delete(RecruitmentPosition recruitmentPosition) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL delete_recruitment_position(?)}");
            callSt.setInt(1, recruitmentPosition.getId());
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public RecruitmentPosition getRecruitmentPositionById(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        RecruitmentPosition recruitmentPosition = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_recruitment_position_by_id(?)}");
            callSt.setInt(1, id);
            var rs = callSt.executeQuery();
            if (rs.next()) {
                recruitmentPosition = new RecruitmentPosition();
                recruitmentPosition.setId(rs.getInt("id"));
                recruitmentPosition.setName(rs.getString("name"));
                recruitmentPosition.setDescription(rs.getString("description"));
                recruitmentPosition.setMinSalary(rs.getDouble("minSalary"));
                recruitmentPosition.setMaxSalary(rs.getDouble("maxSalary"));
                recruitmentPosition.setMinExperience(rs.getInt("minExperience"));
                recruitmentPosition.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
                recruitmentPosition.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                recruitmentPosition.setStatus(RecruitmentPositionStatus.valueOf(rs.getString("status")));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return recruitmentPosition;
    }

    @Override
    public int getTotalPage(int limit) {
        int totalPage = 0;
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_recruitment_position_page(?)}");
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
    public List<RecruitmentPosition> getRecruitmentPositionByPage(int page, int limit) {
        List<RecruitmentPosition> recruitmentPosition = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_recruitment_position(?, ?)}");
            callSt.setInt(1, page);
            callSt.setInt(2, limit);
            rs = callSt.executeQuery();

            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getDouble("minSalary"));
                position.setMaxSalary(rs.getDouble("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                position.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                position.setExpiredDate(rs.getDate("expiredDate").toLocalDate());

                position.setStatus(RecruitmentPositionStatus.valueOf(rs.getString("status")));


                recruitmentPosition.add(position);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi phân trang vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return recruitmentPosition;
    }

    @Override
    public List<RecruitmentPosition> getAllRecruitmentPosition() {
        List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_all_recruitment_position()}");
            rs = callSt.executeQuery();

            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getDouble("minSalary"));
                position.setMaxSalary(rs.getDouble("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                position.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                position.setExpiredDate(rs.getDate("expiredDate").toLocalDate());
                position.setStatus(RecruitmentPositionStatus.valueOf(rs.getString("status")));

                recruitmentPositions.add(position);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy tất cả vị trí tuyển dụng: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return recruitmentPositions;
    }
}
