package com.skillcert.backend.repository;

import com.skillcert.backend.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCategoryId(Long categoryId);
}