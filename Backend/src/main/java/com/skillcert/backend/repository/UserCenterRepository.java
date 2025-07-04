package com.skillcert.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.UserCenter;
import com.skillcert.backend.entity.UserCenter.UserCenterStatus;

@Repository
public interface UserCenterRepository extends JpaRepository<UserCenter, Long> {
    List<UserCenter> findByCenterIdAndStatus(Long centerId, UserCenterStatus status);
}