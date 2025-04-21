package ra.edu.business.service.recruitmentPositionTechnology;

import ra.edu.business.dao.recruitmentPositionTechnology.RecruitmentPositionTechnologyDao;
import ra.edu.business.dao.recruitmentPositionTechnology.RecruitmentPositionTechnologyDaoImp;
import ra.edu.business.model.recruitmentPositionTechnology.RecruitmentPositionTechnology;

public class RecruitmentPositionTechnologyServiceImp implements RecruitmentPositionTechnologyService {
    RecruitmentPositionTechnologyDao recruitmentPositionTechnologyDao;
    public RecruitmentPositionTechnologyServiceImp() {
        recruitmentPositionTechnologyDao = new RecruitmentPositionTechnologyDaoImp();
    }
    @Override
    public boolean addRecruitmentPositionTechnology(RecruitmentPositionTechnology recruitmentPositionTechnology) {
        return recruitmentPositionTechnologyDao.addRecruitmentPositionTechnology(recruitmentPositionTechnology);
    }
}
