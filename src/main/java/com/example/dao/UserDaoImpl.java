package com.example.dao;

import com.example.entity.UserEntity;
import com.example.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }


    @Override
    public void save(UserEntity user) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            System.out.println("User persisted with ID: " + user.getId()); // Проверьте, что тут не null
            transaction.commit();
            System.out.println("Transaction committed");
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DaoException("Error saving user", exception);
        } finally {
            session.close();
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserEntity updated = (UserEntity) session.merge(user);
            transaction.commit();
            return updated;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DaoException("Error updating user", exception);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DaoException("Error deleting user", exception);
        } finally {
            session.close();
        }

    }

    @Override
    public List<UserEntity> findAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            List<UserEntity> users = session.createQuery("from User", UserEntity.class).getResultList();

            transaction.commit(); // Завершаем транзакцию после успешного чтения
            return users;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DaoException("Error getting all users", exception);
        } finally {
            session.close();
        }
    }

    @Override
    public UserEntity findById(Long id) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            UserEntity user = session.get(UserEntity.class, id);

            transaction.commit(); // Для чтения commit просто закрывает транзакцию
            return user;
        } catch (Exception exception) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Error finding user", exception);
        } finally {
            session.close();
        }
    }

}