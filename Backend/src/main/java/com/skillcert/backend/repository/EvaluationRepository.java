package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCategoryId(Long categoryId);
    List<Evaluation> findByAssignedUser_Id(Long userId);
    List<Evaluation> findByCenter_Id(Long centerId);
}