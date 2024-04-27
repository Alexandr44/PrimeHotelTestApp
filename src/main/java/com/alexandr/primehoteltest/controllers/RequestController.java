package com.alexandr.primehoteltest.controllers;

import com.alexandr.primehoteltest.models.dtos.RequestDto;
import com.alexandr.primehoteltest.models.dtos.RequestResponse;
import com.alexandr.primehoteltest.models.exceptions.RequestDataNotValidException;
import com.alexandr.primehoteltest.services.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/{request_id}")
    public Mono<ResponseEntity<RequestResponse>> getRequest(
        @PathVariable(value = "request_id") UUID requestId
    ) {
        return Mono.fromCallable(() -> requestId)
            .map(requestService::getRequestById)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping()
    public Mono<ResponseEntity<RequestResponse>> createRequest(
        @RequestBody RequestDto requestDto
    ) {
        return Mono.fromCallable(() -> requestDto)
            .map(requestService::createRequest)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping()
    public Mono<ResponseEntity<RequestResponse>> updateRequest(
        @RequestBody RequestDto requestDto
    ) {
        return Mono.fromCallable(() -> requestDto)
            .map(requestService::updateRequest)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping()
    public Mono<ResponseEntity<RequestResponse>> deleteRequest(
        @RequestParam("request_id") UUID requestId
    ) {
        return Mono.fromCallable(() -> requestId)
            .map(requestService::deleteRequest)
            .map(ResponseEntity::ok)
            .subscribeOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler(RequestDataNotValidException.class)
    private ResponseEntity<RequestResponse> handleRequestException(Exception exception) {
        return ResponseEntity.badRequest().body(new RequestResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RequestResponse> handleException(Exception exception) {
        log.error("Got exception in RequestController: message: {}, cause {}, stacke trace: {}", exception.getMessage(), exception.getCause(), exception.getStackTrace());
        return ResponseEntity.internalServerError().body(new RequestResponse("Something went wrong: " + exception.getMessage()));
    }

}
