package DAOc;
import java.util.List;

public interface IDao<T> {
    boolean create(T obj);
    boolean update(T obj);
    boolean delete(int id);
    T findById(int id);
    List<T> findAll();
}