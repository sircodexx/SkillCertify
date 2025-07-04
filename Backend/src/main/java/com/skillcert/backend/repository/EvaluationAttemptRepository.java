package com.skillcert.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.EvaluationAttempt;

@Repository
public interface EvaluationAttemptRepository extends JpaRepository<EvaluationAttempt, Long> {

    // Obtener intentos por usuario
    List<EvaluationAttempt> findByUserIdOrderByStartedAtDesc(Long userId);

    // Obtener intentos por usuario y evaluación
    List<EvaluationAttempt> findByUserIdAndEvaluationIdOrderByAttemptNumberDesc(Long userId, Long evaluationId);

    // Obtener el último intento de un usuario para una evaluación
    Optional<EvaluationAttempt> findFirstByUserIdAndEvaluationIdOrderByAttemptNumberDesc(Long userId, Long evaluationId);

    // Contar intentos de un usuario para una evaluación
    Long countByUserIdAndEvaluationId(Long userId, Long evaluationId);

    // Verificar si un usuario tiene un intento en progreso
    @Query("SELECT ea FROM EvaluationAttempt ea WHERE ea.user.id = :userId AND ea.evaluation.id = :evaluationId AND ea.status = 'IN_PROGRESS'")
    Optional<EvaluationAttempt> findInProgressAttempt(@Param("userId") Long userId, @Param("evaluationId") Long evaluationId);

    // Obtener intentos completados de un usuario
    List<EvaluationAttempt> findByUserIdAndStatusOrderByCompletedAtDesc(Long userId, EvaluationAttempt.AttemptStatus status);

    // Obtener intentos por evaluación
    List<EvaluationAttempt> findByEvaluationIdOrderByStartedAtDesc(Long evaluationId);

    // Obtener estadísticas de intentos por evaluación
    @Query("SELECT AVG(ea.percentage) FROM EvaluationAttempt ea WHERE ea.evaluation.id = :evaluationId AND ea.status = 'COMPLETED'")
    Double getAverageScoreByEvaluationId(@Param("evaluationId") Long evaluationId);

    @Query("SELECT COUNT(ea) FROM EvaluationAttempt ea WHERE ea.evaluation.id = :evaluationId AND ea.passed = true")
    Long countPassedAttemptsByEvaluationId(@Param("evaluationId") Long evaluationId);

    @Query("SELECT COUNT(ea) FROM EvaluationAttempt ea WHERE ea.evaluation.id = :evaluationId AND ea.status = 'COMPLETED'")
    Long countCompletedAttemptsByEvaluationId(@Param("evaluationId") Long evaluationId);
}
