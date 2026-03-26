package modular_service_testing;

import dao.UserDao;
import entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock // DB mocking (insulation)
    private UserDao userDao;

    @InjectMocks // real service testing
    private UserService userService;

    @BeforeAll
    public static void testingServiceStarted(TestInfo testInfo){
        System.out.println("Testing: " + testInfo.getTestClass() +  " get started!");
    }

    @AfterAll
    public static void testingServiceFinished(TestInfo testInfo){
        System.out.println("Testing:" + testInfo.getTestClass() + " get finished!");
    }

    @BeforeEach
    public void certainTestGetStarted(TestInfo testInfo){
        System.out.println("Test: " + testInfo.getTestMethod() + " get started!");
    }

    @AfterEach
    public void certainTestGetFinished(TestInfo testInfo){
        System.out.println("Test: " + testInfo.getTestMethod() + " get finished!");
    }

    @Test // testing that service get new user
    public void haveToReturnNewUser(){
        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 19);

        // Act
        userService.createUser(arrangedUser);

        // Assert
        Mockito.verify(userDao, Mockito.atLeastOnce()).save(arrangedUser);
    }


    @Test // testing that service get correct data from DB using update operation
    public void haveToReturnUpdatedUser(){
        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 19);
        User mockUpdatedUser = new User("NewName", "NewName@mail.ru", 1);

        // Stubbing
        Mockito.when(userDao.update(arrangedUser)).thenReturn(mockUpdatedUser);

        // Act
        User actedUser = userService.updateUser(arrangedUser);

        // Assert
        Assertions.assertNotNull(actedUser);
        Assertions.assertEquals("NewName", actedUser.getName());
        Assertions.assertEquals("NewName@mail.ru", actedUser.getEmail());
        Assertions.assertEquals(1, actedUser.getAge());

        Mockito.verify(userDao, Mockito.atLeastOnce()).update(arrangedUser);
    }


    @Test // testing how to service get data about deleting user
    public void haveToGetUserForDeleting(){
        // Arrange
        User arrangedUser = new User("Kristy", "Kristy@mail.ru", 19);
        Long idOfArrangedUser = arrangedUser.getId();

        // Stubbing
        Mockito.when(userDao.findById(idOfArrangedUser)).thenReturn(arrangedUser);

        //Act
        userService.deleteUser(idOfArrangedUser);

        // Assert
        Mockito.verify(userDao, Mockito.atLeastOnce()).delete(idOfArrangedUser);
    }

    @Test
    public void haveToGetListOfAllUsers(){
        // Arrange
        User arrangedUser1 = new User("Kristy1", "Kristy1@mail.ru", 19);
        User arrangedUser2 = new User("Kristy2", "Kristy2@mail.ru", 19);
        List<User> arrangedListOfUsers = List.of(arrangedUser1, arrangedUser2);

        // Objects with different links for equals and hashcode
        User user1 = new User("Kristy","Kristy1@mail.ru", 19);
        User user2 = new User("Kristy", "Kristy1@mail.ru", 19);
        List<User> expectedList = List.of(user1);
        List<User> actualList = List.of(user2);

        // Stubbing
        Mockito.when(userDao.findAll()).thenReturn(arrangedListOfUsers);

        // Act
        List<User> actedListOfUsers = userService.getAllUsers();

        // Assert
        Assertions.assertNotNull(actedListOfUsers);
        Assertions.assertEquals(2, actedListOfUsers.size());
        // Checking names consisting
        List<String> checkAllNames = actedListOfUsers.stream().map(User::getName).toList();
        Assertions.assertIterableEquals(List.of("Kristy1", "Kristy2"), checkAllNames);
        // Checking emails consisting
        List<String> checkAllEmails = actedListOfUsers.stream().map(User::getEmail).toList();
        Assertions.assertIterableEquals(List.of("Kristy1@mail.ru", "Kristy2@mail.ru"), checkAllEmails);

        // Links comparing
        Assertions.assertEquals(arrangedListOfUsers, actedListOfUsers);

        // Data comparing using equals
        Assertions.assertEquals(expectedList, actualList);





    }





}
