package ra.edu.business.dao.candidate;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.candidate.Candidate;

import java.util.List;

public interface CandidateDao extends AppDao<Candidate> {
    boolean loginCandidate(String username, String password);
    int getTotalPage(int limit);
    List<Candidate> getCandidateByPage(int page, int limit);
    Candidate getCandidateById(int id);
    boolean checkEmailCandidate(String email);
    boolean lockCandidateAccount(int candidateId);
    boolean unlockCandidateAccount(int candidateId);
    boolean resetCandidatePassword(int candidateId, String newPassword);
    List<Candidate> searchCandidateByName(String keyword);
    List<Candidate> filterByExperience(int minExperience);
    List<Candidate> filterByAgeRange(int minAge, int maxAge);
    List<Candidate> filterByGender(String gender);
    boolean changePasswordCandidate(int accountId, String newPassword);
    Candidate getCandidateByEmail(String email);
}
