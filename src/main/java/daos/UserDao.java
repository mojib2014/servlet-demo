package daos;

import entity.User;

import java.util.List;

public interface UserDao {
    void create(User user);
    void update(User user);
    void delete(int user_id);
    User get(int user_id);
    List<User> getAll();
}
