package ra.edu.business.service.candidateTechnology;

import ra.edu.business.model.candidateTenology.CandidateTechnology;

import java.util.List;

public interface CandidateTechnologyService {
    boolean addCandidateTechnology(CandidateTechnology candidateTechnology);
    List<CandidateTechnology> getAllCandidateTechnologyByCandidateId(int candidateId);
    boolean deleteCandidateTechnologyById(int id);
}
