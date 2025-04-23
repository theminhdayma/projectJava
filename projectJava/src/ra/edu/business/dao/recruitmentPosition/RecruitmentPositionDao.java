package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public interface RecruitmentPositionDao extends AppDao<RecruitmentPosition> {
    RecruitmentPosition getRecruitmentPositionById(int id);
    int getTotalPage(int limit);
    List<RecruitmentPosition> getRecruitmentPositionByPage(int page, int limit);
    List<RecruitmentPosition> getAllRecruitmentPosition();
}
