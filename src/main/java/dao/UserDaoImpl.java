package dao;

import entity.User;
import exceptions_handling.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.UtilForHibernate;


import java.util.List;

import static util.UtilForHibernate.getSessionFactory;

public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DaoException("Error saving user", exception);
        }
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User updated = (User) session.merge(user);
            transaction.commit();
            return updated;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DaoException("Error updating user", exception);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DaoException("Error deleting user", exception);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        } catch (Exception exception) {
            throw new DaoException("Error getting all users", exception);
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception exception) {
            throw new DaoException("Error getting user by id", exception);
        }
    }

}