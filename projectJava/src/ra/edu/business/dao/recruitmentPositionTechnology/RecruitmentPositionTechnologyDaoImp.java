package ra.edu.business.dao.recruitmentPositionTechnology;

import ra.edu.business.model.recruitmentPositionTechnology.RecruitmentPositionTechnology;
import ra.edu.business.config.ConnectionDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RecruitmentPositionTechnologyDaoImp implements RecruitmentPositionTechnologyDao {
    @Override
    public boolean addRecruitmentPositionTechnology(RecruitmentPositionTechnology recruitmentPositionTechnology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL add_recruitment_position_technology(?, ?)");
            callSt.setInt(1, recruitmentPositionTechnology.getRecruitmentPositionId());
            callSt.setInt(2, recruitmentPositionTechnology.getTechnologyId());

            int result = callSt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println("Lỗi: " + e.getMessage());
            } else {
                System.err.println("Lỗi thêm công nghệ cho vị trí tuyển dụng: " + e.getMessage());
            }
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }
}
