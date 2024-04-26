package com.alexandr.primehoteltest.mappers;

import com.alexandr.primehoteltest.models.dtos.UserDto;
import com.alexandr.primehoteltest.models.dtos.UserResponse;
import com.alexandr.primehoteltest.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

    User mapToEntity(UserDto userDto);

    @Mapping(target = "message", ignore = true)
    UserResponse mapToResponse(User user);

    UserResponse mapToResponse(User user, String message);

}
