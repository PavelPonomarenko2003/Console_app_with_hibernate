package com.example.service.interfaces;

import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;

import java.util.List;

public interface UserServiceDataReading {

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

}
