package com.skillcert.backend.dto;

import com.skillcert.backend.entity.QuestionDifficulty;
import com.skillcert.backend.entity.QuestionType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotNull
    private Long evaluationId;
    
    @NotBlank
    private String questionText;
    
    @NotNull
    private QuestionType questionType;
    
    @Min(1)
    private Integer points = 1;
    
    private Integer orderIndex;
    private String explanation;
    private QuestionDifficulty difficulty = QuestionDifficulty.MEDIUM;
}