package com.skillcert.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer passingScore;

    @ManyToOne
    private Category category;
    @JoinColumn(name = "prerequisite_evaluation_id")
    private Evaluation prerequisiteEvaluation;

    @Enumerated(EnumType.STRING)
    private EvaluationStatus status;
    private Difficulty difficulty;
    private Integer maxAttempts;
    private Boolean timeLimitEnabled;

    public enum EvaluationStatus {
        ACTIVE, INACTIVE
    }
    public Integer getMaxAttempts() {
    return maxAttempts;
    }
    public Evaluation getPrerequisiteEvaluation() {
    return prerequisiteEvaluation;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public Integer getPassingScore() { return passingScore; }
    public Category getCategory() { return category; }
    public Difficulty getDifficulty() { return difficulty; }
    public EvaluationStatus getStatus() {return status; }
}