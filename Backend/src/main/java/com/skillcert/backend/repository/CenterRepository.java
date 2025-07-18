package com.skillcert.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillcert.backend.entity.Center;

public interface CenterRepository extends JpaRepository<Center, Long> {

}
