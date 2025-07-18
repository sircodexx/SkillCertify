package com.skillcert.backend.dto;

import java.util.List;

public class AttemptResultDTO {
    
    private Long attemptId;
    private Double score;
    private List<String> correctAnswers; // Simulación

    public Long getAttemptId() {
        return attemptId;
    }
    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }
    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }
    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
  
}
