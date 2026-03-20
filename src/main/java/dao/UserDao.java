package dao;

import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.UtilForHibernate;

import java.util.List;

public interface UserDao {

    public void saveUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

    public List<User> getAllUsers();


}
