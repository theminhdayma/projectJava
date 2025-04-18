package ra.edu.business.service.candidate;

import ra.edu.business.dao.candidate.CandidateDao;
import ra.edu.business.dao.candidate.CandidateDaoImp;
import ra.edu.business.model.candidate.Candidate;

import java.util.List;

public class CandidateServiceImp implements CandidateService {
    private CandidateDao candidateDao;
    public CandidateServiceImp() {
        candidateDao = new CandidateDaoImp();
    }
    @Override
    public List<Candidate> readAll() {
        return List.of();
    }

    @Override
    public boolean save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidateDao.update(candidate);
    }

    @Override
    public boolean delete(Candidate candidate) {
        return candidateDao.delete(candidate);
    }

    @Override
    public boolean loginCandidate(String username, String password) {
        return candidateDao.loginCandidate(username, password);
    }

    @Override
    public int getTotalPage(int limit) {
        return candidateDao.getTotalPage(limit);
    }

    @Override
    public List<Candidate> getCandidateByPage(int page, int limit) {
        return candidateDao.getCandidateByPage(page, limit);
    }

    @Override
    public Candidate getCandidateById(int id) {
        return candidateDao.getCandidateById(id);
    }

    @Override
    public boolean checkEmailCandidate(Candidate candidate) {
        return candidateDao.checkEmailCandidate(candidate);
    }

    @Override
    public boolean lockCandidateAccount(int candidateId) {
        return candidateDao.lockCandidateAccount(candidateId);
    }

    @Override
    public boolean unlockCandidateAccount(int candidateId) {
        return candidateDao.unlockCandidateAccount(candidateId);
    }

    @Override
    public boolean resetCandidatePassword(int candidateId, String newPassword) {
        return candidateDao.resetCandidatePassword(candidateId, newPassword);
    }

    @Override
    public List<Candidate> searchCandidateByName(String keyword) {
        return candidateDao.searchCandidateByName(keyword);
    }
}
