package com.skillcert.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.skillcert.backend.entity.QuestionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResultDto {
    private Long questionId;
    private String questionText;
    private QuestionType questionType;
    private Integer questionPoints;
    private Long selectedOptionId;
    private String selectedOptionText;
    private String correctOptionText;
    private String textAnswer;
    private String correctTextAnswer;
    private Boolean isCorrect;
    private Integer pointsEarned;
    private String explanation;
    private Boolean flagged;
}