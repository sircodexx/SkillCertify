package com.skillcert.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptAnswersResponse {
    private Long attemptId;
    private Long evaluationId;
    private String evaluationTitle;
    private Integer attemptNumber;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Integer correctAnswers;
    private Integer totalScore;
    private Integer maxPossibleScore;
    private Double percentage;
    private List<UserAnswerResponse> answers;
}