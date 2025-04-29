package ra.edu.business.dao.candidateTechnology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidateTenology.CandidateTechnology;
import ra.edu.utils.Color;

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
            String sqlState = e.getSQLState();
            if ("45000".equals(sqlState)) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi thêm công nghệ của ứng viên: " + e.getMessage() + Color.RESET);
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
            String sqlState = e.getSQLState();
            if ("45000".equals(sqlState)) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi lấy công nghệ của ứng viên: " + e.getMessage() + Color.RESET);
            }
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
            String sqlState = e.getSQLState();
            if ("45000".equals(sqlState)) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi xóa công nghệ của ứng viên: " + e.getMessage() + Color.RESET);
            }
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }
}
