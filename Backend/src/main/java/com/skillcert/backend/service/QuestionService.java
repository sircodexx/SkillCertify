package com.skillcert.backend.service;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.entity.*;
import com.skillcert.backend.exception.ResourceNotFoundException;
import com.skillcert.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final EvaluationRepository evaluationRepository;

    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return mapToQuestionResponse(question);
    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        Evaluation evaluation = evaluationRepository.findById(request.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + request.getEvaluationId()));

        Question question = Question.builder()
                .evaluation(evaluation)
                .questionText(request.getQuestionText())
                .questionType(request.getQuestionType())
                .points(request.getPoints())
                .orderIndex(request.getOrderIndex())
                .explanation(request.getExplanation())
                .difficulty(request.getDifficulty())
                .build();

        Question savedQuestion = questionRepository.save(question);
        return mapToQuestionResponse(savedQuestion);
    }

    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        Evaluation evaluation = evaluationRepository.findById(request.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + request.getEvaluationId()));

        question.setEvaluation(evaluation);
        question.setQuestionText(request.getQuestionText());
        question.setQuestionType(request.getQuestionType());
        question.setPoints(request.getPoints());
        question.setOrderIndex(request.getOrderIndex());
        question.setExplanation(request.getExplanation());
        question.setDifficulty(request.getDifficulty());

        Question updatedQuestion = questionRepository.save(question);
        return mapToQuestionResponse(updatedQuestion);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    public QuestionOptionResponse addOptionToQuestion(Long questionId, QuestionOptionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));

        QuestionOption option = QuestionOption.builder()
                .question(question)
                .optionText(request.getOptionText())
                .isCorrect(request.isCorrect())
                .orderIndex(request.getOrderIndex())
                .build();

        QuestionOption savedOption = optionRepository.save(option);
        return mapToOptionResponse(savedOption);
    }

    public QuestionOptionResponse updateOption(Long optionId, QuestionOptionRequest request) {
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + optionId));

        option.setOptionText(request.getOptionText());
        option.setCorrect(request.isCorrect());
        option.setOrderIndex(request.getOrderIndex());

        QuestionOption updatedOption = optionRepository.save(option);
        return mapToOptionResponse(updatedOption);
    }

    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new ResourceNotFoundException("Option not found with id: " + optionId);
        }
        optionRepository.deleteById(optionId);
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        List<QuestionOptionResponse> options = optionRepository.findByQuestionId(question.getId()).stream()
                .map(this::mapToOptionResponse)
                .collect(Collectors.toList());

        return QuestionResponse.builder()
                .id(question.getId())
                .evaluationId(question.getEvaluation().getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .points(question.getPoints())
                .orderIndex(question.getOrderIndex())
                .explanation(question.getExplanation())
                .difficulty(question.getDifficulty())
                .options(options)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    private QuestionOptionResponse mapToOptionResponse(QuestionOption option) {
        return QuestionOptionResponse.builder()
                .id(option.getId())
                .questionId(option.getQuestion().getId())
                .optionText(option.getOptionText())
                .isCorrect(option.isCorrect())
                .orderIndex(option.getOrderIndex())
                .createdAt(option.getCreatedAt())
                .build();
    }

    public List<QuestionResponse> getQuestionsByEvaluation(Long evaluationId) {
    if (!evaluationRepository.existsById(evaluationId)) {
        throw new ResourceNotFoundException("Evaluation not found with id: " + evaluationId);
    }
    return questionRepository.findByEvaluationId(evaluationId).stream()
            .map(this::mapToQuestionResponse)
            .collect(Collectors.toList());
}
}