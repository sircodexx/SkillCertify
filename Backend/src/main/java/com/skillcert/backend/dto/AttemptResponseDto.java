package com.skillcert.backend.dto;

import com.skillcert.backend.entity.EvaluationAttempt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDto {

    private Long id;
    private Long userId;
    private String userName;
    private Long evaluationId;
    private String evaluationTitle;
    private Integer attemptNumber;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer timeSpentMinutes;
    private Integer score;
    private Integer maxScore;
    private BigDecimal percentage;
    private Boolean passed;
    private Boolean certified;
    private String ipAddress;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private Integer answeredQuestions;

    // Constructor desde entidad
    public AttemptResponseDto(EvaluationAttempt attempt) {
        this.id = attempt.getId();
        this.userId = attempt.getUser().getId();
        this.userName = attempt.getUser().getName();
        this.evaluationId = attempt.getEvaluation().getId();
        this.evaluationTitle = attempt.getEvaluation().getTitle();
        this.attemptNumber = attempt.getAttemptNumber();
        this.status = attempt.getStatus().name();
        this.startedAt = attempt.getStartedAt();
        this.completedAt = attempt.getCompletedAt();
        this.timeSpentMinutes = attempt.getTimeSpentMinutes();
        this.score = attempt.getScore();
        this.maxScore = attempt.getMaxScore();
        this.percentage = attempt.getPercentage();
        this.passed = attempt.getPassed();
        this.certified = attempt.getCertified();
        this.ipAddress = attempt.getIpAddress();
        this.durationMinutes = attempt.getEvaluation().getDurationMinutes();
    }
}
