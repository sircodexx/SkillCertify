package com.skillcert.backend.dto;

import com.skillcert.backend.entity.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private Long evaluationId;
    private String questionText;
    private QuestionType questionType;
    private Integer points;
    private Integer orderIndex;
    private String explanation;
    private QuestionDifficulty difficulty;
    private List<QuestionOptionResponse> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}