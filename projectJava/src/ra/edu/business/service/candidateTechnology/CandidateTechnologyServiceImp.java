package ra.edu.business.service.candidateTechnology;

import ra.edu.business.dao.candidateTechnology.CandidateTechnologyDao;
import ra.edu.business.dao.candidateTechnology.CandidateTechnologyDaoImp;
import ra.edu.business.model.candidateTenology.CandidateTechnology;

import java.util.List;

public class CandidateTechnologyServiceImp implements CandidateTechnologyService {
    CandidateTechnologyDao candidateTechnologyDao;

    public CandidateTechnologyServiceImp() {
        candidateTechnologyDao = new CandidateTechnologyDaoImp();
    }
    @Override
    public boolean addCandidateTechnology(CandidateTechnology candidateTechnology) {
        return candidateTechnologyDao.addCandidateTechnology(candidateTechnology);
    }

    @Override
    public List<CandidateTechnology> getAllCandidateTechnologyByCandidateId(int candidateId) {
        return candidateTechnologyDao.getAllCandidateTechnologyByCandidateId(candidateId);
    }

    @Override
    public boolean deleteCandidateTechnologyById(int id) {
        return candidateTechnologyDao.deleteCandidateTechnologyById(id);
    }
}
