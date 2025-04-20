package ra.edu.business.model.recruitmentPosition;

import ra.edu.business.model.technology.Technology;

public class RecruitmentPositionTechnology {
    private int id;
    private RecruitmentPosition recruitmentPosition;
    private Technology technology;

    public RecruitmentPositionTechnology() {};

    public RecruitmentPositionTechnology(int id, RecruitmentPosition recruitmentPosition, Technology technology) {
        this.id = id;
        this.recruitmentPosition = recruitmentPosition;
        this.technology = technology;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public RecruitmentPosition getRecruitmentPosition() {
        return recruitmentPosition;
    }
    public void setRecruitmentPosition(RecruitmentPosition recruitmentPosition) {
        this.recruitmentPosition = recruitmentPosition;
    }
    public Technology getTechnology() {
        return technology;
    }
    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

}
