package com.example.mapper;

import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
// mapper to convert data to from DB to DTO and opposite
public class UserMapper {

    // converting data of user to entity
    public UserEntity convertToEntityWithCreating(UserCreatingDTO userCreatingDTO) {
        UserEntity user = new UserEntity();
        user.setName(userCreatingDTO.getName());
        user.setEmail(userCreatingDTO.getEmail());
        user.setAge(userCreatingDTO.getAge());
        return user;
    }

    public void convertToEntityWithUpdating(UserUpdatingDTO dto, UserEntity entity) {
        if (dto == null) return;

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setAge(dto.getAge());

    }

    // converting data from db to dto
    public UserResponseDTO convertToResponseDTO(UserEntity user) {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        return userDTO;
    }

    // converting all data that list consists
    public List<UserResponseDTO> toDTOList(List<UserEntity> listOfUsers) {
        if (listOfUsers == null) {
            return List.of();
        }
        return listOfUsers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


}
