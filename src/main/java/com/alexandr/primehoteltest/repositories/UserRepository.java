package com.alexandr.primehoteltest.repositories;

import com.alexandr.primehoteltest.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByLogin(String login);

    boolean existsByLoginAndIdNot(String login, UUID id);

}
