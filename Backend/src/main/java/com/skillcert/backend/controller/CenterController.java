package com.skillcert.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillcert.backend.dto.CenterDTO;
import com.skillcert.backend.dto.EvaluationAssignmentDTO;
import com.skillcert.backend.dto.EvaluationDTO;
import com.skillcert.backend.dto.StudentDTO;
import com.skillcert.backend.service.CenterService;

@RestController
@RequestMapping("/api/centers")
public class CenterController {

    @Autowired
    private CenterService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public List<CenterDTO> getAllCenters() {
        return service.getAllCenters();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public CenterDTO createCenter(@RequestBody CenterDTO dto) {
        return service.createCenter(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public CenterDTO getCenterById(@PathVariable Long id) {
        return service.getCenterById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public CenterDTO updateCenter(@PathVariable Long id, @RequestBody CenterDTO dto) {
        return service.updateCenter(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public void deleteCenter(@PathVariable Long id) {
        service.deleteCenter(id);
    }

    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public List<StudentDTO> getStudents(@PathVariable Long id) {
        return service.getStudentsDTOByCenterId(id);
    }

    @GetMapping("/{id}/evaluations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public List<EvaluationDTO> getEvaluations(@PathVariable Long id) {
        return service.getEvaluationsDTOByCenterId(id);
    }

    @PostMapping("/{id}/assign-evaluation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public void assignEvaluation(@PathVariable Long id, @RequestBody EvaluationAssignmentDTO dto) {
        service.assignEvaluationToCenter(id, dto);
    }
}
