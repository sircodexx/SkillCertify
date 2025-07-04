package com.skillcert.backend.entity;

import jakarta.persistence.*;

@Entity
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

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    // Getters y setters (puedes usar Lombok si lo prefieres)

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public Integer getPassingScore() { return passingScore; }
    public Category getCategory() { return category; }
    public Difficulty getDifficulty() { return difficulty; }
}