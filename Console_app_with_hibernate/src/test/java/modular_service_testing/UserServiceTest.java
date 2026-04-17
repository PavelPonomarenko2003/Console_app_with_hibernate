package modular_service_testing;

import com.example.repository.UserRepository;
import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.entity.UserEntity;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.service.impl.UserServiceImpl;
import com.example.service.producer.UserEventPublisher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

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
        System.out.println("Test: " + testInfo.getDisplayName() + " get started!");
    }

    @AfterEach
    public void certainTestGetFinished(TestInfo testInfo){
        System.out.println("Test: " + testInfo.getDisplayName()+ " get finished!");
    }

    @Test
    @DisplayName("Test has to return user though DTO!")
    public void addUserHaveToReturnUserResponseDTO() {
        // Arrange
        UserCreatingDTO creatingDTO = new UserCreatingDTO("Kristy", "Kristy@mail.ru", 19);
        UserEntity entity = new UserEntity("Kristy", "Kristy@mail.ru", 19);
        UserResponseDTO expectedResponse = new UserResponseDTO(1L, "Kristy", "Kristy@mail.ru", 19);

        when(userMapper.convertToEntityWithCreating(creatingDTO)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.convertToResponseDTO(entity)).thenReturn(expectedResponse);

        // Act
        UserResponseDTO actualResponse = userService.addUser(creatingDTO);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.getId());
        verify(userRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Has to update user's data")
    public void updateUserHaveToReturnUpdatedUserDTO() {
        // Arrange
        Long userId = 1L;
        UserUpdatingDTO updatingDTO = new UserUpdatingDTO("NewName", "NewName@mail.ru", 20);
        UserEntity existingUser = new UserEntity("OldName", "Old@mail.ru", 19);
        UserResponseDTO responseDTO = new UserResponseDTO(userId, "NewName", "NewName@mail.ru", 20);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.convertToResponseDTO(existingUser)).thenReturn(responseDTO);

        // Act
        UserResponseDTO result = userService.updateUser(userId, updatingDTO);

        // Assert
        assertEquals("NewName", result.getName());
        verify(userMapper).convertToEntityWithUpdating(updatingDTO, existingUser);
    }

    @Test
    @DisplayName("Has to throw exception if user wasn't found")
    public void deleteHaveToThrowExceptionWhenUserNotFound() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Has to return list of users")
    public void getAllUsersHaveToReturnListOfUsers() {
        // Arrange
        List<UserEntity> entities = List.of(new UserEntity(), new UserEntity());
        List<UserResponseDTO> expectedDtos = List.of(new UserResponseDTO(), new UserResponseDTO());

        when(userRepository.findAll()).thenReturn(entities);
        when(userMapper.toDTOList(entities)).thenReturn(expectedDtos);

        // Act
        List<UserResponseDTO> actualList = userService.getAllUsers();

        // Assert
        assertEquals(2, actualList.size());
        verify(userRepository).findAll();
    }
}
