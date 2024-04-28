package com.alexandr.primehoteltest.repositories;

import com.alexandr.primehoteltest.models.entities.User;
import com.alexandr.primehoteltest.models.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByLogin(String login);

    boolean existsByLoginAndIdNot(String login, UUID id);

    List<User> findAllByStatus(UserStatus userStatus);

}
