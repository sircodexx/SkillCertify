package com.skillcert.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillcert.backend.dto.AnswerDTO;
import com.skillcert.backend.dto.AttemptDTO;
import com.skillcert.backend.dto.AttemptResultDTO;
import com.skillcert.backend.entity.Attempt;
import com.skillcert.backend.repository.AttemptRepository;

@Service
public class AttemptServiceImpl implements AttemptService {

    @Autowired
    private AttemptRepository repo;

    @Override
    public AttemptDTO createAttempt(AttemptDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    @Override
    public AttemptDTO getAttemptById(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public AttemptDTO updateAttempt(Long id, AttemptDTO dto) {
        Attempt at = repo.findById(id).orElseThrow();
        at.setEndTime(dto.getEndTime());
        at.setScore(dto.getScore());
        return toDTO(repo.save(at));
    }

    @Override
    public void submitAttempt(Long id) {
        Attempt at = repo.findById(id).orElseThrow();
        at.setSubmitted(true);
        at.setEndTime(java.time.LocalDateTime.now());
        repo.save(at);
    }

    @Override
    public AttemptResultDTO getResults(Long id) {
        Attempt at = repo.findById(id).orElseThrow();
        AttemptResultDTO result = new AttemptResultDTO();
        result.setAttemptId(at.getId());
        result.setScore(at.getScore());
        result.setCorrectAnswers(List.of("Respuesta 1", "Respuesta 2")); // Simulado
        return result;
    }

    @Override
    public void saveAnswer(Long attemptId, AnswerDTO answerDTO) {
        System.out.println("Guardando respuesta: " + answerDTO.getAnswer() + " para la pregunta " + answerDTO.getQuestionId());
        // Aquí se conectaría a una entidad real: AttemptAnswer
    }

    @Override
    public List<AttemptDTO> getAttemptsByUserId(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AttemptDTO toDTO(Attempt e) {
        AttemptDTO dto = new AttemptDTO();
        dto.setId(e.getId());
        dto.setUserId(e.getUserId());
        dto.setEvaluationId(e.getEvaluationId());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        dto.setSubmitted(e.getSubmitted());
        dto.setScore(e.getScore());
        return dto;
    }

    private Attempt toEntity(AttemptDTO dto) {
        Attempt e = new Attempt();
        e.setId(dto.getId());
        e.setUserId(dto.getUserId());
        e.setEvaluationId(dto.getEvaluationId());
        e.setStartTime(dto.getStartTime());
        e.setEndTime(dto.getEndTime());
        e.setSubmitted(dto.getSubmitted());
        e.setScore(dto.getScore());
        return e;
    }
}
