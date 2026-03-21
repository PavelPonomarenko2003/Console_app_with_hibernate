package dao;

import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.UtilForHibernate;

import java.util.List;

public interface UserDao {

    public void save(User user);

    public User update(User user);

    public void delete(Long id);

    public List<User> findAll();

    public User findById(Long id);

}