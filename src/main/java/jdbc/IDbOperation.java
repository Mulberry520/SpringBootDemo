package jdbc;

import com.mulberry.entity.User;

import java.util.List;

public interface IDbOperation {
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer id);
    List<User> getAll();
    List<User> getByName(String name);
    User getById(Integer id);
}
