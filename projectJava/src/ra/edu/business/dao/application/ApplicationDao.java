package ra.edu.business.dao.application;

import ra.edu.business.model.application.Application;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationDao {
    boolean addApplication(Application application);
    int getTotalPage(int limit);
    List<Application> getApplicationByPage(int page, int limit);
    List<Application> getAllApplicationCandidateLogin(int candidateId);
    Application getApplicationById(int id);
    boolean updateProgressConfirmInterviewDate(int id, LocalDateTime date, String confirmInterviewDateReason);
    boolean updateProgressInterviewing(int id, LocalDateTime date);
    boolean candidateConfirmInterviewDate(int id);
    boolean adminConfirmInterviewDate(int id, LocalDateTime date);
    boolean updateProgressDestroy(int id, String reason);
    boolean updateProgressReject(int id, String reason);
    boolean updateProgressHanding(int id);
    boolean updateProgressDone(int id, String resultInterview);
    List<Application> getAllApplicationByProgress(String progress);
    List<Application> getAllApplicationByResultInterview(String resultInterview);
}
