package ra.edu.business.dao.technology;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public interface TechnologyDao extends AppDao<Technology> {
    int getTotalPage(int limit);
    List<Technology> getTechnologyByPage(int page, int limit);
    Technology getTechnologyById(int id);
    boolean checkNameTechnology(String name);
    List<Technology> getAllTechnology();
    List<Technology> getTechnologyByRecruitmentPositionId(int recruitmentPositionId);
}
