package com.skillcert.backend.service;

import java.util.List;

import com.skillcert.backend.dto.AnswerDTO;
import com.skillcert.backend.dto.AttemptDTO;
import com.skillcert.backend.dto.AttemptResultDTO;

public interface AttemptService {
    AttemptDTO createAttempt(AttemptDTO dto);
    AttemptDTO getAttemptById(Long id);
    AttemptDTO updateAttempt(Long id, AttemptDTO dto);
    void submitAttempt(Long id);
    AttemptResultDTO getResults(Long id);
    void saveAnswer(Long attemptId, AnswerDTO answerDTO);
    List<AttemptDTO> getAttemptsByUserId(Long userId);
}
