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
public class QuestionOptionResponse {
    private Long id;
    private Long questionId;
    private String optionText;
    private boolean isCorrect;
    private Integer orderIndex;
    private LocalDateTime createdAt;
}