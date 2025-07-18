package com.skillcert.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;
import com.skillcert.backend.dto.StudentDTO;
import com.skillcert.backend.dto.EvaluationDTO;
import com.skillcert.backend.entity.User;
import com.skillcert.backend.entity.Evaluation;
import com.skillcert.backend.entity.Center;
import com.skillcert.backend.repository.UserRepository;
import com.skillcert.backend.repository.EvaluationRepository;
import com.skillcert.backend.repository.CenterRepository;

@Service
public class CenterServiceImpl implements CenterService {

    @Autowired
    private CenterRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;

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
        center.setDescription(dto.getDescription());
        center.setAddress(dto.getAddress());
        center.setCity(dto.getCity());
        center.setRegion(dto.getRegion());
        center.setPhone(dto.getPhone());
        center.setEmail(dto.getEmail());
        center.setDirector(dto.getDirector());
        center.setCapacity(dto.getCapacity());
        center.setStatus(dto.getStatus());
        center.setImageUrl(dto.getImageUrl());
        center.setCreatedAt(dto.getCreatedAt());
        center.setUpdatedAt(dto.getUpdatedAt());
        return toDTO(repository.save(center));
    }

    @Override
    public void deleteCenter(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void assignEvaluationToCenter(Long centerId, EvaluationAssignmentDTO evalDTO) {
        System.out.println("Asignando evaluación " + evalDTO.getEvaluationId() + " al centro " + centerId);
    }

    @Override
    public List<StudentDTO> getStudentsDTOByCenterId(Long centerId) {
        List<User> students = userRepository.findByCenter_Id(centerId);
        return students.stream().map(user -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EvaluationDTO> getEvaluationsDTOByCenterId(Long centerId) {
        List<Evaluation> evaluations = evaluationRepository.findByCenter_Id(centerId);
        return evaluations.stream().map(eval -> {
            EvaluationDTO dto = new EvaluationDTO();
            dto.setId(eval.getId());
            dto.setTitle(eval.getTitle());
            dto.setDescription(eval.getDescription());
            dto.setCategoryId(eval.getCategory() != null ? eval.getCategory().getId() : null);
            dto.setStatus(eval.getStatus() != null ? eval.getStatus().name() : null);
            dto.setDurationMinutes(eval.getDurationMinutes());
            dto.setPassingScore(eval.getPassingScore());
            dto.setDifficulty(eval.getDifficulty() != null ? eval.getDifficulty().name() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    private CenterDTO toDTO(Center center) {
        CenterDTO dto = new CenterDTO();
        dto.setId(center.getId());
        dto.setName(center.getName());
        dto.setLocation(center.getLocation());
        dto.setDescription(center.getDescription());
        dto.setAddress(center.getAddress());
        dto.setCity(center.getCity());
        dto.setRegion(center.getRegion());
        dto.setPhone(center.getPhone());
        dto.setEmail(center.getEmail());
        dto.setDirector(center.getDirector());
        dto.setCapacity(center.getCapacity());
        dto.setStatus(center.getStatus());
        dto.setImageUrl(center.getImageUrl());
        dto.setCreatedAt(center.getCreatedAt());
        dto.setUpdatedAt(center.getUpdatedAt());
        return dto;
    }

    private Center toEntity(CenterDTO dto) {
        Center c = new Center();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setLocation(dto.getLocation());
        c.setDescription(dto.getDescription());
        c.setAddress(dto.getAddress());
        c.setCity(dto.getCity());
        c.setRegion(dto.getRegion());
        c.setPhone(dto.getPhone());
        c.setEmail(dto.getEmail());
        c.setDirector(dto.getDirector());
        c.setCapacity(dto.getCapacity());
        c.setStatus(dto.getStatus());
        c.setImageUrl(dto.getImageUrl());
        c.setCreatedAt(dto.getCreatedAt());
        c.setUpdatedAt(dto.getUpdatedAt());
        return c;
    }
}
