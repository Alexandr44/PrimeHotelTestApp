package com.alexandr.primehoteltest.models.dtos;

import com.alexandr.primehoteltest.models.enums.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {

    private UUID id;

    private String name;

    private String description;

    private UUID userId;

    private RequestStatus status;

}
