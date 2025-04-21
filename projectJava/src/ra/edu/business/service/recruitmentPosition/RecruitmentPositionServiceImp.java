package ra.edu.business.service.recruitmentPosition;

import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDao;
import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDaoImp;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

import java.util.List;

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

    @Override
    public RecruitmentPosition getRecruitmentPositionById(int id) {
        return recruitmentPositionDao.getRecruitmentPositionById(id);
    }

    @Override
    public int getTotalPage(int limit) {
        return recruitmentPositionDao.getTotalPage(limit);
    }

    @Override
    public List<RecruitmentPosition> getRecruitmentPositionByPage(int page, int limit) {
        return recruitmentPositionDao.getRecruitmentPositionByPage(page, limit);
    }

    @Override
    public List<RecruitmentPosition> getAllRecruitmentPosition() {
        return recruitmentPositionDao.getAllRecruitmentPosition();
    }
}
