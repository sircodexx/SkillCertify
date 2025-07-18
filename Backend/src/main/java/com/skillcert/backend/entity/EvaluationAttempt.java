package com.skillcert.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_attempts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    @Column(nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;

    @CreationTimestamp
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;
    private Integer timeSpentMinutes;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer score = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer maxScore = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Double percentage = 0.0;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean passed = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean certified = false;
    
    private String ipAddress;
    private String userAgent;
}

