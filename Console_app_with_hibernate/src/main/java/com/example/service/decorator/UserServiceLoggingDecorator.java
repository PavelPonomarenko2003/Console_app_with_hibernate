package com.example.service.decorator;

import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

// using pattern decorator but...we could go for AOP as well
@Service
@Slf4j // logging
@Primary // there r two different implementations, so we need to indicate what use by default...for sure decorator
public class UserServiceLoggingDecorator implements UserService {

    private final UserService userService;

    public UserServiceLoggingDecorator(
            // to prevent conflict with implementation
            @Qualifier("userServiceImpl") UserService userService) {
            this.userService = userService;
    }

    @Override
    public UserResponseDTO addUser(UserCreatingDTO userCreatingDTO) {
        log.info("[CREATING] Attempt to create user: {}", userCreatingDTO);
        try {
            UserResponseDTO result = userService.addUser(userCreatingDTO);
            log.info("[SUCCESS] User has been created with ID: {}", result.getId());
            return result;
        } catch (Exception e) {
            log.error("[FAILED] Failed to create user. Reason: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdatingDTO userUpdatingDTO) {
        log.info("[UPDATING] Attempt to update user with ID: {}", id);
        try {
            UserResponseDTO result = userService.updateUser(id, userUpdatingDTO);
            log.info("[SUCCESS] User with ID: {} has been updated", id);
            return result;
        } catch (Exception e) {
            log.error("[FAILED] Could not update user with ID: {}. Error: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        log.warn("[DELETING] Attempt to delete user with ID: {}", id);
        try {
            userService.delete(id);
            log.info("[SUCCESS] User with ID: {} has been deleted", id);
        } catch (Exception e) {
            log.error("[FAILED] Failed to delete user with ID: {}. Reason: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        log.info("[READING] Attempt to get user from DB with ID: {}", id);
        try {
            UserResponseDTO user = userService.getUserById(id);
            log.info("[SUCCESS] User with ID: {} has been found", id);
            return user;
        } catch (Exception e) {
            log.error("[FAILED] User with ID: {} not found or error occurred: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("[READING] Attempt to get list of users");
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            log.info("[SUCCESS] Fetched {} users from DB", users.size());
            return users;
        } catch (Exception e) {
            log.error("[FAILED] Could not retrieve user list. Error: {}", e.getMessage());
            throw e;
        }
    }
}