package ra.edu.business.dao.candidateTechnology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidateTenology.CandidateTechnology;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CandidateTechnologyDaoImp implements CandidateTechnologyDao {

    @Override
    public boolean addCandidateTechnology(CandidateTechnology candidateTechnology) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL add_candidate_technology(?, ?)");
            callSt.setInt(1, candidateTechnology.getCandidateId());
            callSt.setInt(2, candidateTechnology.getTechnologyId());

            int result = callSt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println("Lỗi: " + e.getMessage());
            } else {
                System.err.println("Lỗi thêm công nghệ cho ứng viên: " + e.getMessage());
            }
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }
}
