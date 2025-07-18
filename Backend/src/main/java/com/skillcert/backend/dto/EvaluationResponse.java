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
public class EvaluationResponse {
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private Integer durationMinutes;
    private String difficulty;
    private Integer passingScore;
    private String status;
    private Integer maxAttempts;
    private Boolean timeLimitEnabled;
    private Boolean shuffleQuestions;
    private Boolean showResultsImmediately;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}