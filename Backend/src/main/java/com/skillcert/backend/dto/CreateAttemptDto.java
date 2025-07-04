package com.skillcert.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttemptDto {

    @NotNull(message = "El ID de la evaluación es obligatorio")
    private Long evaluationId;

    private String ipAddress;
    private String userAgent;
}
