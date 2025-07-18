package com.skillcert.backend.controller;

import com.skillcert.backend.dto.EvaluationDTO;
import com.skillcert.backend.entity.Evaluation;
import com.skillcert.backend.entity.Category;
import com.skillcert.backend.entity.EvaluationStatus;
import com.skillcert.backend.entity.EvaluationDifficulty;
import com.skillcert.backend.repository.EvaluationRepository;
import com.skillcert.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationRepository evaluationRepository;
    private final CategoryRepository categoryRepository;

    private EvaluationDTO toDTO(Evaluation evaluation) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(evaluation.getId());
        dto.setTitle(evaluation.getTitle());
        dto.setDescription(evaluation.getDescription());
        dto.setCategoryId(evaluation.getCategory() != null ? evaluation.getCategory().getId() : null);
        dto.setStatus(evaluation.getStatus() != null ? evaluation.getStatus().name() : null);
        dto.setDurationMinutes(evaluation.getDurationMinutes());
        dto.setPassingScore(evaluation.getPassingScore());
        dto.setDifficulty(evaluation.getDifficulty() != null ? evaluation.getDifficulty().name() : null);
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations(@RequestParam(required = false) Long categoryId) {
        List<Evaluation> evaluations = (categoryId != null)
            ? evaluationRepository.findByCategoryId(categoryId)
            : evaluationRepository.findAll();
        List<EvaluationDTO> dtos = evaluations.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUser(@PathVariable Long userId) {
        List<Evaluation> evaluations = evaluationRepository.findByAssignedUser_Id(userId);
        List<EvaluationDTO> dtos = evaluations.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EvaluationDTO> createEvaluation(@RequestBody EvaluationDTO dto) {
        Evaluation evaluation = new Evaluation();
        evaluation.setTitle(dto.getTitle());
        evaluation.setDescription(dto.getDescription());
        // Asignar la categoría real usando el id recibido en el DTO
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }
        evaluation.setCategory(category);
        evaluation.setStatus(EvaluationStatus.valueOf(dto.getStatus()));
        evaluation.setDurationMinutes(dto.getDurationMinutes());
        evaluation.setPassingScore(dto.getPassingScore());
        evaluation.setDifficulty(EvaluationDifficulty.valueOf(dto.getDifficulty()));
        Evaluation saved = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationDTO dto) {
        Evaluation evaluation = evaluationRepository.findById(id).orElseThrow();
        evaluation.setTitle(dto.getTitle());
        evaluation.setDescription(dto.getDescription());
        // Asignar la categoría real usando el id recibido en el DTO
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }
        evaluation.setCategory(category);
        evaluation.setStatus(EvaluationStatus.valueOf(dto.getStatus()));
        evaluation.setDurationMinutes(dto.getDurationMinutes());
        evaluation.setPassingScore(dto.getPassingScore());
        evaluation.setDifficulty(EvaluationDifficulty.valueOf(dto.getDifficulty()));
        Evaluation updated = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
