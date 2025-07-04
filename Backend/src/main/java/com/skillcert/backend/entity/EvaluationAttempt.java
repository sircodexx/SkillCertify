package com.skillcert.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.security.cert.Certificate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluation_attempts")
@Data
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

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;

    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes;

    private Integer score = 0;

    @Column(name = "max_score")
    private Integer maxScore = 0;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage = BigDecimal.ZERO;

    private Boolean passed = false;

    private Boolean certified = false;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    // Relaciones
    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers;

    @OneToOne(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Certificate certificate;

    @PreUpdate
    public void preUpdate() {
        if (this.status == AttemptStatus.COMPLETED && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public enum AttemptStatus {
        IN_PROGRESS, COMPLETED, ABANDONED, EXPIRED
    }
}