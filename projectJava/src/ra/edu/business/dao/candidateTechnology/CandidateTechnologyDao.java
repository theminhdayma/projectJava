package ra.edu.business.dao.candidateTechnology;

import ra.edu.business.model.candidateTenology.CandidateTechnology;

import java.util.List;

public interface CandidateTechnologyDao {
    boolean addCandidateTechnology(CandidateTechnology candidateTechnology);
    List<CandidateTechnology> getAllCandidateTechnologyByCandidateId(int candidateId);
    boolean deleteCandidateTechnologyById(int id);
}
