package com.skillcert.backend.dto;
import java.time.LocalDateTime;

import com.skillcert.backend.entity.Center;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de centro
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String region;
    private String phone;
    private String email;
    private String director;
    private Integer capacity;
    private Integer studentsCount;
    private Center.CenterStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor desde entidad
    public CenterResponseDto(Center center) {
        this.id = center.getId();
        this.name = center.getName();
        this.description = center.getDescription();
        this.address = center.getAddress();
        this.city = center.getCity();
        this.region = center.getRegion();
        this.phone = center.getPhone();
        this.email = center.getEmail();
        this.director = center.getDirector();
        this.capacity = center.getCapacity();
        this.studentsCount = center.getStudentsCount();
        this.status = center.getStatus();
        this.createdAt = center.getCreatedAt();
        this.updatedAt = center.getUpdatedAt();
    }
}