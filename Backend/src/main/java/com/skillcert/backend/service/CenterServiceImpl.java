package com.skillcert.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;
import com.skillcert.backend.entity.Center;
import com.skillcert.backend.repository.CenterRepository;

@Service
public class CenterServiceImpl implements CenterService {

    @Autowired
    private CenterRepository repository;

    @Override
    public List<CenterDTO> getAllCenters() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CenterDTO getCenterById(Long id) {
        return repository.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public CenterDTO createCenter(CenterDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public CenterDTO updateCenter(Long id, CenterDTO dto) {
        Center center = repository.findById(id).orElseThrow();
        center.setName(dto.getName());
        center.setLocation(dto.getLocation());
        return toDTO(repository.save(center));
    }

    @Override
    public void deleteCenter(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<String> getStudentsByCenterId(Long centerId) {
        return List.of("Estudiante 1", "Estudiante 2"); // Simulación
    }

    @Override
    public List<String> getEvaluationsByCenterId(Long centerId) {
        return List.of("Evaluación A", "Evaluación B"); // Simulación
    }

    @Override
    public void assignEvaluationToCenter(Long centerId, EvaluationAssignmentDTO evalDTO) {
        System.out.println("Asignando evaluación " + evalDTO.getEvaluationId() + " al centro " + centerId);
    }

    private CenterDTO toDTO(Center center) {
        CenterDTO dto = new CenterDTO();
        dto.setId(center.getId());
        dto.setName(center.getName());
        dto.setLocation(center.getLocation());
        return dto;
    }

    private Center toEntity(CenterDTO dto) {
        Center c = new Center();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setLocation(dto.getLocation());
        return c;
    }
}
