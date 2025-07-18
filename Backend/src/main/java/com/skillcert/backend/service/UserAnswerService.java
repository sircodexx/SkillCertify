package com.skillcert.backend.service;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.entity.*;
import com.skillcert.backend.exception.ResourceNotFoundException;
import com.skillcert.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;
    private final EvaluationAttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;

    @Transactional
    public UserAnswerResponse saveAnswer(UserAnswerRequest request, String userEmail) {
        EvaluationAttempt attempt = attemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));
        
        // Verificar que el intento pertenece al usuario
        if (!attempt.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to modify this attempt");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        QuestionOption selectedOption = null;
        if (request.getSelectedOptionId() != null) {
            selectedOption = optionRepository.findById(request.getSelectedOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option not found"));
        }

        // Verificar si ya existe una respuesta para esta pregunta en este intento
        userAnswerRepository.findByAttemptIdAndQuestionId(request.getAttemptId(), request.getQuestionId())
                .ifPresent(answer -> {
                    throw new IllegalStateException("Answer for this question already exists in this attempt");
                });

        // Calcular si la respuesta es correcta
        boolean isCorrect = isAnswerCorrect(question, selectedOption, request.getTextAnswer());
        int pointsEarned = isCorrect ? question.getPoints() : 0;

        UserAnswer answer = UserAnswer.builder()
                .attempt(attempt)
                .question(question)
                .selectedOption(selectedOption)
                .textAnswer(request.getTextAnswer())
                .isCorrect(isCorrect)
                .pointsEarned(pointsEarned)
                .timeSpentSeconds(request.getTimeSpentSeconds())
                .flagged(false)
                .build();

        UserAnswer savedAnswer = userAnswerRepository.save(answer);
        updateAttemptStatistics(attempt);
        
        return mapToResponse(savedAnswer);
    }

    @Transactional
    public UserAnswerResponse updateAnswer(Long id, UserAnswerRequest request, String userEmail) {
        UserAnswer answer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        // Verificar que la respuesta pertenece al usuario
        if (!answer.getAttempt().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to modify this answer");
        }

        if (request.getSelectedOptionId() != null) {
            QuestionOption selectedOption = optionRepository.findById(request.getSelectedOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option not found"));
            answer.setSelectedOption(selectedOption);
        }

        if (request.getTextAnswer() != null) {
            answer.setTextAnswer(request.getTextAnswer());
        }

        if (request.getTimeSpentSeconds() != null) {
            answer.setTimeSpentSeconds(request.getTimeSpentSeconds());
        }

        // Recalcular si la respuesta es correcta
        boolean isCorrect = isAnswerCorrect(answer.getQuestion(), answer.getSelectedOption(), answer.getTextAnswer());
        answer.setIsCorrect(isCorrect);
        answer.setPointsEarned(isCorrect ? answer.getQuestion().getPoints() : 0);

        UserAnswer updatedAnswer = userAnswerRepository.save(answer);
        updateAttemptStatistics(answer.getAttempt());
        
        return mapToResponse(updatedAnswer);
    }

    @Transactional(readOnly = true)
    public List<UserAnswerResponse> getAnswersByAttempt(Long attemptId, String userEmail) {
        EvaluationAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));

        if (!attempt.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to view these answers");
        }

        return userAnswerRepository.findByAttemptId(attemptId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public UserAnswerResponse flagAnswer(Long id, FlagRequest request, String userEmail) {
        UserAnswer answer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        if (!answer.getAttempt().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to flag this answer");
        }

        answer.setFlagged(true);
        UserAnswer flaggedAnswer = userAnswerRepository.save(answer);
        return mapToResponse(flaggedAnswer);
    }

    @Transactional
    public UserAnswerResponse unflagAnswer(Long id, String userEmail) {
        UserAnswer answer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        if (!answer.getAttempt().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to unflag this answer");
        }

        answer.setFlagged(false);
        UserAnswer unflaggedAnswer = userAnswerRepository.save(answer);
        return mapToResponse(unflaggedAnswer);
    }

    private boolean isAnswerCorrect(Question question, QuestionOption selectedOption, String textAnswer) {
        return switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE -> selectedOption != null && selectedOption.isCorrect();
            case TRUE_FALSE -> selectedOption != null && selectedOption.isCorrect();
            case OPEN_TEXT -> textAnswer != null && 
                question.getOptions().stream()
                    .anyMatch(opt -> opt.isCorrect() && opt.getOptionText().equalsIgnoreCase(textAnswer));
        };
    }

    private void updateAttemptStatistics(EvaluationAttempt attempt) {
        List<UserAnswer> answers = userAnswerRepository.findByAttemptId(attempt.getId());
        
        int totalScore = answers.stream()
                .mapToInt(UserAnswer::getPointsEarned)
                .sum();
        
        int maxScore = answers.stream()
                .mapToInt(answer -> answer.getQuestion().getPoints())
                .sum();
        
        double percentage = maxScore > 0 ? (totalScore * 100.0) / maxScore : 0;
        boolean passed = percentage >= attempt.getEvaluation().getPassingScore();

        attempt.setScore(totalScore);
        attempt.setMaxScore(maxScore);
        attempt.setPercentage(percentage);
        attempt.setPassed(passed);
        
        attemptRepository.save(attempt);
    }

    private UserAnswerResponse mapToResponse(UserAnswer answer) {
    return UserAnswerResponse.builder()
            .id(answer.getId())
            .attemptId(answer.getAttempt().getId())
            .questionId(answer.getQuestion().getId())
            .selectedOptionId(answer.getSelectedOption() != null ? 
                answer.getSelectedOption().getId() : null)
            .textAnswer(answer.getTextAnswer())
            .isCorrect(answer.getIsCorrect())
            .pointsEarned(answer.getPointsEarned())
            .timeSpentSeconds(answer.getTimeSpentSeconds())
            .flagged(answer.getFlagged())
            .answeredAt(answer.getAnsweredAt())
            .build();
}

public AttemptAnswersResponse getAttemptAnswersSummary(Long attemptId, String userEmail) {
    EvaluationAttempt attempt = attemptRepository.findById(attemptId)
            .orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));

    if (!attempt.getUser().getEmail().equals(userEmail)) {
        throw new SecurityException("You don't have permission to view these answers");
    }

    List<UserAnswer> answers = userAnswerRepository.findByAttemptId(attemptId);
    List<UserAnswerResponse> answerResponses = answers.stream()
            .map(this::mapToResponse)
            .toList();

    long totalQuestions = questionRepository.countByEvaluationId(attempt.getEvaluation().getId());
    int correctAnswers = (int) answers.stream().filter(UserAnswer::getIsCorrect).count();


    return AttemptAnswersResponse.builder()
            .attemptId(attempt.getId())
            .evaluationId(attempt.getEvaluation().getId())
            .evaluationTitle(attempt.getEvaluation().getTitle())
            .attemptNumber(attempt.getAttemptNumber())
            .totalQuestions((int) totalQuestions)
            .answeredQuestions(answers.size())
            .correctAnswers(correctAnswers)
            .totalScore(attempt.getScore())
            .maxPossibleScore(attempt.getMaxScore())
            .percentage(attempt.getPercentage())
            .answers(answerResponses)
            .build();
}
        @Transactional(readOnly = true)
    public AnswerResultDto getAnswerDetails(Long answerId, String userEmail) {
        UserAnswer answer = userAnswerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with id: " + answerId));
        
        if (!answer.getAttempt().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You don't have permission to view this answer");
        }

        Question question = answer.getQuestion();
        
        return AnswerResultDto.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .questionPoints(question.getPoints())
                .selectedOptionId(answer.getSelectedOption() != null ? answer.getSelectedOption().getId() : null)
                .selectedOptionText(answer.getSelectedOption() != null ? 
                    answer.getSelectedOption().getOptionText() : null)
                .correctOptionText(getCorrectOptionText(question))
                .textAnswer(answer.getTextAnswer())
                .correctTextAnswer(getCorrectTextAnswer(question))
                .isCorrect(answer.getIsCorrect())
                .pointsEarned(answer.getPointsEarned())
                .explanation(question.getExplanation())
                .flagged(answer.getFlagged())
                .build();
    }

    private String getCorrectOptionText(Question question) {
        if (question.getQuestionType() != QuestionType.OPEN_TEXT) {
            return question.getOptions().stream()
                    .filter(QuestionOption::isCorrect)
                    .findFirst()
                    .map(QuestionOption::getOptionText)
                    .orElse(null);
        }
        return null;
    }

    private String getCorrectTextAnswer(Question question) {
        if (question.getQuestionType() == QuestionType.OPEN_TEXT) {
            return question.getOptions().stream()
                    .filter(QuestionOption::isCorrect)
                    .findFirst()
                    .map(QuestionOption::getOptionText)
                    .orElse(null);
        }
        return null;
    }
}
