package com.alexandr.primehoteltest.services;

import com.alexandr.primehoteltest.mappers.UserMapper;
import com.alexandr.primehoteltest.models.dtos.UserDto;
import com.alexandr.primehoteltest.models.dtos.UserResponse;
import com.alexandr.primehoteltest.models.entities.User;
import com.alexandr.primehoteltest.models.exceptions.RequestDataNotValidException;
import com.alexandr.primehoteltest.models.exceptions.UserDataNotValidException;
import com.alexandr.primehoteltest.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse getUserById(UUID userId) {
        validateId(userId, true);
        return userRepository.findById(userId)
            .map(userMapper::mapToResponse)
            .orElseThrow(() -> new UserDataNotValidException("User with such id " + userId + " not found"));
    }

    @Transactional
    public UserResponse createUser(UserDto userDto) {
        validateDto(userDto);
        validateId(userDto.getId(), false);
        checkLoginExistence(userDto.getLogin());

        User savedUser = userRepository.save(userMapper.mapToEntity(userDto));
        return userMapper.mapToResponse(savedUser, "User created");
    }

    @Transactional
    public UserResponse updateUser(UserDto userDto) {
        validateDto(userDto);
        validateId(userDto.getId(), true);
        User existentUser = checkIdExistence(userDto.getId());
        if (!existentUser.getLogin().equals(userDto.getLogin())) {
            checkLoginNotTaken(userDto.getLogin(), userDto.getId());
        }

        User savedUser = userRepository.save(userMapper.mapToEntity(userDto));
        return userMapper.mapToResponse(savedUser, "User updated");
    }

    @Transactional
    public UserResponse deleteUser(UUID userId) {
        validateId(userId, true);
        User userToDelete = checkIdExistence(userId);

        userRepository.deleteById(userId);
        return userMapper.mapToResponse(userToDelete, "User removed");
    }

    private void validateDto(UserDto userDto) {
        if (userDto == null) {
            throw new UserDataNotValidException("Request body could not be null");
        }
    }

    private void validateId(UUID id, boolean shouldHave) {
        if (shouldHave && id == null) {
            throw new UserDataNotValidException("User should have a not null id");
        } else if (!shouldHave && id != null) {
            throw new UserDataNotValidException("New user shouldn't have an id (" + id + ")");
        }
    }

    private User checkIdExistence(UUID id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new RequestDataNotValidException("User with such id " + id + " not found");
        }
    }

    private void checkLoginExistence(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new UserDataNotValidException("User with such login " + login + " already exists");
        }
    }

    private void checkLoginNotTaken(String login, UUID userId) {
        if (userRepository.existsByLoginAndIdNot(login, userId)) {
            throw new UserDataNotValidException("Another user with such login " + login + " already exists");
        }
    }

}
