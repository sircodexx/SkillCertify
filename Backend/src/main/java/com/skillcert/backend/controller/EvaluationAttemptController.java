package com.skillcert.backend.controller;

import com.skillcert.backend.controller.CenterController.ApiResponse;
import com.skillcert.backend.dto.*;
import com.skillcert.backend.service.EvaluationAttemptService;
import com.skillcert.backend.service.JwtService;
import com.skillcert.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationAttemptController {

    private final EvaluationAttemptService evaluationAttemptService;
    private final JwtService jwtService;
    private final UserService userService;

    // POST /api/attempts - Crear nuevo intento
    @PostMapping
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttemptResponseDto>> createAttempt(
            @Valid @RequestBody CreateAttemptDto createAttemptDto,
            HttpServletRequest request) {
        try {
            // Obtener información del usuario del token
            Long userId = getUserIdFromToken(request);
            
            // Obtener IP y User-Agent del request
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            createAttemptDto.setIpAddress(ipAddress);
            createAttemptDto.setUserAgent(userAgent);
            
            AttemptResponseDto createdAttempt = evaluationAttemptService.createAttempt(userId, createAttemptDto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Intento creado exitosamente",
                            createdAttempt
                    ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al crear intento: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET /api/attempts/{id} - Obtener intento específico
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttemptResponseDto>> getAttemptById(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            AttemptResponseDto attempt = evaluationAttemptService.getAttemptById(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Intento obtenido exitosamente",
                    attempt
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al obtener intento: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT /api/attempts/{id} - Actualizar intento
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttemptResponseDto>> updateAttempt(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAttemptDto updateAttemptDto,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            
            // Actualizar información del request si no se proporciona
            if (updateAttemptDto.getIpAddress() == null) {
                updateAttemptDto.setIpAddress(getClientIpAddress(request));
            }
            if (updateAttemptDto.getUserAgent() == null) {
                updateAttemptDto.setUserAgent(request.getHeader("User-Agent"));
            }
            
            AttemptResponseDto updatedAttempt = evaluationAttemptService.updateAttempt(id, userId, updateAttemptDto);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Intento actualizado exitosamente",
                    updatedAttempt
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al actualizar intento: " + e.getMessage(),
                            null
                    ));
        }
    }

    // POST /api/attempts/{id}/answers - Guardar respuesta
    @PostMapping("/{id}/answers")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> saveAnswer(
            @PathVariable Long id,
            @Valid @RequestBody SubmitAnswerDto submitAnswerDto,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            evaluationAttemptService.saveAnswer(id, userId, submitAnswerDto);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Respuesta guardada exitosamente",
                    null
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al guardar respuesta: " + e.getMessage(),
                            null
                    ));
        }
    }

    // POST /api/attempts/{id}/submit - Enviar evaluación
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttemptResultDto>> submitEvaluation(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            AttemptResultDto result = evaluationAttemptService.submitEvaluation(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Evaluación enviada exitosamente",
                    result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al enviar evaluación: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET /api/attempts/{id}/results - Obtener resultados
    @GetMapping("/{id}/results")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttemptResultDto>> getResults(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            AttemptResultDto result = evaluationAttemptService.getResults(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Resultados obtenidos exitosamente",
                    result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al obtener resultados: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET /api/attempts/user/{userId} - Obtener intentos del usuario
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('NORMAL') and #userId == authentication.principal.id)")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getUserAttempts(
            @PathVariable Long userId,
            HttpServletRequest request) {
        try {
            // Verificar autorización adicional
            Long currentUserId = getUserIdFromToken(request);
            if (!currentUserId.equals(userId) && !isAdmin(request)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(
                                false,
                                "No tienes permisos para ver estos intentos",
                                null
                        ));
            }
            
            List<AttemptResponseDto> attempts = evaluationAttemptService.getUserAttempts(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Intentos del usuario obtenidos exitosamente",
                    attempts
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al obtener intentos del usuario: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET /api/attempts/user/current - Obtener intentos del usuario actual
    @GetMapping("/user/current")
    @PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getCurrentUserAttempts(
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            List<AttemptResponseDto> attempts = evaluationAttemptService.getUserAttempts(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Tus intentos obtenidos exitosamente",
                    attempts
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al obtener tus intentos: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET /api/attempts/evaluation/{evaluationId} - Obtener intentos por evaluación (solo admin)
    @GetMapping("/evaluation/{evaluationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getAttemptsByEvaluation(
            @PathVariable Long evaluationId,
            HttpServletRequest request) {
        try {
            // Este endpoint podría implementarse en el servicio si es necesario
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new ApiResponse<>(
                            false,
                            "Funcionalidad no implementada aún",
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al obtener intentos por evaluación: " + e.getMessage(),
                            null
                    ));
        }
    }

    // DELETE /api/attempts/{id} - Eliminar intento (solo admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAttempt(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            // Este endpoint podría implementarse en el servicio si es necesario
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new ApiResponse<>(
                            false,
                            "Funcionalidad no implementada aún",
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "Error al eliminar intento: " + e.getMessage(),
                            null
                    ));
        }
    }

    // Métodos auxiliares
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            return jwtService.extractUserId(token);
        }
        throw new RuntimeException("Token no encontrado o inválido");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isAdmin(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            return userService.isAdmin(userId);
        } catch (Exception e) {
            return false;
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}