package com.skillcert.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDto {

    private Long questionId;
    private String questionText;
    private String questionType;
    private Integer points;
    private Boolean isCorrect;
    private Integer pointsEarned;
    private String userAnswer;
    private String correctAnswer;
    private String explanation;
    private List<OptionResultDto> options;
}