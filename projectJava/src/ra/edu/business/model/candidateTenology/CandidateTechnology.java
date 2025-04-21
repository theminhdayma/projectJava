package ra.edu.business.model.candidateTenology;

public class CandidateTechnology {
    private int id;
    private int technologyId;
    private int candidateId;

    public CandidateTechnology() {}
    public CandidateTechnology(int id, int technologyId, int candidateId) {
        this.id = id;
        this.technologyId = technologyId;
        this.candidateId = candidateId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(int technologyId) {
        this.technologyId = technologyId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }
}
