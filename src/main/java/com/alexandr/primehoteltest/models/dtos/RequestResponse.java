package com.alexandr.primehoteltest.models.dtos;

import com.alexandr.primehoteltest.mappers.Default;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestResponse {

    private final RequestDto request;

    private final String message;

    @Default
    public RequestResponse(RequestDto request, String message) {
        this.request = request;
        this.message = message;
    }

    public RequestResponse(String message) {
        this.request = null;
        this.message = message;
    }
}
