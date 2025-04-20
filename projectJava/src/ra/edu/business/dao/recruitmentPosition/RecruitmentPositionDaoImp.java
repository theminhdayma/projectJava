package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class RecruitmentPositionDaoImp implements RecruitmentPositionDao {

    @Override
    public boolean save(RecruitmentPosition recruitmentPosition) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL add_recruitment_position(?, ?, ?, ?, ?, ?)}");
            callSt.setString(1, recruitmentPosition.getName());
            callSt.setString(2, recruitmentPosition.getDescription());
            callSt.setDouble(3, recruitmentPosition.getMinSalary());
            callSt.setDouble(4, recruitmentPosition.getMaxSalary());
            callSt.setInt(5, recruitmentPosition.getMinExperience());
            callSt.setDate(6, Date.valueOf(recruitmentPosition.getExpiredDate())); // LocalDate -> java.sql.Date
            int result = callSt.executeUpdate();
            return result > 0;
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
}
