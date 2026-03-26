package service;

import dao.UserDao;
import entity.User;
import exceptions_handling.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {

    private final UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) {
        logger.info("Creating user: {}", user.getName());
        userDao.save(user);
        logger.info("User created with id: {}", user.getId());
    }

    public User updateUser(User user) {
        logger.info("Updating user with id: {}", user.getId());
        User updated = userDao.update(user);
        logger.info("User updated with id: {}", updated.getId());
        return updated;
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        User user = userDao.findById(id);
        if (user == null) {
            logger.warn("User not found with id: {}", id);
            throw new UserNotFoundException("User not found!");
        }
        userDao.delete(id);
        logger.info("User deleted with id: {}", id);
    }

    public List<User> getAllUsers() {
        logger.info("Looking for all users");
        List<User> listOfUsers = userDao.findAll();
        if(listOfUsers.isEmpty()){
            logger.info("There are no any users in Database");
        }
        return listOfUsers;
    }

}
