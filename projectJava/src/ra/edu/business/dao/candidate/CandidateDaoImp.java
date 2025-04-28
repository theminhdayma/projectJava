package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.AccountStatus;
import ra.edu.business.model.account.Role;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidate.Gender;
import ra.edu.utils.SendEmail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ra.edu.utils.SendEmail.sendPasswordEmail;

public class CandidateDaoImp implements CandidateDao {

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
            if ("45000".equals(e.getSQLState())) {
                System.err.println("Tài khoản đã bị khóa hoặc không tồn tại.");
            } else {
                System.err.println("Lỗi hệ thống: " + e.getMessage());
            }
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return isLoggedIn;
    }

    @Override
    public boolean save(Candidate candidate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL add_candidate(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callSt.setString(1, candidate.getName());
            callSt.setString(2, candidate.getEmail());
            callSt.setString(3, candidate.getPhone());
            callSt.setInt(4, candidate.getExperience());
            callSt.setString(5, candidate.getGender().name());
            callSt.setString(6, candidate.getDescription());
            callSt.setDate(7, Date.valueOf(candidate.getDob()));

            callSt.registerOutParameter(8, Types.INTEGER);
            callSt.registerOutParameter(9, Types.VARCHAR);

            int result = callSt.executeUpdate();
            if (result > 0) {
                int generatedId = callSt.getInt(8);
                String randomPassword = callSt.getString(9);

                candidate.setId(generatedId);
                candidate.getAccount().setPassword(randomPassword);

                SendEmail sendEmail = new SendEmail();
                sendEmail.sendPasswordEmail(candidate.getEmail(), randomPassword);

                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean update(Candidate candidate) {

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
            System.err.println("Lỗi cập nhật: " + e.getMessage());
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
            if ("45000".equals(e.getSQLState())) {
                System.out.println(e.getMessage());
            }
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
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_candidate(?, ?)}");
            callSt.setInt(1, page);
            callSt.setInt(2, limit);
            rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDob(rs.getDate("dob").toLocalDate());

                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);

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
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDescription(rs.getString("description"));
                Date dob = rs.getDate("dob");
                if (dob != null) {
                    candidate.setDob(dob.toLocalDate());
                }

                Account account = new Account();
                account.setId(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                String roleStr = rs.getString("role");
                if (roleStr != null) {
                    account.setRole(Role.valueOf(roleStr));
                }

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }

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
    public boolean checkEmailCandidate(String email) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL check_email_candidate(?)}");
            callSt.setString(1, email);
            callSt.execute();
            return false;
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.out.println("Email đã tồn tại trong hệ thống.");
                return true;
            } else {
                System.err.println("Lỗi kiểm tra email: " + e.getMessage());
            }
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public boolean checkPhoneCandidate(String phone) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL check_phone_candidate(?)}");
            callSt.setString(1, phone);
            callSt.execute();
            return false;
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                return true;
            } else {
                System.err.println("Lỗi kiểm tra số điện thoại: " + e.getMessage());
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
                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);
                candidateList.add(candidate);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm ứng viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return candidateList;
    }

    @Override
    public List<Candidate> filterByExperience(int minExperience) {
        List<Candidate> list = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL filter_by_experience(?)}");
            callSt.setInt(1, minExperience);
            rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDob(rs.getDate("dob").toLocalDate());
                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);

                list.add(candidate);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lọc theo kinh nghiệm: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return list;
    }

    @Override
    public List<Candidate> filterByAgeRange(int minAge, int maxAge) {
        List<Candidate> list = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL filter_by_age_range(?, ?)}");
            callSt.setInt(1, minAge);
            callSt.setInt(2, maxAge);
            rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDob(rs.getDate("dob").toLocalDate());
                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);

                list.add(candidate);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lọc theo độ tuổi: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return list;
    }

    @Override
    public List<Candidate> filterByGender(String gender) {
        List<Candidate> list = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL filter_by_gender(?)}");
            callSt.setString(1, gender);
            rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDob(rs.getDate("dob").toLocalDate());
                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);

                list.add(candidate);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lọc theo giới tính: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return list;
    }

    @Override
    public List<Candidate> filterByCandidateTechnology(int technologyId) {
        List<Candidate> list = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL filter_by_candidate_technology(?)}");
            callSt.setInt(1, technologyId);
            rs = callSt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDob(rs.getDate("dob").toLocalDate());
                Account account = new Account();
                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }
                candidate.setAccount(account);

                list.add(candidate);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return list;
    }

    @Override
    public boolean changePasswordCandidate(int accountId, String newPassword) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL change_password_by_id(?, ?)}");
            callSt.setInt(1, accountId);
            callSt.setString(2, newPassword);

            callSt.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }


    @Override
    public Candidate getCandidateByEmail(String email) {
        Candidate candidate = null;
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_candidate_by_email(?)}");
            callSt.setString(1, email);
            rs = callSt.executeQuery();

            if (rs.next()) {
                candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    candidate.setGender(Gender.valueOf(genderStr));
                }
                candidate.setDescription(rs.getString("description"));
                Date dob = rs.getDate("dob");
                if (dob != null) {
                    candidate.setDob(dob.toLocalDate());
                }

                Account account = new Account();
                account.setId(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                String roleStr = rs.getString("role");
                if (roleStr != null) {
                    account.setRole(Role.valueOf(roleStr));
                }

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    account.setStatus(AccountStatus.valueOf(statusStr));
                }

                candidate.setAccount(account);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy ứng viên theo email: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return candidate;
    }

}