package ra.edu.business.service.application;

import ra.edu.business.model.application.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationService {
    boolean addApplication(Application application);
    int getTotalPage(int limit);
    List<Application> getApplicationByPage(int page, int limit);
    List<Application> getAllApplicationCandidateLogin(int candidateId);
    Application getApplicationById(int id);
    boolean updateProgressInterviewing(int id, LocalDateTime date);
    boolean updateProgressDestroy(int id, String reason);
    boolean updateProgressDone(int id);
    List<Application> getAllApplicationByProgress(String progress);
}
