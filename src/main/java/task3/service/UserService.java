package task3.service;

import task3.entities.User;
import task3.repository.JdbcUserRepository;
import task3.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository = new JdbcUserRepository();

    public UserService() {
    }

    public User findUser(int id) {
        return userRepository.findById(id);
    }

    public User findUserByUserCode(String userCode){
        return userRepository.findByUserCode(userCode);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
