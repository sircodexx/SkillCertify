package com.skillcert.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillcert.backend.dto.AnswerDTO;
import com.skillcert.backend.dto.AttemptDTO;
import com.skillcert.backend.dto.AttemptResultDTO;
import com.skillcert.backend.entity.Attempt;
import com.skillcert.backend.entity.AttemptAnswer;
import com.skillcert.backend.repository.AttemptAnswerRepository;
import com.skillcert.backend.repository.AttemptRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AttemptServiceImpl implements AttemptService {

    private static final Logger logger = LoggerFactory.getLogger(AttemptServiceImpl.class);

    @Autowired
    private AttemptRepository repo;
    @Autowired
    private AttemptAnswerRepository answerRepo;

    @Override
    public AttemptDTO createAttempt(AttemptDTO dto) {
        logger.info("Creating attempt for user: {}", dto.getUserId());
        return toDTO(repo.save(toEntity(dto)));
    }

    @Override
    public AttemptDTO getAttemptById(Long id) {
    return repo.findById(id)
        .map(this::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Attempt no encontrado con id: " + id));
    }
  
    @Override
    public AttemptDTO updateAttempt(Long id, AttemptDTO dto) {
        Attempt at = repo.findById(id).orElseThrow();
        at.setEndTime(dto.getEndTime());
        at.setScore(dto.getScore());
        return toDTO(repo.save(at));
    }

    @Override
    @Transactional
    public void submitAttempt(Long id) {
        Attempt at = repo.findById(id).orElseThrow();
        at.setSubmitted(true);
        at.setEndTime(java.time.LocalDateTime.now());
        repo.save(at);
    }

    @Override
    public AttemptResultDTO getResults(Long id) {
        Attempt at = repo.findById(id).orElseThrow();
        List<AttemptAnswer> answers = answerRepo.findByAttemptId(id);

        AttemptResultDTO result = new AttemptResultDTO();
        result.setAttemptId(at.getId());
        result.setScore(at.getScore());
        result.setCorrectAnswers(answers.stream()
            .map(a -> "P" + a.getQuestionId() + ": " + a.getAnswer())
            .collect(Collectors.toList())
        );
        return result;
    }

   @Override
    public void saveAnswer(Long attemptId, AnswerDTO answerDTO) {
        Attempt attempt = repo.findById(attemptId).orElseThrow();
        AttemptAnswer answer = new AttemptAnswer();
        answer.setAttempt(attempt);
        answer.setQuestionId(answerDTO.getQuestionId());
        answer.setAnswer(answerDTO.getAnswer());
        answerRepo.save(answer);
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
