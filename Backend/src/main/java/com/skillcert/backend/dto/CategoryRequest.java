package com.skillcert.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Order is required")
    @PositiveOrZero(message = "Order must be positive or zero")
    private Integer order;

    private String color;
    private String icon;

    private Long prerequisiteCategoryId;
}