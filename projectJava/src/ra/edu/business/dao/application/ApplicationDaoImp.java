package ra.edu.business.dao.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.application.ResultInterview;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDaoImp implements ApplicationDao {
    @Override
    public boolean addApplication(Application application) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL add_application(?, ?, ?, ?)}");

            callSt.setInt(1, application.getCandidateId());
            callSt.setInt(2, application.getRecruitmentPositionId());
            callSt.setString(3, application.getCvUrl());

            callSt.registerOutParameter(4, Types.INTEGER);

            int result = callSt.executeUpdate();

            if (result > 0) {
                int generatedId = callSt.getInt(4);
                application.setId(generatedId);
                return true;
            }
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.err.println(e.getMessage());
            } else {
                System.err.println("Lỗi thêm ứng viên: " + e.getMessage());
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
            callSt = conn.prepareCall("{CALL get_application_page(?)}");
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
    public List<Application> getApplicationByPage(int page, int limit) {
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        List<Application> applicationList = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_application(?, ?)}");
            callSt.setInt(1, page);
            callSt.setInt(2, limit);
            rs = callSt.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setId(rs.getInt("id"));
                application.setCandidateId(rs.getInt("candidateId"));
                application.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                application.setCvUrl(rs.getString("cvUrl"));
                String progressStr = rs.getString("progress").toUpperCase();
                if (progressStr != null) {
                    application.setProgress(Progress.valueOf(progressStr));
                }
                String resultInterviewStr = rs.getString("resultInterview");
                if (resultInterviewStr != null) {
                    application.setResultInterview(ResultInterview.valueOf(resultInterviewStr.toUpperCase()));
                }
                Timestamp interviewTimestamp = rs.getTimestamp("interviewDate");
                if (interviewTimestamp != null) {
                    application.setInterviewDate(interviewTimestamp.toLocalDateTime());
                }
                Timestamp confirmInterviewDate = rs.getTimestamp("confirmInterviewDate");
                if (confirmInterviewDate != null) {
                    application.setConfirmInterviewDate(confirmInterviewDate.toLocalDateTime());
                }
                String confirmInterviewDateReason = rs.getString("confirmInterviewDateReason");
                if (confirmInterviewDateReason != null) {
                    application.setConfirmInterviewDateReason(confirmInterviewDateReason);
                }
                application.setCreateAt(rs.getDate("createAt").toLocalDate());

                Date destroyDate = rs.getDate("destroyAt");
                if (destroyDate != null) {
                    application.setDestroyDate(destroyDate.toLocalDate());
                }
                Date rejectedAt = rs.getDate("rejectedAt");
                if (rejectedAt != null) {
                    application.setRejectedAt(rejectedAt.toLocalDate());
                }
                application.setDestroyReason(rs.getString("destroyReason"));
                application.setRejectedReason(rs.getString("rejectedReason"));
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return applicationList;
    }

    @Override
    public List<Application> getAllApplicationCandidateLogin(int candidateId) {
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        List<Application> applicationList = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_application_of_candidate_login(?)}");
            callSt.setInt(1, candidateId);
            rs = callSt.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setId(rs.getInt("id"));
                application.setCandidateId(rs.getInt("candidateId"));
                application.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                application.setCvUrl(rs.getString("cvUrl"));
                String progressStr = rs.getString("progress").toUpperCase();
                if (progressStr != null) {
                    application.setProgress(Progress.valueOf(progressStr));
                }
                String resultInterviewStr = rs.getString("resultInterview");
                if (resultInterviewStr != null) {
                    application.setResultInterview(ResultInterview.valueOf(resultInterviewStr.toUpperCase()));
                }
                Timestamp interviewTimestamp = rs.getTimestamp("interviewDate");
                if (interviewTimestamp != null) {
                    application.setInterviewDate(interviewTimestamp.toLocalDateTime());
                }
                Timestamp confirmInterviewDate = rs.getTimestamp("confirmInterviewDate");
                if (confirmInterviewDate != null) {
                    application.setConfirmInterviewDate(confirmInterviewDate.toLocalDateTime());
                }
                String confirmInterviewDateReason = rs.getString("confirmInterviewDateReason");
                if (confirmInterviewDateReason != null) {
                    application.setConfirmInterviewDateReason(confirmInterviewDateReason);
                }
                application.setCreateAt(rs.getDate("createAt").toLocalDate());

                Date destroyDate = rs.getDate("destroyAt");
                if (destroyDate != null) {
                    application.setDestroyDate(destroyDate.toLocalDate());
                }
                Date rejectedAt = rs.getDate("rejectedAt");
                if (rejectedAt != null) {
                    application.setRejectedAt(rejectedAt.toLocalDate());
                }
                application.setDestroyReason(rs.getString("destroyReason"));
                application.setRejectedReason(rs.getString("rejectedReason"));
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return applicationList;
    }

    @Override
    public Application getApplicationById(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        Application application = null;

        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_application_by_id(?)}");
            callSt.setInt(1, id);
            rs = callSt.executeQuery();

            if (rs.next()) {
                application = new Application();
                application.setId(rs.getInt("id"));
                application.setCandidateId(rs.getInt("candidateId"));
                application.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                application.setCvUrl(rs.getString("cvUrl"));
                String progressStr = rs.getString("progress").toUpperCase();
                if (progressStr != null) {
                    application.setProgress(Progress.valueOf(progressStr));
                }
                String resultInterviewStr = rs.getString("resultInterview");
                if (resultInterviewStr != null) {
                    application.setResultInterview(ResultInterview.valueOf(resultInterviewStr.toUpperCase()));
                }
                Timestamp interviewTimestamp = rs.getTimestamp("interviewDate");
                if (interviewTimestamp != null) {
                    application.setInterviewDate(interviewTimestamp.toLocalDateTime());
                }
                Timestamp confirmInterviewDate = rs.getTimestamp("confirmInterviewDate");
                if (confirmInterviewDate != null) {
                    application.setConfirmInterviewDate(confirmInterviewDate.toLocalDateTime());
                }
                String confirmInterviewDateReason = rs.getString("confirmInterviewDateReason");
                if (confirmInterviewDateReason != null) {
                    application.setConfirmInterviewDateReason(confirmInterviewDateReason);
                }
                application.setCreateAt(rs.getDate("createAt").toLocalDate());

                Date destroyDate = rs.getDate("destroyAt");
                if (destroyDate != null) {
                    application.setDestroyDate(destroyDate.toLocalDate());
                }
                Date rejectedAt = rs.getDate("rejectedAt");
                if (rejectedAt != null) {
                    application.setRejectedAt(rejectedAt.toLocalDate());
                }
                application.setDestroyReason(rs.getString("destroyReason"));
                application.setRejectedReason(rs.getString("rejectedReason"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }

        return application;
    }

    @Override
    public boolean updateProgressConfirmInterviewDate(int id, LocalDateTime date, String confirmInterviewDateReason) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_to_wait_interviewing_date(?, ?, ?)}");
            callSt.setInt(1, id);
            callSt.setTimestamp(2, Timestamp.valueOf(date));
            callSt.setString(3, confirmInterviewDateReason);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean updateProgressInterviewing(int id, LocalDateTime date) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_to_interviewing(?, ?)}");
            callSt.setInt(1, id);
            callSt.setTimestamp(2, Timestamp.valueOf(date));
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái phỏng vấn: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean candidateConfirmInterviewDate(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL ApplicationDaoImp(?)}");
            callSt.setInt(1, id);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xác nhận ngày phỏng vấn của ứng viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean adminConfirmInterviewDate(int id, LocalDateTime date) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_to_interviewing_confirm(?, ?)}");
            callSt.setInt(1, id);
            callSt.setTimestamp(2, Timestamp.valueOf(date));
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xác nhận ngày phỏng vấn của quản trị viên: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean updateProgressDestroy(int id, String reason) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_to_destroyed(?, ?)}");
            callSt.setInt(1, id);
            callSt.setString(2, reason);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái hủy: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean updateProgressReject(int id, String reason) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_to_rejected(?, ?)}");
            callSt.setInt(1, id);
            callSt.setString(2, reason);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean updateProgressHanding(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_progress_handing(?)}");
            callSt.setInt(1, id);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public boolean updateProgressDone(int id, String resultInterview) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL update_progress_done(?, ?)}");
            callSt.setInt(1, id);
            callSt.setString(2, resultInterview);
            int result = callSt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái hoàn thành: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return false;
    }

    @Override
    public List<Application> getAllApplicationByProgress(String progress) {
        Connection conn = null;
        CallableStatement callSt = null;
        ResultSet rs = null;
        List<Application> applicationList = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{CALL get_application_by_progress(?)}");
            callSt.setString(1, progress);
            rs = callSt.executeQuery();
            while (rs.next()) {
                Application application = new Application();
                application.setId(rs.getInt("id"));
                application.setCandidateId(rs.getInt("candidateId"));
                application.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                application.setCvUrl(rs.getString("cvUrl"));
                String progressStr = rs.getString("progress").toUpperCase();
                if (progressStr != null) {
                    application.setProgress(Progress.valueOf(progressStr));
                }
                String resultInterviewStr = rs.getString("resultInterview");
                if (resultInterviewStr != null) {
                    application.setResultInterview(ResultInterview.valueOf(resultInterviewStr.toUpperCase()));
                }
                Timestamp interviewTimestamp = rs.getTimestamp("interviewDate");
                if (interviewTimestamp != null) {
                    application.setInterviewDate(interviewTimestamp.toLocalDateTime());
                }
                Timestamp confirmInterviewDate = rs.getTimestamp("confirmInterviewDate");
                if (confirmInterviewDate != null) {
                    application.setConfirmInterviewDate(confirmInterviewDate.toLocalDateTime());
                }
                String confirmInterviewDateReason = rs.getString("confirmInterviewDateReason");
                if (confirmInterviewDateReason != null) {
                    application.setConfirmInterviewDateReason(confirmInterviewDateReason);
                }
                application.setCreateAt(rs.getDate("createAt").toLocalDate());

                Date destroyDate = rs.getDate("destroyAt");
                if (destroyDate != null) {
                    application.setDestroyDate(destroyDate.toLocalDate());
                }
                Date rejectedAt = rs.getDate("rejectedAt");
                if (rejectedAt != null) {
                    application.setRejectedAt(rejectedAt.toLocalDate());
                }
                application.setDestroyReason(rs.getString("destroyReason"));
                application.setRejectedReason(rs.getString("rejectedReason"));
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return applicationList;
    }
}
