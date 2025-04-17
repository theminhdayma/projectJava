package ra.edu.business.service;

import java.util.List;

public interface AppService<T> {
    List<T> readAll();

    boolean save(T t);

    boolean update(T t);

    boolean delete(T t);
}
