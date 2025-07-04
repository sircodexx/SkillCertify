package com.skillcert.backend.dto;

import com.skillcert.backend.entity.EvaluationAttempt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttemptDto {

    private EvaluationAttempt.AttemptStatus status;
    private Integer timeSpentMinutes;
    private String ipAddress;
    private String userAgent;
}