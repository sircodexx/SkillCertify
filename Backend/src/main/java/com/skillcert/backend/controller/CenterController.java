package com.skillcert.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillcert.backend.dto.AssignEvaluationDto;
import com.skillcert.backend.dto.CenterResponseDto;
import com.skillcert.backend.dto.CreateCenterDto;
import com.skillcert.backend.dto.EvaluationResponseDto;
import com.skillcert.backend.dto.StudentResponseDto;
import com.skillcert.backend.dto.UpdateCenterDto;
import com.skillcert.backend.service.CenterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/centers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CenterController {
    
    private final CenterService centerService;
    
    // GET /api/centers - Listar centros
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<CenterResponseDto>>> getAllCenters() {
        try {
            List<CenterResponseDto> centers = centerService.getAllCenters();
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Centros obtenidos exitosamente",
                centers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                        false,
                        "Error al obtener centros: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // POST /api/centers - Crear centro
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CenterResponseDto>> createCenter(@Valid @RequestBody CreateCenterDto createCenterDto) {
        try {
            CenterResponseDto createdCenter = centerService.createCenter(createCenterDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                        true,
                        "Centro creado exitosamente",
                        createdCenter
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
                        "Error al crear centro: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // GET /api/centers/{id} - Obtener centro específico
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<CenterResponseDto>> getCenterById(@PathVariable Long id) {
        try {
            CenterResponseDto center = centerService.getCenterById(id);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Centro obtenido exitosamente",
                center
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
                        "Error al obtener centro: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // PUT /api/centers/{id} - Actualizar centro
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CenterResponseDto>> updateCenter(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCenterDto updateCenterDto) {
        try {
            CenterResponseDto updatedCenter = centerService.updateCenter(id, updateCenterDto);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Centro actualizado exitosamente",
                updatedCenter
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
                        "Error al actualizar centro: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // DELETE /api/centers/{id} - Eliminar centro
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCenter(@PathVariable Long id) {
        try {
            centerService.deleteCenter(id);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Centro eliminado exitosamente",
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
                        "Error al eliminar centro: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // GET /api/centers/{id}/students - Estudiantes del centro
    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getCenterStudents(@PathVariable Long id) {
        try {
            List<StudentResponseDto> students = centerService.getCenterStudents(id);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Estudiantes obtenidos exitosamente",
                students
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
                        "Error al obtener estudiantes: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // GET /api/centers/{id}/evaluations - Evaluaciones asignadas
    @GetMapping("/{id}/evaluations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<EvaluationResponseDto>>> getCenterEvaluations(@PathVariable Long id) {
        try {
            List<EvaluationResponseDto> evaluations = centerService.getCenterEvaluations(id);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Evaluaciones obtenidas exitosamente",
                evaluations
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
                        "Error al obtener evaluaciones: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // POST /api/centers/{id}/assign-evaluation - Asignar evaluación
    @PostMapping("/{id}/assign-evaluation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignEvaluationToCenter(
            @PathVariable Long id, 
            @Valid @RequestBody AssignEvaluationDto assignEvaluationDto) {
        try {
            centerService.assignEvaluationToCenter(id, assignEvaluationDto);
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Evaluación asignada exitosamente al centro",
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
                        "Error al asignar evaluación: " + e.getMessage(),
                        null
                    ));
        }
    }
    
    // Clase para respuesta estándar de API
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        
        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        // Getters y setters
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public T getData() {
            return data;
        }
        
        public void setData(T data) {
            this.data = data;
        }
    }
}
