package ra.edu.business.dao;

import java.util.List;

public interface AppDao<T> {

    boolean save(T t);

    boolean update(T t);

    boolean delete(T t);
}
