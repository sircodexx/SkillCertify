package com.skillcert.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private EvaluationAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    @Column(name = "text_answer", columnDefinition = "TEXT")
    private String textAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @Column(name = "points_earned")
    private Integer pointsEarned = 0;

    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds;

    private Boolean flagged = false;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt = LocalDateTime.now();
}