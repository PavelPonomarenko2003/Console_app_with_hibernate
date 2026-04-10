package integrational_dao_testing;

import com.example.dao.UserDaoImpl;
import com.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.hibernate.cfg.Configuration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers // tell docker that need to create Container with certain parameters
public class UserDaoImplTest {

    @Container // control container running automatically
    // Creating docker-container with postgreSQL
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("user_service")
                    .withUsername("postgres")
                    .withPassword("root")
                    .withReuse(true);

    private static SessionFactory sessionFactory;
    private UserDaoImpl userDao;


    @BeforeAll // container setup
    static void setupClass(){
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(UserEntity.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void sessionClosing() {
        sessionFactory.close();
    }


    @BeforeEach
    void setingUp() {
        userDao = new UserDaoImpl(sessionFactory); // Information about our db with fabric of session
        System.out.println("UserDao initialized: " + true);

    }

    @BeforeEach
    void cleaningUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Удаляем всех пользователей перед тестом
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test // testing how to save user in Docker container
    public void haveToSaveUser(){
        // Arrange
        UserEntity arrangedUser = new UserEntity("Krist", "Krist@mail.ru", 21);
        arrangedUser.setAge(21);

        // Act
        userDao.save(arrangedUser);
        Assertions.assertNotNull(arrangedUser.getId(), "Id of user had to be generated after saving");

        // Assert
        try(Session session = sessionFactory.openSession()) {
            UserEntity savedUser = session.get(UserEntity.class, arrangedUser.getId());
            Assertions.assertNotNull(savedUser, "User have to insist in DB");
            Assertions.assertEquals("Krist", savedUser.getName());
            Assertions.assertEquals("Krist@mail.ru", savedUser.getEmail());
            Assertions.assertEquals(21, savedUser.getAge());
        }

    }

    @Test // testing how to update user in Docker COntainer
    public void haveToUpdateUser(){

        // Arrange
        UserEntity arrangedUser = new UserEntity("Krist", "Krist@mail.ru", 21);
        userDao.save(arrangedUser);
        arrangedUser.setName("NewName");
        arrangedUser.setEmail("NewName@mail.ru");
        arrangedUser.setAge(1);

        // Act
        UserEntity updatedUser = userDao.update(arrangedUser);

        // Assert
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(arrangedUser.getId(), updatedUser.getId(), "Id have to be similar!");

        try(Session session = sessionFactory.openSession()) {
            UserEntity updatedUserInDB = session.get(UserEntity.class, arrangedUser.getId());
            Assertions.assertEquals("NewName", updatedUserInDB.getName());
            Assertions.assertEquals("NewName@mail.ru", updatedUserInDB.getEmail());
            Assertions.assertEquals(1, updatedUserInDB.getAge());
        }

    }


    @Test // testing how to delete user in Docker Container
    public void haveToDeleteUser(){

        // Arrange
        UserEntity arrangedUser = new UserEntity("Krist", "Krist@mail.ru", 21);
        userDao.save(arrangedUser);
        Assertions.assertNotNull(arrangedUser);
        Long arrangedUserId = arrangedUser.getId();

        // Act
        userDao.delete(arrangedUserId);

        // Assert
        try(Session session = sessionFactory.openSession()) {
            UserEntity assertedUserInDB = session.get(UserEntity.class, arrangedUserId);
            Assertions.assertNull(assertedUserInDB, "This user should no exist!");
        }


    }

    @Test // testing how to find all users in Docker Container
    public void haveToFindAllUsers(){
        // Arrange
        UserEntity arrangedUser1 = new UserEntity("Krist", "Krist@mail.ru", 21);
        UserEntity arrangedUser2 = new UserEntity("Ann", "Ann@mail.ru", 21);
        userDao.save(arrangedUser1);
        userDao.save(arrangedUser2);
        List<UserEntity> arrangedListOfUsers = List.of(arrangedUser1, arrangedUser2);

        // Act
        List<UserEntity> actedListOfUsers = userDao.findAll();


        // Assert
        Assertions.assertNotNull(actedListOfUsers, "Has not to be null");
        Assertions.assertTrue(actedListOfUsers.size() >= 2, "Should contain at least 2 users");
        List<String> names = actedListOfUsers.stream().map(UserEntity::getName).toList();
        Assertions.assertTrue(names.contains("Krist"));
        Assertions.assertTrue(names.contains("Ann"));


    }



}
