package com.alexandr.primehoteltest.models.exceptions;

public class RequestDataNotValidException extends RuntimeException {

    public RequestDataNotValidException(String message) {
        super(message);
    }

}
