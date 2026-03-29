package com.example.controller;

import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
// using rest controller, working with JSON (btw idk what should I used rest or mvc...)
public class UserController {


    // valid to check user input
    // RequestBody - indicate that result of this method has to convert into JSON

    // OK return status 200 - successful
    // NO_CONTENT return status 204 - successful but it doesn't consist any content
    // CREATED return status 201 - object created successfully



    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreatingDTO userCreatingDTO) {
        UserResponseDTO response = userService.addUser(userCreatingDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdatingDTO userUpdatingDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdatingDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> listOfUsers = userService.getAllUsers();
        return ResponseEntity.ok(listOfUsers);
    }

}