package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByEvaluationId(Long evaluationId);
    @Query("SELECT COUNT(q) FROM Question q WHERE q.evaluation.id = :evaluationId")
    long countByEvaluationId(@Param("evaluationId") Long evaluationId);
}