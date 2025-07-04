package com.skillcert.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerDto {

    @NotNull(message = "El ID de la pregunta es obligatorio")
    private Long questionId;

    private Long selectedOptionId;
    private String textAnswer;
    private Integer timeSpentSeconds;
    private Boolean flagged = false;
}
