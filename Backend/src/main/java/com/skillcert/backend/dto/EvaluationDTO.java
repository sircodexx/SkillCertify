package com.skillcert.backend.dto;

import lombok.Data;

@Data
public class EvaluationDTO {
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private String status;
    private Integer durationMinutes;
    private Integer passingScore;
    private String difficulty;
}

