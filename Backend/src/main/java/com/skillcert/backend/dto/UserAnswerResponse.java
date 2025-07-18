package com.skillcert.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerResponse {
    private Long id;
    private Long attemptId;
    private Long questionId;
    private Long selectedOptionId;
    private String textAnswer;
    private Boolean isCorrect;
    private Integer pointsEarned;
    private Integer timeSpentSeconds;
    private Boolean flagged;
    private LocalDateTime answeredAt;
}