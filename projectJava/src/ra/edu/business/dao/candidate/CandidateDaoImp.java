package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.account.Role;
import ra.edu.business.model.candidate.Active;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidate.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDaoImp implements CandidateDao {

    @Override
    public List<Candidate> readAll() {
        return List.of();
    }

    @Override
    public boolean loginCandidate(String username, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        boolean isLoggedIn = false;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call login_candidate(?, ?)}");
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
    public boolean save(Candidate candidate) {
        if (!checkEmailCandidate(candidate)) {
            return false;
        }
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL add_candidate(?, ?, ?, ?, ?, ?, ?, ?)}");
            callSt.setString(1, candidate.getName());
            callSt.setString(2, candidate.getEmail());
            callSt.setString(3, candidate.getPhone());
            callSt.setInt(4, candidate.getExperience());
            callSt.setString(5, candidate.getGender().name());
            callSt.setString(6, candidate.getDescription());
            callSt.setDate(7, Date.valueOf(candidate.getDob()));
            callSt.setString(8, candidate.getAccount().getPassword());
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean update(Candidate candidate) {
        if (!checkEmailCandidate(candidate)) {
            return false;
        }

        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_candidate(?, ?, ?, ?, ?, ?, ?)}");
            callSt.setInt(1, candidate.getId());
            callSt.setString(2, candidate.getName());
            callSt.setString(3, candidate.getPhone());
            callSt.setInt(4, candidate.getExperience());
            callSt.setString(5, candidate.getGender().name());
            callSt.setString(6, candidate.getDescription());
            callSt.setDate(7, Date.valueOf(candidate.getDob()));
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean delete(Candidate candidate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL delete_candidate(?)}");
            callSt.setInt(1, candidate.getId());
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public int getTotalPage(int limit) {
        int totalPage = 0;
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_candidate_page(?)}");
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
    public List<Candidate> getCandidateByPage(int page, int limit) {
        List<Candidate> candidateList = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_candidate(?, ?)}");
            callSt.setInt(1, page);
            callSt.setInt(2, limit);
            ResultSet rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                // Set Active enum từ status string
                String status = rs.getString("active");
                candidate.setActive("ACTIVE".equalsIgnoreCase(status) ? Active.UNLOCKED : Active.LOCKED);
                candidateList.add(candidate);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi phân trang ứng viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return candidateList;
    }

    @Override
    public Candidate getCandidateById(int id) {
        Candidate candidate = null;
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_candidate_by_id(?)}");
            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();

            if (rs.next()) {
                candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender")));
                candidate.setDescription(rs.getString("description"));
                candidate.setDob(rs.getDate("dob").toLocalDate());
                candidate.setActive(Active.valueOf(rs.getString("action")));

                Account account = new Account();
                account.setId(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(Role.valueOf(rs.getString("role")));
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));

                candidate.setAccount(account);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy chi tiết ứng viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return candidate;
    }


    @Override
    public boolean checkEmailCandidate(Candidate candidate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL check_email_candidate(?)}");
            callSt.setString(1, candidate.getEmail());
            callSt.execute();
            return true;
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println(e.getMessage());
            } else {
                System.err.println("Lỗi kiểm tra email: " + e.getMessage());
            }
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean lockCandidateAccount(int candidateId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL lock_candidate_account(?)}");
            callSt.setInt(1, candidateId);
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khóa tài khoản ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean unlockCandidateAccount(int candidateId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL unlock_candidate_account(?)}");
            callSt.setInt(1, candidateId);
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi mở khóa tài khoản ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean resetCandidatePassword(int candidateId, String newPassword) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL reset_candidate_password(?, ?)}");
            callSt.setInt(1, candidateId);
            callSt.setString(2, newPassword);
            callSt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi reset mật khẩu ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<Candidate> searchCandidateByName(String keyword) {
        List<Candidate> candidateList = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL search_candidate_by_name(?)}");
            callSt.setString(1, "%" + keyword + "%");
            ResultSet rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setActive(rs.getString("status").equals("ACTIVE") ? Active.UNLOCKED : Active.LOCKED);
                candidateList.add(candidate);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm ứng viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return candidateList;
    }

}