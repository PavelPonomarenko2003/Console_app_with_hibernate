package com.example.controller;

import com.example.dto.ErrorResponseDTO;
import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// using rest controller, working with JSON
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations for managing user profiles and data")
public class UserController {
    // valid to check user input
    // RequestBody - indicate that result of this method has to convert into JSON
    // OK return status 200 - successful
    // NO_CONTENT return status 204 - successful, but it doesn't consist any content
    // CREATED return status 201 - object created successfully

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a new user",
            description = "Validates input data and saves a new user to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreatingDTO userCreatingDTO) {
        UserResponseDTO response = userService.addUser(userCreatingDTO);

        // HATEOAS: Link to self and all users
        response.add(linkTo(methodOn(UserController.class).findUserById(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).findAllUsers()).withRel("all-users"));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user", description = "Updates user details by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdatingDTO userUpdatingDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdatingDTO);

        // HATEOAS: Link to self
        updatedUser.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Removes user from DB by ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find user by ID", description = "Returns a single user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);

        // HATEOAS: Self link and links to other actions
        user.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        user.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users", description = "Returns a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved")
    @GetMapping("/all-users")
    public ResponseEntity<CollectionModel<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> listOfUsers = userService.getAllUsers();

        // HATEOAS: Add links to each individual user in the list
        for (UserResponseDTO user : listOfUsers) {
            user.add(linkTo(methodOn(UserController.class).findUserById(user.getId())).withSelfRel());
        }

        // HATEOAS: Link to the collection
        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(listOfUsers);
        collectionModel.add(linkTo(methodOn(UserController.class).findAllUsers()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}
