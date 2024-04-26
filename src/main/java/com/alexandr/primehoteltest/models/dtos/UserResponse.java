package com.alexandr.primehoteltest.models.dtos;

import com.alexandr.primehoteltest.mappers.Default;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {


    private final UserDto user;

    private final String message;

    @Default
    public UserResponse(UserDto user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserResponse(String message) {
        this.user = null;
        this.message = message;
    }
}
