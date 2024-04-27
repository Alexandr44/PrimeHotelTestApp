package com.alexandr.primehoteltest.mappers;

import com.alexandr.primehoteltest.models.dtos.RequestDto;
import com.alexandr.primehoteltest.models.dtos.RequestResponse;
import com.alexandr.primehoteltest.models.entities.Request;
import com.alexandr.primehoteltest.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "userId", source = "user.id")
    RequestDto mapToDto(Request request);

    @Mapping(target = "id", source = "requestDto.id")
    @Mapping(target = "status", source = "requestDto.status")
    Request mapToEntity(RequestDto requestDto, User user);

    @Mapping(target = "message", ignore = true)
    RequestResponse mapToResponse(Request request);

    RequestResponse mapToResponse(Request request, String message);

}
