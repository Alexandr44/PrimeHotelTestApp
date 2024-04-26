package com.alexandr.primehoteltest.models.entities;

import com.alexandr.primehoteltest.models.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private UserStatus status;

}
