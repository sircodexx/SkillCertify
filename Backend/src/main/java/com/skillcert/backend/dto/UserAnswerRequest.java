package com.skillcert.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerRequest {
    @NotNull(message = "Attempt ID is required")
    @Positive(message = "Attempt ID must be positive")
    private Long attemptId;

    @NotNull(message = "Question ID is required")
    @Positive(message = "Question ID must be positive")
    private Long questionId;

    @Positive(message = "Selected option ID must be positive")
    private Long selectedOptionId;

    private String textAnswer;

    @Positive(message = "Time spent must be positive")
    private Integer timeSpentSeconds;
}