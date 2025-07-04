package com.skillcert.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.CenterEvaluation;
import com.skillcert.backend.entity.CenterEvaluation.CenterEvaluationStatus;

@Repository
public interface CenterEvaluationRepository extends JpaRepository<CenterEvaluation, Long> {
    List<CenterEvaluation> findByCenterIdAndStatus(Long centerId, CenterEvaluationStatus status);
    boolean existsByCenterIdAndEvaluationId(Long centerId, Long evaluationId);
}