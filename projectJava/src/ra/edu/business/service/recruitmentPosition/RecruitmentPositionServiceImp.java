package ra.edu.business.service.recruitmentPosition;

import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDao;
import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDaoImp;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

public class RecruitmentPositionServiceImp implements RecruitmentPositionService {
    private RecruitmentPositionDao recruitmentPositionDao;
    public RecruitmentPositionServiceImp() {
        recruitmentPositionDao = new RecruitmentPositionDaoImp();
    }

    @Override
    public boolean save(RecruitmentPosition recruitmentPosition) {
        return recruitmentPositionDao.save(recruitmentPosition);
    }

    @Override
    public boolean update(RecruitmentPosition recruitmentPosition) {
        return recruitmentPositionDao.update(recruitmentPosition);
    }

    @Override
    public boolean delete(RecruitmentPosition recruitmentPosition) {
        return recruitmentPositionDao.delete(recruitmentPosition);
    }
}
