package integrational_dao_testing;

import dao.UserDaoImpl;
import entity.User;
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
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void sessionClosing() {
        sessionFactory.close();
    }


    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(sessionFactory); // Information about our db with fabric of session
        System.out.println("UserDao initialized: " + true);

    }

    @Test // testing how to save user in Docker container
    public void haveToSaveUser(){
        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 21);
        arrangedUser.setAge(21);

        // Act
        userDao.save(arrangedUser);
        Assertions.assertNotNull(arrangedUser.getId(), "Id of user had to be generated after saving");

        // Assert
        try(Session session = sessionFactory.openSession()) {
            User savedUser = session.get(User.class, arrangedUser.getId());
            Assertions.assertNotNull(savedUser, "User have to insist in DB");
            Assertions.assertEquals("Kristy", savedUser.getName());
            Assertions.assertEquals("Kristy@mail.ru", savedUser.getEmail());
            Assertions.assertEquals(21, savedUser.getAge());
        }

    }

    @Test // testing how to update user in Docker COntainer
    public void haveToUpdateUser(){

        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 21);
        userDao.save(arrangedUser);
        arrangedUser.setName("NewName");
        arrangedUser.setEmail("NewName@mail.ru");
        arrangedUser.setAge(1);

        // Act
        User updatedUser = userDao.update(arrangedUser);

        // Assert
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(arrangedUser.getId(), updatedUser.getId(), "Id have to be similar!");

        try(Session session = sessionFactory.openSession()) {
            User updatedUserInDB = session.get(User.class, arrangedUser.getId());
            Assertions.assertEquals("NewName", updatedUserInDB.getName());
            Assertions.assertEquals("NewName@mail.ru", updatedUserInDB.getEmail());
            Assertions.assertEquals(1, updatedUserInDB.getAge());
        }

    }


    @Test // testing how to delete user in Docker Container
    public void haveToDeleteUser(){

        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 21);
        userDao.save(arrangedUser);
        Assertions.assertNotNull(arrangedUser);
        Long arrangedUserId = arrangedUser.getId();

        // Act
        userDao.delete(arrangedUserId);

        // Assert
        try(Session session = sessionFactory.openSession()) {
            User assertedUserInDB = session.get(User.class, arrangedUserId);
            Assertions.assertNull(assertedUserInDB, "This user should no exist!");
        }


    }

    @Test // testing how to find all users in Docker Container
    public void haveToFindAllUsers(){
        // Arrange
        User arrangedUser1 = new User("Kristy", "Kristy@mail.ru", 21);
        User arrangedUser2 = new User("Ann", "Ann@mail.ru", 21);
        userDao.save(arrangedUser1);
        userDao.save(arrangedUser2);
        List<User> arrangedListOfUsers = List.of(arrangedUser1, arrangedUser2);

        // Act
        List<User> actedListOfUsers = userDao.findAll();


        // Assert
        Assertions.assertNotNull(actedListOfUsers, "Has not to be null");
        Assertions.assertTrue(actedListOfUsers.size() >= 2, "Should contain at least 2 users");
        List<String> names = actedListOfUsers.stream().map(User::getName).toList();
        Assertions.assertTrue(names.contains("Kristy"));
        Assertions.assertTrue(names.contains("Ann"));


    }



}
