package com.skillcert.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationDifficulty difficulty;

    @Builder.Default
    @Column(nullable = false)
    private Integer passingScore = 70;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationStatus status = EvaluationStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_evaluation_id")
    private Evaluation prerequisiteEvaluation;

    @Builder.Default
    @Column(nullable = false)
    private Integer maxAttempts = 3;


    @Builder.Default
    @Column(nullable = false)
    private Boolean timeLimitEnabled = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean shuffleQuestions = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean showResultsImmediately = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}