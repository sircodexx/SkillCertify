package com.skillcert.backend.controller;

import com.skillcert.backend.entity.Result;
import com.skillcert.backend.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {
    private final ResultRepository resultRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Result>> getResultsByUser(@PathVariable Long userId) {
        List<Result> results = resultRepository.findByUserId(userId);
        return ResponseEntity.ok(results);
    }
}

