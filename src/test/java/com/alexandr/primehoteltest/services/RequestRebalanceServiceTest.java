package com.alexandr.primehoteltest.services;

import com.alexandr.primehoteltest.models.enums.UserStatus;
import com.alexandr.primehoteltest.repositories.RequestRepository;
import com.alexandr.primehoteltest.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RequestRebalanceServiceTest {

    private static final String USER_ID_1 = "b341d48a-c0c6-46d2-9941-ad3625840e31";
    private static final String USER_ID_2 = "b341d48a-c0c6-46d2-9941-ad3625840e32";
    private static final String USER_ID_3 = "b341d48a-c0c6-46d2-9941-ad3625840e33";
    private static final String USER_ID_4 = "b341d48a-c0c6-46d2-9941-ad3625840e34";
    private static final String USER_ID_5 = "b341d48a-c0c6-46d2-9941-ad3625840e35";
    private static final String REQUEST_ID_1 = "3a259982-0748-4d9e-a003-3dca936cbb01";
    private static final String REQUEST_ID_2 = "3a259982-0748-4d9e-a003-3dca936cbb02";
    private static final String REQUEST_ID_3 = "3a259982-0748-4d9e-a003-3dca936cbb03";
    private static final String REQUEST_ID_4 = "3a259982-0748-4d9e-a003-3dca936cbb04";
    private static final String REQUEST_ID_5 = "3a259982-0748-4d9e-a003-3dca936cbb05";
    private static final String REQUEST_ID_6 = "3a259982-0748-4d9e-a003-3dca936cbb06";
    private static final String REQUEST_ID_7 = "3a259982-0748-4d9e-a003-3dca936cbb07";
    private static final String REQUEST_ID_8 = "3a259982-0748-4d9e-a003-3dca936cbb08";
    private static final String REQUEST_ID_9 = "3a259982-0748-4d9e-a003-3dca936cbb09";
    private static final String REQUEST_ID_10 = "3a259982-0748-4d9e-a003-3dca936cbb10";


@Autowired
TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    private RequestRebalanceService requestRebalanceService;

    @BeforeEach
    public void init() {
        requestRebalanceService = new RequestRebalanceService(
            userRepository, requestRepository
        );
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_1 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_1 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_1 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_1 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_1 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_1 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_1 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_AllOnline() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(10, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (1, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (1, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_1 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_1 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_1 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_1 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_1 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_1 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_1 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_PartlyOnline() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(10, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(5, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(5, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (1, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (1, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (1, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_1 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_1 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_1 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_1 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_1 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_1 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_1 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_AllOnline_PartlyCompleted() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(10, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_2 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_2 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_2 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_4 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_4 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_4 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_5 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_TwoUsersToOffline() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_2)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(4, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_1 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_1 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_1 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_2 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_2 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_2 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_2 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_SecondBusy() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_5)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(6, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(4, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(4, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_1 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_3 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_4 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_4 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_4 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_5 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_5 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_UsersHasIncrementedRequests() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(4, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_2 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_2 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_2 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_3 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_4 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_4 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_4 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_RebalanceBetweenThreeUsers() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_2)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(4, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_1 + "', 'Some full name 1', 'A login 1');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_2 + "', 'Some full name 2', 'A login 2');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_3 + "', 'Some full name 3', 'A login 3');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_4 + "', 'Some full name 4', 'A login 4');",
        "INSERT INTO users (status, id, full_name, login) VALUES (0, '" + USER_ID_5 + "', 'Some full name 5', 'A login 5');",

        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_1 + "', '" + USER_ID_1 + "', 'Some full description 1', 'A name 1');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_2 + "', '" + USER_ID_1 + "', 'Some full description 2', 'A name 2');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_3 + "', '" + USER_ID_1 + "', 'Some full description 3', 'A name 3');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_4 + "', '" + USER_ID_2 + "', 'Some full description 4', 'A name 4');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_5 + "', '" + USER_ID_2 + "', 'Some full description 5', 'A name 5');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_6 + "', '" + USER_ID_2 + "', 'Some full description 6', 'A name 6');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_7 + "', '" + USER_ID_3 + "', 'Some full description 7', 'A name 7');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_8 + "', '" + USER_ID_3 + "', 'Some full description 8', 'A name 8');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_9 + "', '" + USER_ID_4 + "', 'Some full description 9', 'A name 9');",
        "INSERT INTO requests (status, id, user_id, description, name) VALUES (0, '" + REQUEST_ID_10 + "', '" + USER_ID_5 + "', 'Some full description 10', 'A name 10');"
    })
    public void rebalanceTest_OneUserOnline() {
        userRepository.findById(UUID.fromString(USER_ID_1)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_2)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_3)).get().setStatus(UserStatus.OFFLINE);
        userRepository.findById(UUID.fromString(USER_ID_4)).get().setStatus(UserStatus.OFFLINE);

        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(3, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(2, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(1, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());

        requestRebalanceService.rebalanceRequests();

        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_1)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_2)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_3)).size());
        Assertions.assertEquals(0, requestRepository.findAllByUserId(UUID.fromString(USER_ID_4)).size());
        Assertions.assertEquals(10, requestRepository.findAllByUserId(UUID.fromString(USER_ID_5)).size());
    }


}
