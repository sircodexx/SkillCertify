package com.skillcert.backend.controller;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.service.UserAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final UserAnswerService userAnswerService;

    @PostMapping
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<UserAnswerResponse> saveAnswer(
            @Valid @RequestBody UserAnswerRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userAnswerService.saveAnswer(request, userEmail));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<UserAnswerResponse> updateAnswer(
            @PathVariable Long id,
            @Valid @RequestBody UserAnswerRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userAnswerService.updateAnswer(id, request, userEmail));
    }

    @GetMapping("/attempt/{attemptId}")
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<List<UserAnswerResponse>> getAnswersByAttempt(
            @PathVariable Long attemptId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userAnswerService.getAnswersByAttempt(attemptId, userEmail));
    }

    @PostMapping("/{id}/flag")
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<UserAnswerResponse> flagAnswer(
            @PathVariable Long id,
            @Valid @RequestBody FlagRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userAnswerService.flagAnswer(id, request, userEmail));
    }

    @DeleteMapping("/{id}/flag")
    @PreAuthorize("hasRole('NORMAL')")
    public ResponseEntity<UserAnswerResponse> unflagAnswer(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userAnswerService.unflagAnswer(id, userEmail));
    }

    @GetMapping("/attempt/{attemptId}/summary")
@PreAuthorize("hasRole('NORMAL')")
public ResponseEntity<AttemptAnswersResponse> getAttemptAnswersSummary(
        @PathVariable Long attemptId,
        Authentication authentication) {
    String userEmail = authentication.getName();
    return ResponseEntity.ok(userAnswerService.getAttemptAnswersSummary(attemptId, userEmail));
}

@GetMapping("/{answerId}/details")
@PreAuthorize("hasRole('NORMAL')")
public ResponseEntity<AnswerResultDto> getAnswerDetails(
        @PathVariable Long answerId,
        Authentication authentication) {
    String userEmail = authentication.getName();
    return ResponseEntity.ok(userAnswerService.getAnswerDetails(answerId, userEmail));
}
}