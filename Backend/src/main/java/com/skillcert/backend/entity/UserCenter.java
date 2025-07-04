package com.skillcert.backend.entity;

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
public class UserCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Center center;

    private java.time.LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    private UserCenterStatus status;

    public enum UserCenterStatus {
        ACTIVE, INACTIVE
    }
}