package com.alexandr.primehoteltest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RequestRebalanceScheduler {

    private final RequestRebalanceService requestRebalanceService;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Value("${rebalance.scheduler.delay-in-sec}")
    private int rebalanceDelay;
    private ScheduledFuture<?> scheduledFuture = null;

    public RequestRebalanceScheduler(RequestRebalanceService requestRebalanceService) {
        this.requestRebalanceService = requestRebalanceService;
    }

    public void callRequestsRebalance() {
        log.info("Scheduling rebalance of request");
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            log.info("Cancelling previous scheduled rebalance");
            scheduledFuture.cancel(true);
        }
        scheduledFuture = executorService.schedule(requestRebalanceService::rebalanceRequests, rebalanceDelay, TimeUnit.SECONDS);
    }

}
