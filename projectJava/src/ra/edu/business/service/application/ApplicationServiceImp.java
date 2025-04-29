package ra.edu.business.service.application;

import ra.edu.business.dao.application.ApplicationDao;
import ra.edu.business.dao.application.ApplicationDaoImp;
import ra.edu.business.model.application.Application;

import java.time.LocalDateTime;
import java.util.List;

public class ApplicationServiceImp implements ApplicationService {
    private ApplicationDao applicationDao;
    public ApplicationServiceImp() {
        applicationDao = new ApplicationDaoImp();
    }
    @Override
    public boolean addApplication(Application application) {
        return applicationDao.addApplication(application);
    }

    @Override
    public int getTotalPage(int limit) {
        return applicationDao.getTotalPage(limit);
    }

    @Override
    public List<Application> getApplicationByPage(int page, int limit) {
        return applicationDao.getApplicationByPage(page, limit);
    }

    @Override
    public List<Application> getAllApplicationCandidateLogin(int candidateId) {
        return applicationDao.getAllApplicationCandidateLogin(candidateId);
    }

    @Override
    public Application getApplicationById(int id) {
        return applicationDao.getApplicationById(id);
    }

    @Override
    public boolean updateProgressConfirmInterviewDate(int id, LocalDateTime date, String confirmInterviewDateReason) {
        return applicationDao.updateProgressConfirmInterviewDate(id, date, confirmInterviewDateReason);
    }

    @Override
    public boolean updateProgressInterviewing(int id, LocalDateTime date) {
        return applicationDao.updateProgressInterviewing(id, date);
    }

    @Override
    public boolean candidateConfirmInterviewDate(int id) {
        return applicationDao.candidateConfirmInterviewDate(id);
    }

    @Override
    public boolean adminConfirmInterviewDate(int id, LocalDateTime date) {
        return applicationDao.adminConfirmInterviewDate(id, date);
    }

    @Override
    public boolean updateProgressDestroy(int id, String reason) {
        return applicationDao.updateProgressDestroy(id, reason);
    }

    @Override
    public boolean updateProgressReject(int id, String reason) {
        return applicationDao.updateProgressReject(id, reason);
    }

    @Override
    public boolean updateProgressHanding(int id) {
        return applicationDao.updateProgressHanding(id);
    }

    @Override
    public boolean updateProgressDone(int id, String nodeDone) {
        return applicationDao.updateProgressDone(id, nodeDone);
    }

    @Override
    public List<Application> getAllApplicationByProgress(String progress) {
        return applicationDao.getAllApplicationByProgress(progress);
    }

    @Override
    public List<Application> getAllApplicationByResultInterview(String resultInterview) {
        return applicationDao.getAllApplicationByResultInterview(resultInterview);
    }
}
