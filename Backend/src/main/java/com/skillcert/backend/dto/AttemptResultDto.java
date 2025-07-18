package com.skillcert.backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttemptResultDTO {
    private Long attemptId;
    private Double score;
    private List<String> correctAnswers; // Simulación
    // Lombok genera los getters y setters automáticamente
}
