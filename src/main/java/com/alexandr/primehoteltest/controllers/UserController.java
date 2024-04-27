package com.alexandr.primehoteltest.controllers;

import com.alexandr.primehoteltest.models.dtos.UserDto;
import com.alexandr.primehoteltest.models.dtos.UserResponse;
import com.alexandr.primehoteltest.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public Mono<ResponseEntity<UserResponse>> getUser(
        @PathVariable(value = "user_id") UUID userId
    ) {
        return Mono.fromCallable(() -> userId)
            .map(userService::getUserById)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping()
    public Mono<ResponseEntity<UserResponse>> createUser(
        @RequestBody UserDto userDto
    ) {
        return Mono.fromCallable(() -> userDto)
            .map(userService::createUser)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping()
    public Mono<ResponseEntity<UserResponse>> updateUser(
        @RequestBody UserDto userDto
    ) {
        return Mono.fromCallable(() -> userDto)
            .map(userService::updateUser)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping()
    public Mono<ResponseEntity<UserResponse>> deleteUser(
        @RequestParam("user_id") UUID userId
    ) {
        return Mono.fromCallable(() -> userId)
            .map(userService::deleteUser)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

}
