package integrational_controller_testing;

import com.example.SpringApp;
import com.example.controller.UserController;
import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.service.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = SpringApp.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Converting DTO to JSON

    @MockitoBean
    private UserService userService;

    private UserResponseDTO sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new UserResponseDTO();
        sampleUser.setId(1L);
        sampleUser.setName("Alice");
        sampleUser.setEmail("alice@mail.ru");
        sampleUser.setAge(25);
    }

    @Test
    @DisplayName("POST /users/create - Should create user and return HATEOAS links")
    void createUserShouldReturn201AndLinks() throws Exception {
        UserCreatingDTO dto = new UserCreatingDTO("Alice", "alice@mail.ru", 25);
        when(userService.addUser(any(UserCreatingDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST /users/create - Should return 400 Bad Request when email is incorrect")
    void createUserShouldReturn400WhenInvalidEmail() throws Exception {
        UserCreatingDTO invalidDto = new UserCreatingDTO("Alice", "alice@gmail.com", 25);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /users/find/{id} - Should return user details and action links")
    void findUserByIdShouldReturnUserAndHateoasLinks() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/users/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/find/1")))
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @DisplayName("PUT /users/update/{id} - Should update user and return 200 OK")
    void updateUserShouldReturnUpdatedUser() throws Exception {
        UserUpdatingDTO updateDto = new UserUpdatingDTO("Alice Updated", "alice@mail.ru", 26);
        sampleUser.setName("Alice Updated");

        when(userService.updateUser(eq(1L), any(UserUpdatingDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice Updated")));
    }

    @Test
    @DisplayName("DELETE /users/delete/{id} - Should return 204 No Content on success")
    void deleteUserShouldReturn204() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /users/all-users - Should return list of users with HATEOAS links")
    void findAllUsersShouldReturnCollectionModel() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(sampleUser));

        mockMvc.perform(get("/users/all-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDTOList[0].name", is("Alice")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
