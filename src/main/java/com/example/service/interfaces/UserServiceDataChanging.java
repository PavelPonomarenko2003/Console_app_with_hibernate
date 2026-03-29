package com.example.service.interfaces;

import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;

public interface UserServiceDataChanging {

    UserResponseDTO addUser(UserCreatingDTO userCreatingDTO);

    UserResponseDTO updateUser(Long id, UserUpdatingDTO userUpdatingDTO);

    void delete(Long id);

}
