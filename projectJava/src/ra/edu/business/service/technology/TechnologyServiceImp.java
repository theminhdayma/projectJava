package ra.edu.business.service.technology;

import ra.edu.business.dao.technology.TechnologyDao;
import ra.edu.business.dao.technology.TechnologyDaoImp;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public class TechnologyServiceImp implements TechnologyService {
    private TechnologyDao technologyDao;
    public TechnologyServiceImp() {
        technologyDao = new TechnologyDaoImp();
    }

    @Override
    public boolean save(Technology technology) {
        return technologyDao.save(technology);
    }

    @Override
    public boolean update(Technology technology) {
        return technologyDao.update(technology);
    }

    @Override
    public boolean delete(Technology technology) {
        return technologyDao.delete(technology);
    }

    @Override
    public int getTotalPage(int limit) {
        return technologyDao.getTotalPage(limit);
    }

    @Override
    public List<Technology> getTechnologyByPage(int page, int limit) {
        return technologyDao.getTechnologyByPage(page, limit);
    }

    @Override
    public Technology getTechnologyById(int id) {
        return technologyDao.getTechnologyById(id);
    }

    @Override
    public boolean checkNameTechnology(Technology technology) {
        return technologyDao.checkNameTechnology(technology);
    }
}
