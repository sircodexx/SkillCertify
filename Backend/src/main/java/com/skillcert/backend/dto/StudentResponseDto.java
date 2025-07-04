package com.skillcert.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de estudiante
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private LocalDateTime enrolledAt;
    private String status;
    
    public StudentResponseDto(com.skillcert.backend.entity.User user, LocalDateTime enrolledAt, String status) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.company = user.getCompany();
        this.enrolledAt = enrolledAt;
        this.status = status;
    }
}
