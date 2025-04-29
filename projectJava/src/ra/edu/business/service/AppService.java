package ra.edu.business.service;

public interface AppService<T> {

    boolean save(T t);

    boolean update(T t);

    boolean delete(T t);
}
