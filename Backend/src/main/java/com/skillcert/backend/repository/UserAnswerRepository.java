package com.skillcert.backend.repository;

import com.skillcert.backend.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByAttemptId(Long attemptId);
    
    @Query("SELECT ua FROM UserAnswer ua WHERE ua.attempt.id = :attemptId AND ua.question.id = :questionId")
    Optional<UserAnswer> findByAttemptIdAndQuestionId(@Param("attemptId") Long attemptId, 
                                                    @Param("questionId") Long questionId);
    
    @Query("SELECT ua FROM UserAnswer ua WHERE ua.attempt.id = :attemptId AND ua.flagged = true")
    List<UserAnswer> findFlaggedAnswersByAttemptId(@Param("attemptId") Long attemptId);
}