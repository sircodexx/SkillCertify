package com.skillcert.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillcert.backend.entity.UserAnswer;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    // Obtener respuestas de un intento
    List<UserAnswer> findByAttemptIdOrderByQuestionId(Long attemptId);

    // Obtener respuesta específica por intento y pregunta
    Optional<UserAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);

    // Contar respuestas correctas de un intento
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.attempt.id = :attemptId AND ua.isCorrect = true")
    Long countCorrectAnswersByAttemptId(@Param("attemptId") Long attemptId);

    // Obtener total de puntos de un intento
    @Query("SELECT SUM(ua.pointsEarned) FROM UserAnswer ua WHERE ua.attempt.id = :attemptId")
    Long getTotalPointsByAttemptId(@Param("attemptId") Long attemptId);

    // Verificar si todas las preguntas han sido respondidas
    @Query("SELECT COUNT(q) FROM Question q WHERE q.evaluation.id = :evaluationId")
    Long countQuestionsByEvaluationId(@Param("evaluationId") Long evaluationId);

    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.attempt.id = :attemptId")
    Long countAnswersByAttemptId(@Param("attemptId") Long attemptId);

    // Eliminar respuestas de un intento
    void deleteByAttemptId(Long attemptId);
}