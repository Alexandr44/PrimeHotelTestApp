package com.alexandr.primehoteltest.services;

import com.alexandr.primehoteltest.mappers.UserMapper;
import com.alexandr.primehoteltest.models.dtos.UserDto;
import com.alexandr.primehoteltest.models.dtos.UserResponse;
import com.alexandr.primehoteltest.models.entities.User;
import com.alexandr.primehoteltest.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserResponse> getUserById(UUID userId) {
        if (userId == null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User id could not be null"));
        }

        return userRepository.findById(userId)
            .map(user -> ResponseEntity.ok(userMapper.mapToResponse(user)))
            .orElseGet(() -> ResponseEntity.badRequest()
                .body(new UserResponse("User with such id " + userId + " not found"))
            );
    }

    @Transactional
    public ResponseEntity<UserResponse> createUser(UserDto userDto) {
        if (userDto == null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User body could not be null"));
        }

        if (userDto.getId() != null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("New user shouldn't have an id (" + userDto.getId() + ")"));
        }

        if (userRepository.existsByLogin(userDto.getLogin())) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User with such login " + userDto.getLogin() + " already exists"));
        }

        User savedUser;
        try {
            savedUser = userRepository.save(
                userMapper.mapToEntity(userDto)
            );
        } catch (Throwable e) {
            return ResponseEntity.internalServerError()
                .body(new UserResponse(e.getMessage()));
        }

        return ResponseEntity.ok(
            userMapper.mapToResponse(savedUser, "User created")
        );
    }

    @Transactional
    public ResponseEntity<UserResponse> updateUser(UserDto userDto) {
        if (userDto == null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User body could not be null"));
        }

        if (userDto.getId() == null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User should have a not null id"));
        }

        if (!userRepository.existsById(userDto.getId())) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User with such id " + userDto.getId() + " not found"));
        }

        if (userRepository.existsByLoginAndIdNot(userDto.getLogin(), userDto.getId())) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("Another user with such login " + userDto.getLogin() + " already exists"));
        }

        User savedUser;
        try {
            savedUser = userRepository.save(
                userMapper.mapToEntity(userDto)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new UserResponse(e.getMessage()));
        }

        return ResponseEntity.ok(
            userMapper.mapToResponse(savedUser, "User updated")
        );
    }

    @Transactional
    public ResponseEntity<UserResponse> deleteUser(UUID userId) {
        if (userId == null) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User id could not be null"));
        }

        Optional<User> deletedUser = userRepository.findById(userId);
        if (deletedUser.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new UserResponse("User with such id " + userId + " not found"));
        }

        userRepository.deleteById(userId);
        return ResponseEntity.ok(userMapper.mapToResponse(deletedUser.get(), "User removed"));
    }

}
