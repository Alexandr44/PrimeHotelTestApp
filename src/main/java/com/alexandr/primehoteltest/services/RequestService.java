package com.alexandr.primehoteltest.services;

import com.alexandr.primehoteltest.mappers.RequestMapper;
import com.alexandr.primehoteltest.models.dtos.RequestDto;
import com.alexandr.primehoteltest.models.dtos.RequestResponse;
import com.alexandr.primehoteltest.models.entities.Request;
import com.alexandr.primehoteltest.models.entities.User;
import com.alexandr.primehoteltest.models.exceptions.RequestDataNotValidException;
import com.alexandr.primehoteltest.repositories.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;

    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper, UserService userService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.requestMapper = requestMapper;
    }

    @Transactional
    public RequestResponse getRequestById(UUID requestId) {
        validateId(requestId, true);
        return requestRepository.findById(requestId)
            .map(requestMapper::mapToResponse)
            .orElseThrow(() -> new RequestDataNotValidException("Request with such id " + requestId + " not found"));
    }

    @Transactional
    public RequestResponse createRequest(RequestDto requestDto) {
        validateDto(requestDto);
        validateId(requestDto.getId(), false);
        User user = validateUserId(requestDto.getUserId());

        Request savedRequest = requestRepository.save(requestMapper.mapToEntity(requestDto, user));
        return requestMapper.mapToResponse(savedRequest, "Request created");
    }

    @Transactional
    public RequestResponse updateRequest(RequestDto requestDto) {
        validateDto(requestDto);
        validateId(requestDto.getId(), true);
        checkIdExistence(requestDto.getId());
        User user = validateUserId(requestDto.getUserId());

        Request savedRequest = requestRepository.save(requestMapper.mapToEntity(requestDto, user));
        return requestMapper.mapToResponse(savedRequest, "Request updated");
    }

    @Transactional
    public RequestResponse deleteRequest(UUID requestId) {
        validateId(requestId, true);
        Request requestToDelete = checkIdExistence(requestId);

        requestRepository.deleteById(requestId);
        return requestMapper.mapToResponse(requestToDelete, "Request removed");
    }

    private void validateDto(RequestDto requestDto) {
        if (requestDto == null) {
            throw new RequestDataNotValidException("Request body could not be null");
        }
    }

    private void validateId(UUID id, boolean shouldHave) {
        if (shouldHave && id == null) {
            throw new RequestDataNotValidException("Request should have a not null id");
        } else if (!shouldHave && id != null) {
            throw new RequestDataNotValidException("New request shouldn't have an id (" + id + ")");
        }
    }

    private User validateUserId(UUID userId) {
        if (userId == null) {
            return null;
        } else {
            return userService.checkIdExistence(userId);
        }
    }

    private Request checkIdExistence(UUID id) {
        Optional<Request> requestOpt = requestRepository.findById(id);
        if (requestOpt.isPresent()) {
            return requestOpt.get();
        } else {
            throw new RequestDataNotValidException("Request with such id " + id + " not found");
        }
    }

}
