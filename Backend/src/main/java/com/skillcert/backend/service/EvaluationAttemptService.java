package com.skillcert.backend.service;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.entity.*;
import com.skillcert.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationAttemptService {

    private final EvaluationAttemptRepository evaluationAttemptRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final CertificateRepository certificateRepository;

    // Crear nuevo intento
    public AttemptResponseDto createAttempt(Long userId, CreateAttemptDto createAttemptDto) {
        // Verificar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // Verificar que la evaluación existe
        Evaluation evaluation = evaluationRepository.findById(createAttemptDto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + createAttemptDto.getEvaluationId()));

        // Verificar que la evaluación está activa
        if (evaluation.getStatus() != Evaluation.EvaluationStatus.ACTIVE) {
            throw new RuntimeException("La evaluación no está disponible");
        }

        // Verificar si hay un intento en progreso
        if (evaluationAttemptRepository.findInProgressAttempt(userId, createAttemptDto.getEvaluationId()).isPresent()) {
            throw new RuntimeException("Ya tienes un intento en progreso para esta evaluación");
        }

        // Contar intentos previos
        Long attemptCount = evaluationAttemptRepository.countByUserIdAndEvaluationId(userId, createAttemptDto.getEvaluationId());
        
        // Verificar límite de intentos
        if (attemptCount >= evaluation.getMaxAttempts()) {
            throw new RuntimeException("Has alcanzado el límite máximo de intentos para esta evaluación");
        }

        // Verificar prerrequisitos si existen
        if (evaluation.getPrerequisiteEvaluation() != null) {
            boolean hasPassedPrerequisite = evaluationAttemptRepository
                    .findByUserIdAndEvaluationIdOrderByAttemptNumberDesc(userId, evaluation.getPrerequisiteEvaluation().getId())
                    .stream()
                    .anyMatch(attempt -> attempt.getPassed());
            
            if (!hasPassedPrerequisite) {
                throw new RuntimeException("Debes aprobar la evaluación prerequisito antes de iniciar esta evaluación");
            }
        }

        // Crear nuevo intento
        EvaluationAttempt attempt = new EvaluationAttempt();
        attempt.setUser(user);
        attempt.setEvaluation(evaluation);
        attempt.setAttemptNumber(attemptCount.intValue() + 1);
        attempt.setStatus(EvaluationAttempt.AttemptStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setIpAddress(createAttemptDto.getIpAddress());
        attempt.setUserAgent(createAttemptDto.getUserAgent());

        // Calcular puntuación máxima
        Integer maxScore = questionRepository.findByEvaluationId(evaluation.getId())
                .stream()
                .mapToInt(Question::getPoints)
                .sum();
        attempt.setMaxScore(maxScore);

        EvaluationAttempt savedAttempt = evaluationAttemptRepository.save(attempt);

        AttemptResponseDto response = new AttemptResponseDto(savedAttempt);
        
        // Contar preguntas totales
        Long totalQuestions = questionRepository.countByEvaluationId(evaluation.getId());
        response.setTotalQuestions(totalQuestions.intValue());
        response.setAnsweredQuestions(0);

        return response;
    }

    // Obtener intento específico
    public AttemptResponseDto getAttemptById(Long attemptId, Long userId) {
        EvaluationAttempt attempt = evaluationAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Intento no encontrado con ID: " + attemptId));

        // Verificar que el intento pertenece al usuario (excepto si es admin)
        if (!attempt.getUser().getId().equals(userId) && !isAdmin(userId)) {
            throw new RuntimeException("No tienes permisos para acceder a este intento");
        }

        AttemptResponseDto response = new AttemptResponseDto(attempt);
        
        // Contar respuestas
        Long answeredQuestions = userAnswerRepository.countAnswersByAttemptId(attemptId);
        response.setAnsweredQuestions(answeredQuestions.intValue());
        
        Long totalQuestions = questionRepository.countByEvaluationId(attempt.getEvaluation().getId());
        response.setTotalQuestions(totalQuestions.intValue());

        return response;
    }

    // Actualizar intento
    public AttemptResponseDto updateAttempt(Long attemptId, Long userId, UpdateAttemptDto updateAttemptDto) {
        EvaluationAttempt attempt = evaluationAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Intento no encontrado con ID: " + attemptId));

        // Verificar que el intento pertenece al usuario
        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para actualizar este intento");
        }

        // Actualizar campos si se proporcionan
        if (updateAttemptDto.getStatus() != null) {
            attempt.setStatus(updateAttemptDto.getStatus());
        }
        if (updateAttemptDto.getTimeSpentMinutes() != null) {
            attempt.setTimeSpentMinutes(updateAttemptDto.getTimeSpentMinutes());
        }
        if (updateAttemptDto.getIpAddress() != null) {
            attempt.setIpAddress(updateAttemptDto.getIpAddress());
        }
        if (updateAttemptDto.getUserAgent() != null) {
            attempt.setUserAgent(updateAttemptDto.getUserAgent());
        }

        EvaluationAttempt updatedAttempt = evaluationAttemptRepository.save(attempt);
        return new AttemptResponseDto(updatedAttempt);
    }

    // Guardar respuesta
    public void saveAnswer(Long attemptId, Long userId, SubmitAnswerDto submitAnswerDto) {
        EvaluationAttempt attempt = evaluationAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Intento no encontrado con ID: " + attemptId));

        // Verificar que el intento pertenece al usuario
        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para responder en este intento");
        }

        // Verificar que el intento está en progreso
        if (attempt.getStatus() != EvaluationAttempt.AttemptStatus.IN_PROGRESS) {
            throw new RuntimeException("No puedes responder en un intento que no está en progreso");
        }

        // Verificar tiempo límite si está habilitado
        if (attempt.getEvaluation().getTimeLimitEnabled()) {
            long minutesElapsed = ChronoUnit.MINUTES.between(attempt.getStartedAt(), LocalDateTime.now());
            if (minutesElapsed > attempt.getEvaluation().getDurationMinutes()) {
                // Expirar el intento
                attempt.setStatus(EvaluationAttempt.AttemptStatus.EXPIRED);
                evaluationAttemptRepository.save(attempt);
                throw new RuntimeException("El tiempo límite para esta evaluación ha expirado");
            }
        }

        // Verificar que la pregunta existe y pertenece a la evaluación
        Question question = questionRepository.findById(submitAnswerDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con ID: " + submitAnswerDto.getQuestionId()));

        if (!question.getEvaluation().getId().equals(attempt.getEvaluation().getId())) {
            throw new RuntimeException("La pregunta no pertenece a esta evaluación");
        }

        // Buscar respuesta existente o crear nueva
        UserAnswer userAnswer = userAnswerRepository.findByAttemptIdAndQuestionId(attemptId, submitAnswerDto.getQuestionId())
                .orElse(new UserAnswer());

        userAnswer.setAttempt(attempt);
        userAnswer.setQuestion(question);
        userAnswer.setTimeSpentSeconds(submitAnswerDto.getTimeSpentSeconds());
        userAnswer.setFlagged(submitAnswerDto.getFlagged());
        userAnswer.setAnsweredAt(LocalDateTime.now());

        // Procesar respuesta según tipo de pregunta
        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                if (submitAnswerDto.getSelectedOptionId() != null) {
                    QuestionOption selectedOption = questionOptionRepository.findById(submitAnswerDto.getSelectedOptionId())
                            .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
                    
                    if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                        throw new RuntimeException("La opción no pertenece a esta pregunta");
                    }
                    
                    userAnswer.setSelectedOption(selectedOption);
                    userAnswer.setIsCorrect(selectedOption.getIsCorrect());
                    userAnswer.setPointsEarned(selectedOption.getIsCorrect() ? question.getPoints() : 0);
                }
                break;
            case TRUE_FALSE:
                if (submitAnswerDto.getSelectedOptionId() != null) {
                    QuestionOption selectedOption = questionOptionRepository.findById(submitAnswerDto.getSelectedOptionId())
                            .orElseThrow(() -> new RuntimeException("Opción no encontrada"));
                    
                    userAnswer.setSelectedOption(selectedOption);
                    userAnswer.setIsCorrect(selectedOption.getIsCorrect());
                    userAnswer.setPointsEarned(selectedOption.getIsCorrect() ? question.getPoints() : 0);
                }
                break;
            case OPEN_TEXT:
                userAnswer.setTextAnswer(submitAnswerDto.getTextAnswer());
                // Para preguntas abiertas, se requiere revisión manual
                userAnswer.setIsCorrect(false);
                userAnswer.setPointsEarned(0);
                break;
        }

        userAnswerRepository.save(userAnswer);
    }

    // Enviar evaluación
    public AttemptResultDto submitEvaluation(Long attemptId, Long userId) {
        EvaluationAttempt attempt = evaluationAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Intento no encontrado con ID: " + attemptId));

        // Verificar que el intento pertenece al usuario
        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para enviar este intento");
        }

        // Verificar que el intento está en progreso
        if (attempt.getStatus() != EvaluationAttempt.AttemptStatus.IN_PROGRESS) {
            throw new RuntimeException("Este intento ya fue enviado o no está en progreso");
        }

        // Calcular tiempo transcurrido
        long minutesElapsed = ChronoUnit.MINUTES.between(attempt.getStartedAt(), LocalDateTime.now());
        
        // Verificar tiempo límite si está habilitado
        if (attempt.getEvaluation().getTimeLimitEnabled() && minutesElapsed > attempt.getEvaluation().getDurationMinutes()) {
            attempt.setStatus(EvaluationAttempt.AttemptStatus.EXPIRED);
            attempt.setTimeSpentMinutes((int) minutesElapsed);
            evaluationAttemptRepository.save(attempt);
            throw new RuntimeException("El tiempo límite para esta evaluación ha expirado");
        }

        // Calcular puntuación
        Long totalPoints = userAnswerRepository.getTotalPointsByAttemptId(attemptId);
        if (totalPoints == null) totalPoints = 0L;

        attempt.setScore(totalPoints.intValue());
        attempt.setTimeSpentMinutes((int) minutesElapsed);
        attempt.setCompletedAt(LocalDateTime.now());
        attempt.setStatus(EvaluationAttempt.AttemptStatus.COMPLETED);

        // Calcular porcentaje
        BigDecimal percentage = BigDecimal.ZERO;
        if (attempt.getMaxScore() > 0) {
            percentage = BigDecimal.valueOf(totalPoints)
                    .divide(BigDecimal.valueOf(attempt.getMaxScore()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        attempt.setPercentage(percentage);

        // Verificar si aprobó
        boolean passed = percentage.compareTo(BigDecimal.valueOf(attempt.getEvaluation().getPassingScore())) >= 0;
        attempt.setPassed(passed);

        // Generar certificado si aprobó
        if (passed) {
            attempt.setCertified(true);
            generateCertificate(attempt);
        }

        evaluationAttemptRepository.save(attempt);

        return buildAttemptResult(attempt);
    }

    // Obtener resultados
    public AttemptResultDto getResults(Long attemptId, Long userId) {
        EvaluationAttempt attempt = evaluationAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Intento no encontrado con ID: " + attemptId));

        // Verificar que el intento pertenece al usuario o es admin
        if (!attempt.getUser().getId().equals(userId) && !isAdmin(userId)) {
            throw new RuntimeException("No tienes permisos para ver estos resultados");
        }

        // Verificar que el intento está completado
        if (attempt.getStatus() != EvaluationAttempt.AttemptStatus.COMPLETED) {
            throw new RuntimeException("El intento no ha sido completado");
        }

        // Verificar si se deben mostrar resultados inmediatamente
        if (!attempt.getEvaluation().getShowResultsImmediately() && !isAdmin(userId)) {
            throw new RuntimeException("Los resultados no están disponibles inmediatamente");
        }

        return buildAttemptResult(attempt);
    }

    // Obtener intentos del usuario
    public List<AttemptResponseDto> getUserAttempts(Long userId) {
        List<EvaluationAttempt> attempts = evaluationAttemptRepository.findByUserIdOrderByStartedAtDesc(userId);
        
        return attempts.stream()
                .map(attempt -> {
                    AttemptResponseDto dto = new AttemptResponseDto(attempt);
                    Long answeredQuestions = userAnswerRepository.countAnswersByAttemptId(attempt.getId());
                    dto.setAnsweredQuestions(answeredQuestions.intValue());
                    Long totalQuestions = questionRepository.countByEvaluationId(attempt.getEvaluation().getId());
                    dto.setTotalQuestions(totalQuestions.intValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private AttemptResultDto buildAttemptResult(EvaluationAttempt attempt) {
        AttemptResultDto result = new AttemptResultDto();
        result.setAttemptId(attempt.getId());
        result.setEvaluationTitle(attempt.getEvaluation().getTitle());
        result.setAttemptNumber(attempt.getAttemptNumber());
        result.setStartedAt(attempt.getStartedAt());
        result.setCompletedAt(attempt.getCompletedAt());
        result.setTimeSpentMinutes(attempt.getTimeSpentMinutes());
        result.setScore(attempt.getScore());
        result.setMaxScore(attempt.getMaxScore());
        result.setPercentage(attempt.getPercentage());
        result.setPassed(attempt.getPassed());
        result.setCertified(attempt.getCertified());

        // Obtener código de certificado si existe
        if (attempt.getCertificate() != null) {
            result.setCertificateCode(attempt.getCertificate().getCertificateCode());
        }

        // Contar respuestas
        List<UserAnswer> userAnswers = userAnswerRepository.findByAttemptIdOrderByQuestionId(attempt.getId());
        result.setTotalQuestions(userAnswers.size());
        
        long correctAnswers = userAnswers.stream().mapToLong(ua -> ua.getIsCorrect() ? 1 : 0).sum();
        result.setCorrectAnswers((int) correctAnswers);
        result.setIncorrectAnswers(userAnswers.size() - (int) correctAnswers);

        // Construir detalles de preguntas
        List<QuestionResultDto> questionResults = userAnswers.stream()
                .map(this::buildQuestionResult)
                .collect(Collectors.toList());
        result.setQuestionResults(questionResults);

        return result;
    }

    private QuestionResultDto buildQuestionResult(UserAnswer userAnswer) {
        Question question = userAnswer.getQuestion();
        QuestionResultDto result = new QuestionResultDto();
        
        result.setQuestionId(question.getId());
        result.setQuestionText(question.getQuestionText());
        result.setQuestionType(question.getQuestionType().name());
        result.setPoints(question.getPoints());
        result.setIsCorrect(userAnswer.getIsCorrect());
        result.setPointsEarned(userAnswer.getPointsEarned());
        result.setExplanation(question.getExplanation());

        // Establecer respuesta del usuario
        if (userAnswer.getSelectedOption() != null) {
            result.setUserAnswer(userAnswer.getSelectedOption().getOptionText());
        } else if (userAnswer.getTextAnswer() != null) {
            result.setUserAnswer(userAnswer.getTextAnswer());
        }

        // Encontrar respuesta correcta
        List<QuestionOption> options = questionOptionRepository.findByQuestionIdOrderByOrderIndex(question.getId());
        result.setCorrectAnswer(options.stream()
                .filter(QuestionOption::getIsCorrect)
                .map(QuestionOption::getOptionText)
                .findFirst()
                .orElse(""));

        // Construir opciones
        List<OptionResultDto> optionResults = options.stream()
                .map(option -> new OptionResultDto(
                        option.getId(),
                        option.getOptionText(),
                        option.getIsCorrect(),
                        userAnswer.getSelectedOption() != null && userAnswer.getSelectedOption().getId().equals(option.getId())
                ))
                .collect(Collectors.toList());
        result.setOptions(optionResults);

        return result;
    }

    private void generateCertificate(EvaluationAttempt attempt) {
        // Implementar lógica de generación de certificado
        Certificate certificate = new Certificate();
        certificate.setUser(attempt.getUser());
        certificate.setEvaluation(attempt.getEvaluation());
        certificate.setAttempt(attempt);
        certificate.setCertificateCode(generateCertificateCode());
        certificate.setIssuedAt(LocalDateTime.now());
        certificate.setStatus(Certificate.CertificateStatus.ACTIVE);
        
        certificateRepository.save(certificate);
    }

    private String generateCertificateCode() {
        // Generar código único para certificado
        return "CERT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    private boolean isAdmin(Long userId) {
        // Implementar lógica para verificar si el usuario es admin
        return userRepository.findById(userId)
                .map(user -> user.getRole() == User.UserRole.ADMIN)
                .orElse(false);
    }
}