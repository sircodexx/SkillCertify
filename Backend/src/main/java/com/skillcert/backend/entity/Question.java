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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    // Tipo de pregunta
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    public enum QuestionType {
        MULTIPLE_CHOICE, TRUE_FALSE, OPEN_TEXT
    }
    
    private Integer points;
}