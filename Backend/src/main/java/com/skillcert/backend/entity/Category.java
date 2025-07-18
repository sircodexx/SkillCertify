package com.skillcert.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import java.util.List;
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "order_index") // Mapeado a order_index en DB
    private Integer orderIndex; // Cambiado de 'order' a 'orderIndex'

    private String color;
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_category_id")
    private Category prerequisiteCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)

    
    private List<Evaluation> evaluations;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Método getter para evaluations (Lombok a veces tiene problemas con esto)
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }
}