package com.skillcert.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResultDto {

    private Long attemptId;
    private String evaluationTitle;
    private Integer attemptNumber;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer timeSpentMinutes;
    private Integer score;
    private Integer maxScore;
    private BigDecimal percentage;
    private Boolean passed;
    private Boolean certified;
    private String certificateCode;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private List<QuestionResultDto> questionResults;
}
