package com.skillcert.backend.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para asignar evaluación a centro
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignEvaluationDto {
    
    @NotNull(message = "El ID de la evaluación es obligatorio")
    private Long evaluationId;
}
