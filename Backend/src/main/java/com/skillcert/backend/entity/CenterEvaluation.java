package com.skillcert.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CenterEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Center center;

    @ManyToOne
    private Evaluation evaluation;

    private LocalDateTime assignedAt;

    @Enumerated(EnumType.STRING)
    private CenterEvaluationStatus status;

    public enum CenterEvaluationStatus {
        ACTIVE, INACTIVE
    }
}