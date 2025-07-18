package com.skillcert.backend.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithEvaluationsResponse {
    private Long id;
    private String name;
    private List<EvaluationResponse> evaluations;
}