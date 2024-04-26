package com.alexandr.primehoteltest.models.dtos;

import com.alexandr.primehoteltest.models.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private String login;

    private String fullName;

    private UserStatus status;

}
