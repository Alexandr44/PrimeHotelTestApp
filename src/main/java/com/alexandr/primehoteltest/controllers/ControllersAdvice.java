package com.alexandr.primehoteltest.controllers;

import com.alexandr.primehoteltest.models.dtos.RequestResponse;
import com.alexandr.primehoteltest.models.exceptions.RequestDataNotValidException;
import com.alexandr.primehoteltest.models.exceptions.UserDataNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllersAdvice {

    @ExceptionHandler(UserDataNotValidException.class)
    private ResponseEntity<RequestResponse> handleUserException(Exception exception) {
        return ResponseEntity.badRequest().body(new RequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(RequestDataNotValidException.class)
    private ResponseEntity<RequestResponse> handleRequestException(Exception exception) {
        return ResponseEntity.badRequest().body(new RequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RequestResponse> handleException(Exception exception) {
        log.error("Got exception in UserController: message: {}, cause {}, stacke trace: {}", exception.getMessage(), exception.getCause(), exception.getStackTrace());
        return ResponseEntity.internalServerError().body(new RequestResponse("Something went wrong: " + exception.getMessage()));
    }

}
