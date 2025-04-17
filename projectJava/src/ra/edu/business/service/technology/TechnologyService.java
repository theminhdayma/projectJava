package ra.edu.business.service.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.AppService;

import java.util.List;

public interface TechnologyService extends AppService<Technology> {
    int getTotalPage(int limit);
    List<Technology> getTechnologyByPage(int page, int limit);
    Technology getTechnologyById(int id);
}
