package com.alexandr.primehoteltest.repositories;

import com.alexandr.primehoteltest.models.entities.Request;
import com.alexandr.primehoteltest.models.enums.RequestStatus;
import com.alexandr.primehoteltest.models.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

    List<Request> findAllByStatusAndUserStatus(RequestStatus requestStatus, UserStatus userStatus);

    @Query("select r from requests r left join fetch r.user u WHERE u.id IN (:ids)")
    List<Request> findAllByUserIds(List<UUID> ids);

    List<Request> findAllByUserId(UUID id);

}
