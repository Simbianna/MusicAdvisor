package task3.repository;

import task3.entities.User;

import java.util.List;

public interface UserRepository {
    User findById(int id);

    User findByUserCode(String userCode);

    void save(User user);

    void update(User user);

    void delete(User user);

    List<User> findAll();
}
