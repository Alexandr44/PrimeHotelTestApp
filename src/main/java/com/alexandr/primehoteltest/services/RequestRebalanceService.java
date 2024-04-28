package com.alexandr.primehoteltest.services;

import com.alexandr.primehoteltest.models.entities.Request;
import com.alexandr.primehoteltest.models.entities.User;
import com.alexandr.primehoteltest.models.enums.RequestStatus;
import com.alexandr.primehoteltest.models.enums.UserStatus;
import com.alexandr.primehoteltest.repositories.RequestRepository;
import com.alexandr.primehoteltest.repositories.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RequestRebalanceService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final Semaphore semaphore = new Semaphore(1);

    public RequestRebalanceService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @SneakyThrows
    @Transactional
    public void rebalanceRequests() {
        if (!semaphore.tryAcquire(1, 5, TimeUnit.MINUTES)) {
            log.info("Could not acquire semaphore for rebalance");
            return;
        }

        log.info("Starting rebalance requests");
        List<Request> requestsToRebalanceList = requestRepository.findAllByStatusAndUserStatus(RequestStatus.OPEN, UserStatus.OFFLINE);
        if (requestsToRebalanceList.isEmpty()) {
            return;
        }
        List<User> onlineUsersList = userRepository.findAllByStatus(UserStatus.ONLINE);
        if (onlineUsersList.isEmpty()) {
            log.error("No online users available!");
            return;
        }
        List<Request> onlineUserRequestsList = requestRepository.findAllByUserIds(onlineUsersList.stream().map(User::getId).toList());

        if (onlineUsersList.size() == 1) {

            requestsToRebalanceList.forEach(request -> request.setUser(onlineUsersList.get(0)));

        } else {

            Map<User, Integer> userToRequestCountMap = new HashMap<>();
            onlineUsersList.forEach(user -> userToRequestCountMap.put(user, 0));
            onlineUserRequestsList.forEach(request ->
                userToRequestCountMap.put(
                    request.getUser(),
                    userToRequestCountMap.get(request.getUser()) + 1
                )
            );
            List<User> sortedList = userToRequestCountMap.entrySet().stream()
                .sorted((o1, o2) -> {
                    int intResult = Integer.compare(o1.getValue(), o2.getValue());
                    if (intResult == 0) {
                        return o1.getKey().getId().compareTo(o2.getKey().getId());
                    } else {
                        return intResult;
                    }
                })
                .map(Map.Entry::getKey)
                .toList();

            int leftIndex = 0;
            int rightIndex = 0;
            User leftUser;
            User rightUser = sortedList.get(0);

            int leftUserCount;
            int rightUserCount = userToRequestCountMap.getOrDefault(rightUser, 0);

            int curMaxRequests = 0;
            for (int requestIndex = 0; requestIndex < requestsToRebalanceList.size(); ) {
                Request request = requestsToRebalanceList.get(requestIndex);
                leftUser = sortedList.get(leftIndex);
                leftUserCount = userToRequestCountMap.getOrDefault(leftUser, 0);

                if (leftIndex < rightIndex) {

                    request.setUser(leftUser);
                    userToRequestCountMap.put(leftUser, userToRequestCountMap.get(leftUser) + 1);
                    requestIndex++;
                    leftIndex++;
                    curMaxRequests = Math.max(userToRequestCountMap.get(leftUser), rightUserCount);

                } else if (leftIndex == rightIndex) {

                    if (rightUserCount < curMaxRequests || curMaxRequests == 0) {
                        if (curMaxRequests != 0) {
                            request.setUser(rightUser);
                            userToRequestCountMap.put(rightUser, userToRequestCountMap.get(rightUser) + 1);
                            requestIndex++;
                        }

                        while (leftUserCount == rightUserCount && rightIndex < sortedList.size() - 1) {
                            rightIndex++;
                            rightUser = sortedList.get(rightIndex);
                            rightUserCount = userToRequestCountMap.getOrDefault(rightUser, 0);
                        }

                    }
                    leftIndex = 0;
                }
            }

        }

        log.info("Rebalance finished");
        semaphore.release();
    }

}
