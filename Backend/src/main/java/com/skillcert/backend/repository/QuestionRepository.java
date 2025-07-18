package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByEvaluationId(Long evaluationId);
}