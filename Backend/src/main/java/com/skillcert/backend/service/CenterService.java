package com.skillcert.backend.service;

import com.skillcert.backend.dto.*;
import com.skillcert.backend.entity.*;
import com.skillcert.backend.repository.CenterEvaluationRepository;
import com.skillcert.backend.repository.CenterRepository;
import com.skillcert.backend.repository.EvaluationRepository;
import com.skillcert.backend.repository.UserCenterRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterService {
    
    private final CenterRepository centerRepository;
    private final UserCenterRepository userCenterRepository;
    private final CenterEvaluationRepository centerEvaluationRepository;
    private final EvaluationRepository evaluationRepository;
    
    // Listar todos los centros
    public List<CenterResponseDto> getAllCenters() {
        return centerRepository.findAll()
                .stream()
                .map(CenterResponseDto::new)
                .collect(Collectors.toList());
    }
    
    // Crear nuevo centro
    public CenterResponseDto createCenter(CreateCenterDto createCenterDto) {
        // Validar que no exista un centro con el mismo nombre
        if (centerRepository.existsByNameIgnoreCase(createCenterDto.getName())) {
            throw new RuntimeException("Ya existe un centro con este nombre");
        }
        
        Center center = new Center();
        center.setName(createCenterDto.getName());
        center.setDescription(createCenterDto.getDescription());
        center.setAddress(createCenterDto.getAddress());
        center.setCity(createCenterDto.getCity());
        center.setRegion(createCenterDto.getRegion());
        center.setPhone(createCenterDto.getPhone());
        center.setEmail(createCenterDto.getEmail());
        center.setDirector(createCenterDto.getDirector());
        center.setCapacity(createCenterDto.getCapacity() != null ? createCenterDto.getCapacity() : 100);
        center.setStudentsCount(0);
        center.setStatus(Center.CenterStatus.ACTIVE);
        center.setCreatedAt(LocalDateTime.now());
        center.setUpdatedAt(LocalDateTime.now());
        
        Center savedCenter = centerRepository.save(center);
        return new CenterResponseDto(savedCenter);
    }
    
    // Obtener centro por ID
    public CenterResponseDto getCenterById(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado con ID: " + id));
        return new CenterResponseDto(center);
    }
    
    // Actualizar centro
    public CenterResponseDto updateCenter(Long id, UpdateCenterDto updateCenterDto) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado con ID: " + id));
        
        // Validar nombre único si se está cambiando
        if (updateCenterDto.getName() != null && 
            !updateCenterDto.getName().equalsIgnoreCase(center.getName()) &&
            centerRepository.existsByNameIgnoreCase(updateCenterDto.getName())) {
            throw new RuntimeException("Ya existe un centro con este nombre");
        }
        
        // Actualizar campos no nulos
        if (updateCenterDto.getName() != null) {
            center.setName(updateCenterDto.getName());
        }
        if (updateCenterDto.getDescription() != null) {
            center.setDescription(updateCenterDto.getDescription());
        }
        if (updateCenterDto.getAddress() != null) {
            center.setAddress(updateCenterDto.getAddress());
        }
        if (updateCenterDto.getCity() != null) {
            center.setCity(updateCenterDto.getCity());
        }
        if (updateCenterDto.getRegion() != null) {
            center.setRegion(updateCenterDto.getRegion());
        }
        if (updateCenterDto.getPhone() != null) {
            center.setPhone(updateCenterDto.getPhone());
        }
        if (updateCenterDto.getEmail() != null) {
            center.setEmail(updateCenterDto.getEmail());
        }
        if (updateCenterDto.getDirector() != null) {
            center.setDirector(updateCenterDto.getDirector());
        }
        if (updateCenterDto.getCapacity() != null) {
            center.setCapacity(updateCenterDto.getCapacity());
        }
        if (updateCenterDto.getStatus() != null) {
            center.setStatus(updateCenterDto.getStatus());
        }
        
        center.setUpdatedAt(LocalDateTime.now());
        
        Center updatedCenter = centerRepository.save(center);
        return new CenterResponseDto(updatedCenter);
    }
    
    // Eliminar centro
    public void deleteCenter(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado con ID: " + id));
        
        // Verificar si hay estudiantes activos
        Long activeStudents = centerRepository.countActiveStudentsByCenterId(id);
        if (activeStudents > 0) {
            throw new RuntimeException("No se puede eliminar el centro porque tiene estudiantes activos");
        }
        
        centerRepository.deleteById(id);
    }
    
    // Obtener estudiantes del centro
    public List<StudentResponseDto> getCenterStudents(Long id) {
        if (!centerRepository.existsById(id)) {
            throw new RuntimeException("Centro no encontrado con ID: " + id);
        }
        
        List<UserCenter> userCenters = userCenterRepository.findByCenterIdAndStatus(id, UserCenter.UserCenterStatus.ACTIVE);
        
        return userCenters.stream()
                .map(uc -> new StudentResponseDto(
                    uc.getUser(), 
                    uc.getEnrolledAt(), 
                    uc.getStatus().name()
                ))
                .collect(Collectors.toList());
    }
    
    // Obtener evaluaciones asignadas al centro
    public List<EvaluationResponseDto> getCenterEvaluations(Long id) {
        if (!centerRepository.existsById(id)) {
            throw new RuntimeException("Centro no encontrado con ID: " + id);
        }
        
        List<CenterEvaluation> centerEvaluations = centerEvaluationRepository.findByCenterIdAndStatus(id, CenterEvaluation.CenterEvaluationStatus.ACTIVE);
        
        return centerEvaluations.stream()
                .map(ce -> new EvaluationResponseDto(
                    ce.getEvaluation(), 
                    ce.getAssignedAt(), 
                    ce.getStatus().name()
                ))
                .collect(Collectors.toList());
    }
    
    // Asignar evaluación al centro
    public void assignEvaluationToCenter(Long centerId, AssignEvaluationDto assignEvaluationDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado con ID: " + centerId));
        
        Evaluation evaluation = evaluationRepository.findById(assignEvaluationDto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + assignEvaluationDto.getEvaluationId()));
        
        // Verificar que no esté ya asignada
        if (centerEvaluationRepository.existsByCenterIdAndEvaluationId(centerId, assignEvaluationDto.getEvaluationId())) {
            throw new RuntimeException("La evaluación ya está asignada a este centro");
        }
        
        CenterEvaluation centerEvaluation = new CenterEvaluation();
        centerEvaluation.setCenter(center);
        centerEvaluation.setEvaluation(evaluation);
        centerEvaluation.setAssignedAt(LocalDateTime.now());
        centerEvaluation.setStatus(CenterEvaluation.CenterEvaluationStatus.ACTIVE);
        
        centerEvaluationRepository.save(centerEvaluation);
    }
    
    // Actualizar contador de estudiantes
    public void updateStudentsCount(Long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado con ID: " + centerId));
        
        Long activeStudents = centerRepository.countActiveStudentsByCenterId(centerId);
        center.setStudentsCount(activeStudents.intValue());
        center.setUpdatedAt(LocalDateTime.now());
        
        centerRepository.save(center);
    }
}