package com.skillcert.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillcert.backend.dto.AnswerDTO;
import com.skillcert.backend.dto.AttemptDTO;
import com.skillcert.backend.dto.AttemptResultDTO;
import com.skillcert.backend.service.AttemptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    @Autowired
    private AttemptService service;

    @PostMapping
    public AttemptDTO create(@Valid @RequestBody AttemptDTO dto) {
        return service.createAttempt(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttemptDTO> getById(@PathVariable Long id) {
        AttemptDTO attempt = service.getAttemptById(id);
            return ResponseEntity.ok(attempt);
    }
    

    @PutMapping("/{id}")
    public AttemptDTO update(@PathVariable Long id, @RequestBody AttemptDTO dto) {
        return service.updateAttempt(id, dto);
    }

    @PostMapping("/{id}/submit")
    public void submit(@PathVariable Long id) {
        service.submitAttempt(id);
    }

    @GetMapping("/{id}/results")
    public AttemptResultDTO getResults(@PathVariable Long id) {
        return service.getResults(id);
    }

    @PostMapping("/{id}/answers")
    public void saveAnswer(@PathVariable Long id, @RequestBody AnswerDTO dto) {
        service.saveAnswer(id, dto);
    }

    @GetMapping("/user/{userId}")
    public List<AttemptDTO> getByUser(@PathVariable Long userId) {
        return service.getAttemptsByUserId(userId);
    }
}
