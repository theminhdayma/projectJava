package ra.edu.business.model.recruitmentPositionTechnology;

public class RecruitmentPositionTechnology {
    private int id;
    private int technologyId;
    private int recruitmentPositionId;

    public RecruitmentPositionTechnology() {};

    public RecruitmentPositionTechnology(int id, int technologyId, int recruitmentPositionId) {
        this.id = id;
        this.technologyId = technologyId;
        this.recruitmentPositionId = recruitmentPositionId;
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

    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }
}
