package com.skillcert.backend.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer order;
    private String color;
    private String icon;
    private Long prerequisiteCategoryId;
    private String prerequisiteCategoryName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
