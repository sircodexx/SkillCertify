package com.skillcert.backend.repository;

import com.skillcert.backend.entity.EvaluationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationAttemptRepository extends JpaRepository<EvaluationAttempt, Long> {
    List<EvaluationAttempt> findByUserId(Long userId);
    List<EvaluationAttempt> findByEvaluationId(Long evaluationId);
}