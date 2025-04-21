package ra.edu.business.service.candidateTechnology;

import ra.edu.business.dao.candidateTechnology.CandidateTechnologyDao;
import ra.edu.business.dao.candidateTechnology.CandidateTechnologyDaoImp;
import ra.edu.business.model.candidateTenology.CandidateTechnology;

public class CandidateTechnologyServiceImp implements CandidateTechnologyService {
    CandidateTechnologyDao candidateTechnologyDao;

    public CandidateTechnologyServiceImp() {
        candidateTechnologyDao = new CandidateTechnologyDaoImp();
    }
    @Override
    public boolean addCandidateTechnology(CandidateTechnology candidateTechnology) {
        return candidateTechnologyDao.addCandidateTechnology(candidateTechnology);
    }
}
