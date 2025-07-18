package com.skillcert.backend.controller;

import com.skillcert.backend.entity.Progress;
import com.skillcert.backend.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressRepository progressRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Progress>> getProgressByUser(@PathVariable Long userId) {
        List<Progress> progressList = progressRepository.findByUserId(userId);
        return ResponseEntity.ok(progressList);
    }
}

