package com.example.service.impl;

import com.example.repository.UserRepository;
import com.example.dto.UserCreatingDTO;
import com.example.dto.UserResponseDTO;
import com.example.dto.UserUpdatingDTO;
import com.example.entity.UserEntity;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.service.interfaces.UserService;
import com.example.service.producer.UserEventPublisher;
import jakarta.transaction.Transactional;
import com.example.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.entity.Action.*;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserResponseDTO addUser(UserCreatingDTO userCreatingDTO) {
        if (userRepository.existsByEmail(userCreatingDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userCreatingDTO.getEmail() + " already exists");
        }
        UserEntity user = userMapper.convertToEntityWithCreating(userCreatingDTO);
        UserEntity saveUser = userRepository.save(user);
        eventPublisher.publishEvent(saveUser.getName(), saveUser.getEmail(), USER_CREATED);
        return userMapper.convertToResponseDTO(saveUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdatingDTO userUpdatingDTO) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Such user has not found"));
        userMapper.convertToEntityWithUpdating(userUpdatingDTO, user);
        eventPublisher.publishEvent(user.getName(), user.getEmail(), USER_UPDATED);
        return userMapper.convertToResponseDTO(user);
    }

    @Override
    public void delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new  UserNotFoundException("Such user has not found"));
        userRepository.delete(user);
        eventPublisher.publishEvent(user.getName(), user.getEmail(), USER_DELETED);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new  UserNotFoundException("Such user has not found"));
        return userMapper.convertToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> listOfUsers = userRepository.findAll();
        return userMapper.toDTOList(listOfUsers);
    }
}
