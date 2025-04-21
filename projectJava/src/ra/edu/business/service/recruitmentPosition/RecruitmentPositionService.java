package ra.edu.business.service.recruitmentPosition;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

import java.util.List;

public interface RecruitmentPositionService extends AppDao<RecruitmentPosition> {
    RecruitmentPosition getRecruitmentPositionById(int id);
    int getTotalPage(int limit);
    List<RecruitmentPosition> getRecruitmentPositionByPage(int page, int limit);
    List<RecruitmentPosition> getAllRecruitmentPosition();
}
