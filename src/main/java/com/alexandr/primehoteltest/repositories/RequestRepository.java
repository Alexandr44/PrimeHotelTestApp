package com.alexandr.primehoteltest.repositories;

import com.alexandr.primehoteltest.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

}
