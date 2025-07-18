package com.skillcert.backend.service;

import com.skillcert.backend.dto.EvaluationResponse;
import com.skillcert.backend.entity.Evaluation;
import com.skillcert.backend.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    @Transactional(readOnly = true)
    public List<EvaluationResponse> getByCategoryId(Long categoryId) {
        return evaluationRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Método para mapear Evaluation a EvaluationResponse
    private EvaluationResponse mapToResponse(Evaluation evaluation) {
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .title(evaluation.getTitle())
                .description(evaluation.getDescription())
                .categoryId(evaluation.getCategory().getId())
                .durationMinutes(evaluation.getDurationMinutes())
                .difficulty(evaluation.getDifficulty().name())
                .passingScore(evaluation.getPassingScore())
                .status(evaluation.getStatus().name())
                .maxAttempts(evaluation.getMaxAttempts())
                .timeLimitEnabled(evaluation.getTimeLimitEnabled())
                .shuffleQuestions(evaluation.getShuffleQuestions())
                .showResultsImmediately(evaluation.getShowResultsImmediately())
                .createdAt(evaluation.getCreatedAt())
                .updatedAt(evaluation.getUpdatedAt())
                .build();
    }
}