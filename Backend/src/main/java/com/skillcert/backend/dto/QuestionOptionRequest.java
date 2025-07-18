package com.skillcert.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionRequest {
    @NotBlank
    private String optionText;
    
    private boolean isCorrect = false;
    private Integer orderIndex;
}