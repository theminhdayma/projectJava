package ra.edu.business.dao.candidateTechnology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidateTenology.CandidateTechnology;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<CandidateTechnology> getAllCandidateTechnologyByCandidateId(int candidateId) {
        List<CandidateTechnology> candidateTechnologies = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL get_all_candidate_technology_by_candidate_id(?)");
            callSt.setInt(1, candidateId);

            rs = callSt.executeQuery();
            while (rs.next()) {
                CandidateTechnology ct = new CandidateTechnology();
                ct.setId(rs.getInt("id"));
                ct.setCandidateId(rs.getInt("candidateId"));
                ct.setTechnologyId(rs.getInt("technologyId"));
                candidateTechnologies.add(ct);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách công nghệ của ứng viên");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return candidateTechnologies;
    }

    @Override
    public boolean deleteCandidateTechnologyById(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("CALL delete_candidate_technology_by_id(?)");
            callSt.setInt(1, id);

            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa công nghệ của ứng viên");
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }
}
