package ra.edu.business.service.candidate;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.AppService;

import java.util.List;

public interface CandidateService extends AppService<Candidate> {
    boolean loginCandidate(String username, String password);
    int getTotalPage(int limit);
    List<Candidate> getCandidateByPage(int page, int limit);
    Candidate getCandidateById(int id);
    boolean checkEmailCandidate(Candidate candidate);
    boolean lockCandidateAccount(int candidateId);
    boolean unlockCandidateAccount(int candidateId);
    boolean resetCandidatePassword(int candidateId, String newPassword);
    List<Candidate> searchCandidateByName(String keyword);
}
