package dao;

import entity.User;
import exceptions_handling.DaoException;
import exceptions_handling.UserNotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.UtilForHibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void saveUser(User user) {
        logger.info("Saving user: {}", user.getName());

        Transaction transaction = null;

        try (Session session = UtilForHibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

            logger.info("User saved successfully with id: {}", user.getId());

        }
        catch (HibernateException exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Hibernate error while saving user", exception);
            throw new DaoException("Database error while saving user", exception);

        } catch (Exception exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error while saving user", exception);
            throw new DaoException("Hibernate error", exception);
        }
    }
    @Override
    public User updateUser(User user) {
        logger.info("Updating user with id: {}", user.getId());

        Transaction transaction = null;

        try (Session session = UtilForHibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User updatedUser = (User) session.merge(user);

            transaction.commit();

            logger.info("User updated successfully with id: {}", updatedUser.getId());
            return updatedUser;

        } catch (HibernateException exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error("Error while updating user with id: {}", user.getId(), exception);
            throw new DaoException("Error updating user", exception);
        }
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Attempt to delete user with id: {}", id);

        try (Session session = UtilForHibernate.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                User user = session.get(User.class, id);
                if (user == null) {
                    logger.warn("User not found with id: {}", id);
                    throw new UserNotFoundException("User not found with id: " + id);
                }

                session.remove(user);
                transaction.commit();

                logger.info("User deleted successfully with id: {}", id);

            } catch (Exception exception) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                    logger.warn("Transaction rolled back while deleting user with id: {}", id);
                }

                logger.error("Error while deleting user with id: {}", id, exception);
                throw exception;
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");

        try (Session session = UtilForHibernate.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).getResultList();

            logger.info("Getting all users {} ", users.size());
            return users;

        } catch (Exception exception) {
            logger.error("Error while getting all users", exception);
            throw exception;
        }
    }

}