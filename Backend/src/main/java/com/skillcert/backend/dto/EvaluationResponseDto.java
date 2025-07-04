package com.skillcert.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de evaluación
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDto {
    
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private Integer durationMinutes;
    private String difficulty;
    private Integer passingScore;
    private LocalDateTime assignedAt;
    private String status;
    
    public EvaluationResponseDto(com.skillcert.backend.entity.Evaluation evaluation, LocalDateTime assignedAt, String status) {
        this.id = evaluation.getId();
        this.title = evaluation.getTitle();
        this.description = evaluation.getDescription();
        this.categoryName = evaluation.getCategory().getName();
        this.durationMinutes = evaluation.getDurationMinutes();
        this.difficulty = evaluation.getDifficulty().name();
        this.passingScore = evaluation.getPassingScore();
        this.assignedAt = assignedAt;
        this.status = status;
    }
}